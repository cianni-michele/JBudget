package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.transaction.Period;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.repository.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("DefaultStatisticsProvider Tests")
class DefaultStatisticsProviderTest {

    private DefaultStatisticsProvider provider;
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = mock(DataManager.class);
        provider = new DefaultStatisticsProvider();
    }

    @Test
    @DisplayName("Should return empty statistics when no transactions are available")
    void shouldReturnEmptyStatisticsWhenNoTransactions() {
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = LocalDate.of(2021, 12, 31);

        when(dataManager.getTransactionsIn(Period.of(from, to))).thenReturn(Collections.emptyList());

        Map<String, Double> result = provider.compute(dataManager, from, to);

        assertTrue(result.isEmpty(), "Result map should be empty when no data");
    }

    @Nested
    @DisplayName("Compute with transactions")
    class ComputeWithTransactions {

        @Test
        @DisplayName("Should sum amounts by tag for single transaction")
        void shouldSumSingleTransactionByTag() {
            String tagName = "Shopping";
            Tag tag = mock(Tag.class);
            when(tag.name()).thenReturn(tagName);

            Transaction transaction = mock(Transaction.class);
            when(transaction.amount()).thenReturn(50.0);
            when(transaction.date()).thenReturn(LocalDate.of(2025, 6, 15));
            when(transaction.tags()).thenReturn(Set.of(tag));

            LocalDate from = LocalDate.of(2025, 6, 1);
            LocalDate to = LocalDate.of(2025, 6, 30);

            when(dataManager.getTransactionsIn(Period.of(from, to))).thenReturn(List.of(transaction));

            Map<String, Double> result = provider.compute(dataManager, from, to);

            assertEquals(50.0, result.get(tagName),
                    "Should sum 50.0 for 'Shopping' tag from single transaction");
        }

        @Test
        @DisplayName("Should sum amounts correctly when multiple transactions share same tag")
        void shouldSumMultipleTransactionsSameTag() {
            String tagName = "Food";
            Tag tag = mock(Tag.class);
            when(tag.name()).thenReturn(tagName);

            Transaction transaction1 = mock(Transaction.class);
            when(transaction1.amount()).thenReturn(25.0);
            when(transaction1.date()).thenReturn(LocalDate.of(2025, 7, 10));
            when(transaction1.tags()).thenReturn(Set.of(tag));

            Transaction transaction2 = mock(Transaction.class);
            when(transaction2.amount()).thenReturn(35.0);
            when(transaction2.date()).thenReturn(LocalDate.of(2025, 7, 20));
            when(transaction2.tags()).thenReturn(Set.of(tag));

            LocalDate from = LocalDate.of(2025, 7, 1);
            LocalDate to = LocalDate.of(2025, 7, 31);

            when(dataManager.getTransactionsIn(Period.of(from, to))).thenReturn(List.of(transaction1, transaction2));

            Map<String, Double> result = provider.compute(dataManager, from, to);

            assertEquals(60.0, result.get(tagName),
                    "Should sum 60.0 for 'Food' tag from two transactions");
        }

        @Test
        @DisplayName("Should compute separate sums for different tags")
        void shouldComputeSeparateSumsForDifferentTags() {
            String foodTagName = "Food";
            Tag foodTag = mock(Tag.class);
            when(foodTag.name()).thenReturn(foodTagName);

            String transportTagName = "Transport";
            Tag transportTag = mock(Tag.class);
            when(transportTag.name()).thenReturn(transportTagName);

            Transaction transaction1 = mock(Transaction.class);
            when(transaction1.amount()).thenReturn(50.0);
            when(transaction1.date()).thenReturn(LocalDate.of(2025, 8, 5));
            when(transaction1.tags()).thenReturn(Set.of(foodTag));

            Transaction transaction2 = mock(Transaction.class);
            when(transaction2.amount()).thenReturn(30.0);
            when(transaction2.date()).thenReturn(LocalDate.of(2025, 8, 15));
            when(transaction2.tags()).thenReturn(Set.of(transportTag));

            LocalDate from = LocalDate.of(2025, 8, 1);
            LocalDate to = LocalDate.of(2025, 8, 31);

            when(dataManager.getTransactionsIn(Period.of(from, to))).thenReturn(List.of(transaction1, transaction2));

            Map<String, Double> result = provider.compute(dataManager, from, to);

            Map<String, Double> expected = Map.of(foodTagName, 50.0, transportTagName, 30.0);
            assertEquals(expected, result,
                    "Should compute separate sums for different tags");
        }
    }

    @Nested
    @DisplayName("Invalid inputs")
    class InvalidInputs {

        @Test
        @DisplayName("Should throw NullPointerException when DataManager is null")
        void shouldThrowWhenDataManagerIsNull() {
            LocalDate from = LocalDate.now();
            LocalDate to = LocalDate.now();
            assertThrows(NullPointerException.class, () -> provider.compute(null, from, to));
        }

        @Test
        @DisplayName("Should throw NullPointerException when from date is null")
        void shouldThrowWhenFromDateIsNull() {
            assertThrows(NullPointerException.class,
                    () -> provider.compute(dataManager, null, LocalDate.now()),
                    "From date should not be null"
            );
        }

        @Test
        @DisplayName("Should throw NullPointerException when to date is null")
        void shouldThrowWhenToDateIsNull() {
            assertThrows(NullPointerException.class,
                    () -> provider.compute(dataManager, LocalDate.now(), null),
                    "To date should not be null"
            );
        }

        @Test
        @DisplayName("Should throw exception if from date is after to date")
        void shouldThrowWhenFromAfterTo() {
            LocalDate from = LocalDate.of(2022, 1, 1);
            LocalDate to = LocalDate.of(2021, 12, 31);

            assertThrows(IllegalArgumentException.class, () -> provider.compute(dataManager, from, to));
        }
    }
}
