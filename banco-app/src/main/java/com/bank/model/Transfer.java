package com.bank.model;

import com.bank.event.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AGGREGATE ROOT — Transferencia (DDD).
 *
 * Ciclo de vida: PENDING_APPROVAL → EXECUTED / REJECTED / EXPIRED
 */
public class Transfer {

    private static final long EXPIRY_MINUTES = 60;

    private int transferId;
    private String sourceAccount;
    private String destinationAccount;
    private Money amount;
    private LocalDateTime creationDate;
    private LocalDateTime executionDate;
    private TransferStatus status;
    private int creatorUserId;
    private int approverUserId;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Transfer() {}

    /** Factory method — crea una transferencia nueva. */
    public static Transfer create(String sourceAccount, String destinationAccount,
                                   Money amount, int creatorUserId) {
        if (sourceAccount == null || sourceAccount.isBlank()) throw new DomainException("La cuenta de origen es requerida.");
        if (destinationAccount == null || destinationAccount.isBlank()) throw new DomainException("La cuenta de destino es requerida.");
        if (sourceAccount.equals(destinationAccount)) throw new DomainException("Las cuentas de origen y destino no pueden ser la misma.");
        if (amount == null || amount.isZero()) throw new DomainException("El monto de transferencia debe ser mayor que cero.");

        Transfer t = new Transfer();
        t.sourceAccount = sourceAccount;
        t.destinationAccount = destinationAccount;
        t.amount = amount;
        t.creationDate = LocalDateTime.now();
        t.creatorUserId = creatorUserId;
        t.status = TransferStatus.PENDING_APPROVAL;
        return t;
    }

    // -------------------------------------------------------------------------
    // Comportamiento de dominio
    // -------------------------------------------------------------------------

    /** Ejecuta la transferencia directamente (sin aprobación). */
    public void execute(int approverUserId) {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new DomainException("La transferencia no puede ser ejecutada. Estado actual: " + status);
        this.status = TransferStatus.EXECUTED;
        this.executionDate = LocalDateTime.now();
        this.approverUserId = approverUserId;
        domainEvents.add(new TransferExecuted(transferId, sourceAccount, destinationAccount, amount, creatorUserId));
    }

    /** Aprueba la transferencia pendiente. */
    public void approve(int approverUserId) {
        requirePendingApproval("aprobar");
        checkExpiry();
        this.approverUserId = approverUserId;
        this.status = TransferStatus.EXECUTED;
        this.executionDate = LocalDateTime.now();
        domainEvents.add(new TransferExecuted(transferId, sourceAccount, destinationAccount, amount, approverUserId));
    }

    /** Rechaza la transferencia pendiente. */
    public void reject(String reason, int approverUserId) {
        requirePendingApproval("rechazar");
        this.status = TransferStatus.REJECTED;
        this.approverUserId = approverUserId;
        this.executionDate = LocalDateTime.now();
    }

    /** Expira si lleva más de 60 minutos pendiente. Retorna true si fue expirada. */
    public boolean expireIfOverdue() {
        if (this.status != TransferStatus.PENDING_APPROVAL) return false;
        long minutesPending = ChronoUnit.MINUTES.between(creationDate, LocalDateTime.now());
        if (minutesPending >= EXPIRY_MINUTES) {
            this.status = TransferStatus.EXPIRED;
            domainEvents.add(new TransferExpired(transferId, minutesPending));
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Validaciones privadas
    // -------------------------------------------------------------------------

    private void requirePendingApproval(String operation) {
        if (this.status != TransferStatus.PENDING_APPROVAL)
            throw new DomainException("No se puede " + operation + " la transferencia. Estado actual: " + status);
    }

    private void checkExpiry() {
        if (expireIfOverdue())
            throw new DomainException("La transferencia ha expirado y ya no puede ser aprobada.");
    }

    // -------------------------------------------------------------------------
    // Domain Events
    // -------------------------------------------------------------------------

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    public int getTransferId() { return transferId; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public Money getAmount() { return amount; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getExecutionDate() { return executionDate; }
    public TransferStatus getStatus() { return status; }
    public int getCreatorUserId() { return creatorUserId; }
    public int getApproverUserId() { return approverUserId; }

    // Compatibilidad: ConsoleUI usa getAmount() directamente en printf con %.2f
    // Usamos el BigDecimal del Money internamente en los printf de ConsoleUI
    public java.math.BigDecimal getAmountValue() { return amount != null ? amount.getAmount() : java.math.BigDecimal.ZERO; }

    public void setTransferId(int transferId) { this.transferId = transferId; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount = sourceAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    public void setAmount(Money amount) { this.amount = amount; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public void setExecutionDate(LocalDateTime executionDate) { this.executionDate = executionDate; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public void setCreatorUserId(int creatorUserId) { this.creatorUserId = creatorUserId; }
    public void setApproverUserId(int approverUserId) { this.approverUserId = approverUserId; }

    @Override
    public String toString() {
        return String.format("Transfer[id=%d, from=%s, to=%s, amount=%s, status=%s]",
                transferId, sourceAccount, destinationAccount, amount, status);
    }
}
