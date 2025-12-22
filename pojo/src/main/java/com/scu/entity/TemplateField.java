package com.scu.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Schema(description = "模板字段")
@Data
@Builder
public class TemplateField {
    @Schema(description="")
    @TableId
    private Long id;
    @Schema(description="对应模板id")
    private Long templateId;
    @Schema(description="对象 操作 结果")
    private Integer fieldCategory;
    @Schema(description="字段名")
    private String fieldName;
    @Schema(description="字段数据类型 字符串，数组，日期......")
    private String dataType;
}
