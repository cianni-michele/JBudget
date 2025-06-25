package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Path;

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

    public JsonPersistence(JsonRepositoryConfig configuration) {
        gson = configuration.getGson();
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
