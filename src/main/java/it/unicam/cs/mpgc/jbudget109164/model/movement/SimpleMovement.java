package it.unicam.cs.mpgc.jbudget109164.model.movement;

import java.util.Objects;
import java.util.UUID;

public final class SimpleMovement implements Movement{

    private final UUID id;

    private final String description;

    private final double amount;

    public SimpleMovement(UUID id, double amount) {
        this(id, null, amount);
    }

    public SimpleMovement(UUID id, String description, double amount) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.description = Objects.requireNonNullElse(description, "");
        this.amount = amount;
    }

    @Override
    public UUID getId() {
        return id;
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
        if (o == null || getClass() != o.getClass()) return false;
        SimpleMovement that = (SimpleMovement) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SimpleMovement{" +
               "id=" + id +
               ", description='" + description + '\'' +
               ", amount=" + amount +
               '}';
    }
}
