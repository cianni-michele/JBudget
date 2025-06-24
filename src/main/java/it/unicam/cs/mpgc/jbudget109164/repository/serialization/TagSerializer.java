package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.lang.reflect.Type;
import java.util.*;

public class TagSerializer implements CustomSerializer<Tag> {

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return deserialize(json, new HashMap<>());
    }

    private Tag deserialize(JsonElement json, Map<UUID, Tag> visited) {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        UUID id = UUID.fromString(jsonObject.get("id").getAsString());
        String name = jsonObject.get("name").getAsString();

        if (visited.containsKey(id)) {
            return visited.get(id);
        }

        Tag result = createTag(type, id, name);
        visited.put(id, result);

        if (jsonObject.has("children") && jsonObject.get("children").isJsonArray()) {
            JsonArray childrenArray = jsonObject.getAsJsonArray("children");
            for (JsonElement childElement : childrenArray) {
                JsonObject childObject = childElement.getAsJsonObject();
                UUID childId = UUID.fromString(childObject.get("id").getAsString());


                Tag childTag;
                if (visited.containsKey(childId)) {
                    childTag = visited.get(childId);
                } else {
                    childTag = deserialize(childElement, visited);
                }

                if (childTag != null) {
                    result.addChild(childTag);
                }
            }
        }
        return result;
    }

    private Tag createTag(String type, UUID id, String name) {
        try {
            Class<?> clazz = Class.forName(type);
            return (Tag) clazz.getConstructor(UUID.class, String.class).newInstance(id, name);
        } catch (Exception e) {
            throw new JsonParseException("Failed to create Tag instance of type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        return serialize(src, new HashSet<>());
    }

    private JsonElement serialize(Tag src, Set<UUID> visited) {
        JsonObject result = new JsonObject();

        result.addProperty("type", src.getClass().getName());
        result.addProperty("id", src.getId().toString());
        result.addProperty("name", src.getName());

        if (!visited.add(src.getId())) {
            return result;
        }

        JsonArray childrenArray = new JsonArray();
        for (Tag child : src.getChildren()) {
            JsonElement jsonChild = serialize(child, new HashSet<>(visited));
            childrenArray.add(jsonChild);
        }

        result.add("children", childrenArray);
        return result;
    }
}
