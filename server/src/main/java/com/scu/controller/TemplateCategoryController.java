package com.scu.controller;

import com.scu.result.Result;
import com.scu.service.TemplateCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "模板分类接口")
@RestController
@RequestMapping("/templateCategory")
public class TemplateCategoryController {

    @Autowired
    private TemplateCategoryService templateCategoryService;

    @Operation(summary = "获取所有模板分类")
    @GetMapping("/getAllCategory")
    public Result getAllCategory(){
        return Result.success(templateCategoryService.list());
    }
}
