package it.unicam.cs.mpgc.jbudget109164.model.budget;

import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

/**
 * Represents a monthly budget with details about the expected amount,
 * the tag associated with it, and the period it covers.
 * This class implements the {@link Budget} interface, providing
 * methods to access the budget's ID, tag, period, and expected amount.
 *
 * @author Michele Cianni
 */
public class MonthlyBudget implements Budget<YearMonth> {
    private final UUID id;
    private final Tag tag;
    private final YearMonth period;
    private final double expectedAmount;

    public MonthlyBudget(UUID id, BudgetDetails<YearMonth> details) {
        this(id, details.tag(), details.period(), details.expectedAmount());
    }

    public MonthlyBudget(UUID id, Tag tag, YearMonth period, double expectedAmount) {
        this.id = id;
        this.period = period;
        this.tag = tag;
        this.expectedAmount = expectedAmount;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public BudgetDetails<YearMonth> getDetails() {
        return new BudgetDetails<>(tag, period, expectedAmount);
    }

    @Override
    public Tag getTag() {
        return tag;
    }

    @Override
    public YearMonth getPeriod() {
        return period;
    }

    @Override
    public double getExpectedAmount() {
        return expectedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyBudget that = (MonthlyBudget) o;
        return Double.compare(expectedAmount, that.expectedAmount) == 0 &&
               Objects.equals(period, that.period) &&
               Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, tag, expectedAmount);
    }

    @Override
    public String toString() {
        return "MonthlyBudget{" +
               "period=" + period +
               ", tag=" + tag +
               ", expectedAmount=" + expectedAmount +
               '}';
    }
}
