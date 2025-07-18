package it.unicam.cs.mpgc.jbudget109164.mapper.movement;

import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.movement.SimpleMovement;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class SimpleMovementMapper extends MovementMapper {

    public SimpleMovementMapper(TagMapper tagMapper) {
        super(tagMapper);
    }

    @Override
    protected Movement mapToModel(MovementDTO dto) {
        return new SimpleMovement(
                dto.id(),
                dto.date(),
                dto.description(),
                dto.amount(),
                dto.tags() != null ?
                        Arrays.stream(dto.tags()).map(tagMapper::toModel).collect(Collectors.toSet())
                        : Collections.emptySet()
        );
    }
}
