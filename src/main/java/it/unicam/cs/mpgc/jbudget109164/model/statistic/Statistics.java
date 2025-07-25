package it.unicam.cs.mpgc.jbudget109164.model.statistic;

import it.unicam.cs.mpgc.jbudget109164.util.time.Period;

/**
 * Represents a collection of financial statistics for a specific period.
 * This interface provides methods to retrieve various financial metrics such as
 * total income, total expenses, average income, and average expenses.
 * The balance is calculated as the difference between total income and total expenses.
 *
 * @author Michele Cianni
 */
public interface Statistics {

    /**
     * Returns the period for which the statistics are calculated.
     *
     * @return the period
     */
    Period getPeriod();

    /**
     * Returns the balance for the specified period, calculated as total income minus total expenses.
     *
     * @return the balance
     */
    default double getBalance() {
        return getTotalIncome() + getTotalExpenses();
    }

    /**
     * Returns the total income for the specified period.
     *
     * @return the total income
     */
    double getTotalIncome();

    /**
     * Returns the total expenses for the specified period.
     *
     * @return the total expenses
     */
    double getTotalExpenses();

    /**
     * Returns the average income for the specified period.
     *
     * @return the average income
     */
    double getAverageIncome();

    /**
     * Returns the average expenses for the specified period.
     *
     * @return the average expenses
     */
    double getAverageExpenses();
}
