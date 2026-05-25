package com.bank.application.port.input;

import com.bank.application.dto.BankingDto.*;
import java.util.List;

/**
* PUERTO DE ENTRADA DE LA APLICACIÓN - AccountInputPort
* Define las operaciones de cuenta expuestas a la capa de interfaz.
*/
public interface AccountInputPort {
    AccountResponse openAccount(OpenAccountCommand command);
    AccountResponse getAccount(String accountNumber);
    List<AccountResponse> getAccountsByOwner(String ownerIdentificationNumber);
    List<AccountResponse> getAllAccounts();
    AccountResponse deposit(String accountNumber, DepositWithdrawCommand command);
    AccountResponse withdraw(String accountNumber, DepositWithdrawCommand command);
    AccountResponse blockAccount(String accountNumber);
    AccountResponse unblockAccount(String accountNumber);
}
