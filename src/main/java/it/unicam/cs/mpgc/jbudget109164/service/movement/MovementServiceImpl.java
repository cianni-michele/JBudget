package it.unicam.cs.mpgc.jbudget109164.service.movement;

import it.unicam.cs.mpgc.jbudget109164.exception.service.MovementNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.util.time.ScheduledPeriod;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementDetails;
import it.unicam.cs.mpgc.jbudget109164.model.movement.MovementFactory;
import it.unicam.cs.mpgc.jbudget109164.util.time.Period;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.MovementRepository;
import it.unicam.cs.mpgc.jbudget109164.mapper.movement.MovementMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;

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

    @Override
    public List<Movement> generateScheduledMovements(double amount, String description, List<Tag> tags,
                                                     ScheduledPeriod scheduledPeriod) {
        List<Movement> movements = new ArrayList<>();

        scheduledPeriod.forEachOccurrence(date -> {
            MovementDetails movementDetails = new MovementDetails(date, description, amount);
            Movement movement = factory.createMovement(movementDetails, tags);
            repository.save(mapper.toDTO(movement));
            movements.add(movement);
        });

        return movements;
    }

    //Is better to delegate the pagination and sorting to the repository layer
    @Override
    public List<Movement> getMovements(int page, int size, String sortBy, boolean asc) {
        LOGGER.debug("Retrieving movements - Page: {}, Size: {}", page, size);

        List<Movement> allMovements = repository.findAll().stream()
                .map(mapper::toModel)
                .sorted(getComparator(sortBy, asc))
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


    private Comparator<? super Movement> getComparator(String sortBy, boolean asc) {
        Comparator<? super Movement> comparator = switch (sortBy.toUpperCase()) {
            case "AMOUNT" -> {
                LOGGER.debug("Sorting movements by amount");
                yield Comparator.comparing(Movement::getAmount);
            }

            case "DESCRIPTION" -> {
                LOGGER.debug("Sorting movements by description");
                yield Comparator.comparing(Movement::getDescription, Comparator.nullsLast(String::compareTo));
            }

            case "DATE" -> {
                LOGGER.debug("Sorting movements by date");
                yield Comparator.comparing(Movement::getDate);
            }

            default -> {
                LOGGER.warn("Unknown sort field: {}. Defaulting to date sorting.", sortBy);
                yield Comparator.comparing(Movement::getDate);
            }
        };

        if (asc) {
            LOGGER.debug("Sorting movements by {} in ascending order", sortBy);
            return comparator;
        } else {
            LOGGER.debug("Sorting movements by {} in descending order", sortBy);
            return comparator.reversed();
        }

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

    @Override
    public Movement removeTagFromMovement(Tag tag, UUID movementId) {
        LOGGER.debug("Removing tag with ID {} from movement with ID {}", tag.getId(), movementId);

        Optional<Movement> movementFound = getMovementById(movementId);

        if (movementFound.isEmpty()) {
            throw movementNotFoundExceptionAndLogWarning(movementId);
        }

        Movement movement = movementFound.get();
        movement.removeTag(tag);

        repository.save(mapper.toDTO(movement));

        LOGGER.info("Tag removed successfully from movement with ID: {}", movementId);

        return movement;
    }

    @Override
    public int getTotalMovementsCount() {
        return repository.count();
    }

    @Override
    public List<Movement> getMovementsByPeriod(Period period) {
        return getMovements(
                movement -> period.contains(movement.getDate())
        );
    }

    @Override
    public List<Movement> getMovementsByTagAndPeriod(Tag tag, Period period) {
        return getMovements(movement ->
                movement.isTaggedBy(tag) && period.contains(movement.getDate())
        );
    }

    private List<Movement> getMovements(Predicate<? super Movement> predicate) {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .filter(predicate)
                .toList();
    }

    private MovementNotFoundException movementNotFoundExceptionAndLogWarning(UUID id) {
        String message = MessageFormat.format("Movement with ID: {0} does not exist", id);
        LOGGER.warn(message);
        return new MovementNotFoundException(message);
    }
}
