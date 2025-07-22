package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.temporal.Temporal;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

/**
 * Represents a budget entity with details about the expected amount,
 *
 * @param <P> the type of the period, which must extend {@link Temporal}.
 *
 * @author Michele Cianni
 */
public interface Budget<P extends Temporal> extends Entity<BudgetDetails<P>> {

    /**
     * Returns the tag associated with this budget.
     *
     * @return the tag of this budget.
     */
    Tag getTag();

    /**
     * Returns the period for which this budget is applicable.
     *
     * @return the period of this budget.
     */
    P getPeriod();

    /**
     * Returns the expected amount for this budget.
     *
     * @return the expected amount of this budget.
     */
    double getExpectedAmount();

}
