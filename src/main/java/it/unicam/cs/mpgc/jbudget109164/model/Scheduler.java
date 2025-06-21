package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.transaction.Period;
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
     * Returns a list of upcoming transactions that are scheduled within the specified period.
     *
     * @param period the period within which to find upcoming transactions
     * @return a list of scheduled transactions that will occur within the specified period
     */
    List<ScheduledTransaction> getUpcomingTransactions(Period period);

    /**
     * Returns a list of upcoming transactions that are scheduled between the specified dates.
     *
     * @param from the start date of the period
     * @param to   the end date of the period
     * @return a list of scheduled transactions that will occur between the specified dates
     */
    default List<ScheduledTransaction> getUpcomingTransactions(LocalDate from, LocalDate to) {
        return getUpcomingTransactions(Period.of(from, to));
    }
}
