package com.scu.util;

import com.scu.constant.TemplateFieldCategoryConstant;
import com.scu.entity.TemplateField;
import com.scu.enu.FieldDataTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicTableBuilder {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 根据模板字段动态创建一张统一表（包含对象、操作、结果三类字段）
     *
     * @param templateId      模板ID
     * @param templateFields  所有模板字段（包含 OBJECT / OPERATION / RESULT）
     */
    public void createUnifiedTableForTemplate(Long templateId, List<TemplateField> templateFields) {
        //表名
        String tableName = "template_" + templateId;
        //sql语句
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + tableName + "` (")
                .append("`id` BIGINT PRIMARY KEY AUTO_INCREMENT");

        for (TemplateField field : templateFields) {
            String fieldName = field.getFieldName();
            String sqlType = getSqlType(field.getDataType());
            if (sqlType == null) {
                log.warn("未知字段类型 code={}, 字段名={}, 跳过", field.getDataType(), fieldName);
                continue;
            }
            sql.append(", `").append(fieldName).append("` ").append(sqlType);
        }
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        log.info("执行统一建表SQL: {}", sql);
        try {
            jdbcTemplate.execute(sql.toString());
            log.info("成功创建统一表: {}", tableName);
        } catch (Exception e) {
            log.error("创建统一表失败: {}", tableName, e);
            throw new RuntimeException("动态建统一表失败: " + e.getMessage(), e);
        }
    }

    private String getSqlType(Integer dataTypeCode) {
        FieldDataTypeEnum typeEnum = FieldDataTypeEnum.getByCode(dataTypeCode);
        return typeEnum != null ? typeEnum.getSqlType() : null;
    }
}