package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.temporal.Temporal;
import java.util.UUID;

public interface BudgetFactory<P extends Temporal> {

    default Budget<P> createBudget(BudgetDetails<P> details) {
        return createBudget(UUID.randomUUID(), details);
    }

    Budget<P> createBudget(UUID id, BudgetDetails<P> details);

}
