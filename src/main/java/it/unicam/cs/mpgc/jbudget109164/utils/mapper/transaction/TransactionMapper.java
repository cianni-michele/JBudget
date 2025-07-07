package it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.AccountMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagMapper;

public abstract class TransactionMapper extends TemplateMapper<Transaction, TransactionDTO> {

    protected final TagMapper tagMapper;

    protected final MovementMapper movementMapper;

    protected TransactionMapper(TagMapper tagMapper, AccountMapper accountMapper) {
        this.tagMapper = tagMapper;
        this.movementMapper = new MovementMapper(accountMapper);
    }

    @Override
    public TransactionDTO mapToDTO(Transaction entity) {
        return new TransactionDTO(
                entity.getId(),
                entity.getDate(),
                entity.getDescription(),
                entity.getTags().stream().map(tagMapper::toDTO).toArray(TagDTO[]::new),
                entity.getMovements().stream().map(movementMapper::toDTO).toArray(MovementDTO[]::new)
        );
    }
}
