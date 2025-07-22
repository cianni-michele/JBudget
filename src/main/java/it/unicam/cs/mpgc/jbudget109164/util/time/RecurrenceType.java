package it.unicam.cs.mpgc.jbudget109164.util.time;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum RecurrenceType {
    DAILY(ChronoUnit.DAYS),
    WEEKLY(ChronoUnit.WEEKS),
    MONTHLY(ChronoUnit.MONTHS),
    QUARTERLY(ChronoUnit.MONTHS,3),
    YEARLY(ChronoUnit.YEARS);

    private final ChronoUnit unit;
    private final int amount;

    RecurrenceType(ChronoUnit unit) {
        this(unit, 1);
    }

    RecurrenceType(ChronoUnit unit, int amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public LocalDate nextOccurrence(LocalDate date) {
        return date.plus(amount, unit);
    }
}
