package edu.snhu.dayplanner.service;

import edu.snhu.dayplanner.service.contactservice.Contact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServiceFileUtility<T extends CsvSerializable<T>> {
    private final String filePath;
    private final T prototype;
    private static final char DELIMITER = '|';

    public ServiceFileUtility(String filePath, T prototype) {
        this.filePath = filePath;
        this.prototype = prototype;

    }

    public List<T> readFromFile() {
        List<T> entities = new ArrayList<>();

        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    entities.add(prototype.fromCsv(line, DELIMITER));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return entities;
    }

    public void writeToFile(List<T> items) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

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
