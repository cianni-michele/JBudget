package it.unicam.cs.mpgc.jbudget109164.repository;

import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Scheduler;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An in-memory implementation of the DataManager interface that manages transactions and tags.
 * It provides methods to retrieve, add, and remove transactions, as well as to manage tags and budget plans.
 * <p>
 * This class implements the {@link DataManager} interface and uses a {@link Map} to store transactions.
 *
 * @author Michele Cianni
 */
public class InMemoryDataManager implements DataManager {

    private final Map<UUID, Transaction> transactions = new HashMap<>();
    private final BudgetPlan budgetPlan;
    private final Scheduler scheduler;

    public InMemoryDataManager(BudgetPlan budgetPlan, Scheduler scheduler) {
        this.budgetPlan = budgetPlan;
        this.scheduler = scheduler;
    }


    @Override
    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactions.values());
    }

    @Override
    public void add(Transaction transaction) {
        Optional.ofNullable(transaction)
                .ifPresentOrElse(
                        t -> transactions.put(t.id(), t),
                        () -> {
                            throw new IllegalArgumentException("Transaction cannot be null");
                        }
                );
    }

    @Override
    public void removeTransaction(UUID id) {
        Optional.ofNullable(id)
                .ifPresentOrElse(
                        transactions::remove,
                        () -> {
                            throw new IllegalArgumentException("Transaction ID cannot be null");
                        }
                );
    }


    @Override
    public Set<Tag> getAllTags() {
        return transactions.values().stream()
                .flatMap(t -> t.tags().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public BudgetPlan getBudgetPlan() {
        return budgetPlan;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }
}
