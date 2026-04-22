package com.banco.app.domain.ports;

import com.banco.app.domain.models.AuditLog;
import java.util.List;

public interface AuditLogPort {
    void save(AuditLog auditLogId);
    List<AuditLog> findByAffectedProductId(String affectedProductId);
    List<AuditLog> findAll();
}
