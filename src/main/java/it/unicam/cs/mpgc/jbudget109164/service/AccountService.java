package it.unicam.cs.mpgc.jbudget109164.service;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    /**
     * Retrieves all accounts.
     *
     * @return a collection of all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account with the specified ID, or null if not found
     */
    Optional<Account> getAccountById(UUID accountId);

    /**
     * Creates a new account with the provided account information.
     *
     * @param accountInfo the information for the new account
     * @return the created account
     */
    Account createAccount(AccountInfo accountInfo);


    /**
     * Updates an existing account with the provided account information.
     *
     * @param accountId the ID of the account to update
     * @param accountInfo the new information for the account
     * @return the updated account
     */
    Account updateAccount(UUID accountId, AccountInfo accountInfo);


    /**
     * Deletes an account by its ID.
     *
     * @param accountId the ID of the account to delete
     */
    void deleteAccount(UUID accountId);

}
