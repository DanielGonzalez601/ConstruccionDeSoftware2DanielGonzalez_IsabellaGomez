package com.banco.app.domain.models;

import com.banco.app.domain.enums.LoanStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreditLoan {

    private String loanId;
    private String loanType;
    private String clientId;
    private double requestedAmount;
    private double approvedAmount;
    private double interestRate;
    private int termMonths;
    private LoanStatus loanStatus;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;

    public CreditLoan(String loanId, String loanType, String clientId,
                      double requestedAmount, double approvedAmount,
                      double interestRate, int termMonths,
                      LoanStatus loanStatus, LocalDate approvalDate,
                      LocalDate disbursementDate, String disbursementAccountNumber) {

        this.loanId = loanId;
        this.loanType = loanType;
        this.clientId = clientId;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.loanStatus = loanStatus;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.disbursementAccountNumber = disbursementAccountNumber;
    }

    @Override
    public String toString() {
        return "CreditLoan{loanId='" + loanId +
               "', clientId='" + clientId +
               "', status=" + loanStatus +
               ", requestedAmount=" + requestedAmount + "}";
    }
}