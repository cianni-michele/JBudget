package it.unicam.cs.mpgc.jbudget109164.service.budget;

import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetFactory;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.repository.budget.BudgetRepository;
import it.unicam.cs.mpgc.jbudget109164.mapper.budget.BudgetMapper;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementService;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MonthlyBudgetService implements BudgetService<YearMonth> {

    private static final Logger LOGGER = LogManager.getLogger(MonthlyBudgetService.class);

    private final BudgetRepository repository;
    private final BudgetFactory<YearMonth> factory;
    private final BudgetMapper<YearMonth> mapper;

    private final MovementService movementService;

    public MonthlyBudgetService(BudgetRepository repository,
                                BudgetFactory<YearMonth> factory,
                                BudgetMapper<YearMonth> mapper,
                                MovementService movementService) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
        this.movementService = movementService;
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
    public double getActualAmount(YearMonth period) {
        LOGGER.debug("Calculating actual amount for period: {}", period);

        Period monthPeriod = Period.of(
                period.atDay(1),
                period.atEndOfMonth()
        );

        List<Movement> movements = movementService.getMovementsByPeriod(monthPeriod);

        double actualAmount = movements.stream()
                .mapToDouble(Movement::getAmount)
                .filter(amount -> amount < 0)
                .sum();

        LOGGER.info("Actual amount for period {}: {}", period, actualAmount);
        return actualAmount;
    }

    @Override
    public double getActualAmount(Tag tag, YearMonth period) {
        LOGGER.debug("Calculating actual amount for tag: {} and period: {}", tag, period);

        Period monthPeriod = Period.of(
                period.atDay(1),
                period.atEndOfMonth()
        );

        List<Movement> movements = movementService.getMovementsByTagAndPeriod(tag, monthPeriod);

        double actualAmount = movements.stream()
                .mapToDouble(Movement::getAmount)
                .filter(amount -> amount < 0)
                .sum();

        LOGGER.info("Actual amount for tag {} in period {}: {}", tag, period, actualAmount);
        return actualAmount;
    }
}
