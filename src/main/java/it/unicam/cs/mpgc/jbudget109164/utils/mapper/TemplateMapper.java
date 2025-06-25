package it.unicam.cs.mpgc.jbudget109164.utils.mapper;

/**
 * A template mapper that provides a base implementation for converting between entities and DTOs.
 *
 * @param <E> the type of the entity
 * @param <D> the type of the Data Transfer Object (DTO)
 */
public abstract class TemplateMapper<E, D> implements Mapper<E, D> {

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
    public E toEntity(D dto) {
        if (dto == null) return null;
        return mapToEntity(dto);
    }

    /**
     * Maps a DTO to its corresponding entity.
     *
     * @param dto the DTO to map
     * @return the mapped entity
     */
    protected abstract E mapToEntity(D dto);
}
