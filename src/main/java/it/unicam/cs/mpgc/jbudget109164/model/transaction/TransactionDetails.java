package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record TransactionDetails(String description, double amount, LocalDate date, Set<Tag> tags) {
    public TransactionDetails(String description, double amount, LocalDate date, Set<Tag> tags) {
        this.description = Objects.requireNonNullElse(description, "");
        this.amount = amount;
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.tags = Objects.requireNonNullElse(tags, Collections.emptySet());
    }
}