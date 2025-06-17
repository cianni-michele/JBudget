package it.unicam.cs.mpgc.jbudget109164.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CategoryTag Tests")
class CategoryTagTest {

    @Test
    @DisplayName("Should prevent cyclic hierarchies")
    void shouldPreventCyclicHierarchies() {
        Tag root = new CategoryTag("Root");
        Tag child = new CategoryTag(root, "Child");
        root.addChild(child);

        assertThrows(IllegalArgumentException.class, () -> child.addChild(root));
    }

    @Nested
    @DisplayName("Name Property Tests")
    class NameTests {

        @Test
        @DisplayName("Should throw NullPointerException when name is null")
        void shouldThrowNullPointerExceptionWhenNameIsNull() {
            assertThrows(NullPointerException.class, () -> new CategoryTag(null));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is empty")
        void shouldThrowIllegalArgumentExceptionWhenNameIsEmpty() {
            assertThrows(IllegalArgumentException.class, () -> new CategoryTag(""));
        }

        @Test
        @DisplayName("Should return name correctly")
        void shouldReturnNameCorrectly() {
            Tag tag = new CategoryTag("TagName");
            assertEquals("TagName", tag.getName());
        }
    }

    @Nested
    @DisplayName("Parent Property Tests")
    class ParentTests {

        @Test
        @DisplayName("Should have empty parent by default")
        void shouldHaveEmptyParentByDefault() {
            Tag tag = new CategoryTag("TagName");
            assertTrue(tag.getParent().isEmpty());
        }

        @Test
        @DisplayName("Should return parent correctly when provided")
        void shouldReturnParentCorrectlyWhenProvided() {
            Tag parent = new CategoryTag("ParentTag");
            Tag child = new CategoryTag(parent, "ChildTag");
            assertEquals(Optional.of(parent), child.getParent());
        }
    }

    @Nested
    @DisplayName("Children Property Tests")
    class ChildrenTests {

        @Test
        @DisplayName("Should have no children by default")
        void shouldHaveNoChildrenByDefault() {
            Tag tag = new CategoryTag("TagName");
            assertFalse(tag.hasChildren());
        }

        @Test
        @DisplayName("Should throw NullPointerException when adding null child")
        void shouldThrowNullPointerExceptionWhenAddingNullChild() {
            Tag parent = new CategoryTag("ParentTag");
            assertThrows(NullPointerException.class, () -> parent.addChild(null));
        }

        @Test
        @DisplayName("Should correctly add and contain children")
        void shouldCorrectlyAddAndContainChildren() {
            Tag parent = new CategoryTag("ParentTag");
            Tag child1 = new CategoryTag("Child1");
            Tag child2 = new CategoryTag("Child2");

            parent.addChild(child1);
            parent.addChild(child2);

            assertTrue(parent.containsChild(child1, child2));
        }

        @Test
        @DisplayName("Should correctly build and verify multi-level hierarchy")
        void shouldCorrectlyBuildAndVerifyMultiLevelHierarchy() {
            Tag root = new CategoryTag("Root");
            Tag child1 = new CategoryTag(root, "Child1");
            Tag grandChild1 = new CategoryTag(child1, "GrandChild1");
            child1.addChild(grandChild1);
            root.addChild(child1);
            Tag child2 = new CategoryTag(root, "Child2");
            root.addChild(child2);

            assertTrue(root.containsChild(child1, child2, grandChild1));
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should consider tags with same name equal")
        void shouldConsiderTagsWithSameNameEqual() {
            Tag tag1 = new CategoryTag("TagName");
            Tag tag2 = new CategoryTag("TagName");
            assertEquals(tag1, tag2);
        }

        @Test
        @DisplayName("Should consider tags with different names not equal")
        void shouldConsiderTagsWithDifferentNamesNotEqual() {
            Tag tag1 = new CategoryTag("TagName1");
            Tag tag2 = new CategoryTag("TagName2");
            assertNotEquals(tag1, tag2);
        }

        @Test
        @DisplayName("Should have same hashCode for tags with same name")
        void shouldHaveSameHashCodeForTagsWithSameName() {
            Tag tag1 = new CategoryTag("TagName");
            Tag tag2 = new CategoryTag("TagName");
            assertEquals(tag1.hashCode(), tag2.hashCode());
        }

        @Test
        @DisplayName("Should have different hashCode for tags with different names")
        void shouldHaveDifferentHashCodeForTagsWithDifferentNames() {
            Tag tag1 = new CategoryTag("TagName1");
            Tag tag2 = new CategoryTag("TagName2");
            assertNotEquals(tag1.hashCode(), tag2.hashCode());
        }
    }
}