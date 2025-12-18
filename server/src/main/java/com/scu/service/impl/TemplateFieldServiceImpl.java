package com.scu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.dto.TemplateFieldDto;
import com.scu.entity.TemplateField;
import com.scu.mapper.TemplateFieldMapper;
import com.scu.service.TemplateFieldService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateFieldServiceImpl extends ServiceImpl<TemplateFieldMapper, TemplateField> implements TemplateFieldService {
    @Override
    public void saveTemplateFields(List<TemplateFieldDto> templateFieldDtos,Long templateId) {
        List<TemplateField> templateFields =
                templateFieldDtos.stream()
                .map(
                        templateFieldDto -> TemplateField
                                .builder()
                                .fieldName(templateFieldDto.getFieldName())
                                .fieldCategory(templateFieldDto.getFieldCategory())
                                .dataType(templateFieldDto.getDataType())
                                .templateId(templateId)
                                .build()
                )
                .collect(Collectors.toList());
        this.saveBatch(templateFields);
    }

    /**
     * 根据模板id获取模板字段
     * @param templateId 模板id
     * @return 模板字段列表
     */
    @Override
    public List<TemplateField> getTemplateFieldsByTemplateId(Long templateId) {
        LambdaQueryWrapper<TemplateField> wrapper =
                Wrappers.lambdaQuery(TemplateField.class)
                        .eq(TemplateField::getTemplateId, templateId);
        return list(wrapper);
    }

    @Override
    public void deleteTemplateFieldsByTemplateId(Long templateId) {
        LambdaQueryWrapper<TemplateField> wrapper = Wrappers.lambdaQuery(TemplateField.class)
                .eq(TemplateField::getTemplateId, templateId);
        remove(wrapper);
    }

}
