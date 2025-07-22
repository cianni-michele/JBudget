package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.temporal.Temporal;
import java.util.UUID;

/**
 * Factory interface for creating budgets.
 * This interface defines methods to create a new budget with specified details,
 * optionally with a specific ID or a randomly generated one.
 *
 * @param <P> the type of the period for the budget, which must extend {@link Temporal}.
 * @author Michele Cianni
 */
public interface BudgetFactory<P extends Temporal> {

    /**
     * Creates a new budget with the specified details and a randomly generated ID.
     *
     * @param details the details of the budget to create
     * @return a new Budget instance
     */
    default Budget<P> createBudget(BudgetDetails<P> details) {
        return createBudget(UUID.randomUUID(), details);
    }

    /**
     * Creates a new budget with the specified ID and details.
     *
     * @param id      the unique identifier for the budget
     * @param details the details of the budget to create
     * @return a new Budget instance with the specified ID and details
     */
    Budget<P> createBudget(UUID id, BudgetDetails<P> details);

}
