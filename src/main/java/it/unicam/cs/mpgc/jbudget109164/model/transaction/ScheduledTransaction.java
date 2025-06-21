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
     * Generates a list of transactions that are scheduled within the specified period.
     *
     * @param period the period within which to generate transactions
     * @return a list of transactions that will occur within the specified period
     */
    List<Transaction> generate(Period period);

    /**
     * Generates a list of transactions that are scheduled between the specified dates.
     *
     * @param from the start date of the period
     * @param to   the end date of the period
     * @return a list of transactions that will occur between the specified dates
     */
    default List<Transaction> generate(LocalDate from, LocalDate to) {
        return generate(Period.of(from, to));
    }
}
