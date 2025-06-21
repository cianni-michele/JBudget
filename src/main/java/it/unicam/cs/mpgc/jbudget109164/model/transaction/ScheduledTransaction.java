package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * A functional interface representing a scheduled transaction that can generate a list of transactions
 * based on a defined schedule within a specified date range.
 */
@FunctionalInterface
public interface ScheduledTransaction {

    /**
     * Generates a list of transactions based on the schedule defined by this ScheduledTransaction
     * within the specified date range.
     *
     * @param from the start date of the range
     * @param to the end date of the range
     * @return a list of transactions generated for the specified date range
     */
    List<Transaction> generate(LocalDate from, LocalDate to);
}
