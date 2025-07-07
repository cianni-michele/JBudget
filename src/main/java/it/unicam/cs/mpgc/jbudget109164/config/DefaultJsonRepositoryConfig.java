package it.unicam.cs.mpgc.jbudget109164.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.LocalDateTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.UUIDTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.MovementDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.TagDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.TransactionDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.TransactionDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.config.JsonRepositoryConfigException;
import it.unicam.cs.mpgc.jbudget109164.repository.account.JsonAccountRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.transaction.JsonTransactionRepository;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class DefaultJsonRepositoryConfig implements JsonRepositoryConfig {

    private final Map<String, String> repositoryDirectories;

    public DefaultJsonRepositoryConfig() {
        repositoryDirectories = Map.of(
                JsonTransactionRepository.class.getName(), "src/main/resources/transactions",
                JsonAccountRepository.class.getName(), "src/main/resources/accounts",
                JsonTagRepository.class.getName(), "src/main/resources/tags"
        );
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(TagDTO.class, new TagDTOTypeAdapter())
                .registerTypeAdapter(MovementDTO.class, new MovementDTOTypeAdapter())
                .registerTypeAdapter(TransactionDTO.class, new TransactionDTOTypeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Override
    public File getDirectory(Class<?> aClass) {
        if (aClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        String fileName = repositoryDirectories.get(aClass.getName());
        if (fileName != null) {
            return new File(fileName);
        }
        throw new JsonRepositoryConfigException(aClass);
    }
}
