package it.unicam.cs.mpgc.jbudget109164.model.tag;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a category tag in a hierarchical tagging system.
 * This class extends the {@link Tag} class and provides
 * methods to manage child tags, check parent-child relationships,
 * and ensure the integrity of the tag hierarchy.
 * <p>
 * This class is designed to be used in a tagging system
 * where tags can have a parent-child relationship,
 * allowing for the creation of categories and subcategories.
 *
 * @author Michele Cianni
 */
public class CategoryTag extends Tag {


    /**
     * Constructs a TagNode with the specified unique identifier and name.
     *
     * @param id   the unique identifier of the tag node, must not be null
     * @param name the name of the tag node, must not be null or empty
     * @throws IllegalArgumentException if the id is null or the name is null or empty
     */
    public CategoryTag(UUID id, String name) {
        super(id, name);
    }

    @Override
    public boolean addChild(Tag tag) {
        if (Objects.isNull(tag)) {
            throw new NullPointerException("Child tag cannot be null");
        }

        if (createsCycles(tag)) {
            throw new IllegalArgumentException("Adding this tag would create a cycle in the hierarchy");
        }

        return children.add(tag) && tag.parents.add(this);
    }

    @Override
    public boolean removeChild(Tag tag) {
        if (Objects.isNull(tag)) {
            throw new NullPointerException("Child tag cannot be null");
        }

        return children.remove(tag) && tag.parents.remove(this);
    }

    @Override
    public boolean isParentOf(Set<Tag> tags) {
        if (Objects.isNull(tags)) {
            throw new NullPointerException("Tags cannot be null");
        }
        for (Tag tag : tags) {
            if (!isParentOf(tag)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isParentOf(Tag tag) {
        if (Objects.isNull(tag)) {
            throw new NullPointerException("Tag cannot be null");
        }
        if (this.equals(tag)) {
            return false; // A tag cannot be its own parent
        }
        for (Tag child : children) {
            if (child.equals(tag) || child.isParentOf(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isChildOf(Tag tag) {
        if (Objects.isNull(tag)) {
            throw new NullPointerException("Tag cannot be null");
        }
        if (this.equals(tag)) {
            return false; // A tag cannot be its own child
        }
        for (Tag parent : parents) {
            if (parent.equals(tag) || parent.isChildOf(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "CategoryTag{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
