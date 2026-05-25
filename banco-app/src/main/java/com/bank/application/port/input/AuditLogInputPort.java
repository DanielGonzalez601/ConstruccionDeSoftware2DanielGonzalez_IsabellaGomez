package com.bank.application.port.input;

import com.bank.application.dto.BankingDto.*;
import java.util.List;

/**
* PUERTO DE ENTRADA DE LA APLICACIÓN - AuditLogInputPort
* Define las operaciones de lectura en el registro de auditoría inmutable.
*/
public interface AuditLogInputPort {
    List<AuditLogResponse> getAllLogs();
    List<AuditLogResponse> getLogsByProductId(String productId);
    List<AuditLogResponse> getLogsByUserId(Long userId);
}
