package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import java.util.UUID;

/**
 * Represents an event that is triggered when a tag is deleted.
 * This interface provides a method to retrieve the ID of the deleted tag.
 *
 * @author Michele Cianni
 */
@FunctionalInterface
public interface TagDeletedEvent {
    UUID deletedTagId();
}
