package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractJsonRepository<I, D> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractJsonRepository.class);

    protected static final String JSON_EXTENSION = ".json";

    protected final Map<I, Path> filesPath;

    protected final Gson gson;

    protected final File directory;

    protected AbstractJsonRepository(JsonRepositoryConfig config) {
        this.filesPath = new HashMap<>();
        this.gson = config.getGson();
        this.directory = config.getDirectory(this.getClass());
        initializeDirectory();
        loadExistingFiles();
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

    private void loadExistingFiles() {
        File[] files = directory.listFiles(
                (file, name) -> name.endsWith(JSON_EXTENSION)
        );

        if (files != null) {
            for (File file : files) {
                filesPath.put(getKey(file), file.toPath());
            }
        }

        LOGGER.info("{} initialized with {} elements", this.getClass(), filesPath.size());
    }

    private I getKey(File file) {
        return parseToId(file.getName().replace(JSON_EXTENSION, ""));
    }

    /**
     * Parses the given string ID to the appropriate type I.
     *
     * @param id the string representation of the ID
     * @return the parsed ID of type I
     */
    protected abstract I parseToId(String id);

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

    /**
     * Validates the DTO before saving it.
     *
     * @param dto the DTO to validate
     */
    protected abstract void validateDTO(D dto);

    /**
     * Validates the ID of DTO.
     *
     * @param id the ID to validate
     */
    protected abstract void validateId(I id);
}
