package it.unicam.cs.mpgc.jbudget109164.utils.mapper.account;

import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountType;
import it.unicam.cs.mpgc.jbudget109164.model.account.BasicAccount;

public final class BasicAccountMapper extends AccountMapper {

    @Override
    protected Account mapToModel(AccountDTO dto) {
        return new BasicAccount(
                dto.id(),
                dto.name(),
                dto.description(),
                AccountType.valueOf(dto.type()),
                dto.initialBalance()
        );
    }
}
