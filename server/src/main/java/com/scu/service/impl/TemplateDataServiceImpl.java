package com.scu.service.impl;

import com.scu.dto.TemplateDataDto;
import com.scu.service.TemplateDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TemplateDataServiceImpl implements TemplateDataService {
    private final JdbcTemplate jdbcTemplate;

    public TemplateDataServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveTemplateDataSingle(List<TemplateDataDto> dtos) {

        // 获取第一个 templateId 作为基准
        Long templateId = dtos.get(0).getTemplateId();
        // 构造动态表名
        String tableName = "template_data_" + templateId;

        // 校验并提取字段名与值
        Map<String, Object> columnValueMap = new LinkedHashMap<>();
//        columnValueMap.put("template_id", templateId);

        for (TemplateDataDto dto : dtos) {
            String fieldName = dto.getFieldName();
            columnValueMap.put(fieldName, dto.getFieldValue()); // fieldValue 可为 null
        }

        // 构造 SQL
        String columns = String.join(", ", columnValueMap.keySet());
        String placeholders = columnValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        // 提取参数值（顺序必须和 keySet() 一致）
        List<Object> args = new ArrayList<>(columnValueMap.values());

        // 执行插入
        jdbcTemplate.update(sql, args.toArray());

    }


}