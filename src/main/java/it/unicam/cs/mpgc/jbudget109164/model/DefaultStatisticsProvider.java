package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Period;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.repository.DataManager;

import java.util.*;

/**
 * This class computes statistics based on transactions and their associated tags.
 * It aggregates the amounts of transactions by their tags over a specified period.
 * <p>
 * This class implements the {@link StatisticsProvider} interface.
 *
 * @author Michele Cianni
 */
public final class DefaultStatisticsProvider implements StatisticsProvider {

    @Override
    public Map<String, Double> compute(DataManager data, Period period) {
        List<Transaction> transactions = data.getTransactionsIn(period);

        if (transactions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Double> result = new HashMap<>();
        for (Transaction transaction : transactions) {
            for (Tag tag : transaction.getTags()) {
                String tagName = tag.getName();
                double amount = transaction.getAmount();
                result.put(tagName, result.getOrDefault(tagName, 0.0) + amount);
            }
        }

        return result;
    }
}
