package it.unicam.cs.mpgc.jbudget109164.service.movement;

import it.unicam.cs.mpgc.jbudget109164.exception.service.MovementNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementDetails;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.MovementRepository;
import it.unicam.cs.mpgc.jbudget109164.mapper.movement.MovementMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;

public class MovementServiceImpl implements MovementService {

    private static final Logger LOGGER = LogManager.getLogger(MovementServiceImpl.class);

    private final MovementRepository repository;
    private final MovementFactory factory;
    private final MovementMapper mapper;

    public MovementServiceImpl(MovementRepository repository,
                               MovementFactory factory,
                               MovementMapper mapper) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
    }

    //Is better to delegate the pagination and sorting to the repository layer
    @Override
    public List<Movement> getMovements(int page, int size, String sortBy, boolean desc) {
        LOGGER.debug("Retrieving movements - Page: {}, Size: {}", page, size);

        List<Movement> allMovements = repository.findAll().stream()
                .map(mapper::toModel)
                .sorted(getComparator(sortBy, desc))
                .toList();

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allMovements.size());
        if (fromIndex >= allMovements.size() || fromIndex < 0) {
            LOGGER.warn("Requested page {} with size {} is out of bounds. Returning empty list.", page, size);
            return Collections.emptyList();
        }

        List<Movement> paginatedMovements = allMovements.subList(fromIndex, toIndex);

        LOGGER.info("Total movements found: {}", paginatedMovements.size());

        return paginatedMovements;
    }


    private Comparator<? super Movement> getComparator(String sortBy, boolean desc) {
        return switch (sortBy.toLowerCase()) {
            case "amount" -> getAmountComparator(desc);
            case "date" -> getDateComparator(desc);
            default -> Comparator.comparing(Movement::getDate).thenComparing(Movement::getAmount);
        };
    }

    private Comparator<? super Movement> getAmountComparator(boolean desc) {
        Comparator<Movement> comparator = Comparator.comparing(Movement::getAmount);
        if (desc) {
            return comparator.reversed();
        }
        return comparator;
    }

    private Comparator<? super Movement> getDateComparator(boolean desc) {
        Comparator<Movement> comparator = Comparator.comparing(Movement::getDate);
        if (desc) {
            return comparator.reversed();
        }
        return comparator;
    }

    @Override
    public Optional<Movement> getMovementById(UUID id) {
        LOGGER.debug("Retrieving movement with ID: {}", id);

        Optional<Movement> movement = repository.findById(id).map(mapper::toModel);

        LOGGER.info("Movement found: {}", movement);

        return movement;
    }

    @Override
    public Movement createMovement(MovementDetails details) {
        LOGGER.debug("Creating movement with details: {}", details);

        Movement createdMovement = factory.createMovement(details);

        repository.save(mapper.toDTO(createdMovement));

        LOGGER.info("Movement created with ID: {}", createdMovement.getId());

        return createdMovement;
    }

    @Override
    public Movement updateMovement(UUID id, MovementDetails details) {
        LOGGER.debug("Updating movement with ID: {} with details: {}", id, details);

        Optional<Movement> movementFound = getMovementById(id);

        if (movementFound.isEmpty()) {
            throw movementNotFoundExceptionAndLogWarning(id);
        }

        Movement updatedMovement = movementFound.get().copy(details);

        repository.save(mapper.toDTO(updatedMovement));

        LOGGER.info("Movement with ID: {} updated successfully", id);

        return updatedMovement;
    }

    @Override
    public void deleteMovement(UUID id) {
        LOGGER.debug("Deleting movement with ID: {}", id);

        if (!repository.existsById(id)) {
            throw movementNotFoundExceptionAndLogWarning(id);
        }
        
        repository.deleteById(id);

        LOGGER.info("Movement with ID: {} deleted successfully", id);
    }


    @Override
    public Movement addTagToMovement(Tag tag, UUID movementId) {
        LOGGER.debug("Adding tag with ID {} to movement with ID {}", tag.getId(), movementId);

        Optional<Movement> movementFound = getMovementById(movementId);

        if (movementFound.isEmpty()) {
            throw movementNotFoundExceptionAndLogWarning(movementId);
        }

        Movement movement = movementFound.get();
        movement.addTag(tag);

        repository.save(mapper.toDTO(movement));

        LOGGER.info("Tag added successfully to movement with ID: {}", movementId);

        return movement;
    }

    private MovementNotFoundException movementNotFoundExceptionAndLogWarning(UUID id) {
        LOGGER.warn("Movement with ID: {} does not exist", id);
        return new MovementNotFoundException(MessageFormat.format("Movement with ID: {0} does not exist", id));
    }
}
