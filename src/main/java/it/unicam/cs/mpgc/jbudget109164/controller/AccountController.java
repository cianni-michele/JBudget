package it.unicam.cs.mpgc.jbudget109164.controller;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountInfo;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountType;
import it.unicam.cs.mpgc.jbudget109164.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public final class AccountController {

    private static final Logger LOGGER = LogManager.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    public List<Account> getAllAccounts() {
        LOGGER.debug("Retrieving all accounts");

        List<Account> allAccounts = accountService.getAllAccounts();

        LOGGER.info("Retrieved {} accounts", allAccounts.size());

        return allAccounts;
    }

    public void createAccount(AccountType type, String name, String description, double initialBalance) {
        LOGGER.debug("Creating account with type: {}, name: {}, description: {}, initial balance: {}",
                type, name, description, initialBalance);

        AccountInfo accountInfo = new AccountInfo(type, name, description, initialBalance);

        Account account = accountService.createAccount(accountInfo);

        LOGGER.info("Account created successfully with ID: {}", account.getId());
    }

    public void updateAccount(UUID accountId, AccountType type, String name, String description, double initialBalance) {
        LOGGER.debug("Updating account with ID: {}, type: {}, name: {}, description: {}, initial balance: {}",
                accountId, type, name, description, initialBalance);

        validateId(accountId);

        AccountInfo accountInfo = new AccountInfo(type, name, description, initialBalance);

        Account updatedAccount = accountService.updateAccount(accountId, accountInfo);

        LOGGER.info("Account with ID {} updated successfully", accountId);
    }

    public void deleteAccount(UUID accountId) {
        LOGGER.debug("Deleting account with ID: {}", accountId);

        validateId(accountId);

        accountService.deleteAccount(accountId);

        LOGGER.info("Account with ID {} deleted successfully", accountId);
    }

    private void validateId(UUID accountId) {
        if (accountId == null) {
            String message = "Account ID cannot be null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
