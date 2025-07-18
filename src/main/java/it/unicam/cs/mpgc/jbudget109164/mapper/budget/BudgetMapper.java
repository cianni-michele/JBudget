package it.unicam.cs.mpgc.jbudget109164.mapper.budget;

import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;
import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.mapper.AbstractMapper;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;

import java.time.temporal.Temporal;

public abstract class BudgetMapper<P extends Temporal> extends AbstractMapper<Budget<P>, BudgetDTO> {

    protected final TagMapper tagMapper;

    protected BudgetMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    protected BudgetDTO mapToDTO(Budget<P> entity) {
        return BudgetDTO.builder()
                .withId(entity.getId())
                .withTag(tagMapper.toDTO(entity.getTag()))
                .withPeriod(entity.getPeriod().toString())
                .withExpectedAmount(entity.getExpectedAmount())
                .build();
    }
}
