package com.bank.service;

import com.bank.model.*;
import com.bank.repository.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * APPLICATION SERVICE — Casos de uso de préstamos (DDD).
 */
public class LoanService {

    private final LoanRepository loanRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepo;
    private final DomainEventPublisher eventPublisher;

    public LoanService() {
        this.loanRepo = new SqliteLoanRepository();
        this.accountRepo = new SqliteAccountRepository();
        this.userRepo = new SqliteUserRepository();
        this.eventPublisher = new AuditLogRepository();
    }

    public Loan requestLoan(String clientId, String loanType, BigDecimal requestedAmount,
                             int termMonths, String disbursementAccount) {
        AuthService.requireRole(
            UserRole.CLIENT_INDIVIDUAL, UserRole.CLIENT_COMPANY,
            UserRole.COMMERCIAL_EMPLOYEE, UserRole.INTERNAL_ANALYST
        );
        User current = AuthService.getCurrentUser();

        if (current.hasRole(UserRole.CLIENT_INDIVIDUAL, UserRole.CLIENT_COMPANY)) {
            if (!current.isOwner(clientId))
                throw new DomainException("Los clientes solo pueden solicitar préstamos por sí mismos.");
        }

        User client = userRepo.findByIdentification(clientId)
            .orElseThrow(() -> new DomainException("Cliente no encontrado: " + clientId));
        if (!client.isOperational())
            throw new DomainException("El cliente no está activo. No se puede solicitar un préstamo.");

        Loan loan = Loan.request(clientId, loanType,
                new Money(requestedAmount, "USD"), termMonths, disbursementAccount, current.getUserId());

        loanRepo.save(loan);
        eventPublisher.publishAll(loan.pullDomainEvents());
        System.out.println("[LOAN] Solicitud enviada.. ID: " + loan.getLoanId() + " | Estado: UNDER_REVIEW");
        return loan;
    }

    public Loan approveLoan(int loanId, BigDecimal approvedAmount, BigDecimal interestRate, int termMonths) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST);
        User analyst = AuthService.getCurrentUser();

        Loan loan = loadLoan(loanId);
        loan.approve(new Money(approvedAmount, "USD"), interestRate, termMonths, analyst.getUserId());

        loanRepo.save(loan);
        eventPublisher.publishAll(loan.pullDomainEvents());
        System.out.println("[LOAN] Préstamo " + loanId + " APROBADO. Cantidad: " + approvedAmount);
        return loan;
    }

    public Loan rejectLoan(int loanId, String reason) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST);
        User analyst = AuthService.getCurrentUser();

        Loan loan = loadLoan(loanId);
        loan.reject(reason, analyst.getUserId());

        loanRepo.save(loan);
        eventPublisher.publishAll(loan.pullDomainEvents());
        System.out.println("[LOAN] Préstamo " + loanId + " RECHAZADO.");
        return loan;
    }

    public Loan disburseLoan(int loanId) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST);
        User analyst = AuthService.getCurrentUser();

        Loan loan = loadLoan(loanId);

        BankAccount disbAccount = accountRepo.findByAccountNumber(loan.getDisbursementAccountNumber())
            .orElseThrow(() -> new DomainException("Cuenta de desembolso no encontrada: " + loan.getDisbursementAccountNumber()));

        if (disbAccount.getStatus() != AccountStatus.ACTIVE)
            throw new DomainException("La cuenta de desembolso no está activa.");
        if (!disbAccount.getOwnerId().equals(loan.getClientId()))
            throw new DomainException("La cuenta de desembolso no pertenece al cliente del préstamo.");

        loan.markAsDisbursed(analyst.getUserId());
        disbAccount.deposit(loan.getApprovedAmount());

        loanRepo.save(loan);
        accountRepo.save(disbAccount);
        eventPublisher.publishAll(loan.pullDomainEvents());
        eventPublisher.publishAll(disbAccount.pullDomainEvents());

        System.out.printf("[LOAN] Préstamo %d DISPERSADO. Cantidad: %s acreditado a %s%n",
                loanId, loan.getApprovedAmount(), disbAccount.getAccountNumber());
        return loan;
    }

    public List<Loan> getLoansByClient(String clientId) {
        AuthService.requireLogin();
        User current = AuthService.getCurrentUser();
        if (current.hasRole(UserRole.CLIENT_INDIVIDUAL, UserRole.CLIENT_COMPANY)) {
            if (!current.isOwner(clientId))
                throw new DomainException("Solo puedes ver tus propios préstamos.");
        }
        return loanRepo.findByClientId(clientId);
    }

    public List<Loan> getAllLoans() {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST, UserRole.COMMERCIAL_EMPLOYEE);
        return loanRepo.findAll();
    }

    public List<Loan> getLoansByStatus(LoanStatus status) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST, UserRole.COMMERCIAL_EMPLOYEE);
        return loanRepo.findByStatus(status);
    }

    private Loan loadLoan(int loanId) {
        return loanRepo.findById(loanId)
            .orElseThrow(() -> new DomainException("Préstamo no encontrado: " + loanId));
    }
}
