package com.banco.app.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransferExpirationDetail {

    private String expirationReason;
    private LocalDateTime expirationDateTime;
    private String creatorUserId;

    public TransferExpirationDetail(String expirationReason,
                                    LocalDateTime expirationDateTime,
                                    String creatorUserId) {

        this.expirationReason = expirationReason;
        this.expirationDateTime = expirationDateTime;
        this.creatorUserId = creatorUserId;
    }

    @Override
    public String toString() {
        return "TransferExpirationDetail{reason='" + expirationReason +
               "', expirationDateTime=" + expirationDateTime +
               ", creatorUserId='" + creatorUserId + "'}";
    }
}