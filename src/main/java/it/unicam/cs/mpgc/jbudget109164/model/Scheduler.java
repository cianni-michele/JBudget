package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.transaction.ScheduledTransaction;

import java.time.LocalDate;
import java.util.List;

/**
 * A functional interface representing a schduler that can generate a list of scheduled transactions
 * based on a defined schedule within a specified date range.
 */
@FunctionalInterface
public interface Scheduler {

    /**
     * Generates a list of transactions based on the scheduled transactions within the specified date range.
     *
     * @param from the start date of the range
     * @param to the end date of the range
     * @return a list of transactions generated for the specified date range
     */
    List<ScheduledTransaction> getUpcomingTransactions(LocalDate from, LocalDate to);
}
