package com.bank.application.dto;

import com.bank.domain.model.valueobject.UserRole;
import com.bank.domain.model.valueobject.UserStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * APPLICATION LAYER - DTOs
 * Simple data containers to carry data between layers.
 * They shield the domain from external formats (JSON, HTTP, etc.).
 */
public class UserDto {

    // ─── Commands (Input) ───────────────────────────────────────────────

    public record RegisterUserCommand(
        @NotBlank(message = "El nombre completo es requerido")
        String fullName,

        @NotBlank(message = "El número de identificación es requerido")
        String identificationNumber,

        @NotBlank(message = "l email es requerido")
        @Email(message = "l email debe ser válido")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 caracteres")
        String phone,

        LocalDate birthDate,

        @NotBlank(message = "La dirección es requerida")
        String address,

        @NotNull(message = "Se requiere rol")
        UserRole role,

        @NotBlank(message = "Se requiere contraseña")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        String companyId
    ) {}

    public record UpdateUserStatusCommand(
        @NotNull(message = "Se requiere estado")
        UserStatus status
    ) {}

    // ─── Responses (Output) ─────────────────────────────────────────────

    public record UserResponse(
        Long id,
        String fullName,
        String identificationNumber,
        String email,
        String phone,
        LocalDate birthDate,
        String address,
        UserRole role,
        UserStatus status,
        String companyId
    ) {}

    // ─── Auth ────────────────────────────────────────────────────────────

    public record LoginCommand(
        @NotBlank(message = "El número de identificación es requerido")
        String identificationNumber,

        @NotBlank(message = "La contraseña es requerida")
        String password
    ) {}

    public record LoginResponse(
        String token,
        String tokenType,
        Long userId,
        String fullName,
        UserRole role
    ) {}
}
