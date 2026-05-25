package com.bank.domain.service;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.model.aggregate.BankAccount;
import com.bank.domain.model.aggregate.Loan;

/**
* SERVICIO DE DOMINIO - LoanDisbursementDomainService
*
* Gestiona la lógica de negocio para el desembolso de un préstamo aprobado en una cuenta bancaria.
* Valida las invariantes entre agregados (préstamo + titularidad de la cuenta).
*/
public class LoanDisbursementDomainService {

    /**
    * Valida que la cuenta de desembolso sea válida y pertenezca al cliente del préstamo,
    * y luego abona el importe aprobado a la cuenta.
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
        // Abonar fondos a la cuenta
        disbursementAccount.credit(loan.getApprovedAmount());
    }
}
