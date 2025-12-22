package com.scu.controller;

import com.scu.constant.TemplateFieldCategoryConstant;
import com.scu.enu.FieldDataTypeEnum;
import com.scu.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@Tag(name = "模板字段数据类型接口", description = "模板字段数据类型接口")
@RestController
@RequestMapping("/templateFieldDataType")
public class TemplateFieldDataTypeController {

    @Operation(summary = "获取模板字段数据类型列表", description = "获取模板字段数据类型列表")
    @GetMapping("/list")
    public Result list() {
        FieldDataTypeEnum[] values = FieldDataTypeEnum.values();
        List<String> list = Arrays.stream(values).map(FieldDataTypeEnum::getName).toList();
        return Result.success(list);
    }
}
