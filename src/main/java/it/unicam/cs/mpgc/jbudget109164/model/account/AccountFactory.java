package it.unicam.cs.mpgc.jbudget109164.model.account;

import java.util.UUID;

public interface AccountFactory {

    /**
     * Creates an account with the given information.
     *
     * @param accountInfo the information for the account
     * @return the created account
     */
    Account createAccount(AccountInfo accountInfo);

    /**
     * Creates an account with a specific ID and information.
     *
     * @param accountId  the ID of the account to create
     * @param accountInfo the information for the account
     * @return the created account
     */
    Account createAccount(UUID accountId, AccountInfo accountInfo);
}
