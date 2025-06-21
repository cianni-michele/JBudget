package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import java.util.UUID;

/**
 * Factory for creating instances of {@link SimpleTransaction}.
 * This factory is used to create simple transactions with a unique identifier.
 * <p>
 * This class implements the {@link TransactionFactory} interface.
 *
 * @author Michele Cianni
 */
public class SimpleTransactionFactory implements TransactionFactory {

    @Override
    public Transaction createTransaction(UUID id, TransactionDetails transactionDetails) {
        return new SimpleTransaction(id, transactionDetails);
    }
}
