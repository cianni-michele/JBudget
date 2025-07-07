package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SimpleTransactionTest {

    private UUID id;
    private TransactionDetails details;
    private List<Movement> movements;

    private SimpleTransaction transaction;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        details = new TransactionDetails(
                "Test transaction",
                LocalDate.now(),
                Set.of(mock(Tag.class)),
                new ArrayList<>()
        );
        transaction = new SimpleTransaction(id, details);
    }

    @Test
    @DisplayName("Should return the UUID provided in constructor")
    void shouldReturnId() {
        assertEquals(id, transaction.getId());
    }

    @Test
    @DisplayName("Should return the description provided in constructor")
    void shouldReturnDescription() {
        assertEquals(details.description(), transaction.getDescription());
    }

    @Test
    @DisplayName("Should return the date provided in constructor")
    void shouldReturnDate() {
        assertEquals(details.date(), transaction.getDate());
    }

    @Test
    @DisplayName("Should return the tags provided in constructor")
    void shouldReturnTags() {
        assertEquals(details.tags(), transaction.getTags());
    }

    @Test
    @DisplayName("Should return an unmodifiable tags set")
    void shouldReturnUnmodifiableTagsSet() {
        assertThrows(UnsupportedOperationException.class, () -> transaction.getTags().add(mock(Tag.class)));
    }

    @Nested
    @DisplayName("Constructor null argument tests")
    class ConstructorNullTests {

        @Test
        @DisplayName("Should throw NullPointerException when id is null")
        void shouldThrowWhenIdNull() {
            assertThrows(NullPointerException.class, () -> new SimpleTransaction(null, details),
                    "ID cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when description is null")
        void shouldThrowWhenDescriptionNull() {
            TransactionDetails detailsWithNullDescription = new TransactionDetails(
                    null,
                    LocalDate.now(),
                    Set.of(mock(Tag.class)),
                    new ArrayList<>()
            );

            assertDoesNotThrow(() -> new SimpleTransaction(id, detailsWithNullDescription),
                    "Description can be null and should default to an empty string");
        }

        @Test
        @DisplayName("Should not throw NullPointerException when date is null")
        void shouldNotThrowWhenDateNull() {
            TransactionDetails detailsWithNullDate = new TransactionDetails(
                    "Test transaction",
                    null,
                    Set.of(mock(Tag.class)),
                    new ArrayList<>()
            );

            assertDoesNotThrow(() -> new SimpleTransaction(id, detailsWithNullDate),
                    "Date can be null and should default to LocalDate.now()");
        }

        @Test
        @DisplayName("Should not throw Excpetion when tags are null")
        void shouldNotThrowWhenTagsNull() {
            assertDoesNotThrow(() -> new SimpleTransaction(id, details),
                    "Tags can be null and should default to an empty set");
        }
    }
}
