package com.scu.controller;

import com.scu.dto.TemplateDataDto;
import com.scu.result.Result;
import com.scu.service.TemplateDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/templateData")
@Tag(name = "模板数据相关结果接口",description = "指模板对应数据库表创建好之后，对该表的上传数据等操作")
public class TemplateDataController {

    @Autowired
    private TemplateDataService templateDataService;

    @Operation(summary = "添加模板数据单条方式")
    @Parameter(name = "templateDataDtos",description = "模板数据对象")
    @PostMapping("/addATemplateData")
    public Result addATemplateData(@RequestBody List<TemplateDataDto> templateDataDtos){
        templateDataService.saveTemplateData(templateDataDtos);
        return Result.success();
    }

    //TODO 添加模板数据 excel文件批量方式
    @PostMapping("/addTemplateDataBatch")
    public Result addTemplateDataBatch(){
        return null;
    }
}
