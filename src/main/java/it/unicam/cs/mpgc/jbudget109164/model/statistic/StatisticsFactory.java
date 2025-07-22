package it.unicam.cs.mpgc.jbudget109164.model.statistic;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;

import java.util.List;

public interface StatisticsFactory {

    /**
     * Creates a Statistics object based on the provided list of movements.
     *
     * @param movements the list of movements to be used for creating statistics
     * @return a Statistics object containing the calculated statistics
     */
    Statistics createStatistics(Period period, List<Movement> movements);
}
