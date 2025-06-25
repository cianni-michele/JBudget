package it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.transaction.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.SimpleTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public final class SimpleTransactionMapper extends TransactionMapper {

    public SimpleTransactionMapper(TagMapper tagMapper) {
        super(tagMapper);
    }

    @Override
    public Transaction mapToEntity(TransactionDTO dto) {
        return new SimpleTransaction(
                dto.id(),
                dto.date(),
                dto.amount(),
                dto.description(),
                dto.tags() != null
                        ? Arrays.stream(dto.tags()).map(tagMapper::toEntity).collect(Collectors.toSet())
                        : Collections.emptySet()
        );

    }
}
