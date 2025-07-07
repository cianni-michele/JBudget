package it.unicam.cs.mpgc.jbudget109164.model.account;

import java.util.UUID;

public class BasicAccountFactory implements AccountFactory {

    @Override
    public Account createAccount(AccountInfo accountInfo) {
        return createAccount(UUID.randomUUID(), accountInfo);
    }

    @Override
    public Account createAccount(UUID accountId, AccountInfo accountInfo) {
        return new BasicAccount(accountId, accountInfo);
    }
}
