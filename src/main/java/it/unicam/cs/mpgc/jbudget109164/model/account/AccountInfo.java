package it.unicam.cs.mpgc.jbudget109164.model.account;

import java.util.Objects;

/**
 * This record encapsulates the account's name, description, type, and opening balance.
 *
 * @see Account
 * @author Michele Cianni
 */
public record AccountInfo(
        AccountType accountType,
        String name,
        String description,
        double initialBalance
) {

    /**
     * Constructs an AccountInfo instance with the provided parameters.
     *
     * @param accountType    the type of the account, must not be null
     * @param name           the name of the account, must not be null or blank
     * @param description    the description of the account, can be null
     * @param initialBalance the initial balance of the account, must be valid for the given account type
     * @throws NullPointerException     if name or accountType is null
     * @throws IllegalArgumentException if name is blank or initialBalance is not valid for the account type
     */
    public AccountInfo(AccountType accountType, String name, String description, double initialBalance) {
        this.name = validateAndGetName(name);
        this.description = validateAndGetDescription(description);
        this.accountType = validateAndGetAccountType(accountType);
        this.initialBalance = validateAndGetOpeningBalance(initialBalance);
    }

    private String validateAndGetName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        return name;
    }

    private String validateAndGetDescription(String description) {
        return Objects.requireNonNullElse(description, "");
    }

    private AccountType validateAndGetAccountType(AccountType type) {
        return Objects.requireNonNull(type, "Account accountType cannot be null");
    }

    private double validateAndGetOpeningBalance(double initialBalance) {
        if (!accountType.isValidOpeningBalance(initialBalance)) {
            throw new IllegalArgumentException("Opening balance is not valid for the account type: " + accountType);
        }
        return initialBalance;
    }
}
