package it.unicam.cs.mpgc.jbudget109164.repository.transaction;

import it.unicam.cs.mpgc.jbudget109164.TestDTOs;
import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.account.AccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TransactionDTOBuilder;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonTransactionRepositoryTest {

    private static final String DIRECTORY_PATH = "src/test/resources/transactions";

    private JsonTransactionRepository underTest;

    private TransactionDTO testTransaction;

    @BeforeEach
    void setUp() {
        JsonRepositoryConfig config = spy(DefaultJsonRepositoryConfig.class);

        when(config.getDirectory(JsonTransactionRepository.class)).thenReturn(new File(DIRECTORY_PATH));

        TagRepository tagRepository = mock(TagRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);

        underTest = new JsonTransactionRepository(config, accountRepository, tagRepository);

        when(tagRepository.findById(any(Long.class))).thenReturn(mock(TagDTO.class));
        when(accountRepository.findById(any(Long.class))).thenReturn(mock(AccountDTO.class));

        testTransaction = TestDTOs.getTransaction();
    }

    @Test
    @DisplayName("Should throw exception when save a null transaction")
    void shouldThrowExceptionWhenSaveNullTransaction() {
        assertThrows(JsonRepositoryException.class, () -> underTest.save(null));
    }

    @Test
    @DisplayName("Should throw exception when save a transaction with null ID")
    void shouldThrowExceptionWhenSaveTransactionWithNullId() {
        TransactionDTO transactionWithNullId = TransactionDTOBuilder.getInstance()
                .withId(null)
                .build();

        assertThrows(JsonRepositoryException.class, () -> underTest.save(transactionWithNullId));
    }

    @Test
    @DisplayName("Should save a transaction")
    void shouldSaveTransaction() {
        underTest.save(testTransaction);

        File file = getFileOfTestTransaction();

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should not save a duplicate transaction")
    void shouldNotSaveDuplicateTransaction() {
        underTest.save(testTransaction);

        assertThrows(JsonRepositoryException.class, () -> underTest.save(testTransaction));
    }

    @Test
    @DisplayName("Should find a transaction by ID")
    void shouldFindTransactionById() {
        underTest.save(testTransaction);

        TransactionDTO transactionFound = underTest.findById(testTransaction.id());

        assertNotNull(transactionFound);
    }

    @Test
    @DisplayName("Should not find a transaction by non-existing ID")
    void shouldNotFindTransactionByNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();

        TransactionDTO foundTransaction = underTest.findById(nonExistingId);

        assertNull(foundTransaction);
    }

    @Test
    @DisplayName("Should delete a transaction by ID")
    void shouldDeleteTransactionById() {
        underTest.save(testTransaction);

        underTest.deleteById(testTransaction.id());

        File file = getFileOfTestTransaction();

        assertFalse(file.exists());
    }

    @Test
    @DisplayName("Should not delete a non-existing transaction")
    void shouldNotDeleteNonExistingTransaction() {
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(JsonRepositoryException.class, () -> underTest.deleteById(nonExistingId));
    }

    @Test
    @DisplayName("Should update an existing transaction")
    void shouldUpdateExistingTransaction() {
        underTest.save(testTransaction);

        TransactionDTO updatedTransaction = TransactionDTOBuilder.getInstance().copyOf(testTransaction)
                .withDescription("Updated description")
                .build();

        underTest.update(updatedTransaction);

        TransactionDTO foundTransaction = underTest.findById(testTransaction.id());

        assertNotEquals(testTransaction, foundTransaction);
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

    private File getFileOfTestTransaction() {
        return new File(DIRECTORY_PATH + "/" + testTransaction.id() + ".json");
    }
}