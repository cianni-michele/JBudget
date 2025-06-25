package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.dto.transaction.TransactionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class JsonTransactionRepository implements Repository<String, TransactionDTO> {

    private static final Logger LOGGER = LogManager.getLogger(JsonTransactionRepository.class);

    private final File transactionsDirectory;
    private final Map<String, Path> transactionsFilePath = new HashMap<>();
    private final Gson gson;

    public JsonTransactionRepository(JsonRepositoryConfig configuration) {
        this.gson = configuration.getGson();
        this.transactionsDirectory = new File("transactions"); // configuration.getDirectory() can be used for custom directory
        initializeDirectory();
        loadExistringFiles();
    }

    private void initializeDirectory() {
        LOGGER.debug("Initializing JsonTransactionRepository with directory: {}", transactionsDirectory);

        if (!transactionsDirectory.exists() && !transactionsDirectory.mkdirs()) {
            LOGGER.error("Failed to create transactions directory: {}", transactionsDirectory);
            throw new RuntimeException("Failed to create transactions directory: " + transactionsDirectory);
        }

        if (!transactionsDirectory.isDirectory()) {
            LOGGER.error("Transactions directory is not a directory: {}", transactionsDirectory);
            throw new RuntimeException("Transactions directory is not a directory: " + transactionsDirectory);
        }
    }

    private void loadExistringFiles() {
        File[] files = transactionsDirectory.listFiles(
                (file, name) -> file.isFile() && name.endsWith(".json")
        );

        if (files != null) {
            for (File file : files) {
                transactionsFilePath.put(
                        file.getName().replace(".json", ""),
                        file.toPath()
                );
            }
        }

        LOGGER.info("JsonTransactionRepository initialized with {} transactions", transactionsFilePath.size());
    }

    @Override
    public List<TransactionDTO> findAll() {
        LOGGER.debug("Finding all transactions");

        List<TransactionDTO> transactions = new ArrayList<>();

        for (Path transactionPath : transactionsFilePath.values()) {
            try (Reader reader = new FileReader(transactionPath.toFile())) {
                TransactionDTO transaction = gson.fromJson(reader, TransactionDTO.class);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            } catch (IOException e) {
                LOGGER.error("Error reading transaction file: {}", transactionPath, e);
                throw new RuntimeException("Error reading transaction file: " + transactionPath, e);
            }
        }
        LOGGER.info("Found {} transactions", transactions.size());
        return transactions;
    }

    @Override
    public Optional<TransactionDTO> findById(String transactionId) {
        LOGGER.debug("Finding transaction by ID: {}", transactionId);

        checkIfNull(transactionId, "Transaction ID cannot be null");

        if (!transactionsFilePath.containsKey(transactionId)) {
            LOGGER.info("Transaction with ID {} not found", transactionId);
            return Optional.empty();
        }

        Path transactionPath = transactionsFilePath.get(transactionId);

        try (Reader reader = new FileReader(transactionPath.toFile())) {
            TransactionDTO transaction = gson.fromJson(reader, TransactionDTO.class);
            LOGGER.debug("Transaction found: {}", transaction);
            return Optional.ofNullable(transaction);
        } catch (IOException e) {
            LOGGER.error("Error reading transaction file: {}", transactionPath, e);
            throw new RuntimeException("Error reading transaction file: " + transactionPath, e);
        }
    }

    @Override
    public void save(TransactionDTO transaction) {
        LOGGER.debug("Saving transaction: {}", transaction);

        checkIfNull(transaction, "Transaction cannot be null");
        checkIfNull(transaction.id(), "Transaction ID cannot be null");
        checkIfAlreadyExist(transaction, "Unable to save transaction with ID " + transaction.id() + " because it already exists");

        Path transactionPath = transactionsDirectory.toPath().resolve(transaction.id() + ".json");

        transactionsFilePath.put(transaction.id(), transactionPath);

        try (Writer writer = new FileWriter(transactionPath.toFile())) {
            gson.toJson(transaction, TransactionDTO.class, writer);
            LOGGER.info("Transaction saved successfully: {}", transaction.id());
        } catch (IOException e) {
            LOGGER.error("Error writing transaction file: {}", transactionPath, e);
            throw new RuntimeException("Error writing transaction file: " + transactionPath, e);
        }
    }

    private void checkIfAlreadyExist(TransactionDTO transaction, String message) {
        if (transactionsFilePath.containsKey(transaction.id())) {
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void update(TransactionDTO transaction) {
        LOGGER.debug("Updating transaction: {}", transaction);

        checkIfNull(transaction, "Transaction cannot be null");
        checkIfNull(transaction.id(), "Transaction ID cannot be null");
        checkIfNotExist(transaction, "Unable to update transaction with ID " + transaction.id() + " because it does not exist");

        Path transactionPath = transactionsFilePath.get(transaction.id());

        try (Writer writer = new FileWriter(transactionPath.toFile())) {
            gson.toJson(transaction, TransactionDTO.class, writer);
            LOGGER.info("Transaction updated successfully: {}", transaction.id());
        } catch (IOException e) {
            LOGGER.error("Error writing transaction file: {}", transactionPath, e);
            throw new RuntimeException("Error writing transaction file: " + transactionPath, e);
        }
    }

    @Override
    public void delete(String transactionId) {
        LOGGER.debug("Deleting transaction with ID: {}", transactionId);

        checkIfNull(transactionId, "Transaction ID cannot be null");
        checkIfNotExist(transactionId, "Unable to delete transaction with ID " + transactionId + " because it does not exist");

        Path transactionPath = transactionsFilePath.remove(transactionId);

        if (transactionPath != null && !transactionPath.toFile().delete()) {
            LOGGER.error("Failed to delete transaction file: {}", transactionPath);
            throw new RuntimeException("Failed to delete transaction file: " + transactionPath);
        }

        LOGGER.info("Transaction with ID {} deleted successfully", transactionId);
    }

    private void checkIfNotExist(TransactionDTO transaction, String message) {
        checkIfNotExist(transaction.id(), message);
    }

    private void checkIfNotExist(String transactionId, String message) {
        if (!transactionsFilePath.containsKey(transactionId)) {
            LOGGER.error(message);
            throw new NoSuchElementException(message);
        }
    }

    private void checkIfNull(Object object, String message) {
        if (Objects.isNull(object)) {
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
