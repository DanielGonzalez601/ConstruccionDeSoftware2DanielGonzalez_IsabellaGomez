package com.bank.domain.model.aggregate;

import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.exception.InvalidLoanStateTransitionException;
import com.bank.domain.model.valueobject.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
* RAÍZ DE AGREGADO DE DOMINIO - Préstamo
*
* Gestiona todo el ciclo de vida de un préstamo, desde la solicitud hasta el desembolso.
* Aplica todas las transiciones de estado y las reglas de negocio.
*/
public class Loan {

    private Long id;
    private String loanType;
    private String clientId;
    private Money requestedAmount;
    private Money approvedAmount;
    private BigDecimal interestRate;
    private int termMonths;
    private LoanStatus status;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String disbursementAccountNumber;
    private Long creatorUserId;
    private Long analystUserId;

    private Loan() {}

    /**
     * Fábrica de reconstitución: restaura un préstamo desde la persistencia SIN ejecutar verificaciones de invariantes.
     * Utilizado ÚNICAMENTE por el mapeador de persistencia.
     */
    public static Loan reconstitute(Long id, String clientId, String loanType,
                                     Money requestedAmount, Money approvedAmount,
                                     BigDecimal interestRate, int termMonths,
                                     LoanStatus status, LocalDate approvalDate,
                                     LocalDate disbursementDate, String disbursementAccountNumber,
                                     Long creatorUserId, Long analystUserId) {
        Loan loan = new Loan();
        loan.id = id;
        loan.clientId = clientId;
        loan.loanType = loanType;
        loan.requestedAmount = requestedAmount;
        loan.approvedAmount = approvedAmount;
        loan.interestRate = interestRate;
        loan.termMonths = termMonths;
        loan.status = status;
        loan.approvalDate = approvalDate;
        loan.disbursementDate = disbursementDate;
        loan.disbursementAccountNumber = disbursementAccountNumber;
        loan.creatorUserId = creatorUserId;
        loan.analystUserId = analystUserId;
        return loan;
    }

    /**
     * Fábrica de dominio: crea una nueva solicitud de préstamo en estado UNDER_REVIEW.
     */
    public static Loan request(String clientId, String loanType, Money requestedAmount,
                                int termMonths, String disbursementAccountNumber, Long creatorUserId) {
        if (clientId == null || clientId.isBlank())
            throw new DomainValidationException("Se requiere identificación de cliente para solicitar un préstamo.");
        if (loanType == null || loanType.isBlank())
            throw new DomainValidationException("Se requiere tipo de préstamo.");
        if (requestedAmount == null || requestedAmount.isZeroOrNegative())
            throw new DomainValidationException("Se requiere monto solicitado positivo.");
        if (termMonths <= 0)
            throw new DomainValidationException("El plazo debe ser mayor que cero meses.");

        Loan loan = new Loan();
        loan.clientId = clientId.trim();
        loan.loanType = loanType.trim();
        loan.requestedAmount = requestedAmount;
        loan.termMonths = termMonths;
        loan.disbursementAccountNumber = disbursementAccountNumber;
        loan.creatorUserId = creatorUserId;
        loan.status = LoanStatus.UNDER_REVIEW;
        return loan;
    }

    // ============ Reglas de negocio / Transiciones de estado ============

    public void approve(Money approvedAmount, BigDecimal interestRate, int termMonths, Long analystUserId) {
        if (this.status != LoanStatus.UNDER_REVIEW)
            throw new InvalidLoanStateTransitionException("El préstamo solo se puede aprobar desde el estado UNDER_REVIEW. Actual: " + this.status);
        if (approvedAmount == null || approvedAmount.isZeroOrNegative())
            throw new DomainValidationException("Se requiere monto aprobado positivo.");
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainValidationException("Se requiere tasa de interés positiva.");

        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths > 0 ? termMonths : this.termMonths;
        this.analystUserId = analystUserId;
        this.approvalDate = LocalDate.now();
        this.status = LoanStatus.APPROVED;
    }

    public void reject(Long analystUserId) {
        if (this.status != LoanStatus.UNDER_REVIEW)
            throw new InvalidLoanStateTransitionException("El préstamo solo se puede rechazar desde el estado UNDER_REVIEW. Actual: " + this.status);
        this.analystUserId = analystUserId;
        this.status = LoanStatus.REJECTED;
    }

    public void disburse() {
        if (this.status != LoanStatus.APPROVED)
            throw new InvalidLoanStateTransitionException("El préstamo solo se puede desembolsar desde el estado APROBADO. Actual: " + this.status);
        if (disbursementAccountNumber == null || disbursementAccountNumber.isBlank())
            throw new DomainValidationException("Se requiere número de cuenta para el desembolso.");
        if (approvedAmount == null || approvedAmount.isZeroOrNegative())
            throw new DomainValidationException("No se puede desembolsar: el monto aprobado no está establecido o es cero.");

        this.disbursementDate = LocalDate.now();
        this.status = LoanStatus.DISBURSED;
    }

    public boolean isUnderReview() { return status == LoanStatus.UNDER_REVIEW; }
    public boolean isApproved() { return status == LoanStatus.APPROVED; }
    public boolean isDisbursed() { return status == LoanStatus.DISBURSED; }

    // ============ Getters & Setters ============

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Money getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(Money requestedAmount) { this.requestedAmount = requestedAmount; }
    public Money getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(Money approvedAmount) { this.approvedAmount = approvedAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }
    public String getDisbursementAccountNumber() { return disbursementAccountNumber; }
    public void setDisbursementAccountNumber(String disbursementAccountNumber) { this.disbursementAccountNumber = disbursementAccountNumber; }
    public Long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(Long creatorUserId) { this.creatorUserId = creatorUserId; }
    public Long getAnalystUserId() { return analystUserId; }
    public void setAnalystUserId(Long analystUserId) { this.analystUserId = analystUserId; }
}
