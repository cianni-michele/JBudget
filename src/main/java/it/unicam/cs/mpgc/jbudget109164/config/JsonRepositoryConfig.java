package it.unicam.cs.mpgc.jbudget109164.config;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.jbudget109164.exception.JsonRepositoryConfigException;

import java.io.File;


/**
 * This interface defines a configuration for JSON persistence, allowing customization of the Gson instance
 * used for serialization and deserialization of objects.
 * <p>
 * Implementations of this interface can provide custom type adapters or other configurations for Gson.
 * </p>
 *
 * @author Michele Cianni
 */

public interface JsonRepositoryConfig {

    /**
     * Configures a Gson instance with custom type adapters.
     *
     * @return a configured Gson instance
     */
    Gson getGson();

    /**
     * Provides the directory where JSON files for repositories of the specified class will be stored.
     *
     * @param aClass the class of the repository for which the directory is requested
     * @return the directory as a File object
     * @throws NullPointerException if the class is null
     * @throws JsonRepositoryConfigException if the class is not supported
     */
    File getDirectory(Class<?> aClass);
}
