package com.bank.service;

import com.bank.model.*;
import com.bank.repository.SqliteUserRepository;
import com.bank.repository.UserRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepo;

    public UserService() {
        this.userRepo = new SqliteUserRepository();
    }

    public User createUser(User user, String plainPassword) {
        validateUser(user);
        if (userRepo.existsByIdentification(user.getIdentificationNumber()))
            throw new DomainException("El número de identificación ya existe: " + user.getIdentificationNumber());
        user.setPasswordHash(AuthService.hashPassword(plainPassword));
        if (user.getStatus() == null) user.setStatus(UserStatus.ACTIVE);
        return userRepo.save(user);
    }

    public User updateUserStatus(int userId, UserStatus newStatus) {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST, UserRole.COMPANY_SUPERVISOR);
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new DomainException("Usuario no encontrado: " + userId));
        user.setStatus(newStatus);
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        AuthService.requireRole(UserRole.INTERNAL_ANALYST);
        return userRepo.findAll();
    }

    public Optional<User> findByIdentification(String idNumber) {
        return userRepo.findByIdentification(idNumber);
    }

    public Optional<User> findById(int userId) {
        return userRepo.findById(userId);
    }

    public List<User> getUsersByCompany(String companyId) {
        return userRepo.findByCompanyId(companyId);
    }

    private void validateUser(User user) {
        if (user.getFullName() == null || user.getFullName().isBlank())
            throw new DomainException("El nombre completo es requerido.");
        if (user.getIdentificationNumber() == null || user.getIdentificationNumber().isBlank())
            throw new DomainException("El número de identificación es requerido.");
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains("."))
            throw new DomainException("El correo electrónico es requerido.");
        if (user.getPhone() == null || user.getPhone().length() < 7 || user.getPhone().length() > 15)
            throw new DomainException("El teléfono debe tener entre 7 y 15 dígitos.");
        if (user.getAddress() == null || user.getAddress().isBlank())
            throw new DomainException("La dirección es requerida.");
        if (user.getRole() == null)
            throw new DomainException("El rol es requerido.");
        if (user.getRole() == UserRole.CLIENT_INDIVIDUAL) {
            if (user.getBirthDate() == null)
                throw new DomainException("La fecha de nacimiento es requerida para clientes individuales.");
            if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() < 18)
                throw new DomainException("El cliente debe tener al menos 18 años.");
        }
    }
}
