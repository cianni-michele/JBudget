package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.Entity;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a financial movement with details about the date, amount,
 * description, and associated tags. This interface extends the {@link Entity}
 * interface, providing methods to access and manipulate movement details.
 *
 * @author Michele Cianni
 */
public interface Movement extends Entity<MovementDetails> {

    /**
     * Returns the date of this movement.
     *
     * @return the date of the movement
     */
    LocalDate getDate();

    /**
     * Returns the amount of this movement.
     *
     * @return the amount of the movement
     */
    double getAmount();

    /**
     * Returns the description of this movement.
     *
     * @return the description of the movement
     */
    String getDescription();

    /**
     * Adds a tag to this movement.
     *
     * @param tag the tag to be added
     */
    void addTag(Tag tag);

    /**
     * Removes a tag from this movement.
     *
     * @param tag the tag to be removed
     */
    void removeTag(Tag tag);

    /**
     * Checks if this movement is tagged with the specified tag.
     *
     * @param tag the tag to check
     * @return true if the movement is tagged with the specified tag, false otherwise
     */
    boolean isTaggedBy(Tag tag);

    /**
     * Returns the set of tags associated with this movement.
     *
     * @return a set of tags associated with the movement
     */
    Set<Tag> getTags();

    /**
     * Creates a copy of this movement with the specified details.
     *
     * @param details the new details for the copied movement
     * @return a new instance of Movement with the specified details
     */
    Movement copy(MovementDetails details);
}
