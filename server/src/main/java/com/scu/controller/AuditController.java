package com.scu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.constant.TemplateStateConstant;
import com.scu.dto.AuditInfoDTO;
import com.scu.entity.Template;
import com.scu.result.Result;
import com.scu.service.AuditService;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "审核模板接口")
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;
    @Autowired
    private TemplateService templateService;

    @Operation(summary = "审核员审核新建模板接口",description = "审核员审核新建模板接口")
    @PostMapping("/auditNewTemplate")
    public Result passAudit(@RequestBody AuditInfoDTO auditInfoDTO){
        // 审核
        auditService.auditNewTemplate(auditInfoDTO);
        return Result.success();
    }
    @Operation(summary = "获取所有待审核模板")
    @GetMapping("/getTemp")
    public Result auditTemplateData(){
        LambdaQueryWrapper<Template> wrapper =
                Wrappers.lambdaQuery(Template.class)
                .eq(Template::getState, TemplateStateConstant.UNAUDITED);
        List<Template> list = templateService.list(wrapper);
        return Result.success(list);
    }

}
