package com.banco.app.domain.ports;

import com.banco.app.domain.models.Transfer;

public interface TransferPort {
    Transfer findByTransferId(int transferId);
    void save(Transfer transfer);
    void update(Transfer transfer);
}
