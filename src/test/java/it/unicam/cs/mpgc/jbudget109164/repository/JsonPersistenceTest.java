package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.jbudget109164.model.*;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagNode;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.FixedRateScheduledTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.ScheduledTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.SimpleTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.repository.serialization.LocalDateSerializer;
import it.unicam.cs.mpgc.jbudget109164.repository.serialization.RuntimeTypeAdapterFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.serialization.TagSerializer;
import it.unicam.cs.mpgc.jbudget109164.repository.serialization.YearMonthSerializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;
import java.util.UUID;

import static it.unicam.cs.mpgc.jbudget109164.repository.serialization.RuntimeTypeAdapterFactory.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test suite for JsonPersistence")
class JsonPersistenceTest {

    private final JsonRepositoryConfig config = () -> {
        RuntimeTypeAdapterFactory<DataManager> dataManagerTypeAdapterFactory = of(DataManager.class)
                .registerSubtype(InMemoryDataManager.class);

        RuntimeTypeAdapterFactory<Transaction> transactionRuntimeTypeAdapterFactory = of(Transaction.class)
                .registerSubtype(SimpleTransaction.class);

        RuntimeTypeAdapterFactory<Scheduler> schedulerTypeAdapterFactory = of(Scheduler.class)
                .registerSubtype(BasicScheduler.class);

        RuntimeTypeAdapterFactory<ScheduledTransaction> scheduledTransactionTypeAdapterFactory = of(ScheduledTransaction.class)
                .registerSubtype(FixedRateScheduledTransaction.class);

        RuntimeTypeAdapterFactory<BudgetPlan> budgetPlanTypeAdapterFactory = of(BudgetPlan.class)
                .registerSubtype(MonthlyBudgetPlan.class);

        RuntimeTypeAdapterFactory<StatisticsProvider> statisticsProviderTypeAdapterFactory = of(StatisticsProvider.class)
                .registerSubtype(DefaultStatisticsProvider.class);

        return new GsonBuilder()
                .registerTypeAdapter(Tag.class, new TagSerializer())
                .registerTypeAdapter(TagNode.class, new TagSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(YearMonth.class, new YearMonthSerializer())
                .registerTypeAdapterFactory(dataManagerTypeAdapterFactory)
                .registerTypeAdapterFactory(transactionRuntimeTypeAdapterFactory)
                .registerTypeAdapterFactory(schedulerTypeAdapterFactory)
                .registerTypeAdapterFactory(scheduledTransactionTypeAdapterFactory)
                .registerTypeAdapterFactory(budgetPlanTypeAdapterFactory)
                .registerTypeAdapterFactory(statisticsProviderTypeAdapterFactory)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    };


    private JsonPersistence jsonPersistence;

    @BeforeEach
    void setUp() {
        jsonPersistence = new JsonPersistence(config);
    }

    @Nested
    @DisplayName("Tests for saveData method")
    class SaveDataTests {

        private DataManager dataManager;
        private Path tempFilePath;

        @BeforeEach
        void setUp() throws IOException {
            dataManager = new InMemoryDataManager(new MonthlyBudgetPlan(), new BasicScheduler());
            tempFilePath = Files.createTempFile("test_save", ".json");
        }

        @AfterEach
        void tearDown() throws IOException {
            Files.deleteIfExists(tempFilePath);
        }

        @Test
        @DisplayName("Should save data successfully to a file")
        void shouldSaveDataSuccessfully() throws IOException {
            SimpleTransaction transaction = new SimpleTransaction(
                    UUID.randomUUID(),
                    "Test Transaction",
                    100.0,
                    LocalDate.now(),
                    Set.of(new TagNode(UUID.randomUUID(),"TestTag"))
            );

            jsonPersistence.save(dataManager, tempFilePath);

            assertTrue(Files.exists(tempFilePath), "File should exist after saving data");

            String content = Files.readString(tempFilePath);
            assertFalse(content.isEmpty(), "File content should not be empty after saving data");
            assertTrue(content.contains("Test Transaction"), "File content should contain the transaction description");
            assertTrue(content.contains("100.0"), "File content should contain the transaction amount");

        }

        @Test
        @DisplayName("Should throw exception when file path is invalid")
        void shouldThrowExceptionOnInvalidFilePath() {
            // TODO: Use temporary invalid path and verify exception
            fail("Not implemented yet");
        }
    }

    @Nested
    @DisplayName("Tests for loadData method")
    class LoadDataTests {

        @Test
        @DisplayName("Should load data successfully from a file")
        void shouldLoadDataSuccessfully() {
            // TODO: Prepare a temp file with sample JSON and assert parsing
            fail("Not implemented yet");
        }

        @Test
        @DisplayName("Should return empty list when file is empty")
        void shouldReturnEmptyListOnEmptyFile() {
            // TODO: Simulate empty file and check result
            fail("Not implemented yet");
        }

        @Test
        @DisplayName("Should throw exception when file content is invalid")
        void shouldThrowExceptionOnInvalidContent() {
            // TODO: Corrupt JSON and expect parsing exception
            fail("Not implemented yet");
        }
    }
}
