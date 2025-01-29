/******************************************************************************
 * [Appointment.java]
 * Author: Michael Lorenz
 * - Southern New Hampshire University
 ****************************************************************************/
package edu.snhu.dayplanner.service.appointmentservice;
import edu.snhu.dayplanner.service.CsvSerializable;
import edu.snhu.dayplanner.service.Entity;
import edu.snhu.dayplanner.service.InputValidator;
import java.time.LocalDateTime;

/**
 * This class represents an appointment object with fields for date and description.
 *  Parent class gives this a unique id when initialized.
 * <ul>It enforces constraints:
 * <li>description must be non-null and <= 50 chars</li>
 * <li>date must not be before time of initialization</li>
 * </ul>
 * This class provides methods for field updates, validation, and CSV serialization/deserialization.
 * @author Michael Lorenz
 * @version 1.0
 */
public class Appointment extends Entity<Appointment.Field> implements CsvSerializable<Appointment> {
    private LocalDateTime appointmentDate; // required, must not be in past
    private String description; // required, up to 50 chars

    private static final int DESC_CHAR_LIMIT = 50;

    // fields of an appointment, used by updateField method to indicate which field to modify
    public enum Field {
        DATE,
        CURRENT_DATE,
        DESCRIPTION
    }

    /**
     * Initializes a new {@code Appointment} with a specific date and description.
     *
     * @param date        The scheduled date and time for the appointment (non-null, must not be in the past).
     * @param description The description of the appointment (non-null, up to 50 characters).
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Appointment(LocalDateTime date, String description) {
        super(); // generate unique id
        this.appointmentDate = InputValidator.verifyDateNotInPast(date);
        this.description = InputValidator.verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    /**
     * Initializes a new {@code Appointment} with the current date and time as the appointment date.
     *
     * @param description The description of the appointment (non-null, up to 50 characters).
     * @throws IllegalArgumentException if the description is invalid.
     */
    public Appointment(String description) {
        super();
        this.description = InputValidator.verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
        setAppointmentDate(); // sets date to current time

    }

    /**
     * Updates the specified field of the appointment.
     *
     * @param field The field to update. Supported values:
     *              <ul>
     *              <li>{@code CURRENT_DATE} - Updates the date to the current time (value is ignored).</li>
     *              <li>{@code DATE} - Updates the date to the provided value (non-null, valid date string).</li>
     *              <li>{@code DESCRIPTION} - Updates the description to the provided value (non-null, up to 50 characters).</li>
     *              </ul>
     * @param value The new value for the field. Must satisfy the field's constraints.
     * @throws IllegalArgumentException if the field or value is invalid.
     */
    protected void updateField(Field field, String value) {
        switch (field) {
            case DATE -> setAppointmentDate(value != null ? LocalDateTime.parse(value) : null);
            case CURRENT_DATE -> setAppointmentDate();
            case DESCRIPTION -> setAppointmentDescription(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }

    /**
     * Set's an appointment date to the new date. Verifies that date is not before the current system time.
     * @param date new date to set
     * @throws IllegalArgumentException if date is invalid or comes before current system time
     */
    private void setAppointmentDate(LocalDateTime date) {
        appointmentDate = InputValidator.verifyDateNotInPast(date);
    }

    /**
     * Set's an appointment date to the current system time. Verifies that date is not before current date.
     * @throws IllegalArgumentException if date is invalid or comes before minDate
     */
    private void setAppointmentDate() {
        LocalDateTime current = LocalDateTime.now();
        appointmentDate = InputValidator.verifyDateNotBeforeOther(current, current);
    }

    /**
     * Sets the description of the appointment
     * @param description the new description
     * @throws IllegalArgumentException if description is invalid (null or not within 1-50 characters)
     */
    private void setAppointmentDescription(String description) {
        this.description = InputValidator.verifyNonNullWithinChars(description, 1, DESC_CHAR_LIMIT);
    }

    /**
     * Converts this appointment into a CSV-formatted string.
     *
     * @param delimiter The character used to separate fields in the CSV string.
     * @return A CSV-formatted string representing the appointment.
     */
    @Override
    public String toCsv(char delimiter) {
        return (getDate().toString() + delimiter +
                getDescription().replace(String.valueOf(delimiter), ""));
    }

    /**
     * Creates a new {@code Appointment} from a CSV-formatted string.
     *
     * @param csv       The CSV string containing the appointment data.
     * @param delimiter The character used to separate fields in the CSV string.
     * @return A new {@code Appointment} created from the CSV data.
     * @throws IllegalArgumentException if the CSV data is invalid.
     */
    @Override
    public Appointment fromCsv(String csv, char delimiter) {
        String[] parts = csv.split(String.valueOf("\\"+delimiter));
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CSV format");
        }

        return new Appointment(LocalDateTime.parse(parts[0]), parts[1]);
    }

    // GETTERS
    /**
     * Returns the value of the specified field as a string.
     *
     * @param field The field to retrieve. (Valid fields are DATE and DESCRIPTION)
     * @return The value of the field.
     * @throws IllegalArgumentException if the field is unknown.
     */
    @Override
    public String getFieldValue(Field field) {
        switch (field) {
            case DATE -> {
                return getDate().toString();
            } case DESCRIPTION -> {
                return getDescription();
            } default -> throw new IllegalArgumentException("Unknown field");
        }
    }
    public LocalDateTime getDate() {
        return appointmentDate;
    }
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return appointmentDate + ": " + description;
    }
}
