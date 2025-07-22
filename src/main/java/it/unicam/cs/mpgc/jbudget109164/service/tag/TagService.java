package it.unicam.cs.mpgc.jbudget109164.service.tag;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service interface for managing tags in the application.
 * This interface provides methods to create, update, delete, and retrieve tags,
 * as well as manage their hierarchical relationships (parent-child).
 *
 * @author Michele Cianni
 */
public interface TagService {

    /**
     * Retrieves all tags.
     *
     * @return a list of all tags.
     */
    List<Tag> getAllTags();

    /**
     * Retrieves a tag by its ID.
     *
     * @param id the ID of the tag to retrieve.
     * @return an Optional containing the tag if found, or empty if not found.
     */
    Optional<Tag> getTagById(UUID id);

    /**
     * Creates a new tag with the provided name.
     *
     * @param name the name of the new tag.
     * @return the created tag.
     */
    Tag createTag(String name);

    /**
     * Updates an existing tag with the provided ID and name.
     *
     * @param id the ID of the tag to update.
     * @param name the new name for the tag.
     * @return the updated tag.
     */
    Tag updateTag(UUID id, String name);

    /**
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to delete.
     */
    void deleteTag(UUID id);

    /**
     * Adds a child tag to the specified parent tag.
     *
     * @param parentId the ID of the parent tag to which the child tag will be added.
     * @param childName the name of the child tag to be added.
     * @return the created child tag.
     */
    Tag addChildTag(UUID parentId, String childName);

    /**
     * Removes a child tag from the specified parent tag.
     *
     * @param parentId the ID of the parent tag from which the child tag will be removed.
     * @param childId the ID of the child tag to be removed.
     */
    void removeChildTag(UUID parentId, UUID childId);

    /**
     * Retrieves all parent tags of a specified tag.
     *
     * @param id the ID of the tag whose parent tags are to be retrieved.
     * @return a list of parent tags associated with the specified tag.
     */
    Set<Tag> getParentTags(UUID id);

    /**
     * Retrieves all child tags of a specified tag.
     *
     * @param id the ID of the tag whose child tags are to be retrieved.
     * @return a list of child tags associated with the specified tag.
     */
    Set<Tag> getChildTags(UUID id);

}
