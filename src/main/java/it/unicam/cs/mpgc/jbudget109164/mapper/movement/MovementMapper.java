package it.unicam.cs.mpgc.jbudget109164.mapper.movement;

import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.mapper.AbstractMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;

public abstract class MovementMapper extends AbstractMapper<Movement, MovementDTO> {
    protected final TagMapper tagMapper;

    public MovementMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    protected MovementDTO mapToDTO(Movement model) {
        return MovementDTO.builder()
                .withId(model.getId())
                .withDate(model.getDate())
                .withAmount(model.getAmount())
                .withDescription(model.getDescription())
                .withTags(model.getTags().stream().map(tagMapper::mapToDTO).toArray(TagDTO[]::new))
                .build();
    }
}
