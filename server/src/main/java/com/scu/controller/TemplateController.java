package com.scu.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.dto.TemplateDto;
import com.scu.entity.Template;
import com.scu.entity.TemplateField;
import com.scu.exception.BaseException;
import com.scu.result.Result;
import com.scu.service.TemplateFieldService;
import com.scu.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/template")
@Tag(name = "模板相关接口",description = "普通用户申请创建模板，根据模板分类获取模板，删除模板，更新模板等操作")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateFieldService templateFieldService;

    @Operation(summary = "申请创建模板",description = "用户发起创建模板请求")
    @PostMapping("/create")
    public Result createTemplate(@RequestBody TemplateDto templateDro){
        templateService.createTemplate(templateDro);
        return Result.success();
    }

    @Operation(summary = "根据模板分类获取模板")
    @GetMapping("/getTemplateByCategory/{category_id}")
    public Result getTemplateByCategory(@PathVariable("category_id") Integer category_id){
        List<Template> templates = templateService.getTemplateByCategory(category_id);
        return Result.success(templates);
    }

    //TODO 删除模板
    @Operation(summary = "删除模板",description = "用户发起删除模板请求，需等待管理员审核")
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> templateIds) {
        templateService.removeBatchByIds(templateIds);
        // TODO 删除其他表中的模板信息
        return null;
    }

    //TODO 更新模板
    @Operation(summary = "用户更新模板信息接口",description = "更新结果需要等待管理员审核")
    @PostMapping("/update")
    public Result update(@RequestBody TemplateDto templateDto){
        return null;
    }

    //获取模板对应的excel文件
    @Operation(summary = "获取模板对应的excel文件",description = "根据模板id,获取模板对应的excel文件")
    @GetMapping("/getTemplateExcel/{template_id}")
    public ResponseEntity<InputStreamResource> getTemplateExcel(@PathVariable("template_id") Integer templateId) {
        Template template = templateService.getById(templateId);
        // 获取该模板的所有字段
        List<TemplateField> fields = templateFieldService.list(
                Wrappers.lambdaQuery(TemplateField.class)
                        .eq(TemplateField::getTemplateId, templateId)
                );
        //生成 Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(template.getName());
            // 创建表头行
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            for (int i = 0; i < fields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields.get(i).getFieldName());
                cell.setCellStyle(headerStyle);
            }
            // 自动调整列宽
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
        } catch (IOException e) {
            throw new BaseException("生成Excel失败");
        }

        // 构建响应
        String fileName = template.getName() + "_模板.xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + encodeFileName(fileName));
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    // 处理中文文件名编码（防止乱码）
    private String encodeFileName(String fileName) {
        try {
            return java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e) {
            return fileName;
        }
    }

}
