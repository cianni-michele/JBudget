package it.unicam.cs.mpgc.jbudget109164.config.serialization.dto;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.CustomTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TagDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TransactionDTOBuilder;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing {@link TransactionDTO} objects.
 * This adapter handles the conversion between JSON and TransactionDTO, including null checks
 * and default values.
 * <p>
 * Example JSON structure:
 * <pre>
 * {
 *  "id": "123e4567-e89b-12d3-a456-426614174000",
 *  "date": "2023-10-01",
 *  "description": "Transaction description",
 *  "tagIds": [1 , 2],
 *  "movements": [
 *      {
 *          "amount": 100.0,
 *          "description": "Movement description",
 *          "accountId": 1
 *      }
 *   ]
 * }
 * </pre>
 */
public class TransactionDTOTypeAdapter implements CustomTypeAdapter<TransactionDTO> {

    private static final String ID_PROPERTY = "id";
    private static final String DATE_PROPERTY = "date";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String MOVEMENTS_PROPERTY = "movements";
    private static final String TAG_IDS_PROPERTY = "tagIds";

    @Override
    public TransactionDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for TransactionDTO");
        }

        JsonObject transaction = json.getAsJsonObject();

        UUID id = transaction.has(ID_PROPERTY) ?
                context.deserialize(transaction.get(ID_PROPERTY), UUID.class) : null;

        LocalDate date = transaction.has(DATE_PROPERTY) ?
                context.deserialize(transaction.get(DATE_PROPERTY), LocalDate.class) : null;

        String description = transaction.has(DESCRIPTION_PROPERTY) ?
                transaction.get(DESCRIPTION_PROPERTY).getAsString() : null;

        TagDTO[] tags = transaction.has(TAG_IDS_PROPERTY) ?
                deserializeTags(transaction.getAsJsonArray(TAG_IDS_PROPERTY)) : null;

        MovementDTO[] movements = transaction.has(MOVEMENTS_PROPERTY) ?
                context.deserialize(transaction.get(MOVEMENTS_PROPERTY), MovementDTO[].class) : null;

        return TransactionDTOBuilder.getInstance()
                .withId(id)
                .withDate(date)
                .withDescription(description)
                .withTags(tags)
                .withMovements(movements)
                .build();

    }

    private TagDTO[] deserializeTags(JsonArray tagsArray) {
        TagDTO[] tags = new TagDTO[tagsArray.size()];

        for (int i = 0; i < tagsArray.size(); i++) {
            tags[i] = TagDTOBuilder.getInstance()
                    .withId(UUID.fromString(tagsArray.get(i).getAsString()))
                    .build();
        }

        return tags;
    }

    @Override
    public JsonElement serialize(TransactionDTO src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject json = new JsonObject();

        if (src.id() != null) {
            json.add(ID_PROPERTY, context.serialize(src.id(), UUID.class));
        }

        if (src.date() != null) {
            json.add(DATE_PROPERTY, context.serialize(src.date(), LocalDate.class));
        }

        if (src.description() != null) {
            json.addProperty(DESCRIPTION_PROPERTY, src.description());
        }

        if (src.tags() != null) {
            json.add(TAG_IDS_PROPERTY, serializeTags(src.tags()));
        }

        if (src.movements() != null) {
            json.add(MOVEMENTS_PROPERTY, context.serialize(src.movements(), MovementDTO[].class));
        }

        return json;
    }

    private JsonArray serializeTags(TagDTO[] tags) {
        JsonArray tagsArray = new JsonArray();

        for (TagDTO tag : tags) {
            if (tag.id() != null) {
                tagsArray.add(tag.id().toString());
            }
        }

        return tagsArray;
    }
}
