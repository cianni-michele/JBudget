package it.unicam.cs.mpgc.jbudget109164.model.account;

import java.util.UUID;

public interface Account {

    /**
     * Returns the unique identifier of the account.
     *
     * @return the unique identifier of the account
     */
    UUID getId();

    /**
     * Returns the name of the account.
     *
     * @return the name of the account
     */
    String getName();

    /**
     * Returns the description of the account.
     *
     * @return the description of the account
     */
    String getDescription();

    /**
     * Returns the accountType of the account.
     *
     * @return the accountType of the account
     */
    AccountType getType();

    /**
     * Returns the opening balance of the account.
     *
     * @return the opening balance of the account
     */
    double getInitialBalance();

}
