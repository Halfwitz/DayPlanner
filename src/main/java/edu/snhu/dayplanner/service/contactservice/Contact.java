/******************************************************************************
 * [Contact.java]
 * Author: Michael Lorenz
 * - Southern New Hampshire University
 *****************************************************************************/
package edu.snhu.dayplanner.service.contactservice;

import edu.snhu.dayplanner.service.CsvSerializable;
import edu.snhu.dayplanner.service.Entity;
import edu.snhu.dayplanner.service.InputValidator;

/**
 * This class represents a contact object with fields for first name,
 * last name, phone number, and address.
 * <p>>
 * Parent class gives this unique id when initialized.</p>
 * <ul>It enforces constraints:
 * <li>firstName, lastName must be non-null and <= 10 chars</li>
 * <li>phone must be 10 chars and non-null</li>
 * <li>address must be <= 30 chars and non-null</li>
 * </ul>
 * @author Michael Lorenz
 * @version 1.0
 */
public class Contact extends Entity<Contact.Field> implements CsvSerializable<Contact> {
    private String firstName;   // required, up to 10 chars
    private String lastName;    // required, up to 10 chars
    private String phone;       // required, up to 10 digits
    private String address;     // required, up to 30 chars

    // maximum allowed number of characters for each field
    private static final int NAME_CHAR_LIMIT = 10;
    private static final int PHONE_CHAR_LIMIT = 10;
    private static final int ADDRESS_CHAR_LIMIT = 30;

    public enum Field {
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER,
        ADDRESS
    }

    /**
     * Initializes a new Contact object with a unique ID.
     * @param firstName first name (non-null, <= 10 chars)
     * @param lastName last name (non-null, <= 10 chars)
     * @param phone phone number (exactly 10 chars)
     * @param address address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Contact(String firstName, String lastName, String phone, String address) {
        // initialize with unique id, check null and length requirements & throw exceptions
        super(); // generate unique id
        this.firstName = InputValidator.verifyNonNullWithinChars(firstName, 1, NAME_CHAR_LIMIT);
        this.lastName = InputValidator.verifyNonNullWithinChars(lastName, 1, NAME_CHAR_LIMIT);
        this.phone = InputValidator.verifyNonNullWithinChars(phone, PHONE_CHAR_LIMIT, PHONE_CHAR_LIMIT);
        this.address = InputValidator.verifyNonNullWithinChars(address, 1, ADDRESS_CHAR_LIMIT);
    }

    // SET CONTACT FIELDS
    /**
     * Sets specified field name. Use contents from inner Field enum.
     * @param field field to modify.
     *
     * @throws IllegalArgumentException if parameter value is invalid
     */
    protected void updateField(Field field, String value) {
        switch (field) {
            case FIRST_NAME -> setFirstName(value);
            case LAST_NAME -> setLastName(value);
            case PHONE_NUMBER -> setPhoneNumber(value);
            case ADDRESS -> setAddress(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }

    /**
     * Updates firstName if provided is valid
     * @param firstName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setFirstName(String firstName) {
        this.firstName = InputValidator.verifyNonNullWithinChars(firstName, 1, NAME_CHAR_LIMIT);
    }
    /**
     * Updates lastName if provided is valid
     * @param lastName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private  void setLastName(String lastName) {
        this.lastName = InputValidator.verifyNonNullWithinChars(lastName, 1, NAME_CHAR_LIMIT);
    }
    /**
     * Updates phone if provided is valid
     * @param phoneNumber new phone number (non-null, exactly 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setPhoneNumber(String phoneNumber) {
        this.phone = InputValidator.verifyNonNullWithinChars(phoneNumber, PHONE_CHAR_LIMIT, PHONE_CHAR_LIMIT);
    }
    /**
     * Updates address if provided is valid
     * @param address new address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setAddress(String address) {
        this.address = InputValidator.verifyNonNullWithinChars(address, 1, ADDRESS_CHAR_LIMIT);
    }

    @Override
    public String toString() {
        return firstName + ", " + lastName + ", " + phone + ", " + address;
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
            case FIRST_NAME -> {
                return getFirstName();
            } case LAST_NAME -> {
                return getLastName();
            } case PHONE_NUMBER -> {
                return getPhone();
            } case ADDRESS -> {
                return getAddress();
            } default -> throw new IllegalArgumentException("Unknown field");
        }
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
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
        return (
                getFirstName().replace(String.valueOf(delimiter), "") + delimiter +
                        getLastName().replace(String.valueOf(delimiter), "") + delimiter +
                        getPhone().replace(String.valueOf(delimiter), "") + delimiter +
                        getAddress().replace(String.valueOf(delimiter), "")
        );
    }

    /**
     * Converts the given csv string to a new Contact object. splits csv by the delimiter
     * @param csv A single csv line with each attribute separated by a delimiter. Should contain 4 values for contact
     *            first name, last name, phone number, and address fields. Ensure CSV values do not contain the
     *            delimiter within themselves.
     * @param delimiter The character used to split the csv string.
     * @return a new contact object reference created from the delimiter values
     * @throws IllegalArgumentException if contact fields are not valid format
     * @see #Contact(String, String, String, String) valid field format descriptions
     */
    @Override
    public Contact fromCsv(String csv, char delimiter) {
        String[] parts = csv.split("\\"+delimiter);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid CSV format");
        }

        return new Contact(
                parts[0],
                parts[1],
                parts[2],
                parts[3]
        );
    }
}
