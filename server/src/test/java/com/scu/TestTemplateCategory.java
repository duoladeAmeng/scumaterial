package com.scu;

import com.scu.entity.TemplateCategory;
import com.scu.mapper.TemplateCategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TestTemplateCategory {

    @Autowired
    private TemplateCategoryMapper templateCategoryMapper;
    @Test
    public void testInsert() {
        String [] bigCategory= {"材料基本信息","理性设计与预测","制备与加工","理化及力学性能","体外生物学评价","动物实验评价","临床试验及应用","审评及监管"};

        for (String bigCategoryName:bigCategory){
            TemplateCategory templateCategory = TemplateCategory.builder()
                    .categoryName(bigCategoryName)
                    .supLevel(-1)
                    .createTime(LocalDateTime.now())
                    .build();
            templateCategoryMapper.insert(templateCategory);
        }


        String[][] subCategory = {
                {"计算对象", "计算模型参数", "计算结果"},
                {"试剂原料信息", "制备工艺信息"},
                {"表征信息", "表征原始数据", "表征衍生数据"},
                {"试剂原料信息", "评价信息", "评价原始数据", "评价衍生数据"},
                {"试剂原料信息", "评价信息", "评价原始数据", "评价衍生数据"},
                {"产品患者信息", "评价信息", "评价原始数据", "评价衍生数据"},
                {"产品信息", "评价信息", "评价原始数据", "评价衍生数据"}
        };

        for (int i = 0; i < subCategory.length; i++) {
            for (int j = 0; j < subCategory[i].length; j++) {
                TemplateCategory templateCategory = TemplateCategory.builder()
                        .categoryName(subCategory[i][j])
                        .supLevel(i+2)
                        .createTime(LocalDateTime.now())
                        .build();
                templateCategoryMapper.insert(templateCategory);
            }
        }


    }
}
