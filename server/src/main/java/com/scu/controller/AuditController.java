package com.scu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.constant.TemplateStatusConstant;
import com.scu.dto.TemplateAuditInfoDTO;
import com.scu.dto.TemplateDataAuditInfoDTO;
import com.scu.dto.TemplateDetailedInfoDto;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.result.Result;
import com.scu.service.AuditService;
import com.scu.service.TemplateDataService;
import com.scu.service.TemplateFieldService;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TemplateDataService templateDataService;
    @Operation(summary = "审核员审核新建模板接口",description = "审核员审核新建模板接口")
    @PostMapping("/auditNewTemplate")
    public Result passAudit(@Parameter(description = "审核信息DTO") @RequestBody TemplateAuditInfoDTO templateAuditInfoDTO){
        // 审核
        int re= auditService.auditNewTemplate(templateAuditInfoDTO);
        return Result.success();
    }
    @Operation(summary = "获取所有待审核模板")
    @GetMapping("/getTemp")
    public Result auditTemplateData(){
        // 获取所有待审核模板
        LambdaQueryWrapper<Template> wrapper =
                Wrappers.lambdaQuery(Template.class)
                .eq(Template::getState, TemplateStatusConstant.UNAUDITED);
        List<Template> tmp_unaudited = templateService.list(wrapper);
        List<TemplateDetailedInfoDto> templateToAuditDtos=new ArrayList<>();
        for (Template template : tmp_unaudited){
            TemplateDetailedInfoDto templateToAuditDto = TemplateDetailedInfoDto.builder()
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

    @Operation(summary = "获取所有待审核模板数据信息")
    @GetMapping("/getTempData/{templateId}")
    public Result auditTemplateData(@PathVariable("templateId") Long temlateId){
        List<Map<String, Object>> auditedTemplateData = templateDataService.getAllUnAuditedTemplateData(temlateId);
        return Result.success(auditedTemplateData);
    }
    @Operation(summary = "数据审核员审核模板数据")
    @PostMapping("/auditTemplateData")
    public Result auditTemplateData(@Parameter(description = "审核信息DTO") @RequestBody List<TemplateDataAuditInfoDTO> templateDataAuditInfoDTOs){
        // 获取所有待审核模板
        auditService.auditTemplateData(templateDataAuditInfoDTOs);
        return Result.success();
    }

}
