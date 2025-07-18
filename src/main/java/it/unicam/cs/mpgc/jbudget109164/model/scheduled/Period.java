package it.unicam.cs.mpgc.jbudget109164.model.scheduled;

import java.time.LocalDate;

public record Period(LocalDate from, LocalDate to) {

    public Period {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    public static Period of(LocalDate from, LocalDate to) {
        return new Period(from, to);
    }

}
