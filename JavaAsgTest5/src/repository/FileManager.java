package repository;

import exception.FileProcessingException;

/**
 * Abstraction for persisting and retrieving data to/from a file.
 * Any storage format (TXT now, JSON/XML as a bonus later) can
 * implement this interface, demonstrating interface-based programming.
 */
public interface FileManager {

    /** Writes the given data object to the named file. */
    void saveData(Object data, String fileName) throws FileProcessingException;

    /** Reads and returns the data stored in the named file. */
    Object loadData(String fileName) throws FileProcessingException;
}