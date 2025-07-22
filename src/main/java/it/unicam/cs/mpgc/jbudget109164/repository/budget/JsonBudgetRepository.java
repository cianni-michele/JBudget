package it.unicam.cs.mpgc.jbudget109164.repository.budget;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagDeletedEvent;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagDeletedEventListener;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class JsonBudgetRepository extends AbstractJsonRepository<BudgetDTO> implements BudgetRepository, TagDeletedEventListener {

    private static final Logger LOGGER = LogManager.getLogger(JsonBudgetRepository.class);

    private final TagRepository tagRepository;

    public JsonBudgetRepository(JsonRepositoryConfig config,
                                   TagRepository tagRepository) {
        super(config);
        this.tagRepository = tagRepository;
    }

    @Override
    public List<BudgetDTO> findAll() {
        LOGGER.debug("Finding all budgets");

        List<BudgetDTO> budgets = new ArrayList<>();

        for (Path path : filesPath.values()) {
            BudgetDTO budgetFound = readFromFile(path, BudgetDTO.class);
            if (Objects.nonNull(budgetFound)) {
                BudgetDTO budget = getBudgetWithTag(budgetFound);
                budgets.add(budget);
            } else {
                LOGGER.warn("Budget file {} could not be read", path);
            }
        }

        return budgets;
    }

    @Override
    public boolean existsById(UUID id) {
        return filesPath.containsKey(id);
    }

    @Override
    public Optional<BudgetDTO> findById(UUID budgetId) {
        LOGGER.debug("Finding budget with ID {}", budgetId);

        validateId(budgetId);

        if (!filesPath.containsKey(budgetId)) {
            LOGGER.warn("Budget with ID {} not found", budgetId);
            return Optional.empty();
        }

        Path budgetPath = filesPath.get(budgetId);

        BudgetDTO budgetFound = readFromFile(budgetPath, BudgetDTO.class);

        if (Objects.isNull(budgetFound)) {
            LOGGER.warn("Budget with ID {} could not be read from file", budgetId);
            return Optional.empty();
        }

        return Optional.ofNullable(getBudgetWithTag(budgetFound));
    }

    private BudgetDTO getBudgetWithTag(BudgetDTO budgetFound) {
        return BudgetDTO.builder().copyFrom(budgetFound)
                .withTag(resolveTagReference(budgetFound.tag()))
                .build();
    }

    private TagDTO resolveTagReference(TagDTO tag) {
        if (Objects.isNull(tag)) {
            return null;
        }

        if (!tagRepository.existsById(tag.id())) {
            LOGGER.warn("Tag with ID {} does not exist in the repository", tag.id());
            return null;
        }

        Optional<TagDTO> tagFound = tagRepository.findById(tag.id());
        if (tagFound.isEmpty()) {
            LOGGER.warn("Tag with ID {} could not be found in the repository", tag.id());
            return null;
        }

        return tagFound.get();
    }

    @Override
    public void save(BudgetDTO budget) {
        LOGGER.debug("Saving budget {}", budget);

        validateDTO(budget);

        Path filePath;
        if (filesPath.containsKey(budget.id())) {
            filePath = filesPath.get(budget.id());
        } else {
            filePath = directory.toPath().resolve(budget.id() + JSON_EXTENSION);
            filesPath.put(budget.id(), filePath);
        }

        writeToFile(filePath, BudgetDTO.class, budget);

        LOGGER.info("Budget with ID {} saved successfully", budget.id());
    }


    @Override
    public void deleteById(UUID budgetId) {
        LOGGER.debug("Deleting budget with ID {}", budgetId);

        validateId(budgetId);

        if (!filesPath.containsKey(budgetId)) {
            String message = "Unable to delete budget with ID " + budgetId + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path budgetPath = filesPath.remove(budgetId);

        if (Objects.nonNull(budgetPath) && !budgetPath.toFile().delete()) {
            String message = "Unable to delete budget file at " + budgetPath;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        LOGGER.info("Budget with ID {} deleted successfully", budgetId);
    }

    @Override
    protected void validateDTO(BudgetDTO budget) {
        if (Objects.isNull(budget)) {
            String message = "Budget DTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        validateId(budget.id());

        validateTagReference(budget.tag());
    }

    private void validateTagReference(TagDTO tag) {
        if (Objects.isNull(tag)) {
            return;
        }

        if (!tagRepository.existsById(tag.id())) {
            String message = "Tag with ID " + tag.id() + " does not exist in the repository";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }

    @Override
    protected void validateId(UUID budgetId) {
        if (Objects.isNull(budgetId)) {
            String message = "Budget ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }

    @Override
    public void onTagDeleted(TagDeletedEvent event) {
        UUID tagId = event.deletedTagId();
        LOGGER.debug("Removing tag references with ID {} from all budgets", tagId);

        List<UUID> budgetsToDelete = new ArrayList<>();
        for (Path budgetPath : filesPath.values()) {
            BudgetDTO budget = readFromFile(budgetPath, BudgetDTO.class);
            if (budget.tag() != null && budget.tag().id().equals(tagId)) {
                budgetsToDelete.add(budget.id());
            }
        }
        for (UUID budgetId : budgetsToDelete) {
            deleteById(budgetId);
        }
        LOGGER.info("Removed tag references with ID {} from all budgets", tagId);
    }
}
