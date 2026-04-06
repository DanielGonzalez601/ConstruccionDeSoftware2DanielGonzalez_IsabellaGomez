package com.banco.app.domain.ports;

import com.banco.app.domain.models.BankAccount;

public interface AccountPort {

    Optional <BankAccount> findByAccountNumber (String accountNumber);
    void save(BankAccount account);
}
