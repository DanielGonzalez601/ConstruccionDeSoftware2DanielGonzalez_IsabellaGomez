package com.bank.domain.service;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.model.aggregate.BankAccount;
import com.bank.domain.model.aggregate.Loan;

/**
 * DOMAIN SERVICE - LoanDisbursementDomainService
 *
 * Handles the business logic of disbursing an approved loan into a bank account.
 * Validates cross-aggregate invariants (loan + account ownership).
 */
public class LoanDisbursementDomainService {

    /**
     * Validates that the disbursement account is valid and belongs to the loan client,
     * then credits the approved amount to the account.
     */
    public void disburse(Loan loan, BankAccount disbursementAccount) {
        if (!disbursementAccount.isActive()) {
            throw new DomainValidationException(
                "Cuenta de desembolso " + disbursementAccount.getAccountNumber()
                + " No es ACTIVO (estado: " + disbursementAccount.getStatus() + ").");
        }
        if (!disbursementAccount.belongsTo(loan.getClientId())) {
            throw new DomainValidationException(
                "Cuenta de desembolso " + disbursementAccount.getAccountNumber()
                + " No pertenece al cliente " + loan.getClientId() + ".");
        }
        // Trigger state transition in aggregate
        loan.disburse();
        // Credit funds to account
        disbursementAccount.credit(loan.getApprovedAmount());
    }
}
