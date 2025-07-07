package it.unicam.cs.mpgc.jbudget109164.view.account;

import it.unicam.cs.mpgc.jbudget109164.model.account.AccountType;

public record AccountFormData(
        AccountType accountType,
        String accountName,
        String accountDescription,
        double accountInitialBalance
) {
}
