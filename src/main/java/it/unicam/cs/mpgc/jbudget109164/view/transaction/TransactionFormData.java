package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import java.time.LocalDate;

public record TransactionFormData(
        String transactionDescription,
        LocalDate transactionDate
) {
}
