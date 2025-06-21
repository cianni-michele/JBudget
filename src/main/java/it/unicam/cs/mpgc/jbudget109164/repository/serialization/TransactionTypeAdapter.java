package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionDetails;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionFactory;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * This class is responsible for serializing and deserializing
 * {@link Transaction} objects to and from JSON format.
 * <p>
 * It implements the {@link JsonSerializer} and {@link JsonDeserializer} interfaces.
 *
 * @author Michele Cianni
 */
public class TransactionTypeAdapter implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    private final TransactionFactory transactionFactory;

    public TransactionTypeAdapter(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Transaction result;

        JsonObject jsonObject = json.getAsJsonObject();

        UUID id = UUID.fromString(jsonObject.get("id").getAsString());

        TransactionDetails details = context.deserialize(jsonObject.get("details"), TransactionDetails.class);

        result = transactionFactory.createTransaction(id, details);

        return result;
    }

    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.addProperty("id", src.id().toString());

        JsonElement detailsElement = context.serialize(src.details(), TransactionDetails.class);
        result.add("details", detailsElement);

        return  result;
    }
}
