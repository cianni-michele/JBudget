package it.unicam.cs.mpgc.jbudget109164.model.statistic;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;

import java.util.List;

public class DefaultStatisticsFactory implements StatisticsFactory {
    @Override
    public Statistics createStatistics(Period period, List<Movement> movements) {
        return new DefaultStatistics(period, movements);
    }
}
