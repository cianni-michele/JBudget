package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;

public record MovementFormData(
        double movementAmount,
        String movementDescription,
        Account selectedAccount
) {
}
