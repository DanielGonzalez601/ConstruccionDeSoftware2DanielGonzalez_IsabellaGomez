package com.bank.util;

import com.bank.model.*;
import com.bank.repository.*;

/**
 * UTILITY - Account Validator
 * Contiene validaciones reutilizables para cuentas bancarias
 */
public class AccountValidator {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountValidator(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    /**
     * Valida que el usuario tenga permiso para abrir una cuenta
     * @param currentUser usuario actual
     * @param ownerIdentification identificación del propietario
     */
    public void validateOpenAccountPermission(User currentUser, String ownerIdentification) {
        if (currentUser.hasRole(UserRole.CLIENT_INDIVIDUAL, UserRole.CLIENT_COMPANY)) {
            if (!currentUser.isOwner(ownerIdentification))
                throw new DomainException("Las clientes solo pueden abrir cuentas por sí mismas.");
        }
    }

    /**
     * Valida que el propietario exista y esté activo
     * @param ownerIdentification identificación del propietario
     * @return el propietario validado
     */
    public User validateOwnerExists(String ownerIdentification) {
        User owner = userRepository.findByIdentification(ownerIdentification)
            .orElseThrow(() -> new DomainException("Cliente no encontrado: " + ownerIdentification));

        if (!owner.isOperational())
            throw new DomainException("No se puede abrir cuenta para un cliente INACTIVO o BLOQUEADO.");

        return owner;
    }

    /**
     * Valida que una cuenta exista
     * @param accountNumber número de cuenta
     * @return la cuenta validada
     */
    public BankAccount validateAccountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new DomainException("Cuenta no encontrada: " + accountNumber));
    }

    /**
     * Valida que el usuario tenga acceso a la cuenta
     * @param currentUser usuario actual
     * @param account cuenta a validar
     */
    public void validateAccountAccess(User currentUser, BankAccount account) {
        if (currentUser.hasRole(UserRole.CLIENT_INDIVIDUAL)) {
            if (!currentUser.isOwner(account.getOwnerId()))
                throw new DomainException("No tiene permiso para acceder a esta cuenta.");
        }
    }

    /**
     * Valida que el monto sea válido
     * @param amount monto a validar
     */
    public void validateAmount(java.math.BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new DomainException("El monto debe ser mayor que cero.");
    }

    /**
     * Valida que la cuenta pueda recibir depósitos
     * @param account cuenta a validar
     */
    public void validateDepositAllowed(BankAccount account) {
        // Validación del estado se realiza en BankAccount.deposit()
    }

    /**
     * Valida que la cuenta pueda realizar retiros
     * @param account cuenta a validar
     */
    public void validateWithdrawalAllowed(BankAccount account) {
        // Validación del estado se realiza en BankAccount.withdraw()
    }

    /**
     * Valida que la cuenta pueda ser bloqueada
     * @param account cuenta a validar
     */
    public void validateBlockAllowed(BankAccount account) {
        // Validación se realiza en BankAccount.block()
    }
}
