package it.unicam.cs.mpgc.jbudget109164.util.time;

import java.time.LocalDate;
import java.util.function.Consumer;

public record ScheduledPeriod(LocalDate from, LocalDate to, int dayOfMonth, RecurrenceType recurrence) {
    public ScheduledPeriod {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("The 'from' date cannot be after the 'to' date.");
        }
        if (recurrence == null) {
            throw new IllegalArgumentException("Recurrence type cannot be null.");
        }
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new IllegalArgumentException("Day of month must be between 1 and 31.");
        }
    }

    public static ScheduledPeriod of(LocalDate from, LocalDate to, RecurrenceType recurrenceType, int dayOfMonth) {
        return new ScheduledPeriod(from, to, dayOfMonth, recurrenceType);
    }

    public void forEachOccurrence(Consumer<LocalDate> action) {
        LocalDate currentDate = getStartDate();
        while (!currentDate.isAfter(to)) {

            action.accept(currentDate);

            currentDate = recurrence.nextOccurrence(currentDate);
            if (needToAdjustDay()) {
                currentDate = adjustToValidDay(currentDate);
            }
        }
    }

    private LocalDate getStartDate() {
        return adjustToValidDay(from);
    }

    private boolean needToAdjustDay() {
        return recurrence == RecurrenceType.MONTHLY ||
               recurrence == RecurrenceType.QUARTERLY ||
               recurrence == RecurrenceType.YEARLY;
    }

    private LocalDate adjustToValidDay(LocalDate date) {
        int maxDayInMonth = date.lengthOfMonth();
        int actualDay = Math.min(dayOfMonth, maxDayInMonth);
        return date.withDayOfMonth(actualDay);
    }
}
