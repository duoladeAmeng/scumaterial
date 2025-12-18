package com.scu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scu.entity.TemplateField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@Builder
public class TemplateDetailedInfoDto {
    private Long templateId;
    private String templateName;
    private Integer templateCategory;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Schema(description = "模板状态")
    private int state;
    @Schema(description = "模板字段")
    private List<TemplateField> templateFields;
}
