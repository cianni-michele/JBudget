package it.unicam.cs.mpgc.jbudget109164.config.serialization.dto;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.CustomTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing {@link MovementDTO} objects.
 * This adapter handles the conversion between JSON and MovementDTO, including null checks
 * and default values.
 * <p>
 * Example JSON structure:
 * <pre>
 * {
 *  "id": "123e4567-e89b-12d3-a456-426614174000",
 *  "amount": 100.0,
 *  "description": "Payment for services",
 *  "date": "2023-10-01T12:00:00Z",
 *  "tags": [ "123e4567-e89b-12d3-a456-426614174000", "123e4567-e89b-12d3-a456-426614174001" ]
 * }
 * </pre>
 *
 * @author Michele Cianni
 */
public class MovementDTOTypeAdapter implements CustomTypeAdapter<MovementDTO> {

    private static final String ID_PROPERTY = "id";
    private static final String AMOUNT_PROPERTY = "amount";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String DATE_PROPERTY = "date";
    private static final String TAGS_PROPERTY = "tags";

    @Override
    public MovementDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for MovementDTO");
        }

        JsonObject movement = json.getAsJsonObject();

        UUID id = movement.has(ID_PROPERTY) ?
                UUID.fromString(movement.get(ID_PROPERTY).getAsString()) : null;

        Double amount = movement.has(AMOUNT_PROPERTY) ?
                movement.get(AMOUNT_PROPERTY).getAsDouble() : null;

        String description = movement.has(DESCRIPTION_PROPERTY) ?
                movement.get(DESCRIPTION_PROPERTY).getAsString() : null;

        LocalDate date = movement.has(DATE_PROPERTY) ?
                context.deserialize(movement.get(DATE_PROPERTY), LocalDate.class) : null;

        TagDTO[] tags = movement.has(TAGS_PROPERTY) ?
                deserializeTags(movement.get(TAGS_PROPERTY)) : null;

        return MovementDTO.builder()
                .withId(id)
                .withAmount(amount)
                .withDescription(description)
                .withDate(date)
                .withTags(tags)
                .build();
    }

    private TagDTO[] deserializeTags(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        if (!json.isJsonArray()) {
            throw new JsonParseException("Expected a JSON array for tags");
        }
        JsonArray tagsArray = json.getAsJsonArray();
        TagDTO[] tags = new TagDTO[tagsArray.size()];
        for (int i = 0; i < tagsArray.size(); i++) {
            JsonElement tagElement = tagsArray.get(i);
            if (!tagElement.isJsonPrimitive() || !tagElement.getAsJsonPrimitive().isString()) {
                throw new JsonParseException("Expected a string for tag ID");
            }
            UUID tagId = UUID.fromString(tagElement.getAsString());
            tags[i] = TagDTO.builder().withId(tagId).build();
        }
        return tags;
    }


    @Override
    public JsonElement serialize(MovementDTO src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject json = new JsonObject();

        if (src.id() != null) {
            json.addProperty(ID_PROPERTY, src.id().toString());
        }

        if (src.amount() != null) {
            json.addProperty(AMOUNT_PROPERTY, src.amount());
        }

        if (src.description() != null) {
            json.addProperty(DESCRIPTION_PROPERTY, src.description());
        }

        if (src.date() != null) {
            json.add(DATE_PROPERTY, context.serialize(src.date()));
        }

        if (src.tags() != null) {
            json.add(TAGS_PROPERTY, serializeTags(src.tags()));
        }

        return json;
    }

    private static JsonElement serializeTags(TagDTO[] src) {
        if (src == null || src.length == 0) {
            return JsonNull.INSTANCE;
        }
        JsonArray tagsArray = new JsonArray();
        for (TagDTO tag : src) {
            if (tag != null) {
                UUID tagId = tag.id();
                if (tagId != null) {
                    tagsArray.add(tagId.toString());
                }
            }
        }
        return tagsArray;
    }
}
