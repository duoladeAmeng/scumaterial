package com.scu;

import com.scu.mapper.TemplateDataMapper;
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
    @Autowired
    private TemplateDataMapper templateDataMapper;
    @Test
    public void testSELECT(){
        List<Map<String, Object>> auditedTemplateData = templateDataMapper.getAuditedTemplateData(20L);
//        Map<String, Object> auditedTemplateData = templateDataMapper.getAllTemplateData(20L);
        for (Map<String, Object> map:auditedTemplateData){
            System.out.println(map);
        }

//        templateDataMapper.tt(1L);

    }
}
