package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.repository.DataManager;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface defines a method for computing statistics based on transactions and tags
 * within a specified date range from a given data manager.
 * Implementations of this interface should provide the logic to aggregate transaction amounts
 * by tags for the specified period.
 */
public interface StatisticsProvider {

    /**
     * Computes statistics for the given data manager within the specified date range.
     *
     * @param data the data manager containing transactions and tags
     * @param from the start date of the range
     * @param to the end date of the range
     * @return a map where keys are tag names and values are the total amounts for each tag
     */
    Map<String, Double> compute(DataManager data, LocalDate from, LocalDate to);
}
