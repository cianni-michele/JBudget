package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.*;

/**
 * Represents a tag in a hierarchical structure.
 * This abstract class serves as a base for creating tags that can have parent-child relationships.
 *
 * @author Michele Cianni
 */
public abstract class Tag {
    protected final UUID id;
    protected final String name;
    protected final Set<Tag> parents = new HashSet<>();
    protected final Set<Tag> children = new HashSet<>();

    protected Tag(UUID id, String name) {
        this.id = validateAndGetId(id);
        this.name = validateAndGetName(name);
    }

    private UUID validateAndGetId(UUID id) {
        return Optional.ofNullable(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag ID cannot be null"));
    }

    private String validateAndGetName(String name) {
        return Optional.ofNullable(name)
                .filter(n -> !n.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Tag name cannot be null or empty"));
    }

    /**
     * Returns the unique identifier of the tag.
     *
     * @return the unique identifier of the tag
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable set of parent tags.
     * This method provides a read-only view of the parent tags to prevent external modification.
     *
     * @return an unmodifiable set of parent tags
     */
    public Set<Tag> getParents() {
        return Set.copyOf(parents);
    }

    /**
     * Returns an unmodifiable set of parent tags.
     * This method provides a read-only view of the parent tags to prevent external modification.
     *
     * @return an unmodifiable set of parent tags
     */
    public Set<Tag> getChildren() {
        return Set.copyOf(children);
    }

    /**
     * Checks if this tag has any parents.
     *
     * @return true if this tag has parents, false otherwise
     */
    public boolean hasParents() {
        return !parents.isEmpty();
    }

    /**
     * Checks if this tag has any children.
     *
     * @return true if this tag has children, false otherwise
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Checks if adding a child tag would create a cycle in the hierarchy.
     * This method checks if there is a path from this tag to the given tag.
     *
     * @param tag the child tag to check
     * @return true if adding the child would create a cycle, false otherwise
     */
    protected boolean createsCycles(Tag tag) {
        return tag.hasPathTo(this, new HashSet<>());
    }

    /**
     * Checks if there is a path from this tag to the given tag.
     * This method uses a depth-first search approach to find a path.
     *
     * @param tag     the target tag to check for a path
     * @param visited a set of visited tags to avoid cycles
     * @return true if there is a path to the target tag, false otherwise
     */
    protected boolean hasPathTo(Tag tag, Set<Tag> visited) {
        if (this.equals(tag)) {
            return true;
        }

        visited.add(this);

        for (Tag child : this.children) {
            if (!visited.contains(child) && child.hasPathTo(tag, visited)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a child tag to this tag.
     * This method will also add this tag to the child's parent set.
     *
     * @param tag the child tag to add
     * @return true if the child was successfully added, false if it was already a child
     * @throws NullPointerException     if the tag is null
     * @throws IllegalArgumentException if adding this tag would create a cycle in the hierarchy and is not allowed
     */
    public abstract boolean addChild(Tag tag);

    /**
     * Removes a child tag from this tag.
     * This method will also remove this tag from the child's parent set.
     *
     * @param tag the child tag to remove
     * @return true if the child was successfully removed, false otherwise
     * @throws NullPointerException if the tag is null
     */
    public abstract boolean removeChild(Tag tag);

    /**
     * Checks if this tag is a parent of the specified set of tags.
     *
     * @param tags the set of tags to check against
     * @return true if this tag is a parent of all specified tags, false otherwise
     * @throws NullPointerException if the tags set is null
     */
    public abstract boolean isParentOf(Set<Tag> tags);

    /**
     * Checks if this tag is a parent of the specified tag.
     *
     * @param tag the tag to check against
     * @return true if this tag is a parent of the specified tag, false otherwise
     * @throws NullPointerException if the tag is null
     */
    public abstract boolean isParentOf(Tag tag);

    /**
     * Checks if this tag is a child of the specified tag.
     *
     * @param tag the tag to check against
     * @return true if this tag is a child of the specified tag, false otherwise
     * @throws NullPointerException if the tag is null
     */
    public abstract boolean isChildOf(Tag tag);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
