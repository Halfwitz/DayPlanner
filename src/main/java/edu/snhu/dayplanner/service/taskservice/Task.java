/******************************************************************************
 * Module Three Milestone
 * [Task.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 * Description:
 * This class represents a task object with fields for task name, and description.
 * Parent class gives this a unique id when initialized. It enforces constraints:
 * - name must be non-null, and <= 20 characters
 * - description must be non-null and <= 50 characters
 * Date: Due 9/29/2024
 * Modified: 10/09/2024 to remove outer package dependencies
 * Modified: 10/11/2024 to merge with superclass
 *****************************************************************************/
package service.taskservice;

public class Task {
    private final String id;
    private static long idCounter = 0;
    private String name, description;

    // maximum allowed character length of fields
    private final int ID_CHAR_LIMIT = 10;
    private final int NAME_CHAR_LIMIT = 20;
    private final int DESC_CHAR_LIMIT = 50;

    /**
     * Initializes a new Task object
     * @param name task name (non-null, <= 20 chars)
     * @param description task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Task(String name, String description) {
        this.id = verifyNonNullWithinChars(generateId(), 1, ID_CHAR_LIMIT);
        this.name = verifyNonNullWithinChars(name, 1, NAME_CHAR_LIMIT);
        this.description = verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    // Generate a unique ID:
    public static String generateId() {
        long nextId = idCounter;
        String id = String.valueOf(idCounter);
        idCounter++;
        return id;
    }

    // reset id counter - for testing only
    public static void resetCounter() {
        setCounter(0L);
    }

    // modify specific counter - meant for testing only
    public static void setCounter(Long value) {
        idCounter = value;
    }

    /**
     * Verifies and returns a string if it is a valid format, throws an exception if it isn't
     * @param str string to verify
     * @param minCharNum minimum allowed number of characters (inclusive)
     * @param maxCharNum maximum allowed number of characters (inclusive)
     * @return the original str string
     * @throws IllegalArgumentException if str is null, contains leading or trailing whitespace, or not within
     * allowed number of chars (inclusive)
     */
    protected String verifyNonNullWithinChars(String str, int minCharNum, int maxCharNum) {
        // CHECK EDGE CASES
        if (str == null) {
            throw new IllegalArgumentException("Invalid string, must be non-null value.");
        }
        // no leading/trailing whitespace
        String trueStr = str.strip();
        // if str and str.strip() lengths are different, contains invalid leading/trailing characters
        if (str.length() != str.strip().length()) { // (strip() removes leading/trailing whitespace
            throw new IllegalArgumentException("Invalid string, be sure to remove leading or trailing spaces.");
        }
        // if str has too little or too many characters, throw exception
        if (trueStr.length() > maxCharNum || trueStr.length() < minCharNum) {
            throw new IllegalArgumentException("Invalid string, " + str + ", must be within" + minCharNum + "-" + maxCharNum + " characters.");
        }
        // Return data if no exceptions thrown
        return str;
    }

    // SET TASK FIELDS
    /**
     * Sets specified field name to new specified value. Allowed field names are
     * "name", and "description".
     * @param fieldName field name to modify.
     * @throws IllegalArgumentException if parameter value is invalid
     */
    protected void updateField(String fieldName, String value) {
        switch (fieldName.toLowerCase()) {
            case "name" -> setTaskName(value);
            case "description" -> setTaskDescription(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }

    /**
     * Updates task name
     * @param taskName new name (non-null, <= 20 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setTaskName(String taskName) {
        this.name = verifyNonNullWithinChars(taskName, 1, NAME_CHAR_LIMIT);
    }

    /**
     * Updates task description
     * @param description new task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setTaskDescription(String description) {
        this.description = verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
