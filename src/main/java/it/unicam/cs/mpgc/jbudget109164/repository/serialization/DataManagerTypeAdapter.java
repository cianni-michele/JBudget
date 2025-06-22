package it.unicam.cs.mpgc.jbudget109164.repository.serialization;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Scheduler;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.repository.DataManager;
import it.unicam.cs.mpgc.jbudget109164.repository.DataManagerFactory;

import java.lang.reflect.Type;

/**
 * This class is responsible for serializing and deserializing
 * {@link DataManager} objects to and from JSON format.
 * <p>
 * It implements the {@link JsonSerializer} and {@link JsonDeserializer} interfaces.
 *
 * @author Michele Cianni
 */
public class DataManagerTypeAdapter implements JsonSerializer<DataManager>, JsonDeserializer<DataManager> {

    private final DataManagerFactory dataManagerFactory;

    public DataManagerTypeAdapter(DataManagerFactory dataManagerFactory) {
        this.dataManagerFactory = dataManagerFactory;
    }

    @Override
    public DataManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        BudgetPlan budgetPlan = context.deserialize(jsonObject.get("budgetPlan"), BudgetPlan.class);

        Scheduler scheduler = context.deserialize(jsonObject.get("scheduler"), Scheduler.class);

        DataManager result = dataManagerFactory.createDataManager(budgetPlan, scheduler);

        JsonArray transactionsArray = jsonObject.getAsJsonArray("transactions");
        for (JsonElement transactionElement : transactionsArray) {
            Transaction transaction = context.deserialize(transactionElement, Transaction.class);
            result.add(transaction);
        }

        return result;
    }

    @Override
    public JsonElement serialize(DataManager src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        JsonArray transactionsArray = new JsonArray();
        for (Transaction transaction : src.getAllTransactions()) {
            transactionsArray.add(context.serialize(transaction));
        }

        result.add("transactions", transactionsArray);

        result.add("budgetPlan", context.serialize(src.getBudgetPlan()));

        result.add("scheduler", context.serialize(src.getScheduler()));

        return result;
    }
}
