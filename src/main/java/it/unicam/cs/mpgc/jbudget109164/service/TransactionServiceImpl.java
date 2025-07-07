package it.unicam.cs.mpgc.jbudget109164.service;

import it.unicam.cs.mpgc.jbudget109164.exception.service.TransactionNotFoundException;
import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionDetails;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.TransactionFactory;
import it.unicam.cs.mpgc.jbudget109164.repository.transaction.TransactionRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.mapper.transaction.TransactionMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {

    protected final TransactionRepository repository;

    protected final TransactionFactory factory;

    protected final TransactionMapper mapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionFactory transactionFactory,
                                  TransactionMapper transactionMapper) {
        this.repository = transactionRepository;
        this.factory = transactionFactory;
        this.mapper = transactionMapper;
    }

    @Override
    public Optional<Transaction> getTransactionById(UUID transactionId) {
        return Optional.ofNullable(repository.findById(transactionId)).map(mapper::toModel);
    }

    @Override
    public Transaction createTransaction(TransactionDetails transactionDetails) {
        Transaction transaction = factory.createTransaction(transactionDetails);

        repository.save(mapper.toDTO(transaction));

        return transaction;
    }

    @Override
    public Transaction updateTransaction(UUID transactionId, TransactionDetails transactionDetails) {
        Transaction transaction = factory.createTransaction(transactionId, transactionDetails);

        repository.update(mapper.toDTO(transaction));

        return transaction;
    }

    @Override
    public void deleteTransaction(UUID transactionId) {
        repository.deleteById(transactionId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Transaction addMovementToTransaction(UUID transactionId, Movement movement) {
        return getTransactionById(transactionId)
                .map(transaction -> {
                    transaction.addMovement(movement);
                    return updateTransaction(transactionId, transaction.getDetails());
                })
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public Transaction removeMovementFromTransaction(UUID transactionId, int index) {
        return getTransactionById(transactionId)
                .map(transaction -> {
                    transaction.removeMovement(index);
                    return updateTransaction(transactionId, transaction.getDetails());
                })
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public Transaction addTagToTransaction(UUID transactionId, Tag tag) {
        return getTransactionById(transactionId)
                .map(transaction -> {
                    transaction.addTag(tag);
                    return updateTransaction(transactionId, transaction.getDetails());
                })
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }


    @Override
    public int getTransactionCount() {
        return repository.count();
    }
}
