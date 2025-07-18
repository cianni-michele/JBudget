package it.unicam.cs.mpgc.jbudget109164.model.movement;

import java.util.UUID;

@FunctionalInterface
public interface MovementFactory {

    /**
     * Creates a new movement with the specified details.
     *
     * @param details the details of the movement to create
     * @return a new Movement instance
     */
    default Movement createMovement(MovementDetails details) {
        return createMovement(UUID.randomUUID(), details);
    }

    /**
     * Creates a new movement with the specified ID and details.
     *
     * @param id      the unique identifier for the movement
     * @param details the details of the movement to create
     * @return a new Movement instance with the specified ID and details
     */
    Movement createMovement(UUID id, MovementDetails details);

}
