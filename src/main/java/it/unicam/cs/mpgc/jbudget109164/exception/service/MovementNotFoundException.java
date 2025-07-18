package it.unicam.cs.mpgc.jbudget109164.exception.service;

import java.util.UUID;

public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(String message) {
        super(message);
    }

    public MovementNotFoundException(UUID transactionId) {
        this("Transaction with ID " + transactionId + " not found.");
    }
}
