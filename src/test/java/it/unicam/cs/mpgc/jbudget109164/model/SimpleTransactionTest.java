package it.unicam.cs.mpgc.jbudget109164.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SimpleTransactionTest {

    private UUID id;
    private String description;
    private double amount;
    private LocalDate date;
    private Set<Tag> tags;
    private SimpleTransaction transaction;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        description = "Test transaction";
        amount = 100.0;
        date = LocalDate.of(2025, 6, 17);
        tags = Set.of(mock(Tag.class));
        transaction = new SimpleTransaction(id, description, amount, date, tags);
    }

    @Test
    @DisplayName("Should return the UUID provided in constructor")
    void shouldReturnId() {
        assertEquals(id, transaction.id());
    }

    @Test
    @DisplayName("Should return the description provided in constructor")
    void shouldReturnDescription() {
        assertEquals(description, transaction.description());
    }

    @Test
    @DisplayName("Should return the amount provided in constructor")
    void shouldReturnAmount() {
        assertEquals(amount, transaction.amount());
    }

    @Test
    @DisplayName("Should return the date provided in constructor")
    void shouldReturnDate() {
        assertEquals(date, transaction.date());
    }

    @Test
    @DisplayName("Should return the tags provided in constructor")
    void shouldReturnTags() {
        assertEquals(tags, transaction.tags());
    }

    @Test
    @DisplayName("Should return an unmodifiable tags set")
    void shouldReturnUnmodifiableTagsSet() {

        Set<Tag> returnedTags = transaction.tags();
        assertThrows(UnsupportedOperationException.class,
                () -> returnedTags.add(mock(Tag.class)));
    }

    @Nested
    @DisplayName("Constructor null argument tests")
    class ConstructorNullTests {

        @Test
        @DisplayName("Should throw NullPointerException when id is null")
        void shouldThrowWhenIdNull() {
            assertThrows(NullPointerException.class, () ->
                    new SimpleTransaction(null, description, amount, date, tags));
        }

        @Test
        @DisplayName("Should throw NullPointerException when description is null")
        void shouldThrowWhenDescriptionNull() {
            assertDoesNotThrow(() -> new SimpleTransaction(id, null, amount, date, tags),
                    "Description can be null and should default to an empty string");
        }

        @Test
        @DisplayName("Should throw NullPointerException when date is null")
        void shouldThrowWhenDateNull() {
            assertThrows(NullPointerException.class, () ->
                    new SimpleTransaction(id, description, amount, null, tags));
        }

        @Test
        @DisplayName("Should throw NullPointerException when tags is null")
        void shouldThrowWhenTagsNull() {
            assertDoesNotThrow(() -> new SimpleTransaction(id, description, amount, date, null),
                    "Tags can be null and should default to an empty set");
        }
    }
}
