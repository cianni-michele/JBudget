package it.unicam.cs.mpgc.jbudget109164.utils.builder;

import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;

import java.util.UUID;

public final class AccountDTOBuilder {

    private static final AccountDTOBuilder INSTANCE = new AccountDTOBuilder();

    private UUID id;
    private String type;
    private String name;
    private String description;
    private Double initialBalance;

    private AccountDTOBuilder() {
    }

    public static AccountDTOBuilder getInstance() {
        return INSTANCE;
    }

    public AccountDTOBuilder copyOf(AccountDTO accountDTO) {
        id = accountDTO.id();
        type = accountDTO.type();
        name = accountDTO.name();
        description = accountDTO.description();
        initialBalance = accountDTO.initialBalance();
        return this;
    }

    public AccountDTOBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AccountDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public AccountDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AccountDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AccountDTOBuilder withInitialBalance(Double openingBalance) {
        this.initialBalance = openingBalance;
        return this;
    }
    public AccountDTO build() {
        AccountDTO result = new AccountDTO(id, type, name, description, initialBalance);
        reset();
        return result;
    }

    private void reset() {
        id = null;
        type = null;
        name = null;
        description = null;
        initialBalance = null;
    }
}
