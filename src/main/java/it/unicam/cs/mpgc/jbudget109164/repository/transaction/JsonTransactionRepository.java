package it.unicam.cs.mpgc.jbudget109164.repository.transaction;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.account.AccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.TagRepository;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.MovementDTOBuilder;
import it.unicam.cs.mpgc.jbudget109164.utils.builder.TransactionDTOBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class JsonTransactionRepository extends AbstractJsonRepository<UUID, TransactionDTO> implements TransactionRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonTransactionRepository.class);

    private final AccountRepository accountRepository;

    private final TagRepository tagRepository;

    public JsonTransactionRepository(JsonRepositoryConfig config,
                                     AccountRepository accountRepository,
                                     TagRepository tagRepository) {
        super(config);
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    protected UUID parseToId(String id) {
        return UUID.fromString(id);
    }

    @Override
    public List<TransactionDTO> findAll() {
        LOGGER.debug("Finding all transactions");

        List<TransactionDTO> transactions = new ArrayList<>();

        for (Path path : filesPath.values()) {
            TransactionDTO transactionFound = readFromFile(path, TransactionDTO.class);
            if (transactionFound != null) {
                TransactionDTO transaction = enrichTransactionWithRelations(transactionFound);
                transactions.add(transaction);
                LOGGER.debug("Transaction found: {}", transaction);
            } else {
                LOGGER.warn("Transaction file {} could not be read", path);
            }
        }

        LOGGER.info("Found {} transactions", transactions.size());
        return transactions;
    }

    @Override
    public TransactionDTO findById(UUID transactionId) {
        LOGGER.debug("Finding transaction by ID: {}", transactionId);

        validateId(transactionId);

        if (!filesPath.containsKey(transactionId)) {
            LOGGER.info("Transaction with ID {} not found", transactionId);
            return null;
        }

        Path transactionPath = filesPath.get(transactionId);

        TransactionDTO transactionFound = readFromFile(transactionPath, TransactionDTO.class);

        if (transactionFound == null) {
            LOGGER.warn("Transaction with ID {} could not be read from file", transactionId);
            return null;
        }

        return enrichTransactionWithRelations(transactionFound);
    }

    private TransactionDTO enrichTransactionWithRelations(TransactionDTO transaction) {
        MovementDTO[] movements = enrichMovementsWithAccounts(transaction.movements());
        TagDTO[] tags = resolveTagReferences(transaction.tags());
        return TransactionDTOBuilder.getInstance().copyOf(transaction)
                .withMovements(movements)
                .withTags(tags)
                .build();
    }

    private TagDTO[] resolveTagReferences(TagDTO[] tags) {
        if (tags == null) {
            return null;
        }

        List<TagDTO> result = new ArrayList<>();

        for (TagDTO tag : tags) {
            TagDTO resolvedTag = tagRepository.findById(tag.id());
            if (resolvedTag != null) {
                result.add(resolvedTag);
            } else {
                LOGGER.warn("Tag with ID {} not found", tag.id());
            }
        }

        return result.toArray(new TagDTO[0]);
    }

    private MovementDTO[] enrichMovementsWithAccounts(MovementDTO[] movements) {
        if (movements == null) {
            return null;
        }

        List<MovementDTO> result = new ArrayList<>();

        for (MovementDTO movement : movements) {
            result.add(enrichMovementWithAccount(movement));
        }

        return result.toArray(new MovementDTO[0]);
    }

    private MovementDTO enrichMovementWithAccount(MovementDTO movement) {
        UUID accountId = movement.account().id();

        AccountDTO account = accountRepository.findById(accountId);

        if (account == null) {
            LOGGER.warn("Account reference with ID {} not found for movement", accountId);
        }

        return MovementDTOBuilder.getInstance().copyOf(movement)
                .withAccount(account)
                .build();
    }

    @Override
    public void save(TransactionDTO transaction) {
        LOGGER.debug("Saving transaction: {}", transaction);

        validateDTO(transaction);

        if (filesPath.containsKey(transaction.id())) {
            String message = "Unable to save transaction with ID " + transaction.id() + " because it already exists";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = directory.toPath().resolve(transaction.id() + JSON_EXTENSION);

        filesPath.put(transaction.id(), transactionPath);

        writeToFile(transactionPath, TransactionDTO.class, transaction);
    }

    @Override
    public void update(TransactionDTO transaction) {
        LOGGER.debug("Updating transaction: {}", transaction);

        validateDTO(transaction);

        if (!filesPath.containsKey(transaction.id())) {
            String message = "Unable to update transaction with ID " + transaction.id() + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = filesPath.get(transaction.id());

        writeToFile(transactionPath, TransactionDTO.class, transaction);
    }

    @Override
    public void deleteById(UUID transactionId) {
        LOGGER.debug("Deleting transaction with ID: {}", transactionId);

        validateId(transactionId);

        if (!filesPath.containsKey(transactionId)) {
            String message = "Unable to delete transaction with ID " + transactionId + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path transactionPath = filesPath.remove(transactionId);

        if (transactionPath != null && !transactionPath.toFile().delete()) {
            String message = "Failed to delete transaction file: " + transactionPath;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        LOGGER.info("Transaction with ID {} deleted successfully", transactionId);
    }

    @Override
    public int count() {
        return filesPath.size();
    }

    @Override
    protected void validateDTO(TransactionDTO transaction) {
        if (transaction == null) {
            String message = "Transaction DTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        validateId(transaction.id());

        validateTagsReference(transaction.tags());

        validateMovementsToAccountsReference(transaction.movements());
    }

    @Override
    protected void validateId(UUID transactionId) {
        if (transactionId == null) {
            String message = "Transaction ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }

    private void validateTagsReference(TagDTO[] tags) {
        if (tags != null) {
            for (TagDTO tag : tags) {
                if (tag != null) {
                    if (tagRepository.findById(tag.id()) == null) {
                        String message = "Tag with ID " + tag.id() + " does not exist";
                        LOGGER.error(message);
                        throw new JsonRepositoryException(message);
                    }
                } else {
                    LOGGER.warn("Tag ID in transaction cannot be null");
                }
            }
        }
    }

    private void validateMovementsToAccountsReference(MovementDTO[] movements) {
        if (movements != null) {
            for (MovementDTO movement : movements) {
                if (movement != null) {
                    UUID accountId = movement.account().id();
                    if (accountId != null && accountRepository.findById(accountId) == null) {
                        String message = "Account with ID " + accountId + " does not exist for movement";
                        LOGGER.error(message);
                        throw new JsonRepositoryException(message);
                    }
                } else {
                    LOGGER.warn("Movement in transaction cannot be null");
                }
            }
        }
    }
}
