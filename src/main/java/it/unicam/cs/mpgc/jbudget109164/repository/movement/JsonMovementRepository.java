package it.unicam.cs.mpgc.jbudget109164.repository.movement;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("LoggingSimilarMessage")
public class JsonMovementRepository extends AbstractJsonRepository<UUID, MovementDTO> implements MovementRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonMovementRepository.class);

    private final TagRepository tagRepository;

    public JsonMovementRepository(JsonRepositoryConfig config, TagRepository tagRepository) {
        super(config);
        this.tagRepository = tagRepository;
    }

    @Override
    public List<MovementDTO> findAll() {
        LOGGER.debug("Finding all movements");

        List<MovementDTO> movements = new ArrayList<>();

        for (Path path : filesPath.values()) {
            MovementDTO movement = readFromFile(path);
            movements.add(movement);
            LOGGER.debug("Movement found: {}", movement);
        }

        LOGGER.info("Total movements found: {}", movements.size());

        return movements;
    }

    @Override
    public boolean existsById(UUID id) {
        LOGGER.debug("Checking if movement exists by ID: {}", id);

        boolean exists = filesPath.containsKey(id);

        if (exists) {
            LOGGER.info("Movement with ID {} exists", id);
        } else {
            LOGGER.info("Movement with ID {} does not exist", id);
        }

        return exists;
    }

    @Override
    public Optional<MovementDTO> findById(UUID movementId) {
        LOGGER.debug("Finding movement by ID: {}", movementId);

        validateId(movementId);

        if (!filesPath.containsKey(movementId)) {
            LOGGER.info("Movement with ID {} not found", movementId);
            return Optional.empty();
        }

        Path movementPath = filesPath.get(movementId);

        MovementDTO movement = readFromFile(movementPath);

        LOGGER.info("Found movement with ID {}", movementId);
        return Optional.of(movement);
    }

    @Override
    public void save(MovementDTO movement) {
        LOGGER.debug("Saving movement: {}", movement);

        validateDTO(movement);

        Path movementPath;
        if (filesPath.containsKey(movement.id())) {
            movementPath = filesPath.get(movement.id());
            LOGGER.debug("Updating existing movement with ID {}", movement.id());
        } else {
            movementPath = directory.toPath().resolve(movement.id() + JSON_EXTENSION);
            filesPath.put(movement.id(), movementPath);
            LOGGER.debug("Creating new movement with ID {}", movement.id());
        }

        writeToFile(movementPath, movement);
        LOGGER.info("Movement with ID {} saved successfully", movement.id());
    }

    @Override
    public void deleteById(UUID movementId) {
        LOGGER.debug("Deleting movement by ID: {}", movementId);

        validateId(movementId);

        if (!filesPath.containsKey(movementId)) {
            throw exceptionAndLogError("Unable to delete movement with ID {} because it does not exist", movementId);
        }

        Path movementPath = filesPath.remove(movementId);

        if (movementPath != null && !movementPath.toFile().delete()) {
            throw exceptionAndLogError("Failed to delete movement with ID {}", movementId);
        }

        LOGGER.info("Movement with ID {} deleted successfully", movementId);
    }

    @Override
    protected UUID parseToId(String movementId) {
        return UUID.fromString(movementId);
    }

    @Override
    protected void validateDTO(MovementDTO movement) {
        if (movement == null) {
            throw exceptionAndLogError("Movement cannot be null");
        }
        validateId(movement.id());
        validateTagsReference(movement.tags());
    }

    private void validateTagsReference(TagDTO[] movementTags) {
        if (movementTags == null) {
            throw exceptionAndLogError("Movement tags cannot be null");
        }

        for (TagDTO tag : movementTags) {
            if (tag == null) {
                throw exceptionAndLogError("Tag in movement cannot be null");
            }

            UUID tagId = tag.id();

            if (tagId == null) {
                throw exceptionAndLogError("Tag ID cannot be null");
            }

            if (!tagRepository.existsById(tagId)) {
                throw exceptionAndLogError("Tag with ID {} does not exist", tagId);
            }
        }
    }

    @Override
    protected void validateId(UUID movementId) {
        if (movementId == null) {
            throw exceptionAndLogError("Movement ID cannot be null");
        }
    }

    private MovementDTO readFromFile(Path movementPath) {
        return getMovementWithTags(readFromFile(movementPath, MovementDTO.class));
    }

    private MovementDTO getMovementWithTags(MovementDTO movementFound) {
        return MovementDTO.builder().copyFrom(movementFound)
                .withTags(resolveTagReferences(movementFound.tags()))
                .build();
    }

    private TagDTO[] resolveTagReferences(TagDTO[] movementTags) {
        if (Objects.isNull(movementTags)) {
            return null;
        }

        List<TagDTO> result = new ArrayList<>();

        for (TagDTO tag : movementTags) {
            Optional<TagDTO> tagFound = tagRepository.findById(tag.id());
            if (tagFound.isEmpty()) {
                throw exceptionAndLogError("Tag with ID {} does not exist", tag.id());
            }
            result.add(tagFound.get());
        }

        return result.toArray(new TagDTO[0]);
    }

    private void writeToFile(Path movementPath, MovementDTO movement) {
        writeToFile(movementPath, MovementDTO.class, movement);
    }
}
