package com.banco.app.domain.services;

import com.banco.app.domain.models.BankAccount;
import com.banco.app.domain.ports.AccountPort;
import com.banco.app.domain.exceptions.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateAccount {

    private final AccountPort accountPort;

    public CreateAccount(AccountPort accountPort) {
        this.accountPort = accountPort;
    }

    public void createAccount(BankAccount account) {

        Optional<BankAccount> existingAccount = accountPort.findByAccountNumber(account.getAccountNumber());

        if (existingAccount.isPresent()) {
            throw new BusinessException("La cuenta ya existe.");
        }

        accountPort.save(account);
    }
}
