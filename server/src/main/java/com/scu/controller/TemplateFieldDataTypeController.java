package com.scu.controller;

import com.scu.result.Result;
import com.scu.service.TemplateFieldDataTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "模板字段数据类型接口", description = "模板字段数据类型接口")
@RestController
@RequestMapping("/templateFieldDataType")
public class TemplateFieldDataTypeController {
    @Autowired
    private TemplateFieldDataTypeService templateFieldDataTypeService;

    @Operation(summary = "获取模板字段数据类型列表", description = "获取模板字段数据类型列表")
    @GetMapping("/list")
    public Result list() {
        return Result.success(templateFieldDataTypeService.list());
    }
}
