package com.bank.application.port.input;

import com.bank.model.AccountType;
import com.bank.model.BankAccount;

/** 
* PUERTO DE ENTRADA - Crear cuenta 
* Definir el contrato para la creación de cuentas. 
*/
public interface CreateAccountInputPort {
    BankAccount openAccount(String ownerIdentification, AccountType accountType, String currency);
}
