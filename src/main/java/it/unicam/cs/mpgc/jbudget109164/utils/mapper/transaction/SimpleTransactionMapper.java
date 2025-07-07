package it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction;

import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.SimpleTransaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.AccountMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagMapper;

import java.util.*;
import java.util.stream.Collectors;

public final class SimpleTransactionMapper extends TransactionMapper {

    public SimpleTransactionMapper(TagMapper tagMapper, AccountMapper accountMapper) {
        super(tagMapper, accountMapper);
    }

    @Override
    public Transaction mapToModel(TransactionDTO dto) {
        return new SimpleTransaction(
                dto.id(),
                dto.description(),
                dto.date(),
                dto.tags() != null ? Arrays.stream(dto.tags()).map(tagMapper::toModel).collect(Collectors.toSet()) : null,
                dto.movements() != null ? Arrays.stream(dto.movements()).map(movementMapper::toModel).collect(Collectors.toList()) : null
        );
    }

}
