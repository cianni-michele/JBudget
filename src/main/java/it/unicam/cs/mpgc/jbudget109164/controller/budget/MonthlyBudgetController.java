package it.unicam.cs.mpgc.jbudget109164.controller.budget;

import it.unicam.cs.mpgc.jbudget109164.exception.service.BudgetNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.service.budget.BudgetService;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagService;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public final class MonthlyBudgetController {

    private final BudgetService<YearMonth> budgetService;

    private final TagService tagService;

    public MonthlyBudgetController(BudgetService<YearMonth> budgetService,
                                   TagService tagService) {
        this.budgetService = budgetService;
        this.tagService = tagService;
    }

    public List<Budget<YearMonth>> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    public Budget<YearMonth> getBudget(UUID budgetId) {
        if (budgetId == null) {
            throw new IllegalArgumentException("Budget ID cannot be null");
        }
        return budgetService.getBudget(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + budgetId + " not found"));
    }

    public void deleteBudget(UUID budgetId) {
        if (budgetId == null) {
            throw new IllegalArgumentException("Budget ID cannot be null");
        }
        budgetService.deleteBudget(budgetId);
    }

    public Budget<YearMonth> createBudget(BudgetDetails<YearMonth> budgetDetails) {
        if (budgetDetails == null) {
            throw new IllegalArgumentException("Budget cannot be null");
        }
        return budgetService.createBudget(budgetDetails);
    }

    public Budget<YearMonth> updateBudget(UUID budgetId, BudgetDetails<YearMonth> budgetDetails) {
        if (budgetId == null || budgetDetails == null) {
            throw new IllegalArgumentException("Budget ID and details cannot be null");
        }
        return budgetService.updateBudget(budgetId, budgetDetails);
    }

    public double getActualAmount(YearMonth period) {
        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }
        return budgetService.getActualAmount(period);
    }

    public double getActualAmount(UUID tagId, YearMonth period) {
        if (tagId == null || period == null) {
            throw new IllegalArgumentException("Tag ID and period cannot be null");
        }

        Tag tag = tagService.getTagById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag with ID " + tagId + " not found"));

        return budgetService.getActualAmount(tag, period);
    }
}
