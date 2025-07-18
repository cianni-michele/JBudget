package it.unicam.cs.mpgc.jbudget109164.mapper;

/**
 * A template mapper that provides a base implementation for converting between entities and DTOs.
 *
 * @param <E> the type of the entity
 * @param <D> the type of the Data Transfer Object (DTO)
 */
public abstract class AbstractMapper<E, D> implements Mapper<E, D> {

    @Override
    public D toDTO(E entity) {
        if (entity == null) return null;
        return mapToDTO(entity);
    }

    /**
     * Maps an entity to its corresponding DTO.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    protected abstract D mapToDTO(E entity);

    @Override
    public E toModel(D dto) {
        if (dto == null) return null;
        return mapToModel(dto);
    }

    /**
     * Maps a DTO to its corresponding entity.
     *
     * @param dto the DTO to map
     * @return the mapped entity
     */
    protected abstract E mapToModel(D dto);
}
