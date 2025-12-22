package com.scu;

import com.scu.enu.FieldDataTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestTemplateFieldDataType {
    @Test
    public void testInsert() {
        FieldDataTypeEnum[] values = FieldDataTypeEnum.values();
        List<String> list = Arrays.stream(values).map(FieldDataTypeEnum::getName).toList();
        System.out.println(list);
        }
}

