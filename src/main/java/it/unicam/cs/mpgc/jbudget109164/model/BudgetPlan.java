package it.unicam.cs.mpgc.jbudget109164.model;

import java.time.YearMonth;

/**
 * A class implementing this interface represents a budget plan that allows setting and retrieving expected amounts
 * for specific tags over defined periods.
 */
public interface BudgetPlan {

    /**
     * Sets the expected amount for a given tag and period.
     *
     * @param tag    the tag for which the expected amount is set
     * @param period the period for which the expected amount is set
     * @param amount the expected amount to be set
     */
    void setExpectedAmount(Tag tag, YearMonth period, double amount);

    /**
     * Returns the expected amount for a given tag and period.
     *
     * @param tag the tag for which the expected amount is requested
     * @param period the period for which the expected amount is requested
     * @return the expected amount for the specified tag and period
     */
    double getExpectedAmount(Tag tag, YearMonth period);
}
