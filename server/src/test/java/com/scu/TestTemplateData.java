package com.scu;

import com.scu.util.TableOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestTemplateData {
    @Autowired
    private TableOperator tableOperator;
    @Test
    public void testSELECT(){
        Map<String,String> condition = new HashMap<>();
        condition.put("templateId", "19");
        List<Map<String, Object>> maps = tableOperator.queryByCondation(condition);
        for (int i=0;i<maps.size();i++){
            Map<String, Object> stringObjectMap = maps.get(i);
            System.out.println(stringObjectMap);
        }

    }
}
