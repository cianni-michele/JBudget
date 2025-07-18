package it.unicam.cs.mpgc.jbudget109164.controller.tag;

import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.service.tag.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public final class TagController {

    private static final Logger LOGGER = LogManager.getLogger(TagController.class);

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    public List<Tag> getAllTags() {
        LOGGER.debug("Retrieving all tags");

        List<Tag> allTags = tagService.getAllTags();

        LOGGER.info("Retrieved {} tags", allTags.size());

        return allTags;
    }

    public Tag getTagById(UUID tagId) {
        LOGGER.debug("Retrieving tag with ID: {}", tagId);

        validateId(tagId);

        Tag tag = tagService.getTagById(tagId).orElseThrow(() -> {
            String message = "Tag with ID " + tagId + " not found";
            LOGGER.warn(message);
            return new TagNotFoundException(message);
        });

        LOGGER.info("Retrieved tag: {}", tag.getName());

        return tag;
    }

    public Tag createTag(String name) {
        LOGGER.debug("Creating tag with name: {}", name);

        validateName(name);

        Tag tag = tagService.createTag(name);

        LOGGER.info("Tag created successfully: {}", tag.getName());

        return tag;
    }

    public Tag updateTag(UUID tagId, String name) {
        LOGGER.debug("Updating tag with ID: {}, new name: {}", tagId, name);

        validateId(tagId);
        validateName(name);

        Tag updatedTag = tagService.updateTag(tagId, name);

        LOGGER.info("Tag updated successfully: {}", updatedTag.getName());

        return updatedTag;
    }

    public void deleteTag(UUID tagId) {
        LOGGER.debug("Deleting tag with ID: {}", tagId);

        validateId(tagId);

        tagService.deleteTag(tagId);

        LOGGER.info("Tag with ID {} deleted successfully", tagId);
    }

    public Tag addChildTag(UUID parentTagId, String childTagName) {
        LOGGER.debug("Adding child tag with name: {} to parent tag ID: {}", childTagName, parentTagId);

        validateId(parentTagId);
        validateName(childTagName);

        Tag childTag = tagService.addChildTag(parentTagId, childTagName);

        LOGGER.info("Child tag added successfully: {}", childTag.getName());

        return childTag;
    }

    public void removeChildTag(UUID parentTagId, UUID childTagId) {
        LOGGER.debug("Removing child tag with ID: {} from parent tag ID: {}", childTagId, parentTagId);

        validateId(parentTagId);
        validateId(childTagId);

        tagService.removeChildTag(parentTagId, childTagId);

        LOGGER.info("Child tag with ID {} removed successfully from parent tag ID {}", childTagId, parentTagId);
    }

    private void validateId(UUID tagId) {
        if (tagId == null) {
            String message = "Tag ID cannot be null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            String message = "Tag name cannot be null or blank";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }


}
