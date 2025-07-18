package it.unicam.cs.mpgc.jbudget109164.service.budget;

import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.repository.budget.BudgetRepository;
import it.unicam.cs.mpgc.jbudget109164.mapper.budget.BudgetMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BudgetServiceImpl implements BudgetService<YearMonth> {

    private static final Logger LOGGER = LogManager.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository repository;
    private final BudgetFactory<YearMonth> factory;
    private final BudgetMapper<YearMonth> mapper;

    public BudgetServiceImpl(BudgetRepository repository,
                             BudgetFactory<YearMonth> factory,
                             BudgetMapper<YearMonth> mapper) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public List<Budget<YearMonth>> getAllBudgets() {
        LOGGER.debug("Retrieving all budgets");

        List<Budget<YearMonth>> budgets = repository.findAll().stream()
                .map(mapper::toModel)
                .toList();

        LOGGER.info("Total budgets found: {}", budgets.size());

        return budgets;
    }

    @Override
    public Optional<Budget<YearMonth>> getBudget(UUID id) {
        LOGGER.debug("Searching for budget with ID: {}", id);

        Optional<Budget<YearMonth>> budgetFound = repository.findById(id).map(mapper::toModel);

        if (budgetFound.isPresent()) {
            LOGGER.debug("Budget found: {}", budgetFound.get());
        } else {
            LOGGER.debug("No budget found with ID: {}", id);
        }

        LOGGER.info("Budget search completed for ID: {}", id);
        return budgetFound;
    }

    @Override
    public Budget<YearMonth> createBudget(BudgetDetails<YearMonth> budgetDetails) {
        LOGGER.debug("Creating budget with details: {}", budgetDetails);

        Budget<YearMonth> budget = factory.createBudget(budgetDetails);

        repository.save(mapper.toDTO(budget));

        LOGGER.info("Budget created: {}", budget);

        return budget;
    }

    @Override
    public Budget<YearMonth> updateBudget(UUID id, BudgetDetails<YearMonth> budgetDetails) {
        LOGGER.debug("Updating budget with ID: {} and details: {}", id, budgetDetails);

        Optional<Budget<YearMonth>> budgetFound = getBudget(id);

        if (budgetFound.isEmpty()) {
            LOGGER.warn("No budget found with ID: {}, cannot update.", id);
            return null;
        }

        Budget<YearMonth> updatedBudget = factory.createBudget(budgetFound.get().getId(), budgetDetails);

        repository.save(mapper.toDTO(updatedBudget));

        LOGGER.info("Budget updated: {}", updatedBudget);

        return updatedBudget;
    }

    @Override
    public void deleteBudget(UUID id) {
        LOGGER.debug("Deleting budget with ID: {}", id);

        Optional<Budget<YearMonth>> budgetFound = getBudget(id);

        if (budgetFound.isEmpty()) {
            LOGGER.warn("No budget found with ID: {}, cannot delete.", id);
        } else {
            repository.deleteById(id);
            LOGGER.info("Budget with ID: {} deleted successfully.", id);
        }
    }

    @Override
    public double getExpectedAmount(YearMonth period) {
        return getAllBudgets().stream().
                filter(budget -> budget.getPeriod().equals(period))
                .mapToDouble(Budget::getExpectedAmount)
                .sum();
    }

    @Override
    public double getExpectedAmount(Tag tag, YearMonth period) {
        return getAllBudgets().stream()
                .filter(budget -> budget.getTag().equals(tag))
                .filter(budget -> budget.getPeriod().equals(period))
                .mapToDouble(Budget::getExpectedAmount)
                .sum();
    }
}
