package com.scu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "模板实际数据DTO")
public class TemplateDataDto {
    @Schema(description = "数据所属的模板id")
    private Long templateId;
    @Schema(description = "该数据对应的字段名")
    private String fieldName;
    @Schema(description = "该字段对应的值")
    private Object fieldValue;
}