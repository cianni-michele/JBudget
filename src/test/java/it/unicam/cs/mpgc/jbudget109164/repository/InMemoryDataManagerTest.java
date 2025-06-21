package it.unicam.cs.mpgc.jbudget109164.repository;

import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Scheduler;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Period;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InMemoryDataManagerTest {

    private InMemoryDataManager manager;

    @BeforeEach
    void setUp() {
        BudgetPlan budgetPlan = mock(BudgetPlan.class);
        Scheduler scheduler = mock(Scheduler.class);
        manager = new InMemoryDataManager(budgetPlan, scheduler);
    }

    @Nested
    @DisplayName("Transaction Addition and Removal")
    class TransactionModificationTests {

        @Test
        @DisplayName("should throw exception when from date is after to date")
        void shouldThrowWhenFromDateAfterToDate() {
            LocalDate from = LocalDate.of(2025, 12, 31);
            LocalDate to = LocalDate.of(2025, 1, 1);

            assertThrows(IllegalArgumentException.class, () -> manager.getTransactionsBetween(from, to));
        }

        @Test
        @DisplayName("should add transaction successfully")
        void shouldAddTransactionSuccessfully() {
            Transaction t = mock(Transaction.class);
            manager.add(t);

            List<Transaction> all = manager.getAllTransactions();
            assertTrue(all.contains(t));
        }

        @Test
        @DisplayName("should remove transaction successfully")
        void shouldRemoveTransactionSuccessfully() {
            Transaction t1 = mock(Transaction.class);
            UUID id = UUID.randomUUID();
            when(t1.id()).thenReturn(id);

            manager.add(t1);
            manager.removeTransaction(id);

            List<Transaction> all = manager.getAllTransactions();
            assertFalse(all.stream().anyMatch(t -> t.id().equals(id)));
        }

        @Test
        @DisplayName("should not remove non-existing transaction")
        void shouldNotRemoveNonExistingTransaction() {
            Transaction t = mock(Transaction.class);
            UUID id = UUID.randomUUID();
            when(t.id()).thenReturn(id);

            manager.add(t);
            UUID nonExistent = UUID.randomUUID();
            manager.removeTransaction(nonExistent);

            List<Transaction> all = manager.getAllTransactions();
            assertTrue(all.contains(t));
        }
    }

    @Nested
    @DisplayName("Transaction Filtering")
    class TransactionFilteringTests {

        private InMemoryDataManager spyManager;
        private Transaction t1, t2, t3;

        @BeforeEach
        void init() {
            spyManager = spy(manager);
            t1 = mock(Transaction.class);
            t2 = mock(Transaction.class);
            t3 = mock(Transaction.class);

            when(t1.date()).thenReturn(LocalDate.of(2025, 1, 1));
            when(t2.date()).thenReturn(LocalDate.of(2025, 6, 15));
            when(t3.date()).thenReturn(LocalDate.of(2025, 12, 31));

            doReturn(List.of(t1, t2, t3)).when(spyManager).getAllTransactions();
        }

        @Test
        @DisplayName("should return transactions between two dates")
        void shouldGetTransactionsBetweenDates() {
            LocalDate from = LocalDate.of(2025, 1, 1);
            LocalDate to = LocalDate.of(2025, 6, 30);

            List<Transaction> result = spyManager.getTransactionsBetween(from, to);
            assertEquals(List.of(t1, t2), result);
        }



        @Test
        @DisplayName("should return transactions within period")
        void shouldGetTransactionsInPeriodCorrectly() {
            Period period = Period.of(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 12, 31));

            List<Transaction> result = spyManager.getTransactionsIn(period);
            assertEquals(List.of(t2, t3), result);
        }

        @Test
        @DisplayName("should filter transactions by custom predicate")
        void shouldFilterTransactionsByCustomPredicate() {
            Predicate<Transaction> filter = t -> !t.date().isAfter(LocalDate.of(2025, 6, 15));

            List<Transaction> result = spyManager.getTransactions(filter);
            assertEquals(List.of(t1, t2), result);
        }
    }

    @Nested
    @DisplayName("Tag Management")
    class TagManagementTests {

        @Test
        @DisplayName("should return all unique tags")
        void shouldGetAllUniqueTags() {
            Tag tag1 = mock(Tag.class);
            Tag tag2 = mock(Tag.class);

            Transaction t1 = mock(Transaction.class);
            Transaction t2 = mock(Transaction.class);

            when(t1.id()).thenReturn(UUID.randomUUID());
            when(t2.id()).thenReturn(UUID.randomUUID());

            when(t1.tags()).thenReturn(Set.of(tag1));
            when(t2.tags()).thenReturn(Set.of(tag1, tag2));

            manager.add(t1);
            manager.add(t2);

            assertEquals(Set.of(tag1, tag2), manager.getAllTags());
        }
    }

    @Nested
    @DisplayName("BudgetPlan and Scheduler Retrieval")
    class PlanAndSchedulerTests {

        @Test
        @DisplayName("should return budget plan")
        void shouldGetBudgetPlan() {
            BudgetPlan expectedPlan = mock(BudgetPlan.class);

            InMemoryDataManager spyManager = spy(manager);
            doReturn(expectedPlan).when(spyManager).getBudgetPlan();

            assertEquals(expectedPlan, spyManager.getBudgetPlan());
        }

        @Test
        @DisplayName("should return scheduler")
        void shouldGetScheduler() {
            Scheduler expectedScheduler = mock(Scheduler.class);

            InMemoryDataManager spyManager = spy(manager);
            doReturn(expectedScheduler).when(spyManager).getScheduler();

            assertEquals(expectedScheduler, spyManager.getScheduler());
        }
    }
}
