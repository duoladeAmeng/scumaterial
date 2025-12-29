package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.TemplateAuditInfoDTO;
import com.scu.dto.TemplateDataAuditInfoDTO;
import com.scu.entity.TemplateAuditLog;

import java.util.List;

public interface AuditService extends IService<TemplateAuditLog> {
    int auditNewTemplate(TemplateAuditInfoDTO templateAuditInfoDTO);
    TemplateAuditLog getAuditLogByTemplateId(Long templateId);

    void auditTemplateData(List<TemplateDataAuditInfoDTO> templateDataAuditInfoDTOs);
}
