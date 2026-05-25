package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.*;
import com.bank.application.port.input.LoanInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ADAPTADOR (Controlador) - LoanController
* Controlador REST para las operaciones del ciclo de vida del préstamo.
*/
@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Proceso de solicitud, aprobación y desembolso de préstamos.")
@SecurityRequirement(name = "bearerAuth")
public class LoanController {

    private final LoanInputPort loanInputPort;

    public LoanController(LoanInputPort loanInputPort) {
        this.loanInputPort = loanInputPort;
    }

    @PostMapping
    @Operation(summary = "Solicita un préstamo",
        description = "Roles: CLIENT_INDIVIDUAL, CLIENT_COMPANY, COMMERCIAL_EMPLOYEE, INTERNAL_ANALYST. " +
            "Employees must provide clientIdentificationNumber.")
    public ResponseEntity<ApiResponse<LoanResponse>> requestLoan(
            @Valid @RequestBody RequestLoanCommand command) {
        return ResponseEntity.status(201)
            .body(ApiResponse.ok("Solicitud de préstamo enviada", loanInputPort.requestLoan(command)));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos", description = "Roles: INTERNAL_ANALYST, COMMERCIAL_EMPLOYEE")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getAllLoans() {
        return ResponseEntity.ok(ApiResponse.ok("Préstamos recuperados.", loanInputPort.getAllLoans()));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener préstamos por estado",
        description = "Valores de estado: UNDER_REVIEW, APPROVED, REJECTED, DISBURSED")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamos recuperados.", loanInputPort.getLoansByStatus(status)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener préstamo por ID")
    public ResponseEntity<ApiResponse<LoanResponse>> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamo encontrado.", loanInputPort.getLoanById(id)));
    }

    @GetMapping("/client/{clientIdentificationNumber}")
    @Operation(summary = "Obtener préstamos por número de identificación del cliente")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getByClient(
            @PathVariable String clientIdentificationNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamos recuperados.",
            loanInputPort.getLoansByClient(clientIdentificationNumber)));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Aprobar un préstamo", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<LoanResponse>> approveLoan(
            @PathVariable Long id,
            @Valid @RequestBody ApproveLoanCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamo aprobado.", loanInputPort.approveLoan(id, command)));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rechazar un préstamo", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<LoanResponse>> rejectLoan(
            @PathVariable Long id,
            @Valid @RequestBody RejectLoanCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamo rechazado.", loanInputPort.rejectLoan(id, command)));
    }

    @PostMapping("/{id}/disburse")
    @Operation(summary = "Desembolsar un préstamo aprobado", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<LoanResponse>> disburseLoan(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Préstamo desembolsado.", loanInputPort.disburseLoan(id)));
    }
}
