package com.scu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.constant.AuditResultConstant;
import com.scu.constant.TemplateFieldCategoryConstant;
import com.scu.constant.TemplateStateConstant;
import com.scu.dto.AuditInfoDTO;
import com.scu.entity.AuditLog;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.mapper.AuditLogMapper;
import com.scu.mapper.TemplateFieldMapper;
import com.scu.mapper.TemplateMapper;
import com.scu.service.AuditService;
import com.scu.util.DynamicTableBuilder;
import com.scu.util.TableOpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class AuditServiceImpl  extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private TemplateFieldMapper templateFieldMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private DynamicTableBuilder dynamicTableBuilder;

    /**
     * 审核员审核新建模板
     * @param auditInfoDTO
     */
    @Override
    @Transactional
    public int auditNewTemplate(AuditInfoDTO auditInfoDTO) {
        Integer auditResult = auditInfoDTO.getAuditResult();
        String note = auditInfoDTO.getNote();
        Long templateId = auditInfoDTO.getTemplateId();
        Long auditorId = auditInfoDTO.getAuditorId();
        // 保存审核日志
        AuditLog auditLog = AuditLog.builder()
                .auditorId(auditInfoDTO.getAuditorId())
                .auditResult(auditInfoDTO.getAuditResult())
                .note(auditInfoDTO.getNote())
                .templateId(auditInfoDTO.getTemplateId())
                .build();
        auditLogMapper.insert(auditLog);
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
        List<TemplateField> allFields = Stream.of(objFields, operationFields, resultFields)
                .flatMap(List::stream)
                .toList();
        // 建立模板对应的数据库表
        dynamicTableBuilder.createUnifiedTableForTemplate(templateId, allFields);
        // 更新模板状态
        LambdaUpdateWrapper<Template> updateWrapper = Wrappers.lambdaUpdate(Template.class)
                .eq(Template::getId, templateId)
                .set(Template::getState, TemplateStateConstant.AUDITED);
        templateMapper.update(updateWrapper);
        return 1;
    }

}
