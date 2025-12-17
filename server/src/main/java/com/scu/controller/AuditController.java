package com.scu.controller;

import com.scu.dto.AuditInfoDTO;
import com.scu.result.Result;
import com.scu.service.AuditService;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "审核模板接口")
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(summary = "审核员审核新建模板接口",description = "审核员审核新建模板接口")
    @PostMapping("/auditNewTemplate")
    public Result passAudit(@RequestBody AuditInfoDTO auditInfoDTO){
        // 审核
        auditService.auditNewTemplate(auditInfoDTO);
        return Result.success();
    }

}
