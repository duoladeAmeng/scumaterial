package com.scu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.entity.TemplateField;
import com.scu.mapper.TemplateFieldMapper;
import com.scu.service.TemplateFieldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestMap {

    @Autowired
    private TemplateFieldService templateFieldService;
    @Test
    public void test() {
        LambdaQueryWrapper<TemplateField> wrapper = Wrappers.lambdaQuery(TemplateField.class)
                .likeLeft(TemplateField::getFieldName, "所用实验器材");
//                .eq(TemplateField::getTemplateId, 19);
        templateFieldService.getOne( wrapper);
        System.out.println(templateFieldService.list(wrapper).get(0));

    }

}
