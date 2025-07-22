package it.unicam.cs.mpgc.jbudget109164.service.statistic;

import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.Statistics;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

/**
 * Service interface for retrieving financial statistics.
 * This service provides methods to obtain statistics for a specific period
 * and for a specific tag within that period.
 *
 * @author Michele Cianni
 */
public interface StatisticService {

    /**
     * Returns the statistics for the specified period.
     *
     * @param period the period for which to retrieve statistics
     * @return the statistics for the specified period
     */
    Statistics getStatisticsByPeriod(Period period);

    /**
     * Returns the statistics for the specified tag and period.
     *
     * @param tag the ID of the tag for which to retrieve statistics
     * @param period the period for which to retrieve statistics
     * @return the statistics for the specified tag and period
     */
    Statistics getStatisticsByTagAndPeriod(Tag tag, Period period);
}
