package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TagSerializer Tests")
class TagSerializerTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Tag.class, new TagSerializer())
                .registerTypeAdapter(TagNode.class, new TagSerializer())
                .create();
    }

    @Nested
    @DisplayName("TagNode Type Tests")
    class TagNodeTypeTests {

        @Nested
        @DisplayName("Serialization Tests")
        class SerializationTests {

            private Tag tag;

            @BeforeEach
            void setUp() {
                tag = new TagNode(UUID.randomUUID(), "Test Node");
            }

            @Test
            @DisplayName("Should serialize TagNode to JSON object")
            void shouldReturnJsonObject() {
                JsonElement json = gson.toJsonTree(tag);

                assertTrue(json.isJsonObject(), "Serialized JSON should be an object");
            }

            @Test
            @DisplayName("Should serialize TagNode with correct type")
            void shouldSerializeWithCorrectType() {
                JsonElement json = gson.toJsonTree(tag);

                String type = json.getAsJsonObject().get("type").getAsString();

                assertEquals(TagNode.class.getName(), type, "Type should match TagNode class name");
            }

            @Test
            @DisplayName("Should serialize TagNode with correct id")
            void shouldSerializeWithCorrectId() {
                JsonElement json = gson.toJsonTree(tag);

                String id = json.getAsJsonObject().get("id").getAsString();

                assertEquals(tag.getId().toString(), id, "ID should match TagNode ID");
            }

            @Test
            @DisplayName("Should serialize TagNode with correct name")
            void shouldSerializeWithCorrectName() {
                JsonElement json = gson.toJsonTree(tag);

                String name = json.getAsJsonObject().get("name").getAsString();

                assertEquals(tag.getName(), name, "Name should match TagNode name");
            }

            @Test
            @DisplayName("Should serialize TagNode with children array")
            void shouldSerializeWithChildren() {
                JsonElement json = gson.toJsonTree(tag);

                JsonElement children = json.getAsJsonObject().get("children");

                assertTrue(children.isJsonArray(), "Children should be a JSON array");
            }

            @Test
            @DisplayName("Should serialize TagNode with empty children array")
            void shouldSerializeWithEmptyChildren() {
                JsonElement json = gson.toJsonTree(tag);

                JsonElement children = json.getAsJsonObject().get("children");

                assertTrue(children.getAsJsonArray().isEmpty(), "Children array should be empty for new TagNode");
            }

            @Test
            @DisplayName("Should serialize TagNode with non-empty children")
            void shouldSerializeWithNonEmptyChildren() {
                Tag childTag = new TagNode(UUID.randomUUID(), "Child Node");
                tag.addChild(childTag);

                JsonElement json = gson.toJsonTree(tag);

                JsonElement children = json.getAsJsonObject().get("children");

                assertEquals(1, children.getAsJsonArray().size(), "Children array should contain one child");
            }

            @Test
            @DisplayName("Should serialize TagNode with multiple children")
            void shouldSerializeWithMultipleChildren() {
                Tag childTag1 = new TagNode(UUID.randomUUID(), "Child Node 1");
                Tag childTag2 = new TagNode(UUID.randomUUID(), "Child Node 2");
                tag.addChild(childTag1);
                tag.addChild(childTag2);

                JsonElement json = gson.toJsonTree(tag);

                JsonElement children = json.getAsJsonObject().get("children");

                assertEquals(2, children.getAsJsonArray().size(), "Children array should contain two children");
            }

            @Test
            @DisplayName("Should serialize TagNode with nested children")
            void shouldSerializeWithNestedChildren() {
                Tag childTag = new TagNode(UUID.randomUUID(), "Child Node");
                Tag grandChildTag = new TagNode(UUID.randomUUID(), "Grand Child Node");
                childTag.addChild(grandChildTag);
                tag.addChild(childTag);

                JsonElement json = gson.toJsonTree(tag);

                JsonElement children = json.getAsJsonObject().get("children");

                assertEquals(1, children.getAsJsonArray().size(), "Children array should contain one child after serialization");
                JsonObject childJson = children.getAsJsonArray().get(0).getAsJsonObject();
                assertTrue(childJson.has("children"), "Child JSON should have a children array");
                System.out.println(json);
            }
        }

        @Nested
        @DisplayName("Deserialization Tests")
        class DeserializationTests {

            private JsonObject json;

            @BeforeEach
            void setUp() {
                json = new JsonObject();
                json.addProperty("type", TagNode.class.getName());
                json.addProperty("id", UUID.randomUUID().toString());
                json.addProperty("name", "Test Node");
                json.add("children", new JsonArray());
            }

            @Test
            @DisplayName("Should deserialize JSON object to TagNode")
            void shouldDeserializeToTagNode() {
                Tag tag = gson.fromJson(json, Tag.class);

                assertInstanceOf(TagNode.class, tag, "Deserialized object should be a TagNode");
            }

            @Test
            @DisplayName("Should deserialize JSON with correct type")
            void shouldDeserializeWithCorrectType() {
                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(TagNode.class.getName(), tag.getClass().getName(), "Type should match TagNode class name");
            }

            @Test
            @DisplayName("Should deserialize JSON with correct id")
            void shouldDeserializeWithCorrectId() {
                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(json.get("id").getAsString(), tag.getId().toString(), "ID should match TagNode ID");
            }

            @Test
            @DisplayName("Should deserialize JSON with correct name")
            void shouldDeserializeWithCorrectName() {
                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(json.get("name").getAsString(), tag.getName(), "Name should match TagNode name");
            }

            @Test
            @DisplayName("Should deserialize JSON with empty children array")
            void shouldDeserializeWithEmptyChildren() {
                Tag tag = gson.fromJson(json, Tag.class);

                assertFalse(tag.hasChildren(), "Children should be empty for new TagNode");
            }

            @Test
            @DisplayName("Should deserialize JSON with non-empty children")
            void shouldDeserializeWithNonEmptyChildren() {
                JsonObject childJson = new JsonObject();
                childJson.addProperty("type", TagNode.class.getName());
                childJson.addProperty("id", UUID.randomUUID().toString());
                childJson.addProperty("name", "Child Node");

                JsonArray childrenArray = new JsonArray();
                childrenArray.add(childJson);
                json.add("children", childrenArray);

                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(1, tag.getChildren().size(), "Children array should contain one child after deserialization");
            }

            @Test
            @DisplayName("Should deserialize JSON with multiple children")
            void shouldDeserializeWithMultipleChildren() {
                JsonObject childJson1 = new JsonObject();
                childJson1.addProperty("type", TagNode.class.getName());
                childJson1.addProperty("id", UUID.randomUUID().toString());
                childJson1.addProperty("name", "Child Node 1");

                JsonObject childJson2 = new JsonObject();
                childJson2.addProperty("type", TagNode.class.getName());
                childJson2.addProperty("id", UUID.randomUUID().toString());
                childJson2.addProperty("name", "Child Node 2");

                JsonArray childrenArray = new JsonArray();
                childrenArray.add(childJson1);
                childrenArray.add(childJson2);
                json.add("children", childrenArray);

                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(2, tag.getChildren().size(), "Children array should contain two children after deserialization");
            }

            @Test
            @DisplayName("Should deserialize with nested children")
            void shouldDeserializeWithNestedChildren() {
                JsonObject grandChildJson = new JsonObject();
                grandChildJson.addProperty("type", TagNode.class.getName());
                grandChildJson.addProperty("id", UUID.randomUUID().toString());
                grandChildJson.addProperty("name", "Grand Child Node");

                JsonArray childChildrenArray = new JsonArray();
                childChildrenArray.add(grandChildJson);

                JsonObject childJson = new JsonObject();
                childJson.addProperty("type", TagNode.class.getName());
                childJson.addProperty("id", UUID.randomUUID().toString());
                childJson.addProperty("name", "Child Node");
                childJson.add("children", childChildrenArray);

                JsonArray childrendArray = new JsonArray();
                childrendArray.add(childJson);
                json.add("children", childrendArray);

                Tag tag = gson.fromJson(json, Tag.class);

                assertEquals(2, tag.getAllDescendants().size(), "Should have two descendants after deserialization");
            }
        }
    }
}