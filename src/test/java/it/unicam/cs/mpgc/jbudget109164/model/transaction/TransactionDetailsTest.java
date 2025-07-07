package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TransactionDetailsTest {

    private String description;
    private LocalDate date;
    private Set<Tag> tags;
    private List<Movement> movements;

    private TransactionDetails underTest;

    @BeforeEach
    void setUp() {
        description = "Test transaction";
        date = LocalDate.now();
        tags = Set.of(mock(Tag.class));
        movements = List.of(mock(Movement.class));
    }

    @Nested
    @DisplayName("Null Value Tests")
    class NullValueTests {

        @Test
        @DisplayName("Should create TransactionDetails with null description as empty string")
        void shouldCreateWithNullDescriptionAsEmptyString() {
            underTest = new TransactionDetails(null, date, tags, movements);

            assertEquals("", underTest.description());
        }

        @Test
        @DisplayName("Should create TransactionDetails with null date as current date")
        void shouldCreateWithNullDateAsCurrentDate() {
            underTest = new TransactionDetails(description, null, tags, movements);

            assertNotNull(underTest.date(), "Date should not be null");
        }

        @Test
        @DisplayName("Should create TransactionDetails with null tags as empty set")
        void shouldCreateWithNullTagsAsEmptySet() {
            underTest = new TransactionDetails(description, date, null, movements);

            assertNotNull(underTest.tags(), "Tags should not be null");
        }

        @Test
        @DisplayName("Should create TransactionDetails with null movements as empty list")
        void shouldCreateWithNullMovementsAsEmptyList() {
            underTest = new TransactionDetails(description, date, tags, null);

            assertNotNull(underTest.movements(), "Movements should not be null");
        }
    }

    @Nested
    @DisplayName("Valid Value Tests")
    class ValidValueTests {

        @BeforeEach
        void setUp() {
            underTest = new TransactionDetails(description, date, tags, movements);
        }

        @Test
        @DisplayName("Should return the description provided in constructor")
        void shouldReturnDescription() {
            assertEquals(description, underTest.description());
        }

        @Test
        @DisplayName("Should return the date provided in constructor")
        void shouldReturnDate() {
            assertEquals(date, underTest.date());
        }

        @Test
        @DisplayName("Should return the tags provided in constructor")
        void shouldReturnTags() {
            assertEquals(tags, underTest.tags());
        }
    }

    @Nested
    @DisplayName("Tag Modification Tests")
    class TagModificationTests {

        @Test
        @DisplayName("Should throw NullPointerException when checking null tag")
        void shouldThrowWhenCheckingNullTag() {
            underTest = new TransactionDetails(description, date, new HashSet<>(), movements);

            assertThrows(NullPointerException.class, () -> underTest.hasTag(null), "Tag cannot be null");
        }


        @Test
        @DisplayName("Should check if a tag exists in the tags set")
        void shouldCheckIfTagExists() {
            Tag existingTag = mock(Tag.class);
            underTest = new TransactionDetails(description, date, new HashSet<>(Set.of(existingTag)), movements);

            assertTrue(underTest.hasTag(existingTag), "Tag should exist in the set");
        }

        @Test
        @DisplayName("Should return false if tag does not exist in the tags set")
        void shouldReturnFalseIfTagDoesNotExist() {
            Tag nonExistingTag = mock(Tag.class);
            underTest = new TransactionDetails(description, date, new HashSet<>(), movements);

            assertFalse(underTest.hasTag(nonExistingTag), "Tag should not exist in the set");
        }

        @Test
        @DisplayName("Should throw NullPointerException when adding null tag")
        void shouldThrowWhenAddingNullTag() {
            underTest = new TransactionDetails(description, date, new HashSet<>(), movements);

            assertThrows(NullPointerException.class, () -> underTest.addTag(null), "Tag cannot be null");
        }

        @Test
        @DisplayName("Should add a tag to the tags set")
        void shouldAddTag() {
            underTest = new TransactionDetails(description, date, new HashSet<>(), movements);
            Tag newTag = mock(Tag.class);
            underTest.addTag(newTag);

            assertTrue(underTest.hasTag(newTag), "Tag should be added to the set");
        }

        @Test
        @DisplayName("Should throw NullPointerException when removing null tag")
        void shouldThrowWhenRemovingNullTag() {
            underTest = new TransactionDetails(description, date, new HashSet<>(), movements);

            assertThrows(NullPointerException.class, () -> underTest.removeTag(null), "Tag cannot be null");
        }

        @Test
        @DisplayName("Should remove a tag from the tags set")
        void shouldRemoveTag() {
            Tag tagToRemove = mock(Tag.class);
            underTest = new TransactionDetails(description, date, new HashSet<>(Set.of(tagToRemove)), movements);
            underTest.removeTag(tagToRemove);

            assertFalse(underTest.hasTag(tagToRemove), "Tag should be removed from the set");
        }
    }
    
}
