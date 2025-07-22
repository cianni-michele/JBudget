package it.unicam.cs.mpgc.jbudget109164.service.movement;

import it.unicam.cs.mpgc.jbudget109164.util.time.ScheduledPeriod;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementDetails;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementService {


    /**
     * Generates scheduled movements based on the provided details, scheduled period, and tags.
     *
     * @param amount
     * @param description
     * @param tags            the tags to be associated with the generated movements
     * @param scheduledPeriod the scheduled period for which movements are to be generated
     * @return a list of generated movements
     */
    List<Movement> generateScheduledMovements(double amount, String description, List<Tag> tags, ScheduledPeriod scheduledPeriod);

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

    /**
     * Removes a tag from a movement.
     *
     * @param tag the tag to remove from the movement
     * @param movementId the ID of the movement from which the tag will be removed
     * @return the updated movement with the tag removed
     */
    Movement removeTagFromMovement(Tag tag, UUID movementId);

    /**
     * Retrieves the total count of movements.
     *
     * @return the total number of movements
     */
    int getTotalMovementsCount();

    /**
     * Retrieves movements by a specific period.
     *
     * @param period the period for which to retrieve movements
     * @return a list of movements that fall within the specified period
     */
    List<Movement> getMovementsByPeriod(Period period);

    /**
     * Retrieves movements by a specific tag and period.
     *
     * @param tag    the ID of the tag to filter movements
     * @param period the period for which to retrieve movements
     * @return a list of movements that match the specified tag and fall within the specified period
     */
    List<Movement> getMovementsByTagAndPeriod(Tag tag, Period period);
}
