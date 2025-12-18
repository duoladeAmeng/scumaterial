package com.scu.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "模板字段数据类型")
public class TemplateFieldDataType {
    @Schema(description="模板字段类型的id")
    private Integer id;
    @Schema(description="模板字段类型的名称")
    private String name;
}
