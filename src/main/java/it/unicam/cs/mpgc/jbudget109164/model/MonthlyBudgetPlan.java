package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.YearMonth;
import java.util.*;


/**
 * Represents a monthly budget plan that allows setting and retrieving expected amounts for specific tags
 * and periods.
 * <p>
 * This class implements the {@link BudgetPlan} interface.
 *
 * @author Michele Cianni
 * @see BudgetPlan
 */
public final class MonthlyBudgetPlan implements BudgetPlan {

    private final Map<BudgetKey, Double> expectedAmounts = new HashMap<>();

    @Override
    public void setExpectedAmount(Tag tag, YearMonth period, double amount) {
        expectedAmounts.put(
                BudgetKey.of(tag, period),
                amount
        );
    }

    @Override
    public double getExpectedAmount(Tag tag, YearMonth period) {
        return expectedAmounts.getOrDefault(
                BudgetKey.of(tag, period),
                0.0
        );
    }

    private record BudgetKey(Tag tag, YearMonth period) {
        private BudgetKey {
            Objects.requireNonNull(tag, "Tag cannot be null");
            Objects.requireNonNull(period, "Period cannot be null");
        }

        private static BudgetKey of(Tag tag, YearMonth period) {
            return new BudgetKey(tag, period);
        }
    }
}