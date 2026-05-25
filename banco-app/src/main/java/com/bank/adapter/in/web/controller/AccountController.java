package com.bank.adapter.in.web.controller;

import com.bank.application.dto.BankingDto.*;
import com.bank.application.port.input.AccountInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* ADAPTADOR (Controlador) - AccountController
* Controlador REST para operaciones de cuentas bancarias.
*/
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Operaciones de cuentas bancarias")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountInputPort accountInputPort;

    public AccountController(AccountInputPort accountInputPort) {
        this.accountInputPort = accountInputPort;
    }

    @PostMapping
    @Operation(summary = "Abrir una nueva cuenta",
        description = "Roles: TELLER, COMMERCIAL_EMPLOYEE, INTERNAL_ANALYST, CLIENT_INDIVIDUAL, CLIENT_COMPANY")
    public ResponseEntity<ApiResponse<AccountResponse>> openAccount(
            @Valid @RequestBody OpenAccountCommand command) {
        return ResponseEntity.status(201)
            .body(ApiResponse.ok("Cuenta abierta con éxito.", accountInputPort.openAccount(command)));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cuentas", description = "Roles: INTERNAL_ANALYST, TELLER, COMMERCIAL_EMPLOYEE")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        return ResponseEntity.ok(ApiResponse.ok("Cuentas encontradas.", accountInputPort.getAllAccounts()));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Obtener cuenta por número")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Cuenta encontrada.", accountInputPort.getAccount(accountNumber)));
    }

    @GetMapping("/owner/{ownerIdentificationNumber}")
    @Operation(summary = "Obtener cuentas por número de identificación del propietario")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getByOwner(
            @PathVariable String ownerIdentificationNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Cuentas encontradas.",
            accountInputPort.getAccountsByOwner(ownerIdentificationNumber)));
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(summary = "Depositar dinero", description = "Roles: TELLER, INTERNAL_ANALYST")
    public ResponseEntity<ApiResponse<AccountResponse>> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositWithdrawCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Depósito exitoso.",
            accountInputPort.deposit(accountNumber, command)));
    }

    @PostMapping("/{accountNumber}/withdraw")
    @Operation(summary = "Retirar dinero", description = "Roles: TELLER, INTERNAL_ANALYST")
    public ResponseEntity<ApiResponse<AccountResponse>> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositWithdrawCommand command) {
        return ResponseEntity.ok(ApiResponse.ok("Retiro exitoso.",
            accountInputPort.withdraw(accountNumber, command)));
    }

    @PatchMapping("/{accountNumber}/block")
    @Operation(summary = "Bloquear una cuenta", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<AccountResponse>> blockAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Cuenta bloqueada.", accountInputPort.blockAccount(accountNumber)));
    }

    @PatchMapping("/{accountNumber}/unblock")
    @Operation(summary = "Desbloquear una cuenta", description = "INTERNAL_ANALYST only")
    public ResponseEntity<ApiResponse<AccountResponse>> unblockAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.ok("Cuenta desbloqueada.", accountInputPort.unblockAccount(accountNumber)));
    }
}
