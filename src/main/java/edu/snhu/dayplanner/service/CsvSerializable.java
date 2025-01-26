package edu.snhu.dayplanner.service;

/**
 * Handle conversion between objects and Csv strings
 * @param <T> The type of object to convert to and from CSV format.
 */
public interface CsvSerializable<T> {

    /**
     * Converts this object to a CSV line with the given delimiter
     * @param delimiter char used to separate values in the CSV line
     * @return
     */
    String toCsv(char delimiter);


    /**
     * Converts the given csv string to object of type <T>T</T>
     * @param csv
     * @param delimiter
     * @return
     */
    T fromCsv(String csv, char delimiter);
}
