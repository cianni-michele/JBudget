package it.unicam.cs.mpgc.jbudget109164.repository;

import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Scheduler;

/**
 * A factory interface for creating instances of DataManager.
 * <p>
 * This interface defines a method to create a DataManager with a specific BudgetPlan and Scheduler.
 * Implementations of this interface should provide the logic to instantiate a DataManager.
 *
 * @author Michele Cianni
 */
@FunctionalInterface
public interface DataManagerFactory {

    /**
     * Creates a new DataManager instance with the specified BudgetPlan and Scheduler.
     *
     * @param budgetPlan the BudgetPlan to be associated with the DataManager
     * @param scheduler  the Scheduler to be associated with the DataManager
     * @return a new DataManager instance
     */
    DataManager createDataManager(BudgetPlan budgetPlan, Scheduler scheduler);
}
