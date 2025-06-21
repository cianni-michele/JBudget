package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.CategoryTag;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.TagFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TagTypeAdapterTest {

    private final TagFactory factory = (name, children) -> {
        Tag tag = new CategoryTag(name);
        for (Tag child : children) {
            tag.addChild(child);
        }
        return tag;
    };

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Tag.class, new TagTypeAdapter(factory))
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

            Assertions.assertEquals(expected, actual);
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

            Assertions.assertEquals(expected, actual);
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

            Assertions.assertEquals(expected, actual);
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

            Assertions.assertEquals(expected, actual);
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

            assertEquals(expected, actual);
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

            assertEquals(expected, actual);
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

            assertEquals(expected, actual);
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

            assertEquals(expected, actual);
        }

        private void assertEquals(JsonElement expected, JsonElement actual) {
            JsonObject expectedObject = expected.getAsJsonObject();
            JsonObject actualObject = actual.getAsJsonObject();

            Assertions.assertEquals(
                    expectedObject.get("name").getAsString(),
                    actualObject.get("name").getAsString(),
                    "Tag names do not match"
            );


            JsonArray expectedChildrenJsonArray = expectedObject.getAsJsonArray("children");
            JsonArray actualChildrenJsonArray = actualObject.getAsJsonArray("children");

            Assertions.assertEquals(
                    expectedChildrenJsonArray.size(),
                    actualChildrenJsonArray.size(),
                    "Number of children does not match"
            );

            for (JsonElement actualJsonChild : actualChildrenJsonArray) {
                boolean foundMatch = false;
                for (JsonElement expectedJsonChild : expectedChildrenJsonArray) {
                    String actualChildName = actualJsonChild.getAsJsonObject().get("name").getAsString();
                    String expectedChildName = expectedJsonChild.getAsJsonObject().get("name").getAsString();

                    if (actualChildName.equals(expectedChildName)) {
                        JsonArray actualGrandchildren = actualJsonChild.getAsJsonObject().getAsJsonArray("children");
                        JsonArray expectedGrandchildren = expectedJsonChild.getAsJsonObject().getAsJsonArray("children");

                        if (!actualGrandchildren.isEmpty() || !expectedGrandchildren.isEmpty()) {
                            JsonObject actualChildObj = actualJsonChild.getAsJsonObject();
                            JsonObject expectedChildObj = expectedJsonChild.getAsJsonObject();

                            assertEquals(expectedChildObj, actualChildObj);
                        }

                        foundMatch = true;
                        break;
                    }
                }

                Assertions.assertTrue(
                        foundMatch,
                        "Expected child not found in actual children"
                );
            }
        }
    }
}