package com.scu.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;


@Schema(description = "模板数据查询条件DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDataConditionDto {
    @Schema(description = "模板id")
    private Long templateId;
    @Schema(description = "查询条件。fieldName:fieldValue")
    private LinkedHashMap<String, String> conditions;
}
