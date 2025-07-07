package it.unicam.cs.mpgc.jbudget109164.config.serialization.dto;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.CustomTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.AccountDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.MovementDTOBuilder;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Custom type adapter for serializing and deserializing {@link MovementDTO} objects.
 * This adapter handles the conversion between JSON and MovementDTO, including null checks
 * and default values.
 * <p>
 * Example JSON structure:
 * <pre>
 * {
 *  "amount": 100.0,
 *  "description": "Payment for services",
 *  "accountId": 1
 * }
 * </pre>
 */
public class MovementDTOTypeAdapter implements CustomTypeAdapter<MovementDTO> {

    private static final String AMOUNT_PROPERTY = "amount";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String ACCOUNT_ID_PROPERTY = "accountId";

    @Override
    public MovementDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected a JSON object for MovementDTO");
        }

        JsonObject movement = json.getAsJsonObject();

        Double amount = movement.has(AMOUNT_PROPERTY) ?
                movement.getAsJsonObject().get(AMOUNT_PROPERTY).getAsDouble() : null;

        String description = movement.has(DESCRIPTION_PROPERTY) ?
                movement.getAsJsonObject().get(DESCRIPTION_PROPERTY).getAsString() : null;

        AccountDTO account = movement.has(ACCOUNT_ID_PROPERTY) ?
                deserializeAccount(movement.get(ACCOUNT_ID_PROPERTY)) : null;

        return MovementDTOBuilder.getInstance()
                .withAmount(amount)
                .withDescription(description)
                .withAccount(account)
                .build();
    }

    private AccountDTO deserializeAccount(JsonElement accountId) {
        if (accountId.isJsonNull()) {
            return null;
        }

        return AccountDTOBuilder.getInstance()
                .withId(UUID.fromString(accountId.getAsString()))
                .build();
    }

    @Override
    public JsonElement serialize(MovementDTO src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject json = new JsonObject();

        if (src.amount() != null) {
            json.addProperty(AMOUNT_PROPERTY, src.amount());
        }

        if (src.description() != null) {
            json.addProperty(DESCRIPTION_PROPERTY, src.description());
        }

        if (src.account() != null) {
            json.add(ACCOUNT_ID_PROPERTY, serializeAccount(src.account()));
        }

        return json;
    }

    private JsonElement serializeAccount(AccountDTO account) {
        return account.id() != null ? new JsonPrimitive(account.id().toString()) : JsonNull.INSTANCE;
    }
}
