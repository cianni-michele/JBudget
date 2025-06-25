package it.unicam.cs.mpgc.jbudget109164.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.jbudget109164.exception.JsonRepositoryConfigException;
import it.unicam.cs.mpgc.jbudget109164.repository.JsonTransactionRepository;

import java.io.File;
import java.util.Map;

public class DefaultJsonRepositoryConfig implements JsonRepositoryConfig {

    private final Map<String, String> repositoryDirectories;

    public DefaultJsonRepositoryConfig() {
        repositoryDirectories = Map.of(
                JsonTransactionRepository.class.getName(), "transactions.json"
        );
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Override
    public File getDirectory(Class<?> aClass) {
        if (aClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        String fileName = repositoryDirectories.get(aClass.getName());
        if (fileName != null) {
            return new File(fileName);
        }
        throw new JsonRepositoryConfigException(aClass);
    }
}
