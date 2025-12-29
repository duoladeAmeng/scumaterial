package com.scu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.constant.AuditResultConstant;
import com.scu.constant.TemplateFieldCategoryConstant;
import com.scu.constant.TemplateStatusConstant;
import com.scu.dto.TemplateAuditInfoDTO;
import com.scu.dto.TemplateDataAuditInfoDTO;
import com.scu.entity.TemplateAuditLog;
import com.scu.entity.Template;
import com.scu.entity.TemplateDataAuditLog;
import com.scu.entity.TemplateField;
import com.scu.mapper.*;
import com.scu.service.AuditService;
import com.scu.util.TableOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AuditServiceImpl  extends ServiceImpl<TemplateAuditLogMapper, TemplateAuditLog> implements AuditService {

    @Autowired
    private TemplateAuditLogMapper templateAuditLogMapper;
    @Autowired
    private TemplateDataAuditLogMapper templateDataAuditLogMapper;

    @Autowired
    private TemplateFieldMapper templateFieldMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private TableOperator tableOperator;
    @Autowired
    private TemplateDataMapper templateDataMapper;

    /**
     * 审核员审核新建模板
     * @param templateAuditInfoDTO
     */
    @Override
    @Transactional
    public int auditNewTemplate(TemplateAuditInfoDTO templateAuditInfoDTO) {
        // 获取审核信息
        // 审核结果
        Integer auditResult = templateAuditInfoDTO.getAuditResult();
        // 审核备注
        String note = templateAuditInfoDTO.getNote();
        // 模板id
        Long templateId = templateAuditInfoDTO.getTemplateId();
        // 审核员id
        Long auditorId = templateAuditInfoDTO.getAuditorId();
        // 保存审核日志
        TemplateAuditLog templateAuditLog = TemplateAuditLog.builder()
                .auditorId(templateAuditInfoDTO.getAuditorId())
                .auditResult(templateAuditInfoDTO.getAuditResult())
                .note(templateAuditInfoDTO.getNote())
                .templateId(templateAuditInfoDTO.getTemplateId())
                .logDate(LocalDateTime.now())
                .build();
        templateAuditLogMapper.insert(templateAuditLog);
        // 审核不通过
        if (auditResult == AuditResultConstant.REJECT){
            return 0;
        }
        // 审核通过
        // 获取模板字段
        LambdaQueryWrapper<TemplateField> wrapper = Wrappers.lambdaQuery(TemplateField.class)
                .eq(TemplateField::getTemplateId, templateId);
        List<TemplateField> templateFields = templateFieldMapper.selectList(wrapper);
        // 筛选出对象字段、操作字段、结果字段
        // 对象字段
        List<TemplateField> objFields=templateFields
                .stream()
                .filter(
                        templateField ->
                                templateField.getFieldCategory()== TemplateFieldCategoryConstant.OBJECT
                ).toList();
        // 操作字段
        List<TemplateField> operationFields=templateFields
                .stream()
                .filter(
                        templateField ->
                                templateField.getFieldCategory()== TemplateFieldCategoryConstant.OPERATION
                ).toList();
        // 结果字段
        List<TemplateField> resultFields=templateFields
                .stream()
                .filter(
                        templateField ->
                                templateField.getFieldCategory()== TemplateFieldCategoryConstant.RESULT
                ).toList();
        // 有结果字段，但是操作字段或对象字段为空，则抛出异常
//        if(!resultFields.isEmpty()&&(operationFields.isEmpty()||objFields.isEmpty()))
//            throw new TemplateFieldInvalidException(MessageConstant.TEMPLATE_FIELD_ONLY_HAVE_RESULT_FIELD);
        List<TemplateField> allFields = Stream.of(objFields, operationFields, resultFields)
                .flatMap(List::stream)
                .toList();
        // 建立模板对应的数据库表
        tableOperator.createUnifiedTableForTemplate(templateId, allFields);
        // 更新模板状态
        LambdaUpdateWrapper<Template> updateWrapper = Wrappers.lambdaUpdate(Template.class)
                .eq(Template::getId, templateId)
                .set(Template::getState, TemplateStatusConstant.AUDITED);
        templateMapper.update(updateWrapper);
        return 1;
    }

    /**
     * 根据模板id获取审核日志
     * @param templateId
     * @return
     */
    @Override
    public TemplateAuditLog getAuditLogByTemplateId(Long templateId) {
        LambdaQueryWrapper<TemplateAuditLog> wrapper = Wrappers.lambdaQuery(TemplateAuditLog.class)
                .eq(TemplateAuditLog::getTemplateId, templateId)
                .orderByDesc(TemplateAuditLog::getLogDate);
        List<TemplateAuditLog> logList = templateAuditLogMapper.selectList(wrapper);
        return logList.size() == 0 ? null : logList.get(0);
    }

    @Override
    @Transactional
    public void auditTemplateData(List<TemplateDataAuditInfoDTO> templateDataAuditInfoDTOs) {
        Long templateId=templateDataAuditInfoDTOs.get(0).getTemplateId();
        Long auditorId=templateDataAuditInfoDTOs.get(0).getAuditorId();
        // 获取审核通过的数据id
        List<Long> pass_list =
                 templateDataAuditInfoDTOs.stream().filter(templateDataAuditInfoDTO -> templateDataAuditInfoDTO.getAuditResult() == AuditResultConstant.PASS)
                .map(templateDataAuditInfoDTO -> templateDataAuditInfoDTO.getTemplateDataId())
                .toList();
        // 保存审核日志
        List<TemplateDataAuditLog> log_list = templateDataAuditInfoDTOs.stream().map(
                templateDataAuditInfoDTO ->
                        TemplateDataAuditLog.builder()
                                .auditorId(auditorId)
                                .auditResult(templateDataAuditInfoDTO.getAuditResult())
                                .note(templateDataAuditInfoDTO.getNote())
                                .templateId(templateId)
                                .templateDataId(templateDataAuditInfoDTO.getTemplateDataId())
                                .logDate(LocalDateTime.now())
                                .build()
                        ).toList();

        templateDataAuditLogMapper.insert(log_list);
        // 审核通过更新
        templateDataMapper.updateStatusToPass(templateId,pass_list);
    }

}
