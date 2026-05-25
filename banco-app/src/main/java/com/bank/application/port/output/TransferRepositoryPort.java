package com.bank.application.port.output;

import com.bank.domain.model.aggregate.Transfer;
import com.bank.domain.model.valueobject.TransferStatus;
import java.util.List;
import java.util.Optional;

/**
* PUERTO DE SALIDA DE LA APLICACIÓN - TransferRepositoryPort
* Abstrae la persistencia para el agregado Transfer.
*/
public interface TransferRepositoryPort {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(Long id);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findBySourceAccountOrDestinationAccount(String accountNumber);
    List<Transfer> findAll();
}
