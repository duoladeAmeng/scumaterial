package com.scu.service.impl;

import com.scu.dto.TemplateDataDto;
import com.scu.service.TemplateDataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    @Override
    public void saveTemplateDataBatch(MultipartFile file, Long templateId) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // 获取表头行
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();

            // 读取所有数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow == null) continue;

                // 构造TemplateDataDto列表
                List<TemplateDataDto> dtos = new ArrayList<>();


                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = dataRow.getCell(j);
                    String fieldName = headerRow.getCell(j).getStringCellValue();
                    fieldName=fieldName.split("\\(")[0];
                    Object cellValue = getCellValue(cell);

                    TemplateDataDto dto = TemplateDataDto
                             .builder()
                            .templateId(templateId)
                            .fieldName(fieldName)
                            .fieldValue(cellValue)
                            .build();
                    dtos.add(dto);

                }

                // 调用已有的单条保存方法
                if (!dtos.isEmpty()) {
                    saveTemplateDataSingle(dtos);
                }
            }

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("批量导入模板数据失败", e);
        }
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}