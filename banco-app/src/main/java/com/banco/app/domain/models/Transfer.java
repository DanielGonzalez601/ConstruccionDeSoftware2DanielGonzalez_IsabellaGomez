package com.banco.app.domain.models;

import com.banco.app.domain.enums.TransferStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Transfer {

    private int transferId;
    private String originAccount;
    private String destinationAccount;
    private double amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private TransferStatus status;
    private String creatorUserId;
    private String approverUserId;

    public Transfer(int transferId, String originAccount,
                    String destinationAccount, double amount,
                    LocalDateTime creationDate, LocalDateTime approvalDate,
                    TransferStatus status, String creatorUserId,
                    String approverUserId) {

        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creationDate = creationDate;
        this.approvalDate = approvalDate;
        this.status = status;
        this.creatorUserId = creatorUserId;
        this.approverUserId = approverUserId;
    }

    @Override
    public String toString() {
        return "Transfer{transferId='" + transferId +
               "', originAccount='" + originAccount +
               "', destinationAccount='" + destinationAccount +
               "', amount=" + amount +
               ", status=" + status + "}";
    }
}