package it.unicam.cs.mpgc.jbudget109164.repository;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.transaction.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.JsonRepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JsonTransactionRepository extends AbstractJsonRepository<String, TransactionDTO> {

    private static final Logger LOGGER = LogManager.getLogger(JsonTransactionRepository.class);

    private static final String JSON_EXTENSION = ".json";

    private final Map<String, Path> transactionsFilePath = new HashMap<>();

    public JsonTransactionRepository(JsonRepositoryConfig config) {
        super(config);
        loadExistingFiles();
    }

    private void loadExistingFiles() {
        File[] files = directory.listFiles(
                (file, name) -> file.isFile() && name.endsWith(JSON_EXTENSION)
        );

        if (files != null) {
            for (File file : files) {
                transactionsFilePath.put(
                        file.getName().replace(JSON_EXTENSION, ""),
                        file.toPath()
                );
            }
        }

        LOGGER.info("JsonTransactionRepository initialized with {} transactions", transactionsFilePath.size());
    }

    @Override
    public List<TransactionDTO> findAll() {
        LOGGER.debug("Finding all transactions");

        List<TransactionDTO> transactions = transactionsFilePath.values().stream()
                .map(path -> readFromFile(path, TransactionDTO.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        LOGGER.info("Found {} transactions", transactions.size());
        return transactions;
    }

    @Override
    public Optional<TransactionDTO> findById(String transactionId) {
        LOGGER.debug("Finding transaction by ID: {}", transactionId);

        validateId(transactionId);

        if (!transactionsFilePath.containsKey(transactionId)) {
            LOGGER.info("Transaction with ID {} not found", transactionId);
            return Optional.empty();
        }

        Path transactionPath = transactionsFilePath.get(transactionId);

        TransactionDTO dto = readFromFile(transactionPath, TransactionDTO.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public void save(TransactionDTO transaction) {
        LOGGER.debug("Saving transaction: {}", transaction);

        validateDTO(transaction);

        if (transactionsFilePath.containsKey(transaction.id())) {
            String message = "Unable to save transaction with ID " + transaction.id() + " because it already exists";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = directory.toPath().resolve(transaction.id() + JSON_EXTENSION);

        transactionsFilePath.put(transaction.id(), transactionPath);

        writeToFile(transactionPath, TransactionDTO.class, transaction);
    }

    @Override
    public void update(TransactionDTO transaction) {
        LOGGER.debug("Updating transaction: {}", transaction);

        validateDTO(transaction);

        if (!transactionsFilePath.containsKey(transaction.id())) {
            String message = "Unable to update transaction with ID " + transaction.id() + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = transactionsFilePath.get(transaction.id());

        writeToFile(transactionPath, TransactionDTO.class, transaction);
    }

    @Override
    public void deleteById(String transactionId) {
        LOGGER.debug("Deleting transaction with ID: {}", transactionId);

        validateId(transactionId);

        if (!transactionsFilePath.containsKey(transactionId)) {
            String message = "Unable to delete transaction with ID " + transactionId + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = transactionsFilePath.remove(transactionId);

        if (transactionPath != null && !transactionPath.toFile().delete()) {
            String message = "Failed to delete transaction file: " + transactionPath;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        LOGGER.info("Transaction with ID {} deleted successfully", transactionId);
    }

    @Override
    protected void validateId(String id) {
        if (id == null) {
            String message = "Transaction ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }

    @Override
    protected void validateDTO(TransactionDTO dto) {
        if (dto == null) {
            String message = "Transaction DTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
        validateId(dto.id());
    }
}
