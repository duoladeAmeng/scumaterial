package com.scu.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "模板字段")
@Data
public class TemplateField {
    @Schema(description="")
    private Integer id;
    @Schema(description="")
    private Integer templateId;
    @Schema(description="对象 操作 结果")
    private Integer fieldCatgory;
    @Schema(description="")
    private String fieldName;
}
