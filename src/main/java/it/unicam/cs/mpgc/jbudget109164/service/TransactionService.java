package it.unicam.cs.mpgc.jbudget109164.service;

import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {

    Optional<Transaction> getTransactionById(UUID transactionId);

    Transaction createTransaction(TransactionDetails transactionDetails);

    Transaction updateTransaction(UUID transactionId, TransactionDetails transactionDetails);

    void deleteTransaction(UUID transactionId);

    List<Transaction> getAllTransactions();

    Transaction addMovementToTransaction(UUID transactionId, Movement movement);

    Transaction removeMovementFromTransaction(UUID transactionId, int index);

    Transaction addTagToTransaction(UUID transactionId, Tag tag);

    int getTransactionCount();
}
