package com.scu.util;

import com.scu.entity.TemplateField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class TableOpUtil {


    private static JdbcTemplate jdbcTemplate=new JdbcTemplate();

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    // 可选：异步线程池（按需启用）
    private ExecutorService executorService;


    /**
     * 批量创建动态表（同步）
     *
     * @param templateFieldsMap key: templateId, value: 字段列表
     */
    public void createTables(Map<Long, List<TemplateField>> templateFieldsMap) {
        if (templateFieldsMap == null || templateFieldsMap.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, List<TemplateField>> entry : templateFieldsMap.entrySet()) {
            Long templateId = entry.getKey();
            List<TemplateField> fields = entry.getValue();

            // 异步（可选）
            // CompletableFuture.runAsync(() -> doCreateTable(templateId, fields), executorService);

            // 同步（推荐，DDL 快且需事务一致性）
            doCreateTable(templateId, fields);
        }
    }

    private void doCreateTable(Long templateId, List<TemplateField> fields) {
        Assert.notNull(templateId, "templateId 不能为空");
        if (fields == null || fields.isEmpty()) {
            log.warn("templateId={} 字段列表为空，跳过建表", templateId);
            return;
        }

        String tableName = generateTableName(templateId);
        validateTableName(tableName);

        // 构建字段定义
        List<String> columnDefs = new ArrayList<>();
        columnDefs.add("`id` BIGINT AUTO_INCREMENT PRIMARY KEY");

        Set<String> fieldNameSet = new HashSet<>();
        for (TemplateField field : fields) {
            String fieldName = field.getFieldName();
            Integer dataType = field.getDataType();

            // 校验字段名
            if (!validateIdentifier(fieldName)) {
                throw new IllegalArgumentException("非法字段名: " + fieldName);
            }
            if (!fieldNameSet.add(fieldName)) {
                throw new IllegalArgumentException("字段名重复: " + fieldName);
            }

            String sqlType = mapDataTypeToSql(dataType);
            columnDefs.add(String.format("`%s` %s", fieldName, sqlType));
        }

        String sql = String.format("CREATE TABLE IF NOT EXISTS `%s` (%s) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
                tableName, String.join(", ", columnDefs));

        log.info("执行建表 SQL: {}", sql);
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("创建表 {} 失败", tableName, e);
            throw new RuntimeException("动态建表失败: " + tableName, e);
        }
    }

    private String generateTableName(Long templateId) {
        return "template_data_" + templateId;
    }

    private boolean validateIdentifier(String identifier) {
        return identifier != null && IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    private void validateTableName(String tableName) {
        if (!validateIdentifier(tableName)) {
            throw new IllegalArgumentException("非法表名: " + tableName);
        }
    }

    private String mapDataTypeToSql(Integer dataType) {
        if (dataType == null) {
            return "TEXT"; // 默认
        }
        switch (dataType) {
            case 1: // 字符串
                return "VARCHAR(512)";
            case 2: // 整数
                return "BIGINT";
            case 3: // 浮点数
                return "DECIMAL(20,6)";
            case 4: // 日期时间
                return "DATETIME";
            case 5: // 布尔
                return "TINYINT(1)";
            case 6: // 数组/JSON（MySQL 5.7+）
                return "JSON";
            case 7: // 长文本
                return "TEXT";
            default:
                return "TEXT";
        }
    }

    // 可选：提供异步版本
    public void createTablesAsync(Map<Long, List<TemplateField>> templateFieldsMap) {
        if (executorService == null) {
            createTables(templateFieldsMap); // fallback to sync
            return;
        }
        List<CompletableFuture<Void>> futures = templateFieldsMap.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(
                        () -> doCreateTable(entry.getKey(), entry.getValue()), executorService))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}