package com.banco.app.domain.ports;

import com.banco.app.domain.models.CreditLoan;

public interface LoanPort {
    CreditLoan findByLoanId(String loanId);
    void save(CreditLoan loan);
}
