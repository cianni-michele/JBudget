package it.unicam.cs.mpgc.jbudget109164.repository.budget;

import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing BudgetDTO objects.
 * This interface defines methods for finding, checking existence, deleting, and saving budget DTOs.
 * It is designed to be implemented by classes that provide specific storage mechanisms (e.g., in-memory, database).
 *
 * @author Michele Cianni
 */
public interface BudgetRepository {
    /**
     * Finds all dtos managed by this repository.
     *
     * @return a list of all dtos
     */
    List<BudgetDTO> findAll();

    /**
     * Checks if a dto with the given identifier exists in the repository.
     *
     * @param id the identifier of the dto
     * @return true if a dto with the given identifier exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Finds a dto by its identifier.
     *
     * @param id the identifier of the dto
     * @return an optional containing the dto if found, or empty if not found
     */
    Optional<BudgetDTO> findById(UUID id);

    /**
     * Deletes a dto by its identifier.
     *
     * @param id the identifier of the dto to delete
     */
    void deleteById(UUID id);

    /**
     * Saves a new dto in the repository.
     *
     * @param dto the dto to save
     */
    void save(BudgetDTO dto);
}
