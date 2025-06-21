package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Tag;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * Factory interface for creating transactions.
 * This interface allows for the creation of transactions with a specific date, amount, description, and associated tags.
 */
@FunctionalInterface
public interface TransactionFactory {

    /**
     * Creates a new transaction with the specified parameters.
     * @param date the date of the transaction
     * @param amount the amount of the transaction
     * @param description the description of the transaction
     * @param tags the tags associated with the transaction
     * @return a new Transaction object
     */
    default Transaction createTransaction(LocalDate date, double amount, String description, Set<Tag> tags) {
        return createTransaction(new TransactionDetails(description, amount, date, tags));
    }

    /**
     * Creates a new transaction based on the provided transaction details.
     *
     * @param transactionDetails the details of the transaction, including description, amount, date, and tags
     * @return a new Transaction object
     */
    default Transaction createTransaction(TransactionDetails transactionDetails) {
        return createTransaction(UUID.randomUUID(), transactionDetails);
    }

    /**
     * Creates a new transaction with a specific ID and the provided transaction details.
     *
     * @param id the unique identifier for the transaction
     * @param transactionDetails the details of the transaction, including description, amount, date, and tags
     * @return a new Transaction object with the specified ID
     */
    Transaction createTransaction(UUID id, TransactionDetails transactionDetails);
}
