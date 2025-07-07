package it.unicam.cs.mpgc.jbudget109164.exception.service;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(UUID transactionId) {
        this("Transaction with ID " + transactionId + " not found.");
    }
}
