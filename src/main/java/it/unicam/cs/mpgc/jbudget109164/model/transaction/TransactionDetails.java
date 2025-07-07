package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.*;

public record TransactionDetails(String description, LocalDate date, Set<Tag> tags, List<Movement> movements) {

    public TransactionDetails(String description, LocalDate date) {
        this(description, date, null, null);
    }

    public TransactionDetails(String description, LocalDate date, Set<Tag> tags, List<Movement> movements) {
        this.description = Objects.requireNonNullElse(description, "");
        this.date = Objects.requireNonNullElse(date, LocalDate.now());
        this.tags = Objects.requireNonNullElse(tags, new HashSet<>());
        this.movements = Objects.requireNonNullElse(movements, new ArrayList<>());
    }

    public void addTag(Tag tag) {
        tags.add(validateAndGetTag(tag));
    }

    public void removeTag(Tag tag) {
        tags.remove(validateAndGetTag(tag));
    }

    boolean hasTag(Tag tag) {
        return tags.contains(validateAndGetTag(tag));
    }

    private Tag validateAndGetTag(Tag tag) {
        return Objects.requireNonNull(tag, "Tag cannot be null");
    }

    public double totalAmount() {
        return movements.stream()
                .mapToDouble(Movement::amount)
                .sum();
    }

    public int movementCount() {
        return movements.size();
    }

    public void addMovement(Movement movement) {
        movements.add(validateAndGetMovement(movement));
    }

    public void removeMovement(int index) {
        movements.remove(validateAndGetIndex(index));
    }

    private int validateAndGetIndex(int index) {
        if (index < 0 || index >= movements.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return index;
    }

    public void setMovement(int index, Movement movement) {
        movements.set(index, validateAndGetMovement(movement));
    }

    private Movement validateAndGetMovement(Movement movement) {
        return Objects.requireNonNull(movement, "Movement cannot be null");
    }

    @Override
    public List<Movement> movements() {
        return Collections.unmodifiableList(movements);
    }

    @Override
    public Set<Tag> tags() {
        return Collections.unmodifiableSet(tags);
    }
}