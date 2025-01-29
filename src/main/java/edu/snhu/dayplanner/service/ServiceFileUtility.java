package edu.snhu.dayplanner.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling file-based storage and retrieval of entities implementing the {@link CsvSerializable} interface.
 * Provides methods to read from and write to a file using a consistent delimiter for CSV serialization.
 *
 * @param <T> The type of entity being handled, which must implement {@link CsvSerializable}.
 */
public class ServiceFileUtility<T extends CsvSerializable<T>> {
    private final String filePath;
    private final T prototype;
    private static final char DELIMITER = '|';

    public ServiceFileUtility(String filePath, T prototype) {
        this.filePath = filePath;
        this.prototype = prototype; // used to deserialize entities from file
    }

    /**
     * Reads entities from the file at the specified file path.
     * Each line in the file is parsed into an entity using the prototype's {@code fromCsv} method.
     *
     * @return A list of entities read from the file. If the file does not exist, returns an empty list.
     */
    public List<T> readFromFile() {
        List<T> entities = new ArrayList<>();

        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        entities.add(prototype.fromCsv(line, DELIMITER));
                    } catch (Exception e) {
                        System.out.println("ServiceFileUtility["+filePath+"]: Could not create object from file line \"" + line + "\""
                        + "\n\t(" + e.getMessage() + ")") ;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    /**
     * Writes a list of entities to the file at the specified file path.
     * Each entity is serialized to a CSV-formatted string using its {@code toCsv} method.
     *
     * @param items The list of entities to write to the file.
     */
    public void writeToFile(List<T> items) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent()); // create directory if it does not exist

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (T item : items) {
                    writer.write(item.toCsv(DELIMITER));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
