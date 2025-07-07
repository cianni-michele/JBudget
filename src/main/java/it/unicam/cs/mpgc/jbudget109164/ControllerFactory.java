package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.config.DefaultJsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.controller.AccountController;
import it.unicam.cs.mpgc.jbudget109164.controller.TagController;
import it.unicam.cs.mpgc.jbudget109164.controller.TransactionController;
import it.unicam.cs.mpgc.jbudget109164.model.account.AccountFactory;
import it.unicam.cs.mpgc.jbudget109164.model.account.BasicAccountFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagFactory;
import it.unicam.cs.mpgc.jbudget109164.model.tag.TagNodeFactory;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.SimpleTransactionFactory;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.account.AccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.account.JsonAccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.transaction.JsonTransactionRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.transaction.TransactionRepository;
import it.unicam.cs.mpgc.jbudget109164.service.*;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.AccountMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.account.BasicAccountMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.tag.TagNodeMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction.SimpleTransactionMapper;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction.TransactionMapper;

public final class ControllerFactory {

    private final TagService tagService;

    private final AccountService accountService;

    private final TransactionService transactionService;

    public ControllerFactory() {
        JsonRepositoryConfig config = new DefaultJsonRepositoryConfig();

        TagMapper tagMapper = new TagNodeMapper();
        TagRepository tagRepository = new JsonTagRepository(config);
        TagFactory tagFactory = new TagNodeFactory();
        tagService = new TagServiceImpl(tagRepository, tagFactory, tagMapper);

        AccountRepository accountRepository = new JsonAccountRepository(config);
        AccountFactory accountFactory = new BasicAccountFactory();
        AccountMapper accountMapper = new BasicAccountMapper();
        accountService = new AccountServiceImpl(accountRepository, accountFactory, accountMapper);

        TransactionMapper transactionMapper = new SimpleTransactionMapper(tagMapper, accountMapper);
        TransactionRepository transactionRepository = new JsonTransactionRepository(config, accountRepository, tagRepository);
        TransactionFactory transactionFactory = new SimpleTransactionFactory();
        transactionService = new TransactionServiceImpl(transactionRepository, transactionFactory, transactionMapper);
    }

    public AccountController getAccountController() {
        return new AccountController(accountService);
    }

    public TagController getTagController() {
        return new TagController(tagService);
    }

    public TransactionController getTransactionController() {
        return new TransactionController(transactionService, accountService, tagService);
    }
}
