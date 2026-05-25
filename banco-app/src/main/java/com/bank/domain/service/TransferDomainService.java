package com.bank.domain.service;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.exception.InsufficientFundsException;
import com.bank.domain.model.aggregate.BankAccount;
import com.bank.domain.model.aggregate.Transfer;
import com.bank.domain.model.valueobject.Money;

import java.math.BigDecimal;

/**
* SERVICIO DE DOMINIO - TransferDomainService
*
* Contiene lógica de negocio que abarca múltiples agregados (Transferencia + Cuenta bancaria).
* Se utiliza un servicio de dominio cuando una regla no pertenece naturalmente a una sola entidad.
* No tiene dependencias de Spring, JPA ni de infraestructura.
*/
public class TransferDomainService {

    private final BigDecimal approvalThreshold;

    public TransferDomainService(BigDecimal approvalThreshold) {
        this.approvalThreshold = approvalThreshold;
    }

    /**
    * Regla de negocio: Las transferencias de empleados de la empresa que superen un umbral determinado requieren aprobación.
    */
    public boolean requiresApproval(Money amount, boolean isCompanyEmployee) {
        if (!isCompanyEmployee) return false;
        return amount.getAmount().compareTo(approvalThreshold) > 0;
    }

    /**
     * Regla de negocio: Ejecuta la transacción de débito/crédito entre cuentas.
     * Valida fondos suficientes antes de mover dinero.
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
     * Regla de negocio: Valida que una transferencia no haya expirado antes de su aprobación.
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
