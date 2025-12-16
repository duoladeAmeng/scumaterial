package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.TemplateFieldDto;
import com.scu.entity.TemplateField;

import java.util.List;

public interface TemplateFieldService extends IService<TemplateField> {
    void saveTemplateFields(List<TemplateFieldDto> templateFieldDtos,Long templateId);
}
