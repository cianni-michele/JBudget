package it.unicam.cs.mpgc.jbudget109164.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BasicScheduler Tests")
class BasicSchedulerTest {

    private BasicScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new BasicScheduler();
    }

    @Nested
    @DisplayName("getUpcomingTransactions Validations")
    class GetUpcomingTransactionsValidationTests {

        @Test
        @DisplayName("Should throw NullPointerException when from date is null")
        void shouldThrowExceptionWhenFromDateIsNull() {
            LocalDate to = LocalDate.now();
            assertThrows(NullPointerException.class,
                    () -> scheduler.getUpcomingTransactions(null, to),
                    "Expected NullPointerException when from date is null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when to date is null")
        void shouldThrowExceptionWhenToDateIsNull() {
            LocalDate from = LocalDate.now();
            assertThrows(NullPointerException.class,
                    () -> scheduler.getUpcomingTransactions(from, null),
                    "Expected NullPointerException when to date is null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when from date is after to date")
        void shouldThrowExceptionWhenFromAfterTo() {
            LocalDate from = LocalDate.of(2025, 6, 30);
            LocalDate to = LocalDate.of(2025, 6, 1);
            assertThrows(IllegalArgumentException.class,
                    () -> scheduler.getUpcomingTransactions(from, to),
                    "Expected IllegalArgumentException when from is after to");
        }
    }

    @Nested
    @DisplayName("getUpcomingTransactions Behaviour")
    class GetUpcomingTransactionsBehaviourTests {

        @Test
        @DisplayName("Should return empty list when no scheduled transactions exist")
        void shouldReturnEmptyListWhenNoScheduledTransactions() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.isEmpty(),
                    "Expected empty list when there are no scheduled transactions");
        }


        @Test
        @DisplayName("Should handle boundary dates inclusively")
        void shouldHandleBoundaryDatesInclusively() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction startBoundaryTx = mock(ScheduledTransaction.class);
            when(startBoundaryTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            ScheduledTransaction endBoundaryTx = mock(ScheduledTransaction.class);
            when(endBoundaryTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            scheduler.addScheduledTransaction(startBoundaryTx);
            scheduler.addScheduledTransaction(endBoundaryTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertEquals(2, result.size(), "Both boundary transactions should be included");

            verify(startBoundaryTx).generate(from, to);
            verify(endBoundaryTx).generate(from, to);
        }

        @Test
        @DisplayName("Should include start boundary transaction")
        void shouldIncludeStartBoundaryTransaction() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction startBoundaryTx = mock(ScheduledTransaction.class);
            when(startBoundaryTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            scheduler.addScheduledTransaction(startBoundaryTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.contains(startBoundaryTx), "Transaction at the start boundary should be included");
        }

        @Test
        @DisplayName("Should include end boundary transaction")
        void shouldIncludeEndBoundaryTransaction() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction endBoundaryTx = mock(ScheduledTransaction.class);
            when(endBoundaryTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            scheduler.addScheduledTransaction(endBoundaryTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.contains(endBoundaryTx), "Transaction at the end boundary should be included");
        }

        @Test
        @DisplayName("Should filter out empty transactions")
        void shouldFilterOutEmptyTransactions() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction validTx = mock(ScheduledTransaction.class);
            ScheduledTransaction emptyTx = mock(ScheduledTransaction.class);

            when(validTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));
            when(emptyTx.generate(from, to)).thenReturn(List.of());

            scheduler.addScheduledTransaction(validTx);
            scheduler.addScheduledTransaction(emptyTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertEquals(1, result.size(), "Only transactions that generate results should be included");
        }

        @Test
        @DisplayName("Should include valid transaction")
        void shouldIncludeValidTransaction() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction validTx = mock(ScheduledTransaction.class);
            when(validTx.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            scheduler.addScheduledTransaction(validTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.contains(validTx), "Valid transaction should be included");
        }

        @Test
        @DisplayName("Should exclude empty transaction")
        void shouldExcludeEmptyTransaction() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction emptyTx = mock(ScheduledTransaction.class);
            when(emptyTx.generate(from, to)).thenReturn(List.of());

            scheduler.addScheduledTransaction(emptyTx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertFalse(result.contains(emptyTx), "Empty transaction should be excluded");
        }

        @Test
        @DisplayName("Should add transaction")
        void shouldAddTransaction() {
            ScheduledTransaction tx = mock(ScheduledTransaction.class);

            scheduler.addScheduledTransaction(tx);

            assertTrue(scheduler.stream().anyMatch(t -> t == tx), "Transaction should be added to scheduler");
        }

        @Test
        @DisplayName("Should remove transaction")
        void shouldRemoveTransaction() {
            ScheduledTransaction tx = mock(ScheduledTransaction.class);

            scheduler.addScheduledTransaction(tx);
            scheduler.removeScheduledTransaction(tx);

            assertFalse(scheduler.stream().anyMatch(t -> t == tx), "Transaction should be removed from scheduler");
        }

        @Test
        @DisplayName("Should keep other transactions when removing")
        void shouldKeepOtherTransactionsWhenRemoving() {
            ScheduledTransaction tx1 = mock(ScheduledTransaction.class);
            ScheduledTransaction tx2 = mock(ScheduledTransaction.class);

            scheduler.addScheduledTransaction(tx1);
            scheduler.addScheduledTransaction(tx2);
            scheduler.removeScheduledTransaction(tx1);

            assertTrue(scheduler.stream().anyMatch(t -> t == tx2), "Other transactions should remain after removal");
        }

        @Test
        @DisplayName("Should throw NullPointerException when adding null transaction")
        void shouldThrowExceptionWhenAddingNullTransaction() {
            assertThrows(NullPointerException.class,
                    () -> scheduler.addScheduledTransaction(null),
                    "Should throw NullPointerException when adding a null transaction");
        }

        @Test
        @DisplayName("Should throw NullPointerException when removing null transaction")
        void shouldThrowExceptionWhenRemovingNullTransaction() {
            assertThrows(NullPointerException.class,
                    () -> scheduler.removeScheduledTransaction(null),
                    "Should throw NullPointerException when removing a null transaction");
        }

        @Test
        @DisplayName("Should return correct number of matching transactions")
        void shouldReturnCorrectNumberOfMatchingTransactions() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction tx1 = mock(ScheduledTransaction.class);
            ScheduledTransaction tx2 = mock(ScheduledTransaction.class);
            ScheduledTransaction tx3 = mock(ScheduledTransaction.class);

            when(tx1.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));
            when(tx2.generate(from, to)).thenReturn(List.of(mock(Transaction.class), mock(Transaction.class)));
            when(tx3.generate(from, to)).thenReturn(List.of());

            scheduler.addScheduledTransaction(tx1);
            scheduler.addScheduledTransaction(tx2);
            scheduler.addScheduledTransaction(tx3);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertEquals(2, result.size(), "Only transactions that generate results should be returned");
        }

        @Test
        @DisplayName("Should include transaction with single result")
        void shouldIncludeTransactionWithSingleResult() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction tx1 = mock(ScheduledTransaction.class);
            when(tx1.generate(from, to)).thenReturn(List.of(mock(Transaction.class)));

            scheduler.addScheduledTransaction(tx1);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.contains(tx1), "Transaction with single result should be included");
        }

        @Test
        @DisplayName("Should include transaction with multiple results")
        void shouldIncludeTransactionWithMultipleResults() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 1, 31);

            ScheduledTransaction tx = mock(ScheduledTransaction.class);
            when(tx.generate(from, to)).thenReturn(List.of(mock(Transaction.class), mock(Transaction.class)));

            scheduler.addScheduledTransaction(tx);

            List<ScheduledTransaction> result = scheduler.getUpcomingTransactions(from, to);

            assertTrue(result.contains(tx), "Transaction with multiple results should be included");
        }
    }
}