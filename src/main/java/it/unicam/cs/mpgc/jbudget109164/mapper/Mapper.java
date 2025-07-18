package it.unicam.cs.mpgc.jbudget109164.mapper;

public interface Mapper<E, D> {

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO, or null if the entity is null
     */
    D toDTO(E entity);

    /**
     * Converts a DTO to an entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity, or null if the DTO is null
     */
    E toModel(D dto);
}
