package com.bank.application.port.input;

import com.bank.model.BankAccount;
import java.util.List;
import java.util.Optional;

/** 
* PUERTO DE ENTRADA - Recuperar cuenta 
* Definir el contrato para consultar cuentas. 
*/
public interface RetrieveAccountInputPort {
    Optional<BankAccount> getAccountByNumber(String accountNumber);
    List<BankAccount> getAccountsByOwner(String ownerIdentification);
    List<BankAccount> getAllAccounts();
    BankAccount loadAccount(String accountNumber);
}
