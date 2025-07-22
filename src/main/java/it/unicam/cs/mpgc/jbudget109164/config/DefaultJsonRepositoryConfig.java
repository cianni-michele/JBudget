package it.unicam.cs.mpgc.jbudget109164.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.LocalDateTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.BudgetDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.MovementDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.config.serialization.dto.TagDTOTypeAdapter;
import it.unicam.cs.mpgc.jbudget109164.dto.budget.BudgetDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.tag.TagDTO;
import it.unicam.cs.mpgc.jbudget109164.dto.movement.MovementDTO;
import it.unicam.cs.mpgc.jbudget109164.exception.config.JsonRepositoryConfigException;
import it.unicam.cs.mpgc.jbudget109164.repository.budget.JsonBudgetRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.movement.JsonMovementRepository;
import it.unicam.cs.mpgc.jbudget109164.repository.tag.JsonTagRepository;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public class DefaultJsonRepositoryConfig implements JsonRepositoryConfig {

    private static final String DIRECTORY = "jbudget";

    private final Map<String, String> repositoryDirectories;

    public DefaultJsonRepositoryConfig() {
        repositoryDirectories = Map.of(
                JsonMovementRepository.class.getName(), resolveDirectory("movements"),
                JsonTagRepository.class.getName(), resolveDirectory("tags"),
                JsonBudgetRepository.class.getName(), resolveDirectory("budgets")
        );
    }

    private static String resolveDirectory(String name) {
        return DIRECTORY + File.separator + name;
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(TagDTO.class, new TagDTOTypeAdapter())
                .registerTypeAdapter(MovementDTO.class, new MovementDTOTypeAdapter())
                .registerTypeAdapter(BudgetDTO.class, new BudgetDTOTypeAdapter())
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
