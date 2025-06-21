package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Tag;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.*;
import static java.util.Objects.*;

/**
 * An instance of this class represents a simple transaction, which includes a unique identifier,
 * a description, an amount, a date, and a set of tags associated with the transaction.
 * <p>
 * This class implements the {@link Transaction} interface.
 *
 * @author Michele Cianni
 * @see Transaction
 */
public final class SimpleTransaction implements Transaction {

    private final UUID id;
    private final TransactionDetails details;

    /**
     * Constructs a SimpleTransaction with the specified parameters.
     *
     * @param id          the unique identifier of the transaction
     * @param description a brief description of the transaction
     * @param amount      the amount of money involved in the transaction
     * @param date        the date when the transaction occurred
     * @param tags        a set of tags associated with the transaction
     * @throws NullPointerException if any of the parameters are null
     */
    public SimpleTransaction(UUID id,
                             String description,
                             double amount,
                             LocalDate date,
                             Set<Tag> tags) {
        this(id, new TransactionDetails(description, amount, date, tags));
    }

    /**
     * Constructs a SimpleTransaction with the specified parameters.
     *
     * @param id      the unique identifier of the transaction
     * @param details the details of the transaction, including description, amount, date, and tags
     * @throws NullPointerException if any of the parameters are null
     */
    public SimpleTransaction(UUID id, TransactionDetails details) {
        this.id = requireNonNull(id, "ID cannot be null");
        this.details = requireNonNull(details, "Transaction details cannot be null");
    }

    @Override
    public TransactionDetails details() {
        return details;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String description() {
        return details.description();
    }

    @Override
    public double amount() {
        return details.amount();
    }

    @Override
    public LocalDate date() {
        return details.date();
    }

    @Override
    public Set<Tag> tags() {
        return details.tags();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTransaction that = (SimpleTransaction) o;
        return Objects.equals(id, that.id) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return hash(id, details);
    }

    @Override
    public String toString() {
        return "SimpleTransaction{" +
               "id=" + id +
               ", details=" + details +
               '}';
    }
}
