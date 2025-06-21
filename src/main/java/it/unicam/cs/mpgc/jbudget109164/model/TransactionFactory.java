package it.unicam.cs.mpgc.jbudget109164.model;

import java.time.LocalDate;
import java.util.Set;

public interface TransactionFactory {

    Transaction createTransaction(LocalDate date, double amount, String description, Set<Tag> tags);
}
