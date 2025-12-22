package com.scu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.dto.TemplateFieldDto;
import com.scu.entity.TemplateField;
import com.scu.entity.TemplateFieldDataTypeEnum;
import com.scu.enu.FieldDataTypeEnum;
import com.scu.mapper.TemplateFieldDataTypeEnumMapper;
import com.scu.mapper.TemplateFieldMapper;
import com.scu.service.TemplateFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TemplateFieldServiceImpl extends ServiceImpl<TemplateFieldMapper, TemplateField> implements TemplateFieldService {
    @Autowired
    private TemplateFieldDataTypeEnumMapper templateFieldDataTypeEnumMapper;

    @Override
    /**
     * 保存模板字段
     * @param templateFieldDtos 模板字段列表
     * @param templateId 模板id
     */
    @Transactional
    public void saveTemplateFields(List<TemplateFieldDto> templateFieldDtos,Long templateId) {
        //保存模板类型字段
        List<TemplateField> templateFields =
                templateFieldDtos.stream()
                .map(
                        templateFieldDto -> TemplateField
                                .builder()
                                .fieldName(templateFieldDto.getFieldName())//字段名
                                .fieldCategory(templateFieldDto.getFieldCategory())//字段属于对象 操作 结果
                                .dataType(templateFieldDto.getFieldDataType()) //字段数据类型
                                .templateId(templateId)//字段对应的模板id
                                .build()
                )
                .collect(Collectors.toList());
        this.saveBatch(templateFields);
        //特殊处理枚举型，要求前端传入 枚举名:枚举子项名，如"SEASON:SUMMER"
        List<TemplateField> enu_list = templateFields.stream().filter(templateField -> templateField.getDataType().equals(FieldDataTypeEnum.Enumeration.getName())).toList();
        if(enu_list.isEmpty()) return;
        Map<String, List<TemplateField>> listMap = enu_list.stream().collect(Collectors.groupingBy(templateField -> templateField.getFieldName().split(":")[0]));
        for (String s : listMap.keySet()) {
            //获取保存顶级枚举名 如SEASON
            TemplateFieldDataTypeEnum enum_sup = TemplateFieldDataTypeEnum.builder().name(s)
                    .templateId(templateId)
                    .sup(-1)
                    .build();
            templateFieldDataTypeEnumMapper.insert(enum_sup);
            List<TemplateField> templateFieldsOfNowEnum = listMap.get(s);
            //获取保存枚举子项，如SUMMER
            List<TemplateFieldDataTypeEnum> enum_child_list = templateFieldsOfNowEnum.stream().map(
                            templateField -> TemplateFieldDataTypeEnum.builder()
                                    .name(templateField.getFieldName().split(":")[1])
                                    .templateId(templateId)
                                    .sup(enum_sup.getId())
                                    .build())
                    .collect(Collectors.toList());
            templateFieldDataTypeEnumMapper.insert(enum_child_list);
        }

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
