package com.scu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@TableName("template_field_data_type_enum")
@Data
@Schema(description = "模板字段的枚举数据类型")
@Builder
public class TemplateFieldDataTypeEnum {
    Integer id;
    String name;
    Integer sup;
    Long templateId;
}
