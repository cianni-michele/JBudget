package it.unicam.cs.mpgc.jbudget109164.service.budget;

import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetService<P extends Temporal> {

    List<Budget<P>> getAllBudgets();

    Optional<Budget<P>> getBudget(UUID id);

    Budget<P> createBudget(BudgetDetails<P> budgetDetails);

    Budget<P> updateBudget(UUID id, BudgetDetails<P> budgetDetails);

    void deleteBudget(UUID id);


    /**
     * Returns the total expected amount for a given period.
     *
     * @param period the period for which the expected amount is calculated
     * @return the total expected amount for the specified period
     */
    double getExpectedAmount(P period);

    /**
     * Returns the expected amount for a specific tag in a given period.
     *
     * @param tag the tag for which the expected amount is calculated
     * @param period the period for which the expected amount is calculated
     * @return the expected amount for the specified tag in the given period
     */
    double getExpectedAmount(Tag tag, P period);

}
