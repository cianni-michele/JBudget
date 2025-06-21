package it.unicam.cs.mpgc.jbudget109164.model;

import java.time.LocalDate;
import java.util.Set;

/**
 * Factory interface for creating transactions.
 * This interface allows for the creation of transactions with a specific date, amount, description, and associated tags.
 */
public interface TransactionFactory {

    /**
     * Creates a new transaction with the specified parameters.
     * @param date the date of the transaction
     * @param amount the amount of the transaction
     * @param description the description of the transaction
     * @param tags the tags associated with the transaction
     * @return a new Transaction object
     */
    Transaction createTransaction(LocalDate date, double amount, String description, Set<Tag> tags);
}
