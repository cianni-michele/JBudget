package it.unicam.cs.mpgc.jbudget109164.model;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A class implementing this interface represents a tag that can be associated with transactions.
 * It provides methods to manage the tag's name, parent-child relationships, and child tags.
 */
public interface Tag extends Iterable<Tag> {

    /**
     * Returns the name of the tag.
     *
     * @return the name of the tag
     */
    String name();

    /**
     * Returns the parent tag of this tag if it exists.
     *
     * @return an Optional containing the parent tag if it exists, otherwise an empty Optional
     */
    Optional<Tag> parent();

    /**
     * Returns the children tags associated with this tag.
     *
     * @return a Set of child tags
     */
    Set<Tag> children();

    /**
     * Checks if this tag has any child tags.
     *
     * @return true if this tag has children, false otherwise
     */
    boolean hasChildren();

    /**
     * Returns a stream of child tags associated with this tag.
     *
     * @return a stream of child tags
     */
    Stream<Tag> stream();

    /**
     * Add a child tag to this tag.
     *
     * @param tag the child tag to add
     * @throws NullPointerException     if the child tag is null
     * @throws IllegalArgumentException if the child tag is already a child of this tag
     */
    void addChild(Tag tag);

    /**
     * Checks if this tag contains the specified child tags.
     *
     * @param tags the child tags to check for
     * @return true if this tag contains all specified child tags, false otherwise
     */
    default boolean containsChild(Tag... tags) {
        return containsChild(Set.of(tags));
    }

    /**
     * Checks if this tag contains the specified set of child tags.
     *
     * @param tags the set of child tags to check for
     * @return true if this tag contains all specified child tags, false otherwise
     */
    boolean containsChild(Set<Tag> tags);
}
