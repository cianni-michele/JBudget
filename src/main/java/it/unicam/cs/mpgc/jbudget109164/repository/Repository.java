package it.unicam.cs.mpgc.jbudget109164.repository;

import java.util.List;
import java.util.Optional;

/**
 * A generic repository interface for managing dto of type D identified by type I.
 * This interface can be extended to provide specific repository functionalities.
 *
 * @param <I> the type of the identifier for the dto
 * @param <D> the type of the dto managed by the repository
 */
public interface Repository<I, D> {

    /**
     * Finds all dtos managed by this repository.
     *
     * @return a list of all dtos
     */
    List<D> findAll();

    /**
     * Checks if a dto with the given identifier exists in the repository.
     *
     * @param id the identifier of the dto
     * @return true if a dto with the given identifier exists, false otherwise
     */
    boolean existsById(I id);

    /**
     * Finds a dto by its identifier.
     *
     * @param id the identifier of the dto
     * @return an optional containing the dto if found, or empty if not found
     */
    Optional<D> findById(I id);

    /**
     * Deletes a dto by its identifier.
     *
     * @param id the identifier of the dto to delete
     */
    void deleteById(I id);

    /**
     * Saves a new dto in the repository.
     *
     * @param dto the dto to save
     */
    void save(D dto);
}
