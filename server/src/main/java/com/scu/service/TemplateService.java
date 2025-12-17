package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.TemplateDto;
import com.scu.entity.Template;
import com.scu.result.Result;

import java.util.List;

public interface TemplateService extends IService<Template> {
    List<Template> getTemplateByCategory(Integer categoryId);

    void createTemplate(TemplateDto templateDto);

}
