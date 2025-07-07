package it.unicam.cs.mpgc.jbudget109164.model.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleTransactionFactory Tests")
class SimpleTransactionFactoryTest {

    private TransactionDetails transactionDetails;

    @BeforeEach
    void setUp() {
        transactionDetails = new TransactionDetails(
                "Test transaction",
                LocalDate.now(),
                new HashSet<>(),
                new ArrayList<>()
        );
    }

    @Test
    @DisplayName("Should create a simple transaction instance")
    void createTransactionShouldReturnInstanceOfSimpleTransaction() {
        SimpleTransactionFactory factory = new SimpleTransactionFactory();

        Transaction transaction = factory.createTransaction(transactionDetails);

        assertInstanceOf(SimpleTransaction.class, transaction);
    }

    @Test
    @DisplayName("Should generate unique IDs for transactions")
    void createTransactionShouldGenerateUniqueIDs() {
        SimpleTransactionFactory factory = new SimpleTransactionFactory();

        Transaction transaction1 = factory.createTransaction(transactionDetails);
        Transaction transaction2 = factory.createTransaction(transactionDetails);

        assertNotEquals(transaction1.getId(), transaction2.getId());
    }

    @Test
    @DisplayName("Should return transaction with provided details")
    void createTransactionShouldReturnTransactionWithProvidedDetails() {
        SimpleTransactionFactory factory = new SimpleTransactionFactory();

        Transaction transaction = factory.createTransaction(transactionDetails);

        assertEquals(transactionDetails, transaction.getDetails());
    }
}