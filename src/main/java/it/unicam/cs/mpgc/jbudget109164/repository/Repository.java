package it.unicam.cs.mpgc.jbudget109164.repository;

import java.util.List;

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
     * Finds a dto by its identifier.
     *
     * @param id the identifier of the dto
     * @return an optional containing the dto if found, or empty if not found
     */
    D findById(I id);

    /**
     * Saves a new dto in the repository.
     *
     * @param dto the dto to save
     */
    void save(D dto);

    /**
     * Updates an existing dto in the repository.
     *
     * @param dto the dto to update
     */
    void update(D dto);

    /**
     * Deletes a dto by its identifier.
     *
     * @param id the identifier of the dto to delete
     */
    void deleteById(I id);

    /**
     * Counts the number of dtos in the repository.
     *
     * @return the number of dtos in the repository
     */
    int count();
}
