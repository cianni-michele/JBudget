package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.exception.JsonRepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;

public abstract class AbstractJsonRepository<I, D> implements Repository<I, D> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractJsonRepository.class);

    protected final Gson gson;

    protected final File directory;

    protected AbstractJsonRepository(JsonRepositoryConfig config) {
        this.gson = config.getGson();
        this.directory = config.getDirectory(this.getClass());
        initializeDirectory();
    }

    private void initializeDirectory() {
        LOGGER.debug("Initializing {} with directory: {}", this.getClass().getSimpleName(), directory);

        if (!directory.exists() && !directory.mkdirs()) {
            String message = "Failed to create directory: " + directory;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        if (!directory.isDirectory()) {
            String message = "Directory is not a directory: " + directory;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }

    protected D readFromFile(Path filePath, Class<D> type) {
        try (Reader reader = new FileReader(filePath.toFile())) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            String message = "Error reading from file: " + filePath;
            LOGGER.error(message, e);
            throw new JsonRepositoryException(message, e.getCause());
        }
    }

    protected void writeToFile(Path filePath, Class<D> type, D data) {
        try (Writer writer = new FileWriter(filePath.toFile())) {
            gson.toJson(data, type, writer);
        } catch (IOException e) {
            String message = "Error writing to file: " + filePath;
            LOGGER.error(message, e);
            throw new JsonRepositoryException(message, e.getCause());
        }
    }

    protected abstract void validateId(I id);

    protected abstract void validateDTO(D dto);
}
