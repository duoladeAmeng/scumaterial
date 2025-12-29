package com.scu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Schema(description="模板数据审核信息DTO")
@Builder
public class TemplateDataAuditInfoDTO {
    @Schema(description="审核员id")
    private Long auditorId;
    @Schema(description="审核结果")
    private Integer auditResult;
    @Schema(description="审核备注，如审核不通过的原因")
    private String note;
    @Schema(description="所审核的模板的id")
    private Long templateId;
    @Schema(description="所审核的模板数据id")
    private Long templateDataId;
}

