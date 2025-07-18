package it.unicam.cs.mpgc.jbudget109164.mapper.scheduled;

import it.unicam.cs.mpgc.jbudget109164.dto.scheduled.ScheduledMovementsDTO;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.Period;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.ScheduledMovements;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.ScheduledSimpleMovements;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ScheduledSimpleMovementsMapper extends ScheduledMovementsMapper {

    public ScheduledSimpleMovementsMapper(TagMapper tagMapper) {
        super(tagMapper);
    }

    @Override
    protected ScheduledMovements mapToModel(ScheduledMovementsDTO dto) {
        return new ScheduledSimpleMovements(
                dto.id(),
                Period.of(dto.fromDate(), dto.toDate()),
                dto.amount(),
                dto.description(),
                dto.dayOfMonth(),
                Arrays.stream(dto.tags()).map(tagMapper::toModel).collect(Collectors.toSet())
        );
    }
}
