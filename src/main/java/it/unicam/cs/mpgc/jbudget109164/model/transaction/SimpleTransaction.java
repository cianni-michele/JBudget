package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Tag;

import java.time.LocalDate;
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
public record SimpleTransaction(
        UUID id,
        String description,
        double amount,
        LocalDate date,
        Set<Tag> tags
) implements Transaction {

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
        this.id = requireNonNull(id, "ID cannot be null");
        this.description = requireNonNullElse(description, "");
        this.amount = amount;
        this.date = requireNonNull(date, "Date cannot be null");
        this.tags = requireNonNullElse(tags, emptySet());
    }
}
