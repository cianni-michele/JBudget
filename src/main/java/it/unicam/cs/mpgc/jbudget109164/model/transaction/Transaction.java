package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * A class implementing this interface represents a financial transaction.
 */
public interface Transaction {

    /**
     * Returns the unique identifier of the transaction.
     *
     * @return the UUID of the transaction
     */
    UUID id();

    /**
     * Returns the details of the transaction.
     *
     * @return the details of the transaction
     */
    TransactionDetails details();

    /**
     * Returns the description of the transaction.
     *
     * @return the description of the transaction
     */
    String description();

    /**
     * Returns the amount of the transaction.
     *
     * @return the amount of the transaction
     */
    double amount();

    /**
     * Returns the date of the transaction.
     *
     * @return the date of the transaction
     */
    LocalDate date();

    /**
     * Returns the tags associated with the transaction.
     *
     * @return a set of tags associated with the transaction
     */
    Set<Tag> tags();
}
