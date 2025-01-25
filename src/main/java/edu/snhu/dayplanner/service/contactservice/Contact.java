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

public class Contact {
    private final String id;
    private static long idCounter = 0;
    private String firstName;   // required, up to 10 chars
    private String lastName;    // required, up to 10 chars
    private String phone;       // required, up to 10 digits
    private String address;     // required, up to 30 chars

    // maximum allowed number of characters for each field
    private final int ID_CHAR_LIMIT = 10;
    private final int NAME_CHAR_LIMIT = 10;
    private final int PHONE_CHAR_LIMIT = 10;
    private final int ADDRESS_CHAR_LIMIT = 30;

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
     * Sets specified field name to new specified value. Allowed field names are
     * "firstname", "lastname", "phone", and "address".
     * @param fieldName field to modify.
     * @throws IllegalArgumentException if parameter value is invalid
     */
    protected void updateField(String fieldName, String value) {
        switch (fieldName.toLowerCase()) {
            case "firstname" -> setFirstName(value);
            case "lastname" -> setLastName(value);
            case "phone" -> setPhoneNumber(value);
            case "address" -> setAddress(value);
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
}
