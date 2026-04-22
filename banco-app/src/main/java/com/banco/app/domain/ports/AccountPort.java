package com.banco.app.domain.ports;

import com.banco.app.domain.models.BankAccount;
import java.util.Optional;

public interface AccountPort {

    Optional <BankAccount> findByAccountNumber (String accountNumber);
    void save(BankAccount account);
}
