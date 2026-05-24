package com.bank.service;

import com.bank.model.*;
import com.bank.repository.UserRepository;
import com.bank.repository.SqliteUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * APPLICATION SERVICE — Autenticación y autorización (DDD).
 * Usa SHA-256 + salt (Java built-in) en lugar de BCrypt externo.
 */
public class AuthService {

    private static User currentUser = null;
    private final UserRepository userRepo;

    public AuthService() {
        this.userRepo = new SqliteUserRepository();
    }

    public User login(String identificationNumber, String password) {
        User user = userRepo.findByIdentification(identificationNumber)
            .orElseThrow(() -> new DomainException("Usuario no encontrado: " + identificationNumber));

        if (user.getStatus() == UserStatus.BLOCKED)
            throw new DomainException("La cuenta está BLOQUEADA. Contacte al administrador.");
        if (user.getStatus() == UserStatus.INACTIVE)
            throw new DomainException("La cuenta está INACTIVA. Contacte al administrador.");
        if (!checkPassword(password, user.getPasswordHash()))
            throw new DomainException("Credenciales inválidas.");

        currentUser = user;
        System.out.println("\n[AUTH] Iniciar sesión: " + user.getFullName() + " | Rol: " + user.getRole().getDisplayName());
        return user;
    }

    public void logout() {
        if (currentUser != null)
            System.out.println("\n[AUTH] Cerrar sesión: " + currentUser.getFullName());
        currentUser = null;
    }

    public static User getCurrentUser()    { return currentUser; }
    public static boolean isLoggedIn()     { return currentUser != null; }

    public static void requireLogin() {
        if (!isLoggedIn())
            throw new DomainException("Debe estar autenticado para realizar esta operación.");
    }

    public static void requireRole(UserRole... allowedRoles) {
        requireLogin();
        if (!currentUser.hasRole(allowedRoles))
            throw new DomainException("Acceso denegado. Requerido: "
                + Arrays.toString(allowedRoles) + " | Tu rol: " + currentUser.getRole());
    }

    /**
     * Genera hash SHA-256 + salt aleatorio.
     * Formato: base64(salt):base64(hash)
     * @param plainPassword contraseña en texto plano
     * @return hash SHA-256 codificado en base64
     */
    public static String hashPassword(String plainPassword) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(salt) + ":"
                 + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    private static boolean checkPassword(String plainPassword, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) return false;
            byte[] salt         = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] actualHash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }
}
