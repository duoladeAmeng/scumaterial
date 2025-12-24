package com.scu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.constant.MessageConstant;
import com.scu.constant.TemplateFieldCategoryConstant;
import com.scu.constant.TemplateStateConstant;
import com.scu.dto.TemplateDetailedInfoDto;
import com.scu.dto.TemplateDto;
import com.scu.entity.AuditLog;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.entity.TemplateFieldDataTypeEnum;
import com.scu.exception.TemplateExistException;
import com.scu.mapper.TemplateFieldDataTypeEnumMapper;
import com.scu.mapper.TemplateMapper;
import com.scu.service.AuditService;
import com.scu.service.TemplateFieldService;
import com.scu.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Autowired
    private TemplateFieldService templateFieldService;
    @Autowired
    private AuditService auditService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TemplateFieldDataTypeEnumMapper templateFieldDataTypeEnumMapper;

    /**
     * 根据类别id获取模板
     * @param categoryId 类别id
     * @return 模板列表
     */
    @Override
    public List<Template> getTemplateByCategory(Integer categoryId) {
        LambdaQueryWrapper<Template> wrapper =
                Wrappers.lambdaQuery(Template.class)
                .eq(Template::getCategoryId, categoryId);
        return this.list(wrapper);
    }

    /**
     * 根据类别id获取模板详细信息
     * @param categoryId 类别id
     * @return 模板列表
     */
    @Override
    public List<TemplateDetailedInfoDto> getDetailedTemplateByCategory(Integer categoryId) {
        List<Template> templates = getTemplateByCategory(categoryId);
        List templateDetailedInfoDtos = new ArrayList<>();
        for (Template template : templates){
            TemplateDetailedInfoDto templateDetailedInfoDto = TemplateDetailedInfoDto.builder()
                    .templateId(template.getId())
                    .templateCategory(template.getCategoryId())
                    .createTime(template.getCreateTime())
                    .state(template.getState())
                    .templateName(template.getName())
                    .templateFields(templateFieldService.getTemplateFieldsByTemplateId(template.getId()))
                    .note(auditService.getAuditLogByTemplateId(template.getId()).getNote())
                    .build();
            templateDetailedInfoDtos.add(templateDetailedInfoDto);
        }
        return templateDetailedInfoDtos;
    }

    /**
     * 根据用户名(模板创建者)获取其创建的模板的详细信息
     * @param username 用户名
     * @return 模板列表
     */
    @Override
    public List<TemplateDetailedInfoDto> getDetailedTemplateByUsername(String username) {
        List<Template> templates = list(Wrappers.lambdaQuery(Template.class).eq(Template::getCreator, username));
        List templateDetailedInfoDtos = new ArrayList<>();
        for (Template template : templates){
            TemplateDetailedInfoDto templateDetailedInfoDto = TemplateDetailedInfoDto.builder()
                    .templateId(template.getId())
                    .templateCategory(template.getCategoryId())
                    .createTime(template.getCreateTime())
                    .state(template.getState())
                    .templateName(template.getName())
                    .templateFields(templateFieldService.getTemplateFieldsByTemplateId(template.getId()))
                    .note(auditService.getAuditLogByTemplateId(template.getId()).getNote())
                    .build();
            templateDetailedInfoDtos.add(templateDetailedInfoDto);
        }
        return templateDetailedInfoDtos;
    }


    /**
     * 创建模板
     * @param templateDto 模板信息
     */
    @Override
    @Transactional
    public void createTemplate(TemplateDto templateDto) {
        // 获取模板名, 如果模板名已存在则抛出异常
        String templateName = templateDto.getName();
        if (this.getOne(Wrappers.lambdaQuery(Template.class)
                .eq(Template::getName, templateName)) != null) {
            throw new TemplateExistException(MessageConstant.TEMPLATE_NAME_EXIST);
        }
        // 封装模板信息
        Template template = new Template();
        BeanUtil.copyProperties(templateDto,template);
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        // 设置模板状态 新创建的模板状态是未审核
        template.setState(TemplateStateConstant.UNAUDITED);
        System.out.println("保存前ID: " + template.getId());  // 打印为null
        // 保存模板
        this.save(template);
        // 获取模板id
        Long templateId = template.getId();
        System.out.println("模板id:"+templateId);
        // 保存模板字段
        templateFieldService.saveTemplateFields(templateDto.getTemplateFieldDtos(),templateId);
    }

    /**
     * 删除模板
     * @param templateIds 模板id列表
     */

    @Override
    @Transactional
    public void deleteTemplates(List<Long> templateIds) {
        for (Long templateId : templateIds){
            this.removeById(templateId);//删除模板
            templateFieldService.deleteTemplateFieldsByTemplateId(templateId);//删除模板字段
             //删除模板对应的表
            String tableName = "template_data_" + templateId;
            jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName);
            //删除模板对应的枚举字段
            templateFieldDataTypeEnumMapper.delete(
                    Wrappers.lambdaQuery(TemplateFieldDataTypeEnum.class)
                    .eq(TemplateFieldDataTypeEnum::getTemplateId, templateId)
            );
        }
    }

}
