package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.*;
import com.bank.application.port.input.AuditLogInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ADAPTADOR (Controlador) - AuditLogController
* Controlador REST para acceso de solo lectura al registro de auditoría inmutable.
*/
@RestController
@RequestMapping("/api/audit-log")
@Tag(name = "Audit Log", description = "Registro de operaciones inmutable (NoSQL documents)")
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

    private final AuditLogInputPort auditLogInputPort;

    public AuditLogController(AuditLogInputPort auditLogInputPort) {
        this.auditLogInputPort = auditLogInputPort;
    }

    @GetMapping
    @Operation(summary = "Obtenga todas las entradas del registro de auditoría.", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAllLogs() {
        return ResponseEntity.ok(ApiResponse.ok("Registro de auditoría recuperado.", auditLogInputPort.getAllLogs()));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Obtenga los registros de auditoría por ID de producto afectado (número de cuenta, ID de préstamo, ID de transferencia).")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.ok("Registros recuperados.", auditLogInputPort.getLogsByProductId(productId)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtenga los registros de auditoría por ID de usuario", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Registros recuperados.", auditLogInputPort.getLogsByUserId(userId)));
    }
}
