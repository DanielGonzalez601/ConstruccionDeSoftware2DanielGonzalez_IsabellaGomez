package com.banco.app.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class AuditLog {

    private String auditLogId;
    private String operationType;
    private LocalDateTime operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;

    public AuditLog(String auditLogId, String operationType,
                    LocalDateTime operationDateTime, Long userId,
                    String userRole, String affectedProductId,
                    Map<String, Object> detailData) {

        this.auditLogId = auditLogId;
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.userId = userId;
        this.userRole = userRole;
        this.affectedProductId = affectedProductId;
        this.detailData = detailData;
    }

    @Override
    public String toString() {
        return "AuditLog{auditLogId='" + auditLogId +
               "', operationType='" + operationType +
               "', userId=" + userId +
               ", affectedProductId='" + affectedProductId +
               "', dateTime=" + operationDateTime + "}";
    }
}