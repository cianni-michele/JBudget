package it.unicam.cs.mpgc.jbudget109164.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Set<Tag> tags;
    private final double amount;
    private final String description;
    private final int dayOfMonth;

    /**
     * Creates a new FixedRateScheduledTransaction with the specified parameters.
     *
     * @param transactionFactory the factory used to create transactions
     * @param startDate the start date of the transaction schedule, must not be null
     * @param endDate the end date of the transaction schedule, must not be null
     * @param amount the amount of the transaction, must be non-negative
     * @param description the description of the transaction, can be null
     * @param tags the tags associated with the transaction, can be null (will default to an empty set)
     * @param dayOfMonth the day of the month when the transaction occurs, must be between 1 and 31
     */
    public FixedRateScheduledTransaction(TransactionFactory transactionFactory,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         double amount,
                                         String description,
                                         Set<Tag> tags,
                                         int dayOfMonth) {
        this.transactionFactory = transactionFactory;
        this.startDate = requireNonNull(startDate, "Start date cannot be null");
        this.endDate = requireNonNull(endDate, "End date cannot be null");
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.description = description;
        this.tags = requireNonNullElse(tags, Collections.emptySet());
        this.dayOfMonth = Math.max(1, Math.min(dayOfMonth, 31)); // Ensure dayOfMonth is between 1 and 31
    }

    @Override
    public List<Transaction> generate(LocalDate from, LocalDate to) {
        requireNonNull(from, "From date cannot be null");
        requireNonNull(to, "To date cannot be null");
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }
        List<Transaction> result = new ArrayList<>();
        LocalDate current = from.withDayOfMonth(Math.min(dayOfMonth, from.lengthOfMonth()));

        while (!current.isAfter(to)) {
            if (!current.isBefore(startDate) && !current.isAfter(endDate)) {
                Transaction transaction = transactionFactory.createTransaction(current, amount, description, tags);
                result.add(transaction);
            }
            current = current.plusMonths(1).withDayOfMonth(Math.min(dayOfMonth, current.plusMonths(1).lengthOfMonth()));
        }
        return result;
    }
}
