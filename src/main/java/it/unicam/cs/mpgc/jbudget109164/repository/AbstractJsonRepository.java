package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.text.MessageFormat;
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
            throw exceptionAndLogError("Failed to create directory: {}", directory);
        }

        if (!directory.isDirectory()) {
            throw exceptionAndLogError("Is not a directory: {}", directory);
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
            D dto = gson.fromJson(reader, type);
            if (dto == null) {
                throw exceptionAndLogError("Failed to parse JSON from file: {}", filePath);
            }
            return dto;
        } catch (IOException e) {
            throw exceptionAndLogError(e.getCause(), "Error reading from file: {}", filePath);
        }
    }

    protected void writeToFile(Path filePath, Class<D> type, D data) {
        try (Writer writer = new FileWriter(filePath.toFile())) {
            gson.toJson(data, type, writer);
        } catch (IOException e) {
            throw exceptionAndLogError(e.getCause(), "Error writing to file: {} ", filePath);
        }
    }

    protected JsonRepositoryException exceptionAndLogError(String message, Object... args) {
        LOGGER.error(message, args);
        return new JsonRepositoryException(MessageFormat.format(message, args));
    }

    private JsonRepositoryException exceptionAndLogError(Throwable cause, String message, Object... args) {
        LOGGER.error(message, args);
        return new JsonRepositoryException(MessageFormat.format(message, args), cause);
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
