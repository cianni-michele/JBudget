package it.unicam.cs.mpgc.jbudget109164.mapper.scheduled;

import it.unicam.cs.mpgc.jbudget109164.dto.scheduled.ScheduledMovementsDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.mapper.AbstractMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;
import it.unicam.cs.mpgc.jbudget109164.model.scheduled.ScheduledMovements;

public abstract class ScheduledMovementsMapper extends AbstractMapper<ScheduledMovements, ScheduledMovementsDTO> {

    protected final TagMapper tagMapper;

    protected ScheduledMovementsMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    protected ScheduledMovementsDTO mapToDTO(ScheduledMovements entity) {
        return ScheduledMovementsDTO.builder()
                .withId(entity.getId())
                .withDescription(entity.getDescription())
                .withAmount(entity.getAmount())
                .withFromDate(entity.getScheduledPeriod().from())
                .withToDate(entity.getScheduledPeriod().to())
                .withDayOfMonth(entity.getDayOfMonth())
                .withTags(entity.getTags().stream().map(tagMapper::mapToDTO).toArray(TagDTO[]::new))
                .build();
    }
}
