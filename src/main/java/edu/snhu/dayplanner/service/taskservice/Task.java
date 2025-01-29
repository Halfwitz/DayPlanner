/******************************************************************************
 * [Task.java]
 * Author: Michael Lorenz
 * - Southern New Hampshire University
 *****************************************************************************/
package edu.snhu.dayplanner.service.taskservice;

import edu.snhu.dayplanner.service.CsvSerializable;
import edu.snhu.dayplanner.service.Entity;
import edu.snhu.dayplanner.service.InputValidator;
import edu.snhu.dayplanner.service.contactservice.Contact;

/**
 * This class represents a task object with fields for task name, and description.
 * Parent class gives this unique id when initialized.</p>
 * <ul>It enforces constraints:
 * <li>name must be non-null and <= 20 chars</li>
 * <li>description must be non-null and <= 50 characters</li>
 * </ul>
 */
public class Task extends Entity<Task.Field> implements CsvSerializable<Task> {
    private String name;        // required, up to 20 chars
    private String description; // required, up to 50 chars

    // maximum allowed character length of fields
    private static final int NAME_CHAR_LIMIT = 20;
    private static final int DESC_CHAR_LIMIT = 50;

    public enum Field {
        NAME,
        DESCRIPTION
    }

    /**
     * Initializes a new Task object
     * @param name task name (non-null, <= 20 chars)
     * @param description task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Task(String name, String description) {
        super(); // generate unique id
        this.name = InputValidator.verifyNonNullWithinChars(name, 1, NAME_CHAR_LIMIT);
        this.description = InputValidator.verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    // SET TASK FIELDS
    /**
     * Sets specified field name. Use contents from inner Field enum.
     * @param field field to modify.
     *
     * @throws IllegalArgumentException if parameter value is invalid
     */
    protected void updateField(Task.Field field, String value) {
        switch (field) {
            case NAME -> setName(value);
            case DESCRIPTION -> setDescription(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }
    /**
     * Updates task name
     * @param taskName new name (non-null, <= 20 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setName(String taskName) {
        this.name = InputValidator.verifyNonNullWithinChars(taskName, 1, NAME_CHAR_LIMIT);
    }

    /**
     * Updates task description
     * @param description new task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setDescription(String description) {
        this.description = InputValidator.verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    // GETTERS
    /**
     * Returns the correct value indicated by the selected field.
     *
     * @param field should be an enum constant associated with a specific field of an object
     * @return return value of the field as a string.
     */
    @Override
    public String getFieldValue(Field field) {
        switch (field) {
            case NAME -> {
                return getName();
            } case DESCRIPTION -> {
                return getDescription();
            } default -> throw new IllegalArgumentException("Unknown field");
        }
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    /**
     * @return string formatted "name, description"
     */
    @Override
    public String toString() {
        return name + ", " + description;
    }

    /**
     * Converts this object to a CSV line with the given delimiter. Appends attributes like
     * "firstname%lastname%phone%address".
     * Removes usage of the delimiter from original input (delimiter = '%', then "Mich%ael" = "Michael")
     *
     * @param delimiter char used to separate values in the CSV line. Removed from original value input.
     * @return CSV string formatted in Contact field values separated by the delimiter
     */
    @Override
    public String toCsv(char delimiter) {
        return (getName().replace(String.valueOf(delimiter), "") + delimiter +
                getDescription().replace(String.valueOf(delimiter), ""));
    }

    /**
     * Converts the given csv string to a new Task object. splits csv by the delimiter
     * @param csv A single csv line with each attribute separated by a delimiter. Should contain 2 values for task
     *            name and description. Ensure CSV values do not contain the
     *            delimiter within themselves.
     * @param delimiter The character used to split the csv string.
     * @return a new task object reference created from the delimiter values
     * @throws IllegalArgumentException if task fields are not valid format
     * @see #Task(String, String) valid field format descriptions
     */
    @Override
    public Task fromCsv(String csv, char delimiter) {
        // TODO: add logging for failed fromCSV and parts format validation (check parts.length)
        String[] parts = csv.split(String.valueOf("\\"+delimiter));
        for (String part : parts) {
            System.out.println(part);
        }
        return new Task(parts[0], parts[1]);
    }
}
