package com.bank.application.usecase;

import com.bank.model.*;
import com.bank.repository.*;
import com.bank.service.AuthService;

/**
 * BLOCK ACCOUNT USE CASE
 * Responsable de bloquear cuentas bancarias
 */
public class BlockAccountUseCase {

    private final AccountRepository accountRepository;
    private final DomainEventPublisher eventPublisher;

    public BlockAccountUseCase(AccountRepository accountRepository,
                               DomainEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Bloquear una cuenta
     * @param accountNumber número de cuenta a bloquear
     */
    public void execute(String accountNumber) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST);

        BankAccount account = findAccountOrThrow(accountNumber);
        account.block("Bloqueo manual por analista interno");
        
        accountRepository.save(account);
        eventPublisher.publishAll(account.pullDomainEvents());

        System.out.printf("[BLOCK_ACCOUNT] La cuenta %s ha sido bloqueada por el analista%n", accountNumber);
    }

    private BankAccount findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new DomainException("Cuenta no encontrada: " + accountNumber));
    }
}
