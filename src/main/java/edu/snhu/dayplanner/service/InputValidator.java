package edu.snhu.dayplanner.service;

import java.time.LocalDateTime;
// TODO: javadocs
public class InputValidator
{
    /**
     * Verifies and returns a string if it is a valid format, throws an exception if it isn't
     * @param str string to verify
     * @param minCharNum minimum allowed number of characters (inclusive)
     * @param maxCharNum maximum allowed number of characters (inclusive)
     * @return the original str string
     * @throws IllegalArgumentException if str is null, contains leading or trailing whitespace, or not within
     * allowed number of chars (inclusive)
     */
    public static String verifyNonNullWithinChars(String str, int minCharNum, int maxCharNum) {
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

    /**
     * Verifies and returns a string if it is a valid format, throws an exception if it isn't
     * @param str string to verify
     * @param maxCharNum maximum allowed number of characters (inclusive)
     * @return the original str string
     * @throws IllegalArgumentException if str is null, has leading/trailing whitespace, or too many chars.
     */
    protected static String verifyNonNullWithinChars(String str, int maxCharNum) {
        return verifyNonNullWithinChars(str, 0, maxCharNum);
    }

    /**
     * Verify the given date is not before current system time, throws exception if it is.
     * @param date The date to verify is not before current system time
     * @return the original date if no exception.
     * @throws IllegalArgumentException if date is null or before current system time.
     */
    public static LocalDateTime verifyDateNotInPast(LocalDateTime date) {
        return verifyDateNotBeforeOther(date, LocalDateTime.now());
    }

    /**
     * Verify the given date does not come before other date, throws exception if it is.
     * @param date The date to verify is not null or before other date
     * @return the original date if no exception thrown
     * @throws IllegalArgumentException if date is null or before other time.
     */
    public static LocalDateTime verifyDateNotBeforeOther(LocalDateTime date, LocalDateTime other) {
        if (date == null) { // edge case: throw exception if date is null
            throw new IllegalArgumentException("Illegal date. Date must not be null.");
        } else if (date.isBefore(other)) { // throw exception if date is before current time
            throw new IllegalArgumentException("Illegal date (" + date + "): Date must be not be before date (" + other + ")." );
        }

        // return original date if no exceptions thrown
        return date;
    }
}
