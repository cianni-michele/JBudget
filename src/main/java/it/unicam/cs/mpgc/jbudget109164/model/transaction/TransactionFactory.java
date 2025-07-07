package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import java.util.UUID;

/**
 * Factory interface for creating transactions.
 * This interface allows for the creation of transactions with a specific date, amount, description, and associated tags.
 */
public interface TransactionFactory {

    /**
     * Creates a new transaction based on the provided transaction details.
     *
     * @param transactionDetails the details of the transaction, including description, amount, date, and tags
     * @return a new Transaction object
     */
    Transaction createTransaction(TransactionDetails transactionDetails);

    /**
     * Creates a new transaction with a specific ID and the provided transaction details.
     *
     * @param id the unique identifier for the transaction
     * @param transactionDetails the details of the transaction, including description, amount, date, and tags
     * @return a new Transaction object with the specified ID
     */
    Transaction createTransaction(UUID id, TransactionDetails transactionDetails);
}
