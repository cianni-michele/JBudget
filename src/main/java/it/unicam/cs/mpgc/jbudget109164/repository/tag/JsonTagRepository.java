package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TagDTOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class JsonTagRepository extends AbstractJsonRepository<UUID, TagDTO> implements TagRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonTagRepository.class);

    public JsonTagRepository(JsonRepositoryConfig config) {
        super(config);
    }

    @Override
    protected UUID parseToId(String id) {
        return UUID.fromString(id);
    }

    @Override
    public List<TagDTO> findAll() {
        LOGGER.debug("Finding all tags");

        List<TagDTO> tags = new ArrayList<>();

        for (Path filePath : filesPath.values()) {
            TagDTO tagDTO = readFromFile(filePath, TagDTO.class);
            if (tagDTO != null) {
                tags.add(getTagWithChildren(tagDTO));
            } else {
                LOGGER.warn("Tag could not be read from file: {}", filePath);
            }
        }

        LOGGER.info("Found {} tags", tags.size());

        return tags;
    }


    @Override
    public boolean existsById(UUID id) {
        return filesPath.containsKey(id);
    }

    @Override
    public TagDTO findById(UUID tagId) {
        LOGGER.debug("Finding tag by ID: {}", tagId);

        validateId(tagId);

        if (!filesPath.containsKey(tagId)) {
            LOGGER.info("Tag with ID {} not found", tagId);
            return null;
        }

        Path filePath = filesPath.get(tagId);
        TagDTO tagFound = readFromFile(filePath, TagDTO.class);

        if (tagFound == null) {
            LOGGER.warn("Tag with ID {} could not be read from file", tagId);
            return null;
        }

        TagDTO tag = getTagWithChildren(tagFound);

        LOGGER.info("Found tag with ID {}", tagId);
        return tag;
    }

    private TagDTO getTagWithChildren(TagDTO tag) {
        return TagDTOBuilder.getInstance().copyOf(tag)
                .withChildren(findChildren(tag))
                .build();
    }

    private TagDTO[] findChildren(TagDTO tag) {
        if (tag.children() == null) {
            return null;
        }

        List<TagDTO> tags = new ArrayList<>();

        for (TagDTO childTag : tag.children()) {
            tags.add(findById(childTag.id()));
        }

        return tags.toArray(new TagDTO[0]);
    }

    @Override
    public void save(TagDTO tag) {
        LOGGER.debug("Saving tag: {}", tag);

        validateDTO(tag);

        if (filesPath.containsKey(tag.id())) {
            String message = "Unable to save tag with ID " + tag.id() + " because it already exists";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path filePath = directory.toPath().resolve(tag.id() + JSON_EXTENSION);
        writeToFile(filePath, TagDTO.class, tag);
        filesPath.put(tag.id(), filePath);

        saveChildren(tag);
    }

    private void saveChildren(TagDTO tag) {
        if (tag.children() == null) {
            return;
        }

        for (TagDTO tagDTO : tag.children()) {
            save(tagDTO);
        }
    }

    @Override
    public void update(TagDTO dto) {
        LOGGER.debug("Updating tag: {}", dto);

        validateDTO(dto);

        if (!filesPath.containsKey(dto.id())) {
            String message = "Unable to update tag with ID " + dto.id() + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path filePath = filesPath.get(dto.id());
        writeToFile(filePath, TagDTO.class, dto);

        updateChildren(dto);

        LOGGER.info("Updated tag with ID {}", dto.id());

    }

    private void updateChildren(TagDTO tag) {
        if (tag.children() == null) {
            return;
        }

        for (TagDTO child : tag.children()) {
            if (filesPath.containsKey(child.id())) {
                update(child);
            } else {
                save(child);
            }
        }
    }

    @Override
    public void deleteById(UUID tagId) {
        LOGGER.debug("Deleting tag with ID: {}", tagId);

        validateId(tagId);

        if (!filesPath.containsKey(tagId)) {
            String message = "Unable to delete tag with ID " + tagId + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path filePath = filesPath.get(tagId);

        if (filePath != null && !filePath.toFile().delete()) {
            String message = "Failed to delete tag with ID " + tagId;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        filesPath.remove(tagId);

        LOGGER.info("Deleted tag with ID {}", tagId);
    }

    @Override
    public int count() {
        return filesPath.size();
    }

    @Override
    protected void validateDTO(TagDTO tag) {
        if (tag == null) {
            String message = "TagDTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
        validateId(tag.id());
    }

    @Override
    protected void validateId(UUID tagId) {
        if (tagId == null) {
            String message = "Tag ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }
}
