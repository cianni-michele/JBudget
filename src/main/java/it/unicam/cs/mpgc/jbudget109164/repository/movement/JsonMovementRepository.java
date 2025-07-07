package it.unicam.cs.mpgc.jbudget109164.repository.movement;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.NewMovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.NewMovementDTOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class JsonMovementRepository extends AbstractJsonRepository<UUID, NewMovementDTO> implements MovementRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonMovementRepository.class);

    private final TagRepository tagRepository;

    protected JsonMovementRepository(JsonRepositoryConfig config, TagRepository tagRepository) {
        super(config);
        this.tagRepository = tagRepository;
    }

    @Override
    protected UUID parseToId(String movementId) {
        return UUID.fromString(movementId);
    }

    @Override
    public List<NewMovementDTO> findAll() {
        LOGGER.debug("Finding all movements");

        List<NewMovementDTO> movements = new ArrayList<>();

        for (Path path : filesPath.values()) {
            NewMovementDTO movementFound = readFromFile(path, NewMovementDTO.class);
            if (Objects.nonNull(movementFound)) {
                TagDTO[] movementTags = resolveTagReferences(movementFound.tags());
                NewMovementDTO movement = NewMovementDTOBuilder.getInstance().copyOf(movementFound)
                        .setTags(movementTags)
                        .build();
                movements.add(movement);
                LOGGER.debug("Movement found: {}", movementFound);
            } else {
                LOGGER.warn("Movement file {} could not be read", path);
            }
        }

        return movements;
    }

    @Override
    public NewMovementDTO findById(UUID movementId) {
        LOGGER.debug("Finding movement by ID: {}", movementId);

        validateId(movementId);

        if (!filesPath.containsKey(movementId)) {
            LOGGER.info("Movement with ID {} not found", movementId);
            return null;
        }

        Path movementPath = filesPath.get(movementId);

        NewMovementDTO movementFound = readFromFile(movementPath, NewMovementDTO.class);

        if (Objects.isNull(movementFound)) {
            LOGGER.warn("Movement with ID {} could not be read from file", movementId);
            return null;
        }

        TagDTO[] movementTags = resolveTagReferences(movementFound.tags());

        return NewMovementDTOBuilder.getInstance().copyOf(movementFound)
                .setTags(movementTags)
                .build();
    }

    private TagDTO[] resolveTagReferences(TagDTO[] movementTags) {
        if (Objects.isNull(movementTags)) {
            return null;
        }

        List<TagDTO> result = new ArrayList<>();

        for (TagDTO tag : movementTags) {
            TagDTO resolvedTag = tagRepository.findById(tag.id());
            if (resolvedTag != null) {
                result.add(resolvedTag);
            } else {
                LOGGER.warn("Tag with ID {} not found", tag.id());
            }
        }

        return result.toArray(new TagDTO[0]);
    }

    @Override
    public void save(NewMovementDTO movement) {

    }

    @Override
    public void update(NewMovementDTO movement) {

    }

    @Override
    public void deleteById(UUID movementId) {

    }

    @Override
    public int count() {
        return filesPath.size();
    }

    @Override
    protected void validateDTO(NewMovementDTO movement) {
        if (Objects.isNull(movement)) {
            String message = "Movement DTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
        validateId(movement.id());

        validateTagsReference(movement.tags());
    }

    private void validateTagsReference(TagDTO[] movementTags) {
        if (Objects.nonNull(movementTags)) {
            for (TagDTO tag : movementTags) {
                if (Objects.nonNull(tag)) {
                    UUID tagId = tag.id();
                    if (!tagRepository.existsById(tagId)) {
                        String message = "Tag with ID " + tagId + " does not exist";
                        LOGGER.error(message);
                        throw new JsonRepositoryException(message);
                    }
                } else {
                    LOGGER.warn("Tag in movement cannot be null");
                }
            }
        }
    }

    @Override
    protected void validateId(UUID movementId) {
        if (Objects.isNull(movementId)) {
            String message = "Movement ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }
}
