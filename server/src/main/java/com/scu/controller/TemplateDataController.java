package com.scu.controller;

import com.scu.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templateData")
@Tag(name = "模板数据相关结果接口",description = "指模板对应数据库表创建好之后，对该表的上传数据等操作")
public class TemplateDataController {
    //TODO 获取模板数据
    //TODO 添加模板数据 单条方式
    @PostMapping("/addATemplateData")
    public Result addATemplateData(){
        return null;
    }

    //TODO 添加模板数据 excel文件批量方式
    @PostMapping("/addTemplateDataBatch")
    public Result addTemplateDataBatch(){
        return null;
    }
}
