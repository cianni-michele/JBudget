package it.unicam.cs.mpgc.jbudget109164.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.jbudget109164.exception.config.JsonRepositoryConfigException;
import it.unicam.cs.mpgc.jbudget109164.repository.account.JsonAccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.transaction.JsonTransactionRepository;

class DefaultJsonRepositoryConfigTest {

    private DefaultJsonRepositoryConfig underTest;

    @BeforeEach
    void setUp() {
        underTest = new DefaultJsonRepositoryConfig();
    }

    @Test
    @DisplayName("should return a non-null Gson instance")
    void shouldReturnGsonInstance() {
        assertNotNull(underTest.getGson());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for null class")
    void shouldThrowExceptionForNullClass() {
        assertThrows(IllegalArgumentException.class, () -> underTest.getDirectory(null));
    }
    
    @Test
    @DisplayName("should throw JsonRepositoryConfigException for unknown class")
    void shouldThrowExceptionForUnknownClass() {
        assertThrows(JsonRepositoryConfigException.class, () -> underTest.getDirectory(String.class));
    }

    @Test
    @DisplayName("should return a non-null File for JsonTransactionRepository")
    void shouldReturnDirectoryForJsonTransactionRepository() {
        assertNotNull(underTest.getDirectory(JsonTransactionRepository.class));
    }

    @Test
    @DisplayName("should return a non-null File for JsonAccountRepository")
    void shouldReturnDirectoryForJsonAccountRepository() {
        assertNotNull(underTest.getDirectory(JsonAccountRepository.class));
    }

}
