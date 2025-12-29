package com.scu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description="模板数据审核日志")
public class TemplateDataAuditLog {
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
    @Schema(description="所审核的模板数据的id")
    private Long templateDataId;
}