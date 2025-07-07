package it.unicam.cs.mpgc.jbudget109164.controller;

import it.unicam.cs.mpgc.jbudget109164.exception.service.AccountNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.exception.service.TransactionNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionDetails;
import it.unicam.cs.mpgc.jbudget109164.service.AccountService;
import it.unicam.cs.mpgc.jbudget109164.service.TagService;
import it.unicam.cs.mpgc.jbudget109164.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class TransactionController {

    private static final Logger LOGGER = LogManager.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    private final AccountService accountService;

    private final TagService tagService;

    public TransactionController(TransactionService transactionService,
                                 AccountService accountService,
                                 TagService tagService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.tagService = tagService;
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        LOGGER.debug("Retrieving all transactions");

        List<Transaction> allTransactions = transactionService.getAllTransactions();

        LOGGER.info("Retrieved {} transactions", allTransactions.size());

        return allTransactions;
    }

    /**
     * Retrieves all transactions for a specific date.
     *
     * @param date the date for which transactions are to be retrieved
     * @return a list of transactions for the specified date
     */
    public List<Transaction> getAllTransactionsByDate(LocalDate date) {
        LOGGER.debug("Retrieving all transactions for date: {}", date);

        List<Transaction> transactions = transactionService.getAllTransactions().stream()
                .filter(transaction -> transaction.getDate().equals(date))
                .toList();

        LOGGER.info("Retrieved {} transactions for date: {}", transactions.size(), date);

        return transactions;
    }

    /**
     * Creates a new transaction with the given description and date.
     *
     * @param description the description of the transaction
     * @param date the date of the transaction
     */
    public void createTransaction(String description, LocalDate date) {
        LOGGER.debug("Creating transaction with description: {}, date: {}", description, date);

        TransactionDetails transactionDetails = new TransactionDetails(description, date);

        Transaction transaction = transactionService.createTransaction(transactionDetails);

        LOGGER.info("Transaction created successfully with ID: {}", transaction.getId());
    }

    /**
     * Updates an existing transaction with the given ID.
     *
     * @param transactionId the ID of the transaction to be updated
     * @param description the new description for the transaction
     * @param date the new date for the transaction
     */
    public void updateTransaction(UUID transactionId, String description, LocalDate date) {
        LOGGER.debug("Updating transaction with ID: {}, new description: {}, new date: {}",
                transactionId, description, date);

        validateTransactionId(transactionId);

        TransactionDetails transactionDetails = new TransactionDetails(description, date);

        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionDetails);

        LOGGER.info("Transaction updated successfully with ID: {}", updatedTransaction.getId());
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to be deleted
     */
    public void deleteTransaction(UUID transactionId) {
        LOGGER.debug("Deleting transaction with ID: {}", transactionId);

        validateTransactionId(transactionId);

        transactionService.deleteTransaction(transactionId);

        LOGGER.info("Transaction with ID {} deleted successfully", transactionId);
    }

    /**
     * Retrieves all movements associated with a specific transaction.
     *
     * @param transactionId the ID of the transaction for which movements are to be retrieved
     * @return a list of movements associated with the specified transaction
     */
    public List<Movement> getAllMovementsOfTransaction(UUID transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(Transaction::getMovements)
                .orElseThrow(() -> {
                    return getTransactionNotFoundException(transactionId);
                });
    }

    /**
     * Adds a movement to a transaction.
     *
     * @param transactionId the ID of the transaction to which the movement will be added
     * @param amount the amount of the movement
     * @param description the description of the movement
     * @param accountId the ID of the account associated with the movement
     */
    public void addMovementToTransaction(UUID transactionId, double amount, String description, UUID accountId) {
        LOGGER.debug("Adding movement to transaction with ID: {}, amount: {}, description: {}, accountId: {}",
                     transactionId, amount, description, accountId);

        validateTransactionId(transactionId);

        validateAccountId(accountId);

        Account account = accountService.getAccountById(accountId).orElseThrow(() -> {
            String message = "Account with ID " + accountId + " not found";
            LOGGER.warn(message);
            return new AccountNotFoundException(message);
        });

        Movement movement = new Movement(amount, description, account);

        Transaction updatedTransaction = transactionService.addMovementToTransaction(transactionId, movement);

        LOGGER.info("Movement added to transaction with ID: {}", updatedTransaction.getId());
    }

    /**
     * Removes a movement from a transaction at the specified index.
     *
     * @param transactionId the ID of the transaction from which the movement will be removed
     * @param index the index of the movement to be removed
     */
    public void removeMovementFromTransaction(UUID transactionId, int index) {
        LOGGER.debug("Removing movement at index {} from transaction with ID: {}", index, transactionId);

        validateTransactionId(transactionId);

        Transaction updatedTransaction = transactionService.removeMovementFromTransaction(transactionId, index);

        LOGGER.info("Movement removed from transaction with ID: {}", updatedTransaction.getId());
    }

    /**
     * Adds a tag to a transaction.
     *
     * @param transactionId the ID of the transaction to which the tag will be added
     * @param tagId the ID of the tag to be added
     */
    public void addTagToTransaction(UUID transactionId, UUID tagId) {
        LOGGER.debug("Adding tag with ID: {} to transaction with ID: {}", tagId, transactionId);

        validateTransactionId(transactionId);
        validateTagId(tagId);

        Tag tag = tagService.getTagById(tagId).orElseThrow(() -> {
            String message = "Tag with ID " + tagId + " not found";
            LOGGER.warn(message);
            return new TagNotFoundException(message);
        });

        Transaction updatedTransaction = transactionService.addTagToTransaction(transactionId, tag);

        LOGGER.info("Tag with ID {} added to transaction with ID: {}", tagId, updatedTransaction.getId());
    }

    private void validateTransactionId(UUID transactionId) {
        if (transactionId == null) {
            String message = "Transaction ID cannot be null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateAccountId(UUID accountId) {
        if (accountId == null) {
            String message = "Account ID cannot be null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateTagId(UUID tagId) {
        if (tagId == null) {
            String message = "Tag ID cannot be null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public Set<Tag> getAllTagsOfTransaction(UUID transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(Transaction::getTags)
                .orElseThrow(() -> getTransactionNotFoundException(transactionId));
    }

    private TransactionNotFoundException getTransactionNotFoundException(UUID transactionId) {
        String message = "Transaction with ID " + transactionId + " not found";
        LOGGER.warn(message);
        return new TransactionNotFoundException(message);
    }
}
