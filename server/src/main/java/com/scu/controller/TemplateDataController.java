package com.scu.controller;

import com.scu.dto.TemplateDataDto;
import com.scu.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/templateData")
@Tag(name = "模板数据相关结果接口",description = "指模板对应数据库表创建好之后，对该表的上传数据等操作")
public class TemplateDataController {
    //TODO 获取模板数据
    //TODO 添加模板数据 单条方式
    @PostMapping("/addATemplateData")
    public Result addATemplateData(@RequestBody List<TemplateDataDto> templateDataDtos){
//        templateDataDtos.stream().forEach();
        return null;
    }

    //TODO 添加模板数据 excel文件批量方式
    @PostMapping("/addTemplateDataBatch")
    public Result addTemplateDataBatch(){
        return null;
    }
}
