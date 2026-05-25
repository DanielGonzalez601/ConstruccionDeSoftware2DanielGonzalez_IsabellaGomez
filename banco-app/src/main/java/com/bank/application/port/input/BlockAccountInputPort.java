package com.bank.application.port.input;

/** 
* PUERTO DE ENTRADA - Bloquear cuenta 
* Definir el contrato para bloquear cuentas 
*/
public interface BlockAccountInputPort {
    void blockAccount(String accountNumber);
}
