package it.unicam.cs.mpgc.jbudget109164.repository.movement;

import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementRepository  {

    /**
     * Finds all dtos managed by this repository.
     *
     * @return a list of all dtos
     */
    List<MovementDTO> findAll();

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
    Optional<MovementDTO> findById(UUID id);

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
    void save(MovementDTO dto);

    /**
     * Counts the number of dtos managed by this repository.
     *
     * @return the number of dtos
     */
    int count();
}
