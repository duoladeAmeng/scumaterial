package com.scu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.AuditInfoDTO;
import com.scu.entity.AuditLog;

import java.util.List;

public interface AuditService extends IService<AuditLog> {
    int auditNewTemplate(AuditInfoDTO auditInfoDTO);
}
