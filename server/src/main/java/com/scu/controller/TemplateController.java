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

    @Operation(summary = "根据模板分类获取模板")
    @GetMapping("/getTemplateByCategory/{category_id}")
    public Result getTemplateByCategory(@PathVariable("category_id") Integer category_id){
        List<Template> templates = templateService.getTemplateByCategory(category_id);
        return Result.success(templates);
    }

    @Operation(summary = "申请创建模板",description = "用户发起创建模板请求")
    @PostMapping("/create")
    public Result createTemplate(@RequestBody TemplateDto templateDro){
        templateService.createTemplate(templateDro);
        return Result.success();
    }


}
