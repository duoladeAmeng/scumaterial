package com.scu.controller;

import com.scu.dto.TemplateDto;
import com.scu.entity.Template;
import com.scu.result.Result;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
@Tag(name = "模板相关接口",description = "模板相关操作")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Operation(summary = "申请创建模板",description = "用户发起创建模板请求")
    @PostMapping("/create")
    public Result createTemplate(@RequestBody TemplateDto templateDro){
        templateService.createTemplate(templateDro);
        return Result.success();
    }

    @Operation(summary = "根据模板分类获取模板")
    @GetMapping("/getTemplateByCategory/{category_id}")
    public Result getTemplateByCategory(@PathVariable("category_id") Integer category_id){
        List<Template> templates = templateService.getTemplateByCategory(category_id);
        return Result.success(templates);
    }

    //TODO 删除模板
    @Operation(summary = "删除模板",description = "用户发起删除模板请求，需等待管理员审核")
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> moduleNames) {
        return null;
    }

    //TODO 更新模板
    @Operation(summary = "用户更新模板信息接口",description = "更新结果需要等待管理员审核")
    @PostMapping("/update")
    public Result update(@RequestBody TemplateDto templateDto){
        return null;
    }

    //TODO 审核模板
    @Operation(summary = "审核员审核模板通过接口",description = "审核员审核模板通过接口")
    @GetMapping("/auditPass/{templateIid}")
    public Result passAudit(@PathVariable Integer templateId){
        templateService.passAudit(templateId);
        return null;
    }


}
