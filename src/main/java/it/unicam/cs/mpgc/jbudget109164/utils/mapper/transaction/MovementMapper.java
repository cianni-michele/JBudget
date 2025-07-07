package it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.MovementDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.AccountMapper;

public final class MovementMapper extends TemplateMapper<Movement, MovementDTO> {

    private final AccountMapper accountMapper;

    public MovementMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public MovementDTO mapToDTO(Movement model) {
        return MovementDTOBuilder.getInstance()
                .withAmount(model.amount())
                .withDescription(model.description())
                .withAccount(accountMapper.toDTO(model.account()))
                .build();
    }

    @Override
    public Movement mapToModel(MovementDTO dto) {
        return new Movement(
                dto.amount(),
                dto.description(),
                accountMapper.toModel(dto.account())
        );
    }
}
