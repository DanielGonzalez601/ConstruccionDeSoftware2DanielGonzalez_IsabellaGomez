package com.banco.app.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApprovalDetail {

    private double approvedAmount;
    private double interestRate;
    private String previousStatus;
    private String newStatus;
    private String analystId;

    public LoanApprovalDetail(double approvedAmount,
                              double interestRate,
                              String previousStatus,
                              String newStatus,
                              String analystId) {

        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.analystId = analystId;
    }

    @Override
    public String toString() {
        return "LoanApprovalDetail{approvedAmount=" + approvedAmount +
               ", interestRate=" + interestRate +
               ", previousStatus='" + previousStatus +
               "', newStatus='" + newStatus +
               "', analystId='" + analystId + "'}";
    }
}