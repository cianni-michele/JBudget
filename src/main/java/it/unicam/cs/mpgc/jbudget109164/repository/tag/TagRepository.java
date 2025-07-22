package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link TagDTO} objects.
 * This interface defines methods for checking existence, finding, deleting, and saving tag DTOs.
 * It is designed to be implemented by classes that provide specific storage mechanisms (e.g., in-memory, database).
 *
 * @author Michele Cianni
 */
public interface TagRepository {

    /**
     * Checks if a dto with the given identifier exists in the repository.
     *
     * @param tagId the identifier of the dto
     * @return true if a dto with the given identifier exists, false otherwise
     */
    boolean existsById(UUID tagId);

    /**
     * Finds all dtos managed by this repository.
     *
     * @return a list of all dtos
     */
    List<TagDTO> findAll();

    /**
     * Finds a dto by its identifier.
     *
     * @param tagId the identifier of the dto
     * @return an optional containing the dto if found, or empty if not found
     */
    Optional<TagDTO> findById(UUID tagId);

    /**
     * Deletes a dto by its identifier.
     *
     * @param tagId the identifier of the dto to delete
     */
    void deleteById(UUID tagId);

    /**
     * Saves a new dto in the repository.
     *
     * @param dto the dto to save
     */
    void save(TagDTO dto);
}
