package com.scu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "模板字段DTO")
@Data
public class TemplateFieldDto {
    @Schema(description="字段名")
    private String fieldName;
    @Schema(description="字段类别,对象，操作，结果")
    private Integer fieldCategory;
    @Schema(description="字段数据类型 字符串，数字，日期......")
    private Integer dataType;
}
