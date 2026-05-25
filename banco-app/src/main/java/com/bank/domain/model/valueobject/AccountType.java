package com.bank.domain.model.valueobject;

/**
 * Value Object - representa el tipo de cuenta bancaria.
 */
public enum AccountType {
    SAVINGS("Savings"),
    CHECKING("Checking"),
    PERSONAL("Personal"),
    BUSINESS("Business");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
