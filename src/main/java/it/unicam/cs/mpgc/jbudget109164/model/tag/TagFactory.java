package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.Set;
import java.util.UUID;

/**
 * Factory interface for creating instances of {@link Tag}.
 * This interface provides methods to create new tags with specified names,
 * optionally with a specific ID or a randomly generated one.
 *
 * @author Michele Cianni
 */
public interface TagFactory {

    /**
     * Creates a new tag with a randomly generated ID and the specified name.
     *
     * @param name the name of the tag to create
     * @return a new Tag instance with a random ID
     */
    Tag createTag(String name);

    /**
     * Creates a new tag with the specified ID and name, and optionally with child tags.
     *
     * @param id       the unique identifier for the tag
     * @param name     the name of the tag to create
     * @param children the child tags associated with this tag
     * @return a new Tag instance with the specified ID, name, and children
     */
    Tag createTag(UUID id, String name, Set<Tag> children);

}
