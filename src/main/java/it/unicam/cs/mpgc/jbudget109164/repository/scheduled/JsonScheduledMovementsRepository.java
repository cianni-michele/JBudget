package it.unicam.cs.mpgc.jbudget109164.repository.scheduled;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.scheduled.ScheduledMovementsDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class JsonScheduledMovementsRepository extends AbstractJsonRepository<UUID, ScheduledMovementsDTO>
        implements ScheduledMovementsRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonScheduledMovementsRepository.class);

    public JsonScheduledMovementsRepository(JsonRepositoryConfig config) {
        super(config);
    }

    @Override
    public List<ScheduledMovementsDTO> findAll() {
        LOGGER.debug("Retrieving all scheduled movements");

        List<ScheduledMovementsDTO> scheduledMovements = new ArrayList<>();

        for (Path path : filesPath.values()) {
            ScheduledMovementsDTO scheduledMovementFound = readFromFile(path, ScheduledMovementsDTO.class);
            if (Objects.nonNull(scheduledMovementFound)) {
                scheduledMovements.add(scheduledMovementFound);
                LOGGER.debug("Scheduled movement found: {}", scheduledMovementFound);
            } else {
                LOGGER.warn("Scheduled movement file {} could not be read", path);
            }
        }

        LOGGER.info("Total scheduled movements found: {}", scheduledMovements.size());

        return scheduledMovements;
    }

    @Override
    public boolean existsById(UUID id) {
        LOGGER.debug("Checking if scheduled movement exists by ID: {}", id);

        validateId(id);

        boolean exists = filesPath.containsKey(id);

        if (exists) {
            LOGGER.info("Scheduled movement with ID {} exists", id);
        } else {
            LOGGER.warn("Scheduled movement with ID {} does not exist", id);
        }

        return exists;
    }

    @Override
    public Optional<ScheduledMovementsDTO> findById(UUID id) {
        LOGGER.debug("Retrieving scheduled movement with ID: {}", id);

        validateId(id);

        if (!filesPath.containsKey(id)) {
            LOGGER.warn("Scheduled movement with ID {} does not exist", id);
            return Optional.empty();
        }

        ScheduledMovementsDTO scheduledMovement = readFromFile(filesPath.get(id), ScheduledMovementsDTO.class);

        if (scheduledMovement == null) {
            LOGGER.error("Failed to read scheduled movement with ID {}", id);
            return Optional.empty();
        }

        LOGGER.info("Scheduled movement found: {}", scheduledMovement);
        return Optional.of(scheduledMovement);
    }

    @Override
    public void deleteById(UUID id) {
        LOGGER.debug("Deleting scheduled movement with ID: {}", id);

        validateId(id);

        if (!filesPath.containsKey(id)) {
            LOGGER.warn("Scheduled movement with ID {} does not exist", id);
            return;
        }

        Path path = filesPath.get(id);
        if (path == null || !path.toFile().delete()) {
            LOGGER.error("Failed to delete scheduled movement with ID {}", id);
            return;
        }

        filesPath.remove(id);
        LOGGER.info("Scheduled movement with ID {} deleted successfully", id);
    }

    @Override
    public void save(ScheduledMovementsDTO dto) {
        LOGGER.debug("Saving scheduled movement: {}", dto);

        validateDTO(dto);

        Path scheduledMovementPath;
        if (filesPath.containsKey(dto.id())) {
            scheduledMovementPath = filesPath.get(dto.id());
            LOGGER.debug("Scheduled movement with ID {} already exists, updating", dto.id());
        } else {
            scheduledMovementPath = directory.toPath().resolve(dto.id() + JSON_EXTENSION);
            filesPath.put(dto.id(), scheduledMovementPath);
            LOGGER.debug("Creating new scheduled movement with ID {}", dto.id());
        }

        writeToFile(scheduledMovementPath, ScheduledMovementsDTO.class, dto);
        LOGGER.info("Scheduled movement with ID {} saved successfully", dto.id());
    }

    @Override
    protected UUID parseToId(String id) {
        return UUID.fromString(id);
    }

    @Override
    protected void validateDTO(ScheduledMovementsDTO dto) {
        if (dto == null) {
            String message = "ScheduledMovementsDTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
        validateId(dto.id());
    }

    @Override
    protected void validateId(UUID id) {
        if (id == null) {
            String message = "ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }
}
