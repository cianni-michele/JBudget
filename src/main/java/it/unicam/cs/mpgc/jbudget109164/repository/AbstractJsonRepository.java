package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;

import java.io.File;

public abstract class AbstractJsonRepository<I, D> implements Repository<I, D> {

    protected final Gson gson;

    protected final File directory;

    protected AbstractJsonRepository(JsonRepositoryConfig configuration) {
        this.gson = configuration.getGson();
        this.directory = null; // configuration.getDirectory() can be used for custom directory
    }
}
