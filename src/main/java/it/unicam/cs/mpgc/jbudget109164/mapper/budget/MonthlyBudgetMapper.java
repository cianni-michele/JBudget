package it.unicam.cs.mpgc.jbudget109164.mapper.budget;

import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;
import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.MonthlyBudget;
import it.unicam.cs.mpgc.jbudget109164.mapper.tag.TagMapper;

import java.time.YearMonth;

public final class MonthlyBudgetMapper extends BudgetMapper<YearMonth> {

    public MonthlyBudgetMapper(TagMapper tagMapper) {
        super(tagMapper);
    }

    @Override
    protected Budget<YearMonth> mapToModel(BudgetDTO dto) {
        return new MonthlyBudget(
                dto.id(),
                tagMapper.toModel(dto.tag()),
                YearMonth.parse(dto.period()),
                dto.expectedAmount()
        );
    }
}
