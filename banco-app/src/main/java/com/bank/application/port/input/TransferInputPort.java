package com.bank.application.port.input;

import com.bank.application.dto.BankingDto.*;
import java.util.List;

/**
* PUERTO DE ENTRADA DE LA APLICACIÓN - TransferInputPort
* Define las operaciones de transferencia, incluido el flujo de aprobación.
*/
public interface TransferInputPort {
    TransferResponse createTransfer(CreateTransferCommand command);
    TransferResponse approveTransfer(Long transferId);
    TransferResponse rejectTransfer(Long transferId, ApproveRejectTransferCommand command);
    TransferResponse getTransferById(Long transferId);
    List<TransferResponse> getTransfersByAccount(String accountNumber);
    List<TransferResponse> getPendingTransfers();
    List<TransferResponse> getAllTransfers();
    int processExpiredTransfers();
}
