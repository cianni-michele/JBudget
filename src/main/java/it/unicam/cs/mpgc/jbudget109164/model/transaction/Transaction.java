package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.List;
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
    UUID getId();

    /**
     * Returns the details of the transaction.
     *
     * @return the details of the transaction
     */
    TransactionDetails getDetails();

    /**
     * Returns the description of the transaction.
     *
     * @return the description of the transaction
     */
    String getDescription();

    /**
     * Returns the date of the transaction.
     *
     * @return the date of the transaction
     */
    LocalDate getDate();

    /**
     * Returns the tags associated with the transaction.
     *
     * @return a set of tags associated with the transaction
     */
    Set<Tag> getTags();

    /**
     * Checks if the transaction is tagged by a specific tag.
     *
     * @param tag the tag to check
     * @return true if the transaction is tagged by the specified tag, false otherwise
     */
    boolean isTaggedBy(Tag tag);

    /**
     * Returns the list of movements associated with the transaction.
     *
     * @return a list of movements associated with the transaction
     */
    List<Movement> getMovements();

    /**
     * Adds a movement to the transaction's movements list.
     * @param movement the movement to be added
     */
    void addMovement(Movement movement);

    /**
     * Removes a movement from the transaction's movements list at a specific index.
     * @param index the index of the movement to be removed
     */
    void removeMovement(int index);

    /**
     * Sets a movement at a specific index in the transaction's movements list.
     * @param index the index at which to set the movement
     * @param movement the movement to set at the specified index
     */
    void setMovement(int index, Movement movement);

    /**
     * Returns the number of movements associated with the transaction.
     *
     * @return the number of movements
     */
    int movementsCount();

    /**
     * Adds a tag to the transaction.
     *
     * @param tag the tag to be added
     */
    void addTag(Tag tag);

    /**
     * Returns the balance of the transaction, which is the sum of all movements' amounts.
     *
     * @return the balance of the transaction
     */
    double getBalance();
}
