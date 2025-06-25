package it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.transaction.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagMapper;

public abstract class TransactionMapper extends TemplateMapper<Transaction, TransactionDTO> {

    protected final TagMapper tagMapper;

    protected TransactionMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public TransactionDTO mapToDTO(Transaction entity) {
        return new TransactionDTO(
                entity.getId(),
                entity.getDate(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getTags().stream().map(tagMapper::toDTO).toArray(TagDTO[]::new)
        );
    }
}
