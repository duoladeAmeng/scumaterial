package com.scu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Schema(description="审核信息DTO")
@Builder
public class AuditInfoDTO {
    @Schema(description="审核员id")
    private Long auditorId;
    @Schema(description="审核结果")
    private Integer auditResult;
    @Schema(description="审核备注，如审核不通过的原因")
    private String note;
    @Schema(description="所审核的模板的id")
    private Long templateId;
}
