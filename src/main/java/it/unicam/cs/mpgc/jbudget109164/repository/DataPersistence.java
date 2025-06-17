package it.unicam.cs.mpgc.jbudget109164.repository;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This interface defines methods for saving and loading a DataManager to and from a specified path.
 * Implementations of this interface should handle the serialization and deserialization of the DataManager.
 */
public interface DataPersistence {

    /**
     * Saves the given DataManager to the specified path.
     *
     * @param data the DataManager to save
     * @param path the path where the data should be saved
     * @throws IOException if an I/O error occurs during saving
     */
    void save(DataManager data, Path path) throws IOException;

    /**
     * Loads a DataManager from the specified path.
     *
     * @param path the path from which to load the DataManager
     * @return the loaded DataManager
     * @throws IOException if an I/O error occurs during loading
     */
    DataManager load(Path path) throws IOException;
}
