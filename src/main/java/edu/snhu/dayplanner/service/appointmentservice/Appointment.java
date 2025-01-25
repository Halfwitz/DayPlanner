/******************************************************************************
 * <h1>Module Five Milestone</h1>
 * [Appointment.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class represents an appointment object with fields for date and description.
 * Parent class gives this a unique id when initialized. It enforces constraints:
 * - date must be required, non-null, and not in the past
 * - description must be required, non-null and <= 50 characters
 * Date: Due 10/06/2024
 *****************************************************************************/
package edu.snhu.dayplanner.service.appointmentservice;
import java.util.Date;

public class Appointment {
    private final String id;
    private static Long idCounter = 0L;
    private Date appointmentDate;
    private String description;

    private final int ID_CHAR_LIMIT = 10;
    private final int DESC_CHAR_LIMIT = 50;

    // create Appointment with unique id and verify all parameters
    public Appointment(Date date, String description) {
        this.id = verifyNonNullWithinChars(generateId(), 1, ID_CHAR_LIMIT);
        this.appointmentDate = verifyDateNotInPast(date);
        this.description = verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    // create Appointment for current system time with description and verify all parameters
    public Appointment(String description) {
        this.id = verifyNonNullWithinChars(generateId(), 1, ID_CHAR_LIMIT);
        this.description = verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
        setAppointmentDate(); // sets date to current time

    }

    // Generate a unique ID:
    private static String generateId() {
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
     * Set's an appointment date to the new date. Verifies that date is not before the current system time.
     * @param date new date to set
     * @throws IllegalArgumentException if date is invalid or comes before current system time
     */
    private void setAppointmentDate(Date date) {
        appointmentDate = verifyDateNotInPast(date);
    }

    /**
     * Set's an appointment date to the current system time. Verifies that date is not before current date.
     * @throws IllegalArgumentException if date is invalid or comes before minDate
     */
    private void setAppointmentDate() {
        Date current = new Date();
        appointmentDate = verifyDateNotBeforeOther(current, current);
    }

    private void setAppointmentDescription(String description) {
        this.description = verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    /**
     * Used publicly to update various fields depending on fieldName
     * @param fieldName the name of the field to update.
     *                  <p>case "date" -> updates appointmentDate. Value should be string representation of a
     *                                  Date object's time. (String.valueOf(Date object.getTime()))  non-null.</p>
     *                  <p>case "description" -> updates description field to specified value field.
     *                                            Value Should be non-null and <= 50 chars.</p>
     * @param value the new value to set for the specified field.
     * @throws IllegalArgumentException if fieldName does not match expected strings
     */
    public void updateField(String fieldName, String value) {
        switch (fieldName.toLowerCase()) {
            case "date" -> setAppointmentDate(new Date(Long.parseLong(value)));
            case "date-now" -> setAppointmentDate();
            case "description" -> setAppointmentDescription(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }

    /**
     * Verify the given date is not before current system time, throws exception if it is.
     * @param date The date to verify is not before current system time
     * @return the original date if no exception.
     * @throws IllegalArgumentException if date is null or before current system time.
     */
    private Date verifyDateNotInPast(Date date) {
        return verifyDateNotBeforeOther(date, new Date());
    }

    /**
     * Verify the given date does not come before other date, throws exception if it is.
     * @param date The date to verify is not null or before other date
     * @return the original date if no exception thrown
     * @throws IllegalArgumentException if date is null or before other time.
     */
    private Date verifyDateNotBeforeOther(Date date, Date other) {
        if (date == null) { // edge case: throw exception if date is null
            throw new IllegalArgumentException("Illegal date. Date must not be null.");
        } else if (date.before(other)) { // throw exception if date is before current time
            throw new IllegalArgumentException("Illegal date-(" + date.getTime() + "ms.) Date must be not be before date-(" + other.getTime() + "ms.)" );
        }

        // return original date if no exceptions thrown
        return date;
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

    // GETTERS
    public Date getDate() {
        return appointmentDate;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
