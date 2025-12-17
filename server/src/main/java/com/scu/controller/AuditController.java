package com.scu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.constant.TemplateStateConstant;
import com.scu.dto.AuditInfoDTO;
import com.scu.dto.TemplateToAuditDto;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.result.Result;
import com.scu.service.AuditService;
import com.scu.service.TemplateFieldService;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "审核模板接口")
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TemplateFieldService templateFieldService;

    @Operation(summary = "审核员审核新建模板接口",description = "审核员审核新建模板接口")
    @PostMapping("/auditNewTemplate")
    public Result passAudit(@Parameter(description = "审核信息DTO") @RequestBody AuditInfoDTO auditInfoDTO){
        // 审核
        int re= auditService.auditNewTemplate(auditInfoDTO);
        return Result.success();
    }
    @Operation(summary = "获取所有待审核模板")
    @GetMapping("/getTemp")
    public Result auditTemplateData(){
        // 获取所有待审核模板
        LambdaQueryWrapper<Template> wrapper =
                Wrappers.lambdaQuery(Template.class)
                .eq(Template::getState, TemplateStateConstant.UNAUDITED);
        List<Template> tmp_unaudited = templateService.list(wrapper);
        List<TemplateToAuditDto> templateToAuditDtos=new ArrayList<>();
        for (Template template : tmp_unaudited){
            TemplateToAuditDto templateToAuditDto = TemplateToAuditDto.builder()
                    .templateId(template.getId())
                    .templateName(template.getName())
                    .build();
            Long id = template.getId();
            LambdaQueryWrapper<TemplateField> templateFieldWrapper = Wrappers.lambdaQuery(TemplateField.class)
                    .eq(TemplateField::getTemplateId, id);
            List<TemplateField> templateFields = templateFieldService.list(templateFieldWrapper);
            templateToAuditDto.setTemplateFields(templateFields);
            templateToAuditDtos.add(templateToAuditDto);
        }
        return Result.success(templateToAuditDtos);
    }

}
