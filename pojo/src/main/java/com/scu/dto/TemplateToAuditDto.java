package com.scu.dto;

import com.scu.entity.TemplateField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateToAuditDto {
    private Long templateId;
    private String templateName;
    private List<TemplateField> templateFields;
}
