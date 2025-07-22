package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class JsonTagRepository extends AbstractJsonRepository<TagDTO> implements TagRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonTagRepository.class);

    private final EventBus eventBus;

    public JsonTagRepository(JsonRepositoryConfig config, EventBus eventBus) {
        super(config);
        this.eventBus = eventBus;
    }

    @Override
    public List<TagDTO> findAll() {
        LOGGER.debug("Finding all tags");

        List<TagDTO> tags = new ArrayList<>();

        for (Path filePath : filesPath.values()) {
            TagDTO tag = readFromFile(filePath);
            tags.add(tag);
            LOGGER.debug("Tag found: {}", tag);
        }

        LOGGER.info("Found {} tags", tags.size());

        return tags;
    }

    @Override
    public boolean existsById(UUID tagId) {
        return filesPath.containsKey(tagId);
    }

    @Override
    public Optional<TagDTO> findById(UUID tagId) {
        LOGGER.debug("Finding tag by ID: {}", tagId);

        validateId(tagId);

        if (!filesPath.containsKey(tagId)) {
            LOGGER.info("Tag with ID {} not found", tagId);
            return Optional.empty();
        }

        Path filePath = filesPath.get(tagId);

        TagDTO tag = readFromFile(filePath);

        LOGGER.info("Found tag with ID {}", tagId);
        return Optional.of(tag);
    }

    @Override
    public void save(TagDTO tag) {
        LOGGER.debug("Saving tag: {}", tag);

        validateDTO(tag);

        Path filePath;
        if (filesPath.containsKey(tag.id())) {
            filePath = filesPath.get(tag.id());
        } else {
            filePath = directory.toPath().resolve(tag.id() + JSON_EXTENSION);
            filesPath.put(tag.id(), filePath);
        }

        writeToFile(tag, filePath);

        if (tag.children() != null) {
            for (TagDTO tagDTO : tag.children()) {
                save(tagDTO);
            }
        }

        LOGGER.info("Saved tag with ID {}", tag.id());
    }

    @Override
    public void deleteById(UUID tagId) {
        LOGGER.debug("Deleting tag with ID: {}", tagId);

        validateId(tagId);

        if (!filesPath.containsKey(tagId)) {
            throw exceptionAndLogError("Unable to delete tag with ID {} because it does not exist", tagId);
        }

        eventBus.publish(() -> tagId);

        removeRefFromParent(tagId);

        Path filePath = filesPath.get(tagId);

        TagDTO tagDTO = readFromFile(filePath);

        if (tagDTO.children() != null) {
            for (TagDTO child : tagDTO.children()) {
                UUID childId = child.id();
                if (!existsById(childId)) {
                    throw exceptionAndLogError("Unable to delete tag with ID {} because it has children that do not exist", tagId);
                }
                deleteById(childId);
            }
        }


        if (filePath != null && !filePath.toFile().delete()) {
            throw exceptionAndLogError("Failed to delete tag with ID {}", tagId);
        }

        filesPath.remove(tagId);

        LOGGER.info("Deleted tag with ID {}", tagId);
    }

    private void removeRefFromParent(UUID tagId) {
        LOGGER.debug("Removing reference to tag with ID {} from its parent", tagId);

        for (Path filePath : filesPath.values()) {
            TagDTO parentTag = readFromFile(filePath);
            if (parentTag.children() == null) {
                continue;
            }
            List<TagDTO> updatedChildren = new ArrayList<>();
            boolean found = false;
            for (TagDTO child : parentTag.children()) {
                if (child.id().equals(tagId)) {
                    found = true;
                } else {
                    updatedChildren.add(child);
                }
            }
            if (found) {
                parentTag = TagDTO.builder().copyFrom(parentTag)
                        .withChildren(updatedChildren.toArray(new TagDTO[0]))
                        .build();
                writeToFile(parentTag, filePath);
                LOGGER.info("Removed reference to tag with ID {} from parent tag with ID {}", tagId, parentTag.id());
            }
        }
    }

    @Override
    protected void validateDTO(TagDTO tag) {
        if (tag == null) {
            throw exceptionAndLogError("Tag cannot be null");
        }

        validateId(tag.id());
    }

    private void validateTagsReference(TagDTO[] children) {
        if (children == null) {
            throw exceptionAndLogError("Children tags cannot be null");
        }

        for (TagDTO tag : children) {
            if (tag == null) {
                throw exceptionAndLogError("Tag in children cannot be null");
            }
            UUID tagId = tag.id();
            validateId(tagId);
            if (!existsById(tagId)) {
                throw exceptionAndLogError("Tag with ID {} does not exist", tagId);
            }
        }
    }

    @Override
    protected void validateId(UUID tagId) {
        if (tagId == null) {
            throw exceptionAndLogError("Tag ID cannot be null");
        }
    }

    private TagDTO readFromFile(Path filePath) {
        return getTagWithChildren(readFromFile(filePath, TagDTO.class));
    }

    private TagDTO getTagWithChildren(TagDTO tag) {
        return TagDTO.builder().copyFrom(tag)
                .withChildren(resolveTagsReference(tag))
                .build();
    }

    private TagDTO[] resolveTagsReference(TagDTO tag) {
        if (tag.children() == null) {
            return null;
        }

        List<TagDTO> tags = new ArrayList<>();

        for (TagDTO childTag : tag.children()) {
            Optional<TagDTO> tagFound = findById(childTag.id());
            if (tagFound.isEmpty()) {
                throw exceptionAndLogError("Tag with ID {} not found", childTag.id());
            }
            tags.add(tagFound.get());
        }

        return tags.toArray(new TagDTO[0]);
    }

    private void writeToFile(TagDTO tag, Path filePath) {
        writeToFile(filePath, TagDTO.class, tag);
    }
}
