package com.bank.domain.model.valueobject;

import com.bank.domain.exception.DomainValidationException;

import java.util.Objects;

/**
 * Value Object - representa un número de teléfono validado.
 * Immutable, validated on construction.
 */
public final class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        validate(value);
        this.value = value.trim().replaceAll("[^0-9+]", "");
    }

    private void validate(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new DomainValidationException("El número de teléfono es requerido.");
        }
        String digits = phone.trim().replaceAll("[^0-9+]", "");
        if (digits.length() < 7 || digits.length() > 15) {
            throw new DomainValidationException("El número de teléfono debe tener entre 7 y 15 dígitos: " + phone);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
