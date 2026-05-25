package com.bank.domain.model.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
* ENTIDAD DE DOMINIO - Registro de auditoría
*
* Representa un registro inmutable de una operación importante en el sistema.
* Se almacena de forma similar a NoSQL (documento JSON en el campo de detalles).
* Una vez creados, los registros de auditoría no se pueden modificar.
*/
public class AuditLog {

    private String logId;
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;

    private AuditLog() {}

    /**
     * Fábrica: crea una entrada de registro de auditoría inmutable.
     */
    public static AuditLog record(String operationType, Long userId, String userRole,
                                    String affectedProductId, Map<String, Object> detailData) {
        AuditLog log = new AuditLog();
        log.logId = UUID.randomUUID().toString();
        log.operationType = operationType;
        log.operationDateTime = LocalDateTime.now();
        log.userId = userId;
        log.userRole = userRole;
        log.affectedProductId = affectedProductId;
        log.detailData = detailData;
        return log;
    }

    // Getters only — this is immutable
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getOperationType() { return operationType; }
    public LocalDateTime getOperationDateTime() { return operationDateTime; }
    public Long getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    public String getAffectedProductId() { return affectedProductId; }
    public Map<String, Object> getDetailData() { return detailData; }

    @Override
    public String toString() {
        return "AuditLog{id=" + logId + ", op=" + operationType + ", user=" + userId + ", time=" + operationDateTime + "}";
    }
}
