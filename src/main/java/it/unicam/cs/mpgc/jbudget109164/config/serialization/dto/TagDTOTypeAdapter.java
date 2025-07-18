package it.unicam.cs.mpgc.jbudget109164.config.serialization.dto;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.CustomTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing {@link TagDTO} objects.
 * This adapter handles the conversion between JSON and TagDTO, including null checks
 * and default values.
 * <p>
 * Example JSON structure:
 * <pre>
 * {
 *  "id": 1,
 *  "name": "Tag Name",
 *  "childrenIds": [2, 3]
 * }
 * </pre>
 */
public class TagDTOTypeAdapter implements CustomTypeAdapter<TagDTO> {

    private static final String ID_PROPERTY = "id";
    private static final String NAME_PROPERTY = "name";
    private static final String CHILDREN_IDS_PROPERTY = "childrenIds";

    @Override
    public TagDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for TagDTO");
        }

        JsonObject tag = json.getAsJsonObject();

        UUID id = tag.has(ID_PROPERTY) ?
                UUID.fromString(tag.get(ID_PROPERTY).getAsString()) : null;

        String name = tag.has(NAME_PROPERTY) ?
                tag.get(NAME_PROPERTY).getAsString() : null;

        TagDTO[] children = tag.has(CHILDREN_IDS_PROPERTY) ?
                deserializeChildren(tag.getAsJsonArray(CHILDREN_IDS_PROPERTY)) : null;

        return TagDTO.builder()
                .withId(id)
                .withName(name)
                .withChildren(children)
                .build();
    }

    private TagDTO[] deserializeChildren(JsonArray childrenArray) {
        if (childrenArray.isEmpty()) {
            return null;
        }

        TagDTO[] children = new TagDTO[childrenArray.size()];

        for (int i = 0; i < childrenArray.size(); i++) {
            children[i] = TagDTO.builder()
                    .withId(UUID.fromString(childrenArray.get(i).getAsString()))
                    .build();
        }

        return children;
    }

    @Override
    public JsonElement serialize(TagDTO src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject json = new JsonObject();

        if (src.id() != null) {
            json.addProperty(ID_PROPERTY, src.id().toString());
        }

        if (src.name() != null) {
            json.addProperty(NAME_PROPERTY, src.name());
        }

        if (src.children() != null) {
            json.add(CHILDREN_IDS_PROPERTY, serializeChildren(src.children()));
        }

        return json;
    }

    private JsonElement serializeChildren(TagDTO[] children) {
        JsonArray childrenArray = new JsonArray();

        for (TagDTO child : children) {
            if (child.id() != null) {
                childrenArray.add(child.id().toString());
            }
        }

        return childrenArray;
    }
}
