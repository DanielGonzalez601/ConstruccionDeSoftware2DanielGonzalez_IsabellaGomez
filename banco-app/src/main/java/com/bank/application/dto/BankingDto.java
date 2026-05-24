package com.bank.application.dto;

import com.bank.domain.model.valueobject.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class BankingDto {

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT DTOs
    // ═══════════════════════════════════════════════════════════

    public record OpenAccountCommand(
        @NotBlank(message = "Se requiere número de identificación del propietario")
        String ownerIdentificationNumber,

        @NotNull(message = "El tipo de cuenta es requerido")
        AccountType accountType,

        @NotBlank(message = "La moneda es requerida")
        String currency
    ) {}

    public record AccountResponse(
        Long id,
        String accountNumber,
        AccountType accountType,
        String ownerId,
        BigDecimal balance,
        String currency,
        AccountStatus status,
        LocalDate openingDate
    ) {}

    public record DepositWithdrawCommand(
        @NotNull(message = "Se requiere cantidad")
        @DecimalMin(value = "0.01", message = "El importe debe ser mayor que cero.")
        BigDecimal amount
    ) {}

    // ═══════════════════════════════════════════════════════════
    // LOAN DTOs
    // ═══════════════════════════════════════════════════════════

    public record RequestLoanCommand(
        @NotBlank(message = "El tipo de préstamo es requerido")
        String loanType,

        @NotNull(message = "El monto solicitado es requerido")
        @DecimalMin(value = "0.01", message = "El monto solicitado debe ser mayor que cero")
        BigDecimal requestedAmount,

        @NotBlank(message = "La moneda es requerida")
        String currency,

        @Min(value = 1, message = "El plazo debe ser al menos 1 mes")
        int termMonths,

        @NotBlank(message = "El número de cuenta de desembolso es requerido")
        String disbursementAccountNumber,

        // For bank employees requesting on behalf of a client
        String clientIdentificationNumber
    ) {}

    public record ApproveLoanCommand(
        @NotNull(message = "El monto aprobado es requerido")
        @DecimalMin(value = "0.01", message = "El monto aprobado debe ser mayor que cero")
        BigDecimal approvedAmount,

        @NotBlank(message = "La moneda es requerida")
        String currency,

        @NotNull(message = "La tasa de interés es requerida")
        @DecimalMin(value = "0.01", message = "La tasa de interés debe ser mayor que cero")
        BigDecimal interestRate,

        @Min(value = 1, message = "El plazo debe ser al menos 1 mes")
        int termMonths
    ) {}

    public record RejectLoanCommand(
        @NotBlank(message = "La razón de rechazo es requerida")
        String reason
    ) {}

    public record LoanResponse(
        Long id,
        String loanType,
        String clientId,
        BigDecimal requestedAmount,
        BigDecimal approvedAmount,
        BigDecimal interestRate,
        int termMonths,
        LoanStatus status,
        LocalDate approvalDate,
        LocalDate disbursementDate,
        String disbursementAccountNumber,
        String currency
    ) {}

    // ═══════════════════════════════════════════════════════════
    // TRANSFER DTOs
    // ═══════════════════════════════════════════════════════════

    public record CreateTransferCommand(
        @NotBlank(message = "La cuenta de origen es requerida")
        String sourceAccount,

        @NotBlank(message = "La cuenta de destino es requerida")
        String destinationAccount,

        @NotNull(message = "El monto es requerido")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
        BigDecimal amount
    ) {}

    public record ApproveRejectTransferCommand(
        @NotBlank(message = "La razón es requerida para el rechazo")
        String reason
    ) {}

    public record TransferResponse(
        Long id,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        String currency,
        LocalDateTime creationDateTime,
        LocalDateTime approvalDateTime,
        TransferStatus status,
        Long creatorUserId,
        Long approverUserId
    ) {}

    // ═══════════════════════════════════════════════════════════
    // AUDIT LOG DTOs
    // ═══════════════════════════════════════════════════════════

    public record AuditLogResponse(
        String logId,
        String operationType,
        LocalDateTime operationDateTime,
        Long userId,
        String userRole,
        String affectedProductId,
        Map<String, Object> detailData
    ) {}

    // ═══════════════════════════════════════════════════════════
    // GENERIC RESPONSES
    // ═══════════════════════════════════════════════════════════

    public record ApiResponse<T>(
        boolean success,
        String message,
        T data
    ) {
        public static <T> ApiResponse<T> ok(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }
        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message, null);
        }
    }
}
