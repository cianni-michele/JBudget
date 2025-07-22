package it.unicam.cs.mpgc.jbudget109164.controller.movement;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import it.unicam.cs.mpgc.jbudget109164.service.tag.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unicam.cs.mpgc.jbudget109164.exception.service.TagNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.service.movement.MovementService;

public final class MovementController {

    private static final Logger LOGGER = LogManager.getLogger(MovementController.class);

    private final MovementService movementService;
    private final TagService tagService;


    public MovementController(MovementService movementService,
                              TagService tagService) {
        this.movementService = movementService;
        this.tagService = tagService;
    }

    public List<Movement> getMovements(int page, int size, String sortBy, boolean asc) {
        LOGGER.debug("Retrieving all movements");

        List<Movement> movements = movementService.getMovements(page, size, sortBy, asc);

        LOGGER.info("Total movements found: {}", movements.size());

        return movements;    
    }

    public Movement createMovement(LocalDate date, String description, double amount) {
        LOGGER.debug("Creating a new movement");

        validateParameters(date, description);

        Movement createdMovement = movementService.createMovement(new MovementDetails(date, description, amount));

        LOGGER.info("Movement created successfully: {}", createdMovement);

        return createdMovement;
    }


    public Movement updateMovement(UUID id, LocalDate date, String description, double amount) {
        LOGGER.debug("Updating movement with ID: {}", id);

        validateParameters(id, date, description);

        Movement updatedMovement = movementService.updateMovement(id, new MovementDetails(date, description, amount));

        LOGGER.info("Movement updated successfully: {}", updatedMovement);

        return updatedMovement;
    }


    public void deleteMovement(UUID id) {
        LOGGER.debug("Deleting movement with ID: {}", id);

        validateParameters(id);

        movementService.deleteMovement(id);

        LOGGER.info("Movement with ID: {} deleted successfully", id);
    }

    public Movement addTagToMovement(UUID tagId, UUID movementId) {
        LOGGER.debug("Adding tag with ID {} to movement with ID {}", tagId, movementId);

        validateParameters(tagId, movementId);

        Tag tag = tagService.getTagById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + tagId));

        return movementService.addTagToMovement(tag, movementId);
    }

    public Movement removeTagFromMovement(UUID tagId, UUID movementId) {
        LOGGER.debug("Removing tag with ID {} from movement with ID {}", tagId, movementId);

        validateParameters(tagId, movementId);

        Tag tag = tagService.getTagById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + tagId));

        return movementService.removeTagFromMovement(tag, movementId);
    }

    private void validateParameters(Object... params) {
        for (Object param : params) {
            if (Objects.isNull(param)) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
    }

    public int getTotalMovementsCount() {
        return movementService.getTotalMovementsCount();
    }
}
