package it.unicam.cs.mpgc.jbudget109164.repository;

import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Scheduler;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A class implementing this interface represents a data manager that handles transactions and tags.
 * It provides methods to retrieve, add, and remove transactions, as well as to manage tags and budget plans.
 */
public interface DataManager {

    /**
     * Returns a list of all transactions that match the given filter.
     *
     * @param filter a predicate to filter transactions
     * @return a list of transactions that match the filter
     */
    default List<Transaction> getTransactions(Predicate<? super Transaction> filter) {
        return getAllTransactions().stream()
                .filter(filter)
                .toList();
    }

    /**
     * Returns a list of all transactions.
     *
     * @return a list of all transactions
     */
    List<Transaction> getAllTransactions();

    /**
     * Adds a new transaction to the data manager.
     *
     * @param transaction the transaction to add
     */
    void add(Transaction transaction);

    /**
     * Removes a transaction from the data manager.
     *
     * @param id the unique identifier of the transaction to remove
     */
    void removeTransaction(UUID id);

    /**
     * Returns a list of all tags associated with transactions.
     *
     * @return a list of all tags
     */
    List<Tag> getAllTags();

    /**
     * Returns the budget plan associated with this data manager.
     *
     * @return the budget plan
     */
    BudgetPlan getBudgetPlan();

    /**
     * Returns the scheduler associated with this data manager.
     *
     * @return the scheduler
     */
    Scheduler getScheduler();

}
