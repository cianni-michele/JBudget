package it.unicam.cs.mpgc.jbudget109164.repository;

import com.google.gson.Gson;


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
}
