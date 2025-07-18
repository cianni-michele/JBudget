package it.unicam.cs.mpgc.jbudget109164.repository.tag;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.repository.Repository;

import java.util.UUID;

public interface TagRepository extends Repository<UUID, TagDTO> {

    /**
     * Checks if a dto with the given identifier exists in the repository.
     *
     * @param id the identifier of the dto to check
     * @return true if a dto with the given identifier exists, false otherwise
     */
    boolean existsById(UUID id);

}
