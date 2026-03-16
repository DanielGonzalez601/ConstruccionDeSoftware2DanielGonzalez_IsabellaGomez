package com.banco.app.domain.models;

import com.banco.app.domain.enums.AccountStatus;
import com.banco.app.domain.enums.TypeAccounts;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BankAccount {

    private String accountNumber;
    private TypeAccounts accountType;
    private String holderId;
    private double currentBalance;
    private String currency;
    private AccountStatus status;
    private LocalDate openingDate;

    public BankAccount(String accountNumber, TypeAccounts accountType,
                       String holderId, String currency) {

        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.holderId = holderId;
        this.currency = currency;
        this.currentBalance = 0.0;
        this.status = AccountStatus.ACTIVE;
        this.openingDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "BankAccount{accountNumber='" + accountNumber +
               "', holderId='" + holderId +
               "', balance=" + currentBalance +
               ", status=" + status + "}";
    }
}