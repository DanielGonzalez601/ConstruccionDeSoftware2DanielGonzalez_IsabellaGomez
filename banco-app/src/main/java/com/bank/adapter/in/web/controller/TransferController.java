package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.*;
import com.bank.application.port.input.TransferInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ADAPTADOR (Controlador) - TransferController
* Controlador REST para operaciones de transferencia, incluyendo el flujo de aprobación.
*/
@RestController
@RequestMapping("/api/transfers")
@Tag(name = "Transfers", description = "Transferencias de fondos con flujo de aprobación para transferencias de alto valor entre empresas.")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferInputPort transferInputPort;

    public TransferController(TransferInputPort transferInputPort) {
        this.transferInputPort = transferInputPort;
    }

    @PostMapping
    @Operation(summary = "Crear una transferencia",
        description = "Roles: CLIENT_INDIVIDUAL, CLIENT_COMPANY, COMPANY_EMPLOYEE, INTERNAL_ANALYST. " +
            "Transferencias de empleados de empresa por encima de $5,000 van al estado PENDING_APPROVAL.")
    public ResponseEntity<ApiResponse<TransferResponse>> createTransfer(
            @Valid @RequestBody CreateTransferCommand command) {
        TransferResponse response = transferInputPort.createTransfer(command);
        return ResponseEntity.status(201).body(ApiResponse.ok("Transferencia creada.", response));
    }

    @GetMapping
    @Operation(summary = "Obtén todas las transferencias", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getAllTransfers() {
        return ResponseEntity.ok(ApiResponse.ok("Transferencias obtenidas.", transferInputPort.getAllTransfers()));
    }

    @GetMapping("/pending")
    @Operation(summary = "Obtener transferencias pendientes (en espera de aprobación)",
        description = "Roles: COMPANY_SUPERVISOR, CLIENT_COMPANY, INTERNAL_ANALYST. Auto-expires stale transfers.")
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getPendingTransfers() {
        return ResponseEntity.ok(ApiResponse.ok("Transferencias pendientes obtenidas.", transferInputPort.getPendingTransfers()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transferencia por ID")
    public ResponseEntity<ApiResponse<TransferResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Transferencia encontrada.", transferInputPort.getTransferById(id)));
    }

    @GetMapping("/account/{accountNumber}")
    @Operation(summary = "Obtener transferencias para una cuenta (origen o destino)")
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getByAccount(
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Transferencias obtenidas.",
            transferInputPort.getTransfersByAccount(accountNumber)));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Aprobar una transferencia pendiente",
        description = "Roles: COMPANY_SUPERVISOR, CLIENT_COMPANY, INTERNAL_ANALYST")
    public ResponseEntity<ApiResponse<TransferResponse>> approveTransfer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Transferencia aprobada y ejecutada.",
            transferInputPort.approveTransfer(id)));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rechazar una transferencia pendiente",
        description = "Roles: COMPANY_SUPERVISOR, CLIENT_COMPANY, INTERNAL_ANALYST")
    public ResponseEntity<ApiResponse<TransferResponse>> rejectTransfer(
            @PathVariable Long id,
            @Valid @RequestBody ApproveRejectTransferCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Transferencia rechazada.",
            transferInputPort.rejectTransfer(id, command)));
    }

    @PostMapping("/process-expired")
    @Operation(summary = "Comprobación de caducidad activada manualmente",
        description = "Se han caducado todas las transferencias PENDING_APPROVAL con más de 60 minutos de antigüedad. Solo para INTERNAL_ANALYST.")
    public ResponseEntity<ApiResponse<Integer>> processExpired() {
        int count = transferInputPort.processExpiredTransfers();
        return ResponseEntity.ok(ApiResponse.ok("Transferencias expiradas: " + count, count));
    }
}
