package com.scu.dto;

//import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Schema(description = "模板信息DTO")
@Data
@Builder
public class TemplateDto {
    @Schema(description = "模板名")
    String name;
    @Schema(description = "模板所属的类别")
    Integer categoryId;
    @Schema(description = "模板描述")
    String description;
    @Schema(description = "模板创建者")
    String creator;
}
