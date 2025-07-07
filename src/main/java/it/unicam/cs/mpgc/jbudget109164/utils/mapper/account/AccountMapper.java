package it.unicam.cs.mpgc.jbudget109164.utils.mapper.account;

import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.AccountDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.TemplateMapper;

public abstract class AccountMapper extends TemplateMapper<Account, AccountDTO> {

    @Override
    protected AccountDTO mapToDTO(Account model) {
        return AccountDTOBuilder.getInstance()
                .withId(model.getId())
                .withType(model.getType().name())
                .withName(model.getName())
                .withDescription(model.getDescription())
                .withInitialBalance(model.getInitialBalance())
                .build();
    }
}
