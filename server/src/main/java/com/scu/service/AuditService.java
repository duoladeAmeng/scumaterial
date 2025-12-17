package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.AuditInfoDTO;
import com.scu.entity.AuditLog;

public interface AuditService extends IService<AuditLog> {
    void auditNewTemplate(AuditInfoDTO auditInfoDTO);
}
