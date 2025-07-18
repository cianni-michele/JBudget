package it.unicam.cs.mpgc.jbudget109164.service.movement;

import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementService {

    /**
     * Retrieves a paginated list of movements.
     *
     * @param page   the page number to retrieve (0-based index)
     * @param size   the number of movements per page
     * @param sortBy the field by which to sort the movements
     * @param desc   the order in which to sort the movements (e.g., "asc" for ascending, "desc" for descending)
     * @return a list of movements for the specified page and size
     */
    List<Movement> getMovements(int page, int size, String sortBy, boolean desc);

    /**
     * Retrieves a movement by its ID.
     *
     * @param id the ID of the movement to retrieve
     * @return the movement with the specified ID, or null if not found
     */
    Optional<Movement> getMovementById(UUID id);

    /**
     * Creates a new movement with the given details.
     *
     * @param details the details of the movement to create
     * @return the created movement
     */
    Movement createMovement(MovementDetails details);

    /**
     * Updates an existing movement with the given ID and new details.
     *
     * @param id      the ID of the movement to update
     * @param details the new details for the movement
     * @return the updated movement
     */
    Movement updateMovement(UUID id, MovementDetails details);

    /**
     * Deletes a movement by its ID.
     *
     * @param id the ID of the movement to delete
     */
    void deleteMovement(UUID id);

    /**
     * Adds a tag to a movement.
     *
     * @param tag        the tag to add to the movement
     * @param movementId the ID of the movement to which the tag will be added
     * @return the updated movement with the tag added
     */
    Movement addTagToMovement(Tag tag, UUID movementId);

}
