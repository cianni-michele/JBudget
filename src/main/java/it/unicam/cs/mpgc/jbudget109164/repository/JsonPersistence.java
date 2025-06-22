package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.*;
import it.unicam.cs.mpgc.jbudget109164.model.BudgetPlan;
import it.unicam.cs.mpgc.jbudget109164.model.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.TagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.serialization.*;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * A class implementing this interface is responsible for saving and loading
 * data from a JSON file.
 * <p>
 * This class implements the {@link DataPersistence} interface.
 *
 * @author Michele Cianni
 */
//TODO: Not fully implemented yet, missing tests
public class JsonPersistence implements DataPersistence {

    private final Gson gson;

    public JsonPersistence(TagFactory tagFactory,
                           TransactionFactory transactionFactory,
                           DataManagerFactory dataManagerFactory) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(YearMonth.class, new YearMonthTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(BudgetPlan.class, new BudgetPlanTypeAdapter())
                .registerTypeAdapter(Tag.class, new TagTypeAdapter(tagFactory))
                .registerTypeAdapter(Transaction.class, new TransactionTypeAdapter(transactionFactory))
                .registerTypeAdapter(DataManager.class, new DataManagerTypeAdapter(dataManagerFactory))
                .create();
    }

    @Override
    public void save(DataManager data, Path path) throws IOException {
        try (Writer writer = new FileWriter(path.toFile())) {
            gson.toJson(data, DataManager.class, writer);
        }
    }

    @Override
    public DataManager load(Path path) throws IOException {
        try (Reader reader = new FileReader(path.toFile())) {
            return gson.fromJson(reader, DataManager.class);
        }
    }

}
