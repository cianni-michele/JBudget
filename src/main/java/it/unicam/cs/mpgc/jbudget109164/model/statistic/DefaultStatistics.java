package it.unicam.cs.mpgc.jbudget109164.model.statistic;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

public class DefaultStatistics implements Statistics {

    private final Period period;
    private final List<Movement> movements;

    public DefaultStatistics(Period period, List<Movement> movements) {
        this.period = period;
        this.movements = movements;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public double getTotalIncome() {
        return getStreamIncomeAmounts().sum();
    }

    @Override
    public double getAverageIncome() {
        return getStreamIncomeAmounts().average().orElse(0);
    }

    private DoubleStream getStreamIncomeAmounts() {
        return getStreamAmounts(movement -> movement.getAmount() > 0);
    }

    @Override
    public double getTotalExpenses() {
        return getStreamExpensesAmounts().sum();
    }

    @Override
    public double getAverageExpenses() {
        return getStreamExpensesAmounts().average().orElse(0);
    }

    private DoubleStream getStreamExpensesAmounts() {
        return getStreamAmounts(movement -> movement.getAmount() < 0);
    }

    private DoubleStream getStreamAmounts(Predicate<? super Movement> predicate) {
        return movements.stream().filter(predicate).mapToDouble(Movement::getAmount);
    }
}
