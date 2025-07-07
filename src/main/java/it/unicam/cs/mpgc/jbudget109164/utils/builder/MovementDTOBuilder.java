package it.unicam.cs.mpgc.jbudget109164.utils.builder;

import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;

public final class MovementDTOBuilder {

    private static final MovementDTOBuilder INSTANCE = new MovementDTOBuilder();

    private Double amount;

    private String description;

    private AccountDTO account;

    private MovementDTOBuilder() {
    }

    public static MovementDTOBuilder getInstance() {
        return INSTANCE;
    }

    public MovementDTOBuilder copyOf(MovementDTO transaction) {
        this.amount = transaction.amount();
        this.description = transaction.description();
        this.account = transaction.account();
        return this;
    }
    
    public MovementDTOBuilder withAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public MovementDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public MovementDTOBuilder withAccount(AccountDTO account) {
        this.account = account;
        return this;
    }

    public MovementDTO build() {
        MovementDTO result = new MovementDTO(amount, description, account);
        reset();
        return result;
    }

    private void reset() {
        this.amount = null;
        this.description = null;
        this.account = null;
    }
}
