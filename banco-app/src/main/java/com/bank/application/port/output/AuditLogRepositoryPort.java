package com.bank.application.port.output;

import com.bank.domain.model.entity.AuditLog;
import java.util.List;

/**
* PUERTO DE SALIDA DE LA APLICACIÓN - AuditLogRepositoryPort
* Abstrae la persistencia para AuditLog (almacén de documentos NoSQL).
*/
public interface AuditLogRepositoryPort {
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findAll();
    List<AuditLog> findByAffectedProductId(String productId);
    List<AuditLog> findByUserId(Long userId);
}
