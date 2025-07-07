package it.unicam.cs.mpgc.jbudget109164.service;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountFactory;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountInfo;
import it.unicam.cs.mpgc.jbudget109164.repository.account.AccountRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.AccountMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final AccountFactory factory;

    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                                 AccountFactory accountFactory,
                                 AccountMapper accountMapper) {
        this.repository = accountRepository;
        this.factory = accountFactory;
        this.mapper = accountMapper;
    }

    @Override
    public List<Account> getAllAccounts() {
        return repository.findAll()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<Account> getAccountById(UUID accountId) {
        return Optional.ofNullable(repository.findById(accountId)).map(mapper::toModel);
    }

    @Override
    public Account createAccount(AccountInfo accountInfo) {
        Account account = factory.createAccount(accountInfo);

        repository.save(mapper.toDTO(account));

        return account;
    }

    @Override
    public Account updateAccount(UUID accountId, AccountInfo accountInfo) {
        Account account = factory.createAccount(accountId, accountInfo);

        repository.update(mapper.toDTO(account));

        return account;
    }

    @Override
    public void deleteAccount(UUID accountId) {
        repository.deleteById(accountId);
    }
}
