package it.unicam.cs.mpgc.jbudget109164.model;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;

public record Movement(double amount, String description, Account account) {
}
