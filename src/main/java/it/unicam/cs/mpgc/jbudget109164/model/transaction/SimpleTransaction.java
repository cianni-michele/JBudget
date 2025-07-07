package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.*;

/**
 * An instance of this class represents a simple transaction, which includes a unique identifier,
 * a description, an amount, a date, and a set of tags associated with the transaction.
 * <p>
 * This class implements the {@link Transaction} interface.
 *
 * @author Michele Cianni
 * @see Transaction
 */
public class SimpleTransaction implements Transaction {

    private final UUID id;
    private final TransactionDetails details;


    /**
     * Constructs a SimpleTransaction with the specified parameters.
     *
     * @param id the unique identifier of the transaction
     * @param description the description of the transaction
     * @param date the date of the transaction
     * @param tags the set of tags associated with the transaction
     * @param movements the list of movements associated with the transaction
     */
    public SimpleTransaction(UUID id,
                             String description,
                             LocalDate date,
                             Set<Tag> tags,
                             List<Movement> movements) {
        this(id, new TransactionDetails(description, date, tags, movements));
    }

    /**
     * Constructs a SimpleTransaction with the specified parameters.
     *
     * @param id      the unique identifier of the transaction
     * @param details the details of the transaction, including description, amount, date, and tags
     * @throws NullPointerException if any of the parameters are null
     */
    public SimpleTransaction(UUID id, TransactionDetails details) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.details = Objects.requireNonNull(details, "Transaction details cannot be null");
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public TransactionDetails getDetails() {
        return details;
    }

    @Override
    public String getDescription() {
        return details.description();
    }

    @Override
    public LocalDate getDate() {
        return details.date();
    }

    @Override
    public Set<Tag> getTags() {
        return details.tags();
    }

    @Override
    public boolean isTaggedBy(Tag tag) {
        return details.hasTag(tag);
    }

    @Override
    public List<Movement> getMovements() {
        return details.movements();
    }

    @Override
    public void addMovement(Movement movement) {
        details.addMovement(movement);
    }

    @Override
    public void removeMovement(int index) {
        details.removeMovement(index);
    }

    @Override
    public void setMovement(int index, Movement movement) {
        details.setMovement(index, movement);
    }

    @Override
    public int movementsCount() {
        return details.movementCount();
    }

    @Override
    public void addTag(Tag tag) {
        details.addTag(tag);
    }

    @Override
    public double getBalance() {
        return details.totalAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTransaction that = (SimpleTransaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SimpleTransaction[" +
               "id=" + id + ", " +
               "details=" + details + ']';
    }
}
