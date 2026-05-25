package com.bank.application.port.output;

import com.bank.domain.model.aggregate.Loan;
import com.bank.domain.model.valueobject.LoanStatus;
import java.util.List;
import java.util.Optional;

/**
* PUERTO DE SALIDA DE LA APLICACIÓN - LoanRepositoryPort
* Abstrae la persistencia para el agregado de préstamos.
*/
public interface LoanRepositoryPort {
    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findByClientId(String clientId);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findAll();
}
