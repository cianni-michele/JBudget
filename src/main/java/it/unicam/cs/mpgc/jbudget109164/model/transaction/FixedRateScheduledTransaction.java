package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.*;

/**
 * Represents a fixed-rate scheduled transaction that occurs on a specific day of the month.
 * This transaction is generated at regular intervals (monthly) between a start and end date.
 * <p>
 * This class implements the {@link ScheduledTransaction} interface.
 *
 * @author Michele Cianni
 */
public class FixedRateScheduledTransaction implements ScheduledTransaction {

    private final TransactionFactory transactionFactory;
    private final Period scheduledPeriod;
    private final Set<Tag> tags;
    private final double amount;
    private final String description;
    private final int dayOfMonth;

    /**
     * Creates a new FixedRateScheduledTransaction with the specified parameters.
     *
     * @param transactionFactory the factory used to create transactions
     * @param amount             the amount of the transaction, must be non-negative
     * @param scheduledPeriod    the period during which the transactions are scheduled, must not be null
     * @param description        the description of the transaction, can be null
     * @param tags               the tags associated with the transaction, can be null (will default to an empty set)
     * @param dayOfMonth         the day of the month when the transaction occurs, must be between 1 and 31
     */
    public FixedRateScheduledTransaction(TransactionFactory transactionFactory,
                                         double amount,
                                         Period scheduledPeriod,
                                         String description,
                                         Set<Tag> tags,
                                         int dayOfMonth) {
        this.transactionFactory = transactionFactory;
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.scheduledPeriod = requireNonNull(scheduledPeriod, "Period cannot be null");
        this.description = description;
        this.tags = requireNonNullElse(tags, Collections.emptySet());
        this.dayOfMonth = Math.max(1, Math.min(dayOfMonth, 31)); // Ensure dayOfMonth is between 1 and 31
    }

    @Override
    public List<Transaction> generate(Period period) {
        List<Transaction> result = new ArrayList<>();
        LocalDate current = period.from().withDayOfMonth(Math.min(dayOfMonth, period.from().lengthOfMonth()));

        while (!current.isAfter(period.to())) {
            if (!current.isBefore(scheduledPeriod.from()) && !current.isAfter(scheduledPeriod.to())) {
                Transaction transaction = transactionFactory.createTransaction(current, amount, description, tags);
                result.add(transaction);
            }
            current = current.plusMonths(1).withDayOfMonth(Math.min(dayOfMonth, current.plusMonths(1).lengthOfMonth()));
        }
        return result;
    }
}
