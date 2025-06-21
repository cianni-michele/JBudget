package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import java.time.LocalDate;

/**
 * Represents a period defined by a start date and an end date.
 * The period is valid only if the start date is not null, the end date is not null,
 * and the end date is after the start date.
 * <p>
 * This class is immutable and provides a constructor that enforces these constraints.
 */
public record Period(LocalDate from, LocalDate to) {

    public static Period of(LocalDate from, LocalDate to) {
        return new Period(from, to);
    }

    /**
     * Constructs a Period with the specified start and end dates.
     *
     * @param from the start date of the period, must not be null
     * @param to   the end date of the period, must not be null and must be after the start date
     * @throws NullPointerException     if from or to is null
     * @throws IllegalArgumentException if to is before from
     */
    public Period {
        if (from == null || to == null) {
            throw new NullPointerException("Start and end dates must not be null");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
