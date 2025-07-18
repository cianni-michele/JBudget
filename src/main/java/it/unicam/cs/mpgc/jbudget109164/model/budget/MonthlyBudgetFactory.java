package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.YearMonth;
import java.util.UUID;

public class MonthlyBudgetFactory implements BudgetFactory<YearMonth> {

    @Override
    public Budget<YearMonth> createBudget(UUID id, BudgetDetails<YearMonth> details) {
        return new MonthlyBudget(id, details);
    }
}
