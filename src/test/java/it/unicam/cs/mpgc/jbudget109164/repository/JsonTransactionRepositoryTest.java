package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.transaction.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.JsonRepositoryException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class JsonTransactionRepositoryTest {

    private static final String DIRECTORY_PATH = "src/test/resources/transactions";

    private JsonTransactionRepository repository;
    private TransactionDTO testTransaction;

    @BeforeEach
    void setUp() {
        JsonRepositoryConfig config = mock(JsonRepositoryConfig.class);

        when(config.getDirectory(JsonTransactionRepository.class)).thenReturn(new File(DIRECTORY_PATH));
        when(config.getGson()).thenReturn(new Gson());

        repository = new JsonTransactionRepository(config);

        testTransaction =  new TransactionDTO(
                UUID.randomUUID().toString(),
                "2023-10-01",
                "Test Transaction",
                100.0,
                null
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterEach
    void tearDown() {
        File dir = new File(DIRECTORY_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    @Test
    @DisplayName("Should throw exception when save a null transaction")
    void shouldThrowExceptionWhenSaveNullTransaction() {
        assertThrows(JsonRepositoryException.class, () -> repository.save(null));
    }

    @Test
    @DisplayName("Should throw exception when save a transaction with null ID")
    void shouldThrowExceptionWhenSaveTransactionWithNullId() {
        TransactionDTO transactionWithNullId = new TransactionDTO(
                null,
                "2023-10-01",
                "Test Transaction",
                100.0,
                null
        );

        assertThrows(JsonRepositoryException.class, () -> repository.save(transactionWithNullId));
    }

    @Test
    @DisplayName("Should save a transaction")
    void shouldSaveTransaction() {
        repository.save(testTransaction);

        File file = getFileOfTestTransaction();

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should not save a duplicate transaction")
    void shouldNotSaveDuplicateTransaction() {
        repository.save(testTransaction);

        assertThrows(JsonRepositoryException.class, () -> repository.save(testTransaction));
    }

    @Test
    @DisplayName("Should find a transaction by ID")
    void shouldFindTransactionById() {
        repository.save(testTransaction);

        Optional<TransactionDTO> foundTransaction = repository.findById(testTransaction.id());

        assertTrue(foundTransaction.isPresent());
    }

    @Test
    @DisplayName("Should not find a transaction by non-existing ID")
    void shouldNotFindTransactionByNonExistingId() {
        String nonExistingId = UUID.randomUUID().toString();

        Optional<TransactionDTO> foundTransaction = repository.findById(nonExistingId);

        assertFalse(foundTransaction.isPresent());
    }

    @Test
    @DisplayName("Should delete a transaction by ID")
    void shouldDeleteTransactionById() {
        repository.save(testTransaction);

        repository.deleteById(testTransaction.id());

        File file = getFileOfTestTransaction();

        assertFalse(file.exists());
    }

    @Test
    @DisplayName("Should not delete a non-existing transaction")
    void shouldNotDeleteNonExistingTransaction() {
        String nonExistingId = UUID.randomUUID().toString();

        assertThrows(JsonRepositoryException.class, () -> repository.deleteById(nonExistingId));
    }

    @Test
    @DisplayName("Should update an existing transaction")
    void shouldUpdateExistingTransaction() {
        repository.save(testTransaction);

        TransactionDTO updatedTransaction = new TransactionDTO(
                testTransaction.id(),
                "2023-10-02",
                "Updated Transaction",
                150.0,
                null
        );

        repository.update(updatedTransaction);

        Optional<TransactionDTO> foundTransaction = repository.findById(testTransaction.id());

        assertNotEquals(testTransaction, foundTransaction.get());
    }

    private File getFileOfTestTransaction() {
        return new File(DIRECTORY_PATH + "/" + testTransaction.id() + ".json");
    }

}