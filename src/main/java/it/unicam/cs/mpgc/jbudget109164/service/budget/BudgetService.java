package it.unicam.cs.mpgc.jbudget109164.service.budget;

import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetService<P extends Temporal> {

    /**
     * Returns all budgets.
     *
     * @return a list of all budgets.
     */
    List<Budget<P>> getAllBudgets();

    /**
     * Returns a budget by its ID.
     *
     * @param id the UUID of the budget.
     * @return an Optional containing the budget if found, or empty if not found.
     */
    Optional<Budget<P>> getBudget(UUID id);

    /**
     * Creates a new budget based on the provided budget details.
     *
     * @param budgetDetails the details of the budget to be created.
     * @return the created budget.
     */
    Budget<P> createBudget(BudgetDetails<P> budgetDetails);

    /**
     * Updates an existing budget with the provided details.
     *
     * @param id the UUID of the budget to be updated.
     * @param budgetDetails the new details for the budget.
     * @return the updated budget.
     */
    Budget<P> updateBudget(UUID id, BudgetDetails<P> budgetDetails);

    /**
     * Deletes a budget by its ID.
     *
     * @param id the UUID of the budget to be deleted.
     */
    void deleteBudget(UUID id);

    /**
     * Returns the actual amount for a given budget period.
     *
     * @param period the period for which the actual amount is requested.
     * @return the actual amount for the specified period.
     */
    double getActualAmount(P period);

    /**
     * Returns the actual amount for a specific tag and period.
     *
     * @param tag the tag for which the actual amount is requested.
     * @param period the period for which the actual amount is requested.
     * @return the actual amount for the specified tag and period.
     */
    double getActualAmount(Tag tag, P period);

}
