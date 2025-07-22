package it.unicam.cs.mpgc.jbudget109164.service.statistic;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.Statistics;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.StatisticsFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementService;

import java.util.List;

public class StatisticServiceImpl implements StatisticService {

    private final MovementService movementService;

    private final StatisticsFactory statisticsFactory;

    public StatisticServiceImpl(MovementService movementService,
                                StatisticsFactory statisticsFactory) {
        this.movementService = movementService;
        this.statisticsFactory = statisticsFactory;
    }

    @Override
    public Statistics getStatisticsByPeriod(Period period) {
        List<Movement> movements = movementService.getMovementsByPeriod(period);

        return statisticsFactory.createStatistics(period, movements);
    }

    @Override
    public Statistics getStatisticsByTagAndPeriod(Tag tag, Period period) {
        List<Movement> movements = movementService.getMovementsByTagAndPeriod(tag, period);

        return statisticsFactory.createStatistics(period, movements);
    }
}
