package com.bank.domain.model.aggregate;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.exception.InvalidTransferStateException;
import com.bank.domain.model.valueobject.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
* RAÍZ DE AGREGADO DE DOMINIO - Transferencia
*
* Representa una transferencia de fondos entre cuentas.
* Aplica un flujo de aprobación para transferencias de alto valor entre empresas.
* y expira automáticamente después de 60 minutos sin aprobación.
*/
public class Transfer {

    private Long id;
    private String sourceAccount;
    private String destinationAccount;
    private Money amount;
    private LocalDateTime creationDateTime;
    private LocalDateTime approvalDateTime;
    private TransferStatus status;
    private Long creatorUserId;
    private Long approverUserId;

    private Transfer() {}

    /**
    * Reconstitution factory : restaura una transferencia desde la persistencia.
    * Utilizado ÚNICAMENTE por el mapeador de persistencia.
    */
    public static Transfer reconstitute(Long id, String sourceAccount, String destinationAccount,
                                         Money amount, TransferStatus status,
                                         java.time.LocalDateTime creationDateTime,
                                         java.time.LocalDateTime approvalDateTime,
                                         Long creatorUserId, Long approverUserId) {
        Transfer t = new Transfer();
        t.id = id;
        t.sourceAccount = sourceAccount;
        t.destinationAccount = destinationAccount;
        t.amount = amount;
        t.status = status;
        t.creationDateTime = creationDateTime;
        t.approvalDateTime = approvalDateTime;
        t.creatorUserId = creatorUserId;
        t.approverUserId = approverUserId;
        return t;
    }

    /**
    * Fábrica: crea una transferencia que se ejecuta inmediatamente (no requiere aprobación).
    */
    public static Transfer createDirect(String sourceAccount, String destinationAccount,
                                         Money amount, Long creatorUserId) {
        Transfer t = buildTransfer(sourceAccount, destinationAccount, amount, creatorUserId);
        t.status = TransferStatus.EXECUTED;
        t.approvalDateTime = t.creationDateTime;
        return t;
    }

    /**
     * Fábrica: crea una transferencia pendiente de aprobación del supervisor.
     */
    public static Transfer createPendingApproval(String sourceAccount, String destinationAccount,
                                                   Money amount, Long creatorUserId) {
        Transfer t = buildTransfer(sourceAccount, destinationAccount, amount, creatorUserId);
        t.status = TransferStatus.PENDING_APPROVAL;
        return t;
    }

    private static Transfer buildTransfer(String sourceAccount, String destinationAccount,
                                           Money amount, Long creatorUserId) {
        if (sourceAccount == null || sourceAccount.isBlank())
            throw new DomainValidationException("Se requiere cuenta de origen.");
        if (destinationAccount == null || destinationAccount.isBlank())
            throw new DomainValidationException("Se requiere cuenta de destino.");
        if (amount == null || amount.isZeroOrNegative())
            throw new DomainValidationException("El monto de transferencia debe ser mayor que cero.");

        Transfer t = new Transfer();
        t.sourceAccount = sourceAccount.trim();
        t.destinationAccount = destinationAccount.trim();
        t.amount = amount;
        t.creationDateTime = LocalDateTime.now();
        t.creatorUserId = creatorUserId;
        return t;
    }

    // ============ Reglas de negocio ============

    public void approve(Long approverUserId) {
        assertPendingApproval();
        checkNotExpired();
        this.approverUserId = approverUserId;
        this.approvalDateTime = LocalDateTime.now();
        this.status = TransferStatus.EXECUTED;
    }

    public void reject(Long approverUserId) {
        assertPendingApproval();
        this.approverUserId = approverUserId;
        this.approvalDateTime = LocalDateTime.now();
        this.status = TransferStatus.REJECTED;
    }

    public void expire() {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new InvalidTransferStateException("Solo las transferencias PENDING_APPROVAL pueden expirar.");
        this.status = TransferStatus.EXPIRED;
    }

    public boolean isExpired() {
        if (status != TransferStatus.PENDING_APPROVAL) return false;
        long minutes = ChronoUnit.MINUTES.between(creationDateTime, LocalDateTime.now());
        return minutes >= 60;
    }

    public boolean isPendingApproval() {
        return this.status == TransferStatus.PENDING_APPROVAL;
    }

    public boolean isExecuted() {
        return this.status == TransferStatus.EXECUTED;
    }

    public long getMinutesPending() {
        return ChronoUnit.MINUTES.between(creationDateTime, LocalDateTime.now());
    }

    private void assertPendingApproval() {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new InvalidTransferStateException(
                    "Esta acción requiere el estado PENDING_APPROVAL. Actual: " + this.status);
    }

    private void checkNotExpired() {
        if (isExpired())
            throw new InvalidTransferStateException("La transferencia ha expirado (pendiente por " + getMinutesPending() + " minutos).");
    }

    // ============ Getters & Setters ============

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSourceAccount() { return sourceAccount; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }
    public LocalDateTime getCreationDateTime() { return creationDateTime; }
    public void setCreationDateTime(LocalDateTime creationDateTime) { this.creationDateTime = creationDateTime; }
    public LocalDateTime getApprovalDateTime() { return approvalDateTime; }
    public void setApprovalDateTime(LocalDateTime approvalDateTime) { this.approvalDateTime = approvalDateTime; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public Long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(Long creatorUserId) { this.creatorUserId = creatorUserId; }
    public Long getApproverUserId() { return approverUserId; }
    public void setApproverUserId(Long approverUserId) { this.approverUserId = approverUserId; }
}
