package com.bank.domain.service;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.exception.InsufficientFundsException;
import com.bank.domain.model.aggregate.BankAccount;
import com.bank.domain.model.aggregate.Transfer;
import com.bank.domain.model.valueobject.Money;

import java.math.BigDecimal;

/**
 * DOMAIN SERVICE - TransferDomainService
 *
 * Contains business logic that spans multiple aggregates (Transfer + BankAccount).
 * A domain service is used when a rule doesn't naturally belong to a single entity.
 * No Spring, JPA, or infrastructure dependencies.
 */
public class TransferDomainService {

    private final BigDecimal approvalThreshold;

    public TransferDomainService(BigDecimal approvalThreshold) {
        this.approvalThreshold = approvalThreshold;
    }

    /**
     * Business rule: Company employee transfers above threshold require approval.
     */
    public boolean requiresApproval(Money amount, boolean isCompanyEmployee) {
        if (!isCompanyEmployee) return false;
        return amount.getAmount().compareTo(approvalThreshold) > 0;
    }

    /**
     * Executes the actual debit/credit between accounts.
     * Validates sufficient funds before moving money.
     */
    public void executeTransfer(Transfer transfer, BankAccount source, BankAccount destination) {
        if (!source.isOperable()) {
            throw new DomainValidationException(
                "Cuenta de origen " + source.getAccountNumber() + " no es operable (estado: " + source.getStatus() + ").");
        }
        if (!source.hasSufficientFunds(transfer.getAmount())) {
            throw new InsufficientFundsException(
                "Fondos insuficientes en la cuenta " + source.getAccountNumber()
                + ". Saldo: " + source.getBalance() + ", Requerido: " + transfer.getAmount());
        }
        source.withdraw(transfer.getAmount());
        destination.credit(transfer.getAmount());
    }

    /**
     * Business rule: Validates that a transfer hasn't expired before approval.
     */
    public void validateNotExpired(Transfer transfer) {
        if (transfer.isExpired()) {
            throw new DomainValidationException(
                "ID de transferencia " + transfer.getId() + " ha expirado después de " + transfer.getMinutesPending() + " minutos sin aprobación.");
        }
    }

    public BigDecimal getApprovalThreshold() {
        return approvalThreshold;
    }
}
