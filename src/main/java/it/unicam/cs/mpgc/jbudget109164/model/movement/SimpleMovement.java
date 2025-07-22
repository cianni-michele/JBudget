package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.EntityWithTags;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a simple financial movement with details about the date, amount,
 * description, and associated tags. This class implements the {@link Movement}
 * interface and provides methods to access and manipulate movement details.
 *
 * @author Michele Cianni
 */
public final class SimpleMovement extends EntityWithTags<MovementDetails> implements Movement {

    private final UUID id;

    private final LocalDate date;

    private final String description;

    private final double amount;

    public SimpleMovement(UUID id, MovementDetails details, Set<Tag> tags) {
        this(id, details.date(), details.description(), details.amount(), tags);
    }

    public SimpleMovement(UUID id, LocalDate date, String description, double amount, Set<Tag> tags) {
        super(tags);
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.date = Objects.requireNonNullElse(date, LocalDate.now());
        this.description = Objects.requireNonNullElse(description, "");
        this.amount = amount;
    }

    @Override
    public Movement copy(MovementDetails details) {
        return new SimpleMovement(id, details.date(), details.description(), details.amount(), tags);
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public MovementDetails getDetails() {
        return new MovementDetails(date, description, amount);
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        SimpleMovement that = (SimpleMovement) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SimpleMovement [id=" + id + ", date=" + date + ", description=" + description + ", amount=" + amount
                + ", tags=" + tags + "]";
    }

    
}
