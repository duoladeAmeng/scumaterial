package com.scu.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
@Schema(description="模板审核日志")
public class TemplateAuditLog {
    @Schema(description="日志id")
    private Long id;
    @Schema(description="日志时间")
    private LocalDateTime logDate;
    @Schema(description="审核员id")
    private Long auditorId;
    @Schema(description="审核结果")
    private Integer auditResult;
    @Schema(description="审核备注，如审核不通过的原因")
    private String note;
    @Schema(description="所审核的模板的id")
    private Long templateId;
}
