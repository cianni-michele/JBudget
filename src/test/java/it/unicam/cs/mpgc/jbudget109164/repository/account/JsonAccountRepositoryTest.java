package it.unicam.cs.mpgc.jbudget109164.repository.account;

import it.unicam.cs.mpgc.jbudget109164.TestDTOs;
import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.AccountDTOBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonAccountRepositoryTest {

    private static final String DIRECTORY_PATH = "src/test/resources/accounts";

    private JsonAccountRepository underTest;

    private AccountDTO testAccount;

    @BeforeEach
    void setUp() {
        JsonRepositoryConfig config = spy(DefaultJsonRepositoryConfig.class);

        when(config.getDirectory(JsonAccountRepository.class)).thenReturn(new File(DIRECTORY_PATH));

        underTest = new JsonAccountRepository(config);

        testAccount = TestDTOs.getAccount();
    }

    @Test
    @DisplayName("Should throw exception when save a null account")
    void shouldThrowExceptionWhenSaveNullAccount() {
        assertThrows(JsonRepositoryException.class, () -> underTest.save(null));
    }

    @Test
    @DisplayName("Should throw exception when save a account with null ID")
    void shouldThrowExceptionWhenSaveAccountWithNullId() {
        AccountDTO accountWithNullId = AccountDTOBuilder.getInstance()
                .withId(null)
                .build();

        assertThrows(JsonRepositoryException.class, () -> underTest.save(accountWithNullId));
    }

    @Test
    @DisplayName("Should save an account")
    void shouldSaveAccount() {
        underTest.save(testAccount);

        File file = getFileOfTestAccount();

        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should not save a duplicate account")
    void shouldNotSaveDuplicateAccount() {
        underTest.save(testAccount);

        assertThrows(JsonRepositoryException.class, () -> underTest.save(testAccount));
    }

    @Test
    @DisplayName("Should find a account by ID")
    void shouldFindAccountById() {
        underTest.save(testAccount);

        AccountDTO accountFound = underTest.findById(testAccount.id());

        assertNotNull(accountFound);
    }

    @Test
    @DisplayName("Should not find a account by non-existing ID")
    void shouldNotFindAccountByNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();

        AccountDTO accountFound = underTest.findById(nonExistingId);

        assertNull(accountFound);
    }

    @Test
    @DisplayName("Should delete a account by ID")
    void shouldDeleteAccountById() {
        underTest.save(testAccount);

        underTest.deleteById(testAccount.id());

        File file = getFileOfTestAccount();

        assertFalse(file.exists());
    }

    @Test
    @DisplayName("Should not delete a non-existing account")
    void shouldNotDeleteNonExistingAccount() {
        UUID nonExistingId = UUID.randomUUID();

        assertThrows(JsonRepositoryException.class, () -> underTest.deleteById(nonExistingId));
    }

    @Test
    @DisplayName("Should update an existing account")
    void shouldUpdateExistingAccount() {
        underTest.save(testAccount);

        AccountDTO updatedAccount = AccountDTOBuilder.getInstance().copyOf(testAccount)
                .withName("Updated Account Name")
                .withInitialBalance(2000.0)
                .build();

        underTest.update(updatedAccount);

        AccountDTO foundAccount = underTest.findById(testAccount.id());

        assertNotEquals(testAccount, foundAccount);
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

    private File getFileOfTestAccount() {
        return new File(DIRECTORY_PATH + "/" + testAccount.id() + ".json");
    }
}