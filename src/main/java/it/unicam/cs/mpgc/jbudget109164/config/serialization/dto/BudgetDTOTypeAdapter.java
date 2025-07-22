package it.unicam.cs.mpgc.jbudget109164.config.serialization.dto;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.CustomTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing {@link BudgetDTO} objects.
 * This adapter handles the conversion between JSON and BudgetDTO, including null checks
 * and default values.
 * <p>
 * Example JSON structure:
 * <pre>
 * {
 *  "id": "123e4567-e89b-12d3-a456-426614174000",
 *  "tagId": "123e4567-e89b-12d3-a456-426614174001",
 *  "period": "2023-10",
 *  "expectedAmount": 1000.0
 * }
 * </pre>
 */
public class BudgetDTOTypeAdapter implements CustomTypeAdapter<BudgetDTO> {

    private static final String ID_PROPERTY = "id";
    private static final String TAG_PROPERTY = "tag";
    private static final String PERIOD_PROPERTY = "period";
    private static final String EXPECTED_AMOUNT_PROPERTY = "expectedAmount";

    @Override
    public BudgetDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for BudgetDTO");
        }

        JsonObject budget = json.getAsJsonObject();

        UUID id = budget.has(ID_PROPERTY) ?
                UUID.fromString(budget.getAsJsonObject().get(ID_PROPERTY).getAsString()) : null;

        TagDTO tag = budget.has(TAG_PROPERTY) ?
                deserializeTag(budget.get(TAG_PROPERTY)) : null;

        String period = budget.has(PERIOD_PROPERTY) ?
                budget.get(PERIOD_PROPERTY).getAsString() : null;

        Double expectedAmount = budget.has(EXPECTED_AMOUNT_PROPERTY) ?
                budget.get(EXPECTED_AMOUNT_PROPERTY).getAsDouble() : null;

        return BudgetDTO.builder()
                .withId(id)
                .withTag(tag)
                .withPeriod(period)
                .withExpectedAmount(expectedAmount)
                .build();
    }

    private TagDTO deserializeTag(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonPrimitive()) {
            throw new JsonParseException("Expected a JSON primitive for tag ID");
        }

        UUID tagId = UUID.fromString(json.getAsString());

        return TagDTO.builder().withId(tagId).build();
    }

    @Override
    public JsonElement serialize(BudgetDTO src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject json = new JsonObject();

        if (src.id() != null) {
            json.addProperty(ID_PROPERTY, src.id().toString());
        }

        if (src.tag() != null) {
            json.add(TAG_PROPERTY, serializeTag(src.tag()));
        }

        if (src.period() != null) {
            json.addProperty(PERIOD_PROPERTY, src.period());
        }

        if (src.expectedAmount() != null) {
            json.addProperty(EXPECTED_AMOUNT_PROPERTY, src.expectedAmount());
        }

        return json;
    }

    private JsonElement serializeTag(TagDTO src) {
        if (src == null || src.id() == null) {
            return JsonNull.INSTANCE;
        }

        UUID tagId = src.id();

        return new JsonPrimitive(tagId.toString());
    }
}
