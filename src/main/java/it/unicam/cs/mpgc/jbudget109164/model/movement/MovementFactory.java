package it.unicam.cs.mpgc.jbudget109164.model.movement;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.util.Collection;
import java.util.UUID;

/**
 * Factory interface for creating instances of {@link Movement}.
 * This interface provides methods to create new movements with specified details,
 * optionally with a specific ID or a randomly generated one.
 *
 * @author Michele Cianni
 */
public interface MovementFactory {

    /**
     * Creates a new movement with the specified details with a randomly generated ID and no tags.
     *
     * @param details the details of the movement to create
     * @return a new Movement instance
     */
    default Movement createMovement(MovementDetails details) {
        return createMovement(UUID.randomUUID(), details);
    }

    /**
     * Creates a new movement with the specified ID and details without any tags.
     *
     * @param id      the unique identifier for the movement
     * @param details the details of the movement to create
     * @return a new Movement instance with the specified ID and details
     */
    Movement createMovement(UUID id, MovementDetails details);

    /**
     * Creates a new movement with the specified details and associated tags.
     *
     * @param details the details of the movement to create
     * @param tags    the tags to be associated with the movement
     * @return a new Movement instance with the specified details and tags
     */
    default Movement createMovement(MovementDetails details, Collection<Tag> tags) {
        return createMovement(UUID.randomUUID(), details, tags);
    }

    /**
     * Creates a new movement with the specified ID, details, and associated tags.
     *
     * @param id      the unique identifier for the movement
     * @param details the details of the movement to create
     * @param tags    the tags to be associated with the movement
     * @return a new Movement instance with the specified ID, details, and tags
     */
    Movement createMovement(UUID id, MovementDetails details, Collection<Tag> tags);

}
