package it.unicam.cs.mpgc.jbudget109164.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.unicam.cs.mpgc.jbudget109164.repository.movement.JsonMovementRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.jbudget109164.exception.config.JsonRepositoryConfigException;

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
    @DisplayName("should return a non-null File for JsonTagRepository")
    void shouldReturnDirectoryForJsonTagRepository() {
        assertNotNull(underTest.getDirectory(JsonTagRepository.class));
    }

    @Test
    @DisplayName("should return a non-null File for JsonMovementRepository")
    void shouldReturnDirectoryForJsonMovementRepository() {
        assertNotNull(underTest.getDirectory(JsonMovementRepository.class));
    }

}
