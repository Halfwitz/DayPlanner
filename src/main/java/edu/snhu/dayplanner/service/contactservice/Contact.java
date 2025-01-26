/******************************************************************************
 * Module Three Milestone
 * [Contact.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class represents a contact object with fields for first name,
 * last name, phone number, and address. Parent class gives this unique id
 * when initialized. It enforces constraints:
 * - firstName, lastName must be non-null and <= 10 chars
 * - phone must be 10 chars and non-null
 * - address must be <= 30 chars and non-null
 *
 * Date: Due 9/22/2024
 * Modified: 9/29/2024 to extend Entity
 * Modified: 10/09/2024 to remove outer package dependencies
 * Modified: 10/11/2024 to merge with superclass
 *****************************************************************************/
package edu.snhu.dayplanner.service.contactservice;

import edu.snhu.dayplanner.service.CsvSerializable;
import edu.snhu.dayplanner.service.Entity;

public class Contact extends Entity implements CsvSerializable<Contact> {

    private final String id;
    private static long idCounter = 0;
    private String firstName;   // required, up to 10 chars
    private String lastName;    // required, up to 10 chars
    private String phone;       // required, up to 10 digits
    private String address;     // required, up to 30 chars

    // maximum allowed number of characters for each field
    private static final int ID_CHAR_LIMIT = 10;
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
     * Initializes a new Contact object
     * @param firstName first name (non-null, <= 10 chars)
     * @param lastName last name (non-null, <= 10 chars)
     * @param phone phone number (exactly 10 chars)
     * @param address address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Contact(String firstName, String lastName, String phone, String address) {
        // initialize with unique id, check null and length requirements & throw exceptions
        this.id = verifyNonNullWithinChars(generateId(), 1, ID_CHAR_LIMIT);
        this.firstName = verifyNonNullWithinChars(firstName, 1, NAME_CHAR_LIMIT);
        this.lastName = verifyNonNullWithinChars(lastName, 1, NAME_CHAR_LIMIT);
        this.phone = verifyNonNullWithinChars(phone, PHONE_CHAR_LIMIT, PHONE_CHAR_LIMIT);
        this.address = verifyNonNullWithinChars(address, 1, ADDRESS_CHAR_LIMIT);
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
            throw new IllegalArgumentException("Invalid input, must be non-null value.");
        }
        // no leading/trailing whitespace
        String trueStr = str.strip();
        // if str and str.strip() lengths are different, contains invalid leading/trailing characters
        if (str.length() != str.strip().length()) { // (strip() removes leading/trailing whitespace
            throw new IllegalArgumentException("Invalid input, be sure to remove leading or trailing spaces.");
        }
        // if str has too little or too many characters, throw exception
        if (trueStr.length() > maxCharNum || trueStr.length() < minCharNum) {
            throw new IllegalArgumentException("Invalid input, " + str + ", must be within " + minCharNum + "-" + maxCharNum + " characters.");
        }
        // Return data if no exceptions thrown
        return str;
    }

    // SET CONTACT FIELDS
    /**
     * Updates firstName if provided is valid
     * @param firstName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setFirstName(String firstName) {
        this.firstName = verifyNonNullWithinChars(firstName, 1, NAME_CHAR_LIMIT);
    }
    /**
     * Updates lastName if provided is valid
     * @param lastName new first name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private  void setLastName(String lastName) {
        this.lastName = verifyNonNullWithinChars(lastName, 1, NAME_CHAR_LIMIT);
    }
    /**
     * Updates phone if provided is valid
     * @param phoneNumber new phone number (non-null, exactly 10 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setPhoneNumber(String phoneNumber) {
        this.phone = verifyNonNullWithinChars(phoneNumber, PHONE_CHAR_LIMIT, PHONE_CHAR_LIMIT);
    }
    /**
     * Updates address if provided is valid
     * @param address new address (non-null, <= 30 chars)
     * @throws IllegalArgumentException if parameter is invalid
     */
    private void setAddress(String address) {
        this.address = verifyNonNullWithinChars(address, 1, ADDRESS_CHAR_LIMIT);
    }

    /**
     * Sets specified field name. Use contants from inner Field enum.
     * @param field field to modify.
     *
     * @throws IllegalArgumentException if parameter value is invalid
     */
    protected void updateField(Contact.Field field, String value) {
        switch (field) {
            case FIRST_NAME -> setFirstName(value);
            case LAST_NAME -> setLastName(value);
            case PHONE_NUMBER -> setPhoneNumber(value);
            case ADDRESS -> setAddress(value);
            default -> throw new IllegalArgumentException("Unknown field name");
        }
    }

    @Override
    public String toString() {
        return firstName + ", " + lastName + ", " + phone + ", " + address;
    }

    // GETTERS
    public String getId() {
        return id;
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
                        getLastName().replace(String.valueOf(delimiter), "")
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
        // TODO: add logging for failed fromCSV and parts format validation (check parts.length)
        String[] parts = csv.split(String.valueOf("\\"+delimiter));
        for (String part : parts) {
            System.out.println(part);
        }
        return new Contact(
                parts[0],
                parts[1],
                parts[2],
                parts[3]
        );
    }
}
