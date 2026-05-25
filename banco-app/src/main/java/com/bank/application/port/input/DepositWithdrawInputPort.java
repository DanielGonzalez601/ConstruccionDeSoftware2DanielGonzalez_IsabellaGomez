package com.bank.application.port.input;

import java.math.BigDecimal;

/** 
* PUERTO DE ENTRADA - Depositar y Retirar 
* Definir el contrato para depósitos y retiros. 
*/
public interface DepositWithdrawInputPort {
    void deposit(String accountNumber, BigDecimal amount);
    void withdraw(String accountNumber, BigDecimal amount);
}
