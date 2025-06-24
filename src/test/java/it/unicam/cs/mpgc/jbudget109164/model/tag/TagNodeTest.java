package it.unicam.cs.mpgc.jbudget109164.model.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TagNode Tests")
class TagNodeTest {

    private static final String TEST_TAG_NAME = "TestTagName";

    private static TagNode buildTestTag() {
        return buildTagNodeWithName(TEST_TAG_NAME);
    }

    private static TagNode buildTagNodeWithName(String name) {
        return buildTagNodeWithIdAndName(UUID.randomUUID(), name);
    }

    private static TagNode buildTagNodeWithIdAndName(UUID id, String name) {
        return new TagNode(id, name);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Nested
        @DisplayName("Throwing Tests")
        class ThrowingTests {

            @Test
            @DisplayName("Should throw IllegalArgumentException when id is null")
            void shouldThrowExceptionWhenIdIsNull() {
                assertThrows(IllegalArgumentException.class, () -> buildTagNodeWithIdAndName(null, TEST_TAG_NAME));
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when name is null")
            void shouldThrowExceptionWhenNameIsNull() {
                assertThrows(IllegalArgumentException.class, () -> buildTagNodeWithName(null));
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when name is empty")
            void shouldThrowExceptionWhenNameIsEmpty() {
                assertThrows(IllegalArgumentException.class, () -> buildTagNodeWithName(""));
            }
        }

        @Nested
        @DisplayName("Successful Creation Tests")
        class SuccessfulCreationTests {

            private TagNode tag;

            @BeforeEach
            void setUp() {
                tag = buildTestTag();
            }

            @Test
            @DisplayName("Should create tag with id")
            void shouldCreateTagWithId() {
                assertNotNull(tag.getId());
            }

            @Test
            @DisplayName("Should create tag with name")
            void shouldCreateTagWithName() {
                assertNotNull(tag.getName());
            }

            @Test
            @DisplayName("Should return name correctly")
            void shouldCreateWithCorrectName() {
                assertEquals(TEST_TAG_NAME, tag.getName());
            }

            @Test
            @DisplayName("Should have no parents by default")
            void shouldNotHaveParentsByDefault() {
                assertFalse(tag.hasParents());
            }

            @Test
            @DisplayName("Should have no children by default")
            void shouldNotHaveChildrenByDefault() {
                assertFalse(tag.hasChildren());
            }

        }

        @Nested
        @DisplayName("Hierarchy Tests")
        class HierarchyTests {
            private TagNode rootTag;

            private TagNode childTag;

            private TagNode grandChildTag;

            @BeforeEach
            void setUp() {
                rootTag = buildTagNodeWithName("RootTag");
            }

            @Test
            @DisplayName("Should throw NullPointerException when adding null child")
            void shouldThrowNullPointerExceptionWhenAddingNullChild() {
                assertThrows(NullPointerException.class, () -> rootTag.addChild(null));
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when adding self as child")
            void shouldThrowIllegalArgumentExceptionWhenAddingSelfAsChild() {
                assertThrows(IllegalArgumentException.class, () -> rootTag.addChild(rootTag));
            }

            @Test
            @DisplayName("Should throw NullPointerException when removing null child")
            void shouldThrowNullPointerExceptionWhenRemovingNullChild() {
                assertThrows(NullPointerException.class, () -> rootTag.removeChild(null));
            }

            @Test
            @DisplayName("Should not remove non-child tag")
            void shouldNotRemoveNonChildTag() {
                assertFalse(rootTag.removeChild(buildTagNodeWithName("NonChildTag")));
            }

            @Nested
            @DisplayName("Child Tag Tests")
            class ChildTagTests {

                private boolean isChildAdded;

                @BeforeEach
                void setUp() {
                    childTag = buildTagNodeWithName("ChildTag");
                    isChildAdded = rootTag.addChild(childTag);
                }

                @Test
                @DisplayName("Should throw IllegalArgumentException when adding child that creates cycle")
                void shouldThrowExceptionWhenAddingChildThatCreatesCycle() {
                    assertThrows(IllegalArgumentException.class, () -> childTag.addChild(rootTag));
                }

                @Nested
                @DisplayName("Adding Child Tests")
                class AddingChildTests {

                    @Test
                    @DisplayName("Should add child tag successfully")
                    void shouldAddChildTagSuccessfully() {
                        assertTrue(isChildAdded, "Child tag should be added successfully");
                    }

                    @Test
                    @DisplayName("Should consider root as parent of child")
                    void shouldConsiderRootAsParentOfChild() {
                        assertTrue(rootTag.isParentOf(childTag));
                    }

                    @Test
                    @DisplayName("Should consider child as child of root")
                    void shouldConsiderChildAsChildOfRoot() {
                        assertTrue(childTag.isChildOf(rootTag));
                    }

                    @Test
                    @DisplayName("Should not consider root as child of child")
                    void shouldNotConsiderRootAsChildOfChild() {
                        assertFalse(childTag.isParentOf(rootTag));
                    }

                    @Nested
                    @DisplayName("Child Removal Tests")
                    class ChildRemovalTests {

                        private boolean isChildRemoved;

                        @BeforeEach
                        void setUp() {
                            isChildRemoved = rootTag.removeChild(childTag);
                        }

                        @Test
                        @DisplayName("Should remove child tag correctly")
                        void shouldRemoveChildCorrectly() {
                            assertTrue(isChildRemoved, "Child tag should be removed successfully");
                        }
                    }
                }

                @Nested
                @DisplayName("Grandchild Tag Tests")
                class GrandChildTagTests {

                    @BeforeEach
                    void setUp() {
                        grandChildTag = buildTagNodeWithName("GrandChildTag");
                    }

                    @Test
                    @DisplayName("Should correctly add and contain children")
                    void shouldCorrectlyAddAndContainChildren() {
                        rootTag.addChild(childTag);
                        childTag.addChild(grandChildTag);

                        Set<Tag> childs = Set.of(childTag, grandChildTag);

                        assertTrue(rootTag.isParentOf(childs));
                    }

                    @Test
                    @DisplayName("Should correctly identify grandchild as child of root")
                    void shouldCorrectlyIdentifyGrandchildAsChildOfRoot() {
                        rootTag.addChild(childTag);
                        childTag.addChild(grandChildTag);

                        assertTrue(rootTag.isParentOf(grandChildTag));
                    }

                    @Test
                    @DisplayName("Should not consider root as child of grandchild")
                    void shouldNotConsiderRootAsChildOfGrandchild() {
                        rootTag.addChild(childTag);
                        childTag.addChild(grandChildTag);

                        assertFalse(grandChildTag.isParentOf(rootTag));
                    }

                }
            }
        }
    }
}