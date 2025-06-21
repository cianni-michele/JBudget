package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.TagFactory;

import java.lang.reflect.Type;

public class TagTypeAdapter implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

    private final TagFactory tagFactory;

    public TagTypeAdapter(TagFactory tagFactory) {
        this.tagFactory = tagFactory;
    }

    @Override
    public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Tag result;
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();

        result = tagFactory.createTag(name);

        JsonArray childrenArray = jsonObject.getAsJsonArray("children");

        for (int i = 0; i < childrenArray.size(); i++) {
            Tag child = context.deserialize(childrenArray.get(i), Tag.class);
            result.addChild(child);
        }

        return result;
    }

    @Override
    public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.addProperty("name", src.name());

        JsonArray childrenArray = new JsonArray();
        for (Tag child : src) {
            JsonElement childJson = context.serialize(child, Tag.class);
            childrenArray.add(childJson);
        }
        result.add("children", childrenArray);

        return result;

    }
}
