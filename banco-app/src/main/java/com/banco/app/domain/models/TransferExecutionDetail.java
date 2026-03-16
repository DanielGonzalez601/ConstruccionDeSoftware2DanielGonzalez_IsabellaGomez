package com.banco.app.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferExecutionDetail {

    private double amount;
    private double originBalanceBefore;
    private double originBalanceAfter;
    private double destinationBalanceBefore;
    private double destinationBalanceAfter;

    public TransferExecutionDetail(double amount,
                                   double originBalanceBefore,
                                   double originBalanceAfter,
                                   double destinationBalanceBefore,
                                   double destinationBalanceAfter) {

        this.amount = amount;
        this.originBalanceBefore = originBalanceBefore;
        this.originBalanceAfter = originBalanceAfter;
        this.destinationBalanceBefore = destinationBalanceBefore;
        this.destinationBalanceAfter = destinationBalanceAfter;
    }

    @Override
    public String toString() {
        return "TransferExecutionDetail{amount=" + amount +
               ", originBefore=" + originBalanceBefore +
               ", originAfter=" + originBalanceAfter +
               ", destinationBefore=" + destinationBalanceBefore +
               ", destinationAfter=" + destinationBalanceAfter + "}";
    }
}