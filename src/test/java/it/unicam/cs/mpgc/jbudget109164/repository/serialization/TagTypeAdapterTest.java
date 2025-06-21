package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.CategoryTag;
import it.unicam.cs.mpgc.jbudget109164.model.CategoryTagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.TagFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTypeAdapterTest {

    private final TagFactory tagFactory = new CategoryTagFactory();

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Tag.class, new TagTypeAdapter(tagFactory))
                .create();
    }

    @Nested
    class DeserializeTests {

        @Test
        void shouldDeserializeCategoryTagWithoutChildren() {
            String json = """
                {
                    "name": "Test Tag",
                    "children": []
                }
                """;

            Tag actual = gson.fromJson(json, Tag.class);
            Tag expected = new CategoryTag("Test Tag");

            assertEquals(expected, actual);
        }

        @Test
        void shouldDeserializeCategoryTagWithChildren() {
            String json = """
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": []
                        }
                    ]
                }
                """;

            Tag actual = gson.fromJson(json, Tag.class);
            Tag expected = new CategoryTag("Test Tag");
            expected.addChild(new CategoryTag("Child Tag 1"));

            assertEquals(expected, actual);
        }

        @Test
        void shouldDeserializeCategoryTagWithMultipleChildren() {
            String json = """
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": []
                        },
                        {
                            "name": "Child Tag 2",
                            "children": []
                        }
                    ]
                }
                """;

            Tag actual = gson.fromJson(json, Tag.class);
            Tag expected = new CategoryTag("Test Tag");
            expected.addChild(new CategoryTag("Child Tag 1"));
            expected.addChild(new CategoryTag("Child Tag 2"));

            assertEquals(expected, actual);
        }

        @Test
        void shouldDeserializeCategoryTagWithNestedChildren() {
            String json = """
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": [
                                {
                                    "name": "Grandchild Tag 1",
                                    "children": []
                                }
                            ]
                        }
                    ]
                }
                """;

            Tag actual = gson.fromJson(json, Tag.class);
            Tag expected = new CategoryTag("Test Tag");
            Tag child1 = new CategoryTag("Child Tag 1");
            Tag grandChild1 = new CategoryTag("Grandchild Tag 1");
            child1.addChild(grandChild1);
            expected.addChild(child1);

            assertEquals(expected, actual);
        }

    }

    @Nested
    class SerializeTests {

        @Test
        void shouldSerializeCategoryTagWithoutChildren() {
            Tag tag = new CategoryTag("Test Tag");


            JsonElement actual = gson.toJsonTree(tag, Tag.class);
            JsonElement expected = gson.fromJson("""
                {
                    "name": "Test Tag",
                    "children": []
                }
                """, JsonElement.class);

            CustomAssertions.Tag.assertEquals(expected, actual);
        }

        @Test
        void shouldSerializeCategoryTagWithChildren() {
            Tag tag = new CategoryTag("Test Tag");
            Tag child1 = new CategoryTag("Child Tag 1");
            tag.addChild(child1);

            JsonElement actual = gson.toJsonTree(tag, Tag.class);
            JsonElement expected = gson.fromJson("""
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": []
                        }
                    ]
                }
                """, JsonElement.class);

            CustomAssertions.Tag.assertEquals(expected, actual);
        }

        @Test
        void shouldSerializeCategoryTagWithMultipleChildren() {
            Tag tag = new CategoryTag("Test Tag");
            Tag child1 = new CategoryTag("Child Tag 1");
            tag.addChild(child1);
            Tag child2 = new CategoryTag("Child Tag 2");
            tag.addChild(child2);

            JsonElement actual = gson.toJsonTree(tag, Tag.class);
            JsonElement expected = gson.fromJson("""
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": []
                        },
                        {
                            "name": "Child Tag 2",
                            "children": []
                        }
                    ]
                }
                """, JsonElement.class);

            CustomAssertions.Tag.assertEquals(expected, actual);
        }

        @Test
        void shouldSerializeCategoryTagWithNestedChildren() {
            Tag tag = new CategoryTag("Test Tag");
            Tag child1 = new CategoryTag("Child Tag 1");
            Tag grandChild1 = new CategoryTag("Grandchild Tag 1");
            child1.addChild(grandChild1);
            tag.addChild(child1);

            JsonElement actual = gson.toJsonTree(tag, Tag.class);
            JsonElement expected = gson.fromJson("""
                {
                    "name": "Test Tag",
                    "children": [
                        {
                            "name": "Child Tag 1",
                            "children": [
                                {
                                    "name": "Grandchild Tag 1",
                                    "children": []
                                }
                            ]
                        }
                    ]
                }
                """, JsonElement.class);

            CustomAssertions.Tag.assertEquals(expected, actual);
        }
    }
}