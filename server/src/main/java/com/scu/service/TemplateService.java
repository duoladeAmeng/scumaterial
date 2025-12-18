package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.TemplateDetailedInfoDto;
import com.scu.dto.TemplateDto;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.result.Result;

import java.util.List;

public interface TemplateService extends IService<Template> {
    List<Template> getTemplateByCategory(Integer categoryId);
    List<TemplateDetailedInfoDto> getDetailedTemplateByCategory(Integer categoryId);
    List<TemplateDetailedInfoDto> getDetailedTemplateByUsername(String username);
    void createTemplate(TemplateDto templateDto);

}
