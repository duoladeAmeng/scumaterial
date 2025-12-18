package com.scu;

import com.scu.entity.TemplateFieldDataType;
import com.scu.mapper.TemplateFieldDataTypeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
@SpringBootTest
public class TestTemplateFieldDataType {
    @Autowired
    private TemplateFieldDataTypeMapper templateFieldDataTypeMapper;

    @Test
    public void testInsert() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                "字符串型",
                "数值型",
                "浮点型",
                "范围型",
                "候选型",
                "枚举型",
                "图片型",
                "文件型",
                "数组型",
                "表格型",
                "容器型",
                "日期型"
        ));
        for (int i = 0; i < list.size(); i++) {
            TemplateFieldDataType templateFieldDataType =
                    TemplateFieldDataType.builder()
                    .name(list.get(i))
                    .build();
            templateFieldDataTypeMapper.insert(templateFieldDataType);
        }
    }
}
