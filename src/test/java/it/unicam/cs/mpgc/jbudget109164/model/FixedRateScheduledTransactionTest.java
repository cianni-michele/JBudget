package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.transaction.FixedRateScheduledTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Period;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@DisplayName("FixedRateScheduledTransaction Tests")
class FixedRateScheduledTransactionTest {

    private static final LocalDate START_DATE = LocalDate.of(2025, 5, 1);
    private static final LocalDate END_DATE = LocalDate.of(2025, 8, 30);
    private static final double AMOUNT = 10.0;
    private static final String DESCRIPTION = "Description";

    private TransactionFactory transactionFactory;
    private FixedRateScheduledTransaction scheduledTransaction;

    @BeforeEach
    void setUp() {
        transactionFactory = mock(TransactionFactory.class);
    }


    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should throw NullPointerException when start date is null")
        void shouldThrowNullPointerExceptionWhenStartDateIsNull() {
            assertThrows(NullPointerException.class, () -> new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(null, END_DATE), DESCRIPTION, Set.of(), 1));
        }

        @Test
        @DisplayName("should throw NullPointerException when end date is null")
        void shouldThrowNullPointerExceptionWhenEndDateIsNull() {
            assertThrows(NullPointerException.class, () -> new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, null), DESCRIPTION, Set.of(), 1));
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when amount is negative")
        void shouldThrowIllegalArgumentExceptionWhenAmountIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> new FixedRateScheduledTransaction(transactionFactory, -100.0, Period.of(START_DATE, START_DATE), DESCRIPTION, Set.of(), 1));
        }

        @Test
        @DisplayName("should not throw exception when description is null")
        void shouldDoesNotThrowExceptionWhenDescriptionIsNull() {
            assertDoesNotThrow(() -> new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, END_DATE), null, Set.of(), 1));
        }

        @Test
        @DisplayName("should create instance with valid parameters")
        void shouldCreateInstanceWithValidParameters() {
            assertDoesNotThrow(() -> new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, END_DATE), DESCRIPTION, Set.of(), 1));
        }

        @Test
        void shouldCreateInstanceWithEmptyTagsWhenTagsAreNull() {
            assertDoesNotThrow(() -> new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, END_DATE), DESCRIPTION, null, 1));
        }

    }

    @Nested
    @DisplayName("Generate Tests")
    class GenerateTests {

        @BeforeEach
        void init() {
            scheduledTransaction = new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, END_DATE), DESCRIPTION, Set.of(), 1);
        }

        @Test
        @DisplayName("should throw NullPointerException when from date is null")
        void shouldThrowNullPointerExceptionWhenFromIsNull() {
            LocalDate to = LocalDate.of(2025, 6, 10);
            assertThrows(NullPointerException.class, () -> scheduledTransaction.generate(Period.of(null, to)));
        }

        @Test
        @DisplayName("should throw NullPointerException when to date is null")
        void shouldThrowNullPointerExceptionWhenToIsNull() {
            LocalDate from = LocalDate.of(2025, 6, 10);
            assertThrows(NullPointerException.class, () -> scheduledTransaction.generate(Period.of(from, null)));
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when from date is after to date")
        void shouldThrowIllegalArgumentExceptionWhenFromAfterTo() {
            LocalDate from = LocalDate.of(2025, 6, 10);
            LocalDate to = LocalDate.of(2025, 6, 1);
            assertThrows(IllegalArgumentException.class, () -> scheduledTransaction.generate(Period.of(from, to)));
        }

        @Test
        @DisplayName("should generate transactions only within specified range")
        void shouldGenerateTransactionsOnlyWithinSpecifiedRange() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            when(transactionFactory.createTransaction(any(), anyDouble(), anyString(), anySet()))
                    .thenReturn(mock(Transaction.class));
            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(4, transactions.size());
        }

        @Test
        @DisplayName("should not generate transactions outside start/end date")
        void shouldNotGenerateTransactionsOutsideStartEndDate() {
            LocalDate from = LocalDate.of(2025, 4, 1);
            LocalDate to = LocalDate.of(2025, 9, 1);


            when(transactionFactory.createTransaction(any(), anyDouble(), anyString(), anySet()))
                    .thenReturn(mock(Transaction.class));

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(4, transactions.size());
        }

        @Test
        @DisplayName("should return empty list when no transactions in range")
        void shouldReturnEmptyListWhenNoTransactionsInRange() {
            LocalDate from = LocalDate.of(2024, 1, 1);
            LocalDate to = LocalDate.of(2024, 2, 1);

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertTrue(transactions.isEmpty());
        }

        @Test
        @DisplayName("should use transactionFactory to create transactions")
        void shouldUseTransactionFactoryToCreateTransactions() {
            Transaction transaction = mock(Transaction.class);
            when(transactionFactory.createTransaction(any(), anyDouble(), anyString(), anySet()))
                    .thenReturn(transaction);

            LocalDate from = LocalDate.of(2025, 6, 1);
            LocalDate to = LocalDate.of(2025, 6, 30);

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(1, transactions.size());
            verify(transactionFactory).createTransaction(
                    eq(LocalDate.of(2025, 6, 1)),
                    eq(AMOUNT),
                    eq(DESCRIPTION),
                    eq(Set.of())
            );
        }
    }

    @Nested
    @DisplayName("Day of Month Tests")
    class DayOfMonthTests {

        @BeforeEach
        void setUp() {
            scheduledTransaction = new FixedRateScheduledTransaction(transactionFactory, AMOUNT, Period.of(START_DATE, END_DATE), DESCRIPTION, Set.of(), 15);
        }

        @Test
        @DisplayName("should generate transactions on the 15th day of each month")
        void shouldGenerateTransactionsOnDay15OfEachMonth() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            mockTransactionsForDates();

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(4, transactions.size());
        }

        @Test
        @DisplayName("should generate transaction on the 15th of May")
        void shouldGenerateTransactionOnMay15() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            mockTransactionsForDates();

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(LocalDate.of(2025, 5, 15), transactions.getFirst().date());
        }

        @Test
        @DisplayName("should generate transaction on the 15th of June")
        void shouldGenerateTransactionOnJune15() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            mockTransactionsForDates();

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(LocalDate.of(2025, 6, 15), transactions.get(1).date());
        }

        @Test
        @DisplayName("should generate transaction on the 15th of July")
        void shouldGenerateTransactionOnJuly15() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            mockTransactionsForDates();

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(LocalDate.of(2025, 7, 15), transactions.get(2).date());
        }

        @Test
        @DisplayName("should generate transaction on the 15th of August")
        void shouldGenerateTransactionOnAugust15() {
            LocalDate from = LocalDate.of(2025, 5, 1);
            LocalDate to = LocalDate.of(2025, 8, 30);

            mockTransactionsForDates();

            List<Transaction> transactions = scheduledTransaction.generate(Period.of(from, to));

            assertEquals(LocalDate.of(2025, 8, 15), transactions.get(3).date());
        }

        // Helper method to mock transactions for specific dates
        private void mockTransactionsForDates() {
            Transaction mayTransaction = mock(Transaction.class);
            when(mayTransaction.date()).thenReturn(LocalDate.of(2025, 5, 15));

            Transaction junTransaction = mock(Transaction.class);
            when(junTransaction.date()).thenReturn(LocalDate.of(2025, 6, 15));

            Transaction julTransaction = mock(Transaction.class);
            when(julTransaction.date()).thenReturn(LocalDate.of(2025, 7, 15));

            Transaction augTransaction = mock(Transaction.class);
            when(augTransaction.date()).thenReturn(LocalDate.of(2025, 8, 15));

            when(transactionFactory.createTransaction(eq(LocalDate.of(2025, 5, 15)), anyDouble(), anyString(), anySet()))
                    .thenReturn(mayTransaction);
            when(transactionFactory.createTransaction(eq(LocalDate.of(2025, 6, 15)), anyDouble(), anyString(), anySet()))
                    .thenReturn(junTransaction);
            when(transactionFactory.createTransaction(eq(LocalDate.of(2025, 7, 15)), anyDouble(), anyString(), anySet()))
                    .thenReturn(julTransaction);
            when(transactionFactory.createTransaction(eq(LocalDate.of(2025, 8, 15)), anyDouble(), anyString(), anySet()))
                    .thenReturn(augTransaction);
        }
    }
}
