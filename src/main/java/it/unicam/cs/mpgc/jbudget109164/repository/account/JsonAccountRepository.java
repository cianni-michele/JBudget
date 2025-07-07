package it.unicam.cs.mpgc.jbudget109164.repository.account;

import it.unicam.cs.mpgc.jbudget109164.config.JsonRepositoryConfig;
import it.unicam.cs.mpgc.jbudget109164.dto.AccountDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.repository.JsonRepositoryException;
import it.unicam.cs.mpgc.jbudget109164.repository.AbstractJsonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class JsonAccountRepository extends AbstractJsonRepository<UUID, AccountDTO> implements AccountRepository {

    private static final Logger LOGGER = LogManager.getLogger(JsonAccountRepository.class);

    public JsonAccountRepository(JsonRepositoryConfig config) {
        super(config);
    }

    @Override
    protected UUID parseToId(String id) {
        return UUID.fromString(id);
    }

    @Override
    public List<AccountDTO> findAll() {
        LOGGER.debug("Finding all accounts");

        List<AccountDTO> accounts = filesPath.values().stream()
                .map(path -> readFromFile(path, AccountDTO.class))
                .filter(Objects::nonNull)
                .toList();

        LOGGER.info("Found {} accounts", accounts.size());

        return accounts;
    }

    @Override
    public AccountDTO findById(UUID accountId) {
        LOGGER.debug("Finding account by ID: {}", accountId);

        validateId(accountId);

        if (!filesPath.containsKey(accountId)) {
            LOGGER.info("Account with ID {} not found", accountId);
            return null;
        }

        Path accountPath = filesPath.get(accountId);

        return readFromFile(accountPath, AccountDTO.class);
    }

    @Override
    public void save(AccountDTO account) {
        LOGGER.debug("Saving account: {}", account);

        validateDTO(account);

        if (filesPath.containsKey(account.id())) {
            String message = "Unable to save account with ID " + account.id() + " because it already exists";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path accountPath = directory.toPath().resolve(account.id() + JSON_EXTENSION);

        filesPath.put(account.id(), accountPath);

        writeToFile(accountPath, AccountDTO.class, account);
    }

    @Override
    public void update(AccountDTO account) {
        LOGGER.debug("Updating account: {}", account);

        validateDTO(account);

        if (!filesPath.containsKey(account.id())) {
            String message = "Unable to update account with ID " + account.id() + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path accountPath = filesPath.get(account.id());

        writeToFile(accountPath, AccountDTO.class, account);
    }

    @Override
    public void deleteById(UUID id) {
        LOGGER.debug("Deleting account with ID: {}", id);

        validateId(id);

        if (!filesPath.containsKey(id)) {
            String message = "Unable to delete account with ID " + id + " because it does not exist";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        Path accountPath = filesPath.get(id);

        if (accountPath != null && !accountPath.toFile().delete()) {
            String message = "Failed to delete account with ID " + id;
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }

        LOGGER.info("Account with ID {} deleted successfully", id);
    }

    @Override
    public int count() {
        return filesPath.size();
    }

    @Override
    protected void validateDTO(AccountDTO account) {
        if (account == null) {
            String message = "AccountDTO cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
        validateId(account.id());
    }

    @Override
    protected void validateId(UUID accountId) {
        if (accountId == null) {
            String message = "Account ID cannot be null";
            LOGGER.error(message);
            throw new JsonRepositoryException(message);
        }
    }
}
