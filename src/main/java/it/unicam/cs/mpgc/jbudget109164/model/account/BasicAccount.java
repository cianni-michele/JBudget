package it.unicam.cs.mpgc.jbudget109164.model.account;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a basic implementation of an account with essential account information.
 *
 * @author Michele Cianni
 * @see Account
 */
public class BasicAccount implements Account {

    private final UUID id;

    private final AccountInfo accountInfo;

    /**
     * Creates a new BasicAccount with the specified ID and account information.
     *
     * @param id             the unique identifier of the account
     * @param name           the name of the account
     * @param description    the description of the account
     * @param type           the accountType of the account
     * @param openingBalance the opening balance of the account
     */
    public BasicAccount(UUID id,
                        String name,
                        String description,
                        AccountType type,
                        double openingBalance) {
        this(id, new AccountInfo(type, name, description, openingBalance));
    }

    /**
     * Creates a new BasicAccount with the specified ID and account information.
     *
     * @param id          the unique identifier of the account
     * @param accountInfo the account information containing name, description, accountType, and opening balance
     */
    public BasicAccount(UUID id, AccountInfo accountInfo) {
        this.id = validateAndGetId(id);
        this.accountInfo = validateAndGetAccountInfo(accountInfo);
    }

    private UUID validateAndGetId(UUID id) {
        return Objects.requireNonNull(id, "ID cannot be null");
    }

    private AccountInfo validateAndGetAccountInfo(AccountInfo accountInfo) {
        return Objects.requireNonNull(accountInfo, "AccountInfo cannot be null");
    }

    /**
     * Returns the account information, which includes name, description, accountType, and opening balance.
     *
     * @return the account information
     */
    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return accountInfo.name();
    }

    @Override
    public String getDescription() {
        return accountInfo.description();
    }

    @Override
    public AccountType getType() {
        return accountInfo.accountType();
    }

    @Override
    public double getInitialBalance() {
        return accountInfo.initialBalance();
    }
}
