package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.transaction.ScheduledTransaction;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class BasicScheduler implements Scheduler, Iterable<ScheduledTransaction> {

    private final List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();

    /**
     * Adds a scheduled transaction to the scheduler.
     *
     * @param transaction the transaction to add
     * @throws NullPointerException if the transaction is null
     */
    public void addScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.add(
                requireNonNull(transaction, "Transaction cannot be null")
        );
    }

    /**
     * Removes a scheduled transaction from the scheduler.
     *
     * @param transaction the transaction to remove
     * @throws NullPointerException if the transaction is null
     */
    public void removeScheduledTransaction(ScheduledTransaction transaction) {
        scheduledTransactions.remove(
                requireNonNull(transaction, "Transaction cannot be null")
        );
    }

    /**
     * Returns a stream of all scheduled transactions.
     *
     * @return a stream of scheduled transactions
     */
    public Stream<ScheduledTransaction> stream() {
        return scheduledTransactions.stream();
    }

    @Override
    public List<ScheduledTransaction> getUpcomingTransactions(LocalDate from, LocalDate to) {
        requireNonNull(from, "From date cannot be null");
        requireNonNull(to, "To date cannot be null");
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        return stream().filter(
                t -> !t.generate(from, to).isEmpty()
        ).toList();
    }

    @Override
    public Iterator<ScheduledTransaction> iterator() {
        return scheduledTransactions.iterator();
    }

    @Override
    public void forEach(Consumer<? super ScheduledTransaction> action) {
        scheduledTransactions.forEach(action);
    }

    @Override
    public Spliterator<ScheduledTransaction> spliterator() {
        return scheduledTransactions.spliterator();
    }
}
