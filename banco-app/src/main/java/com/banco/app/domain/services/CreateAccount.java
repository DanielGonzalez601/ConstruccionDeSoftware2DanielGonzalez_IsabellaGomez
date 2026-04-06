package com.banco.app.domain.services;

import com.banco.app.domain.models.BankAccount;
import com.banco.app.domain.ports.AccountPort;
import com.banco.app.domain.exceptions.BusinessException;

import java.util.Optional;

public class CreateAccount {

    private final AccountPort accountPort;

    public CreateAccount(AccountPort accountPort) {
        this.accountPort = accountPort;
    }

    public void createAccount(BankAccount account) {

        Optional<BankAccount> existingAccount = accountPort.findByAccountNumber(account.getAccountNumber());

        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new BusinessException("Se requiere número de cuenta.");
        }

        if (existingAccount.isPresent()) {
            throw new BusinessException("La cuenta ya existe.");
        }

        accountPort.save(account);
    }
}
