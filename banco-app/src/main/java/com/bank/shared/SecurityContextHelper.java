package com.bank.shared;

import com.bank.domain.exception.AccessDeniedException;
import com.bank.domain.model.entity.User;
import com.bank.domain.model.valueobject.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

/**
 * SHARED UTILITY - SecurityContextHelper
 *
 * Proporciona acceso conveniente al usuario autenticado desde el contexto de Spring Security.
 * Se utiliza en casos de uso para aplicar el control de acceso basado en roles.
 */
public class SecurityContextHelper {

    private SecurityContextHelper() {}

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof User user) return user;
        return null;
    }

    public static void requireLogin() {
        if (getCurrentUser() == null)
            throw new AccessDeniedException("Debe estar autenticado para realizar esta operación.");
    }

    public static void requireAnyRole(UserRole... allowedRoles) {
        requireLogin();
        User current = getCurrentUser();
        boolean hasRole = Arrays.stream(allowedRoles)
            .anyMatch(role -> role == current.getRole());
        if (!hasRole) {
            throw new AccessDeniedException(
                "Acceso denegado. Tu Rol '" + current.getRole().name()
                + "' no está autorizado. Requerido: " + Arrays.toString(allowedRoles));
        }
    }
}
