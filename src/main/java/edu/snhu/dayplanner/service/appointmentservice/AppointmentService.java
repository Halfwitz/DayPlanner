/******************************************************************************
 * Module Five Milestone
 * [AppointmentService.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 *
 * Description:
 * This class supports storing appointments. Allows functionalities to:
 * - add appointments with a unique ID, date, and description
 * - delete appointments per appointment ID
 *
 * Date: Due 10/06/2024
 *****************************************************************************/
package edu.snhu.dayplanner.service.appointmentservice;

import edu.snhu.dayplanner.service.Service;
import edu.snhu.dayplanner.service.ServiceFileUtility;

import java.time.LocalDateTime;

public class AppointmentService extends Service<Appointment, Appointment.Field>
{
    /**
     * Adds an appointment object mapped to its unique id in storage.
     * @param date - Scheduled date of the appointment (Must not be non-null, in the past)
     * @param description - Description of the appointment (Must be non-null, no more than 50 characters)
     * @throws IllegalArgumentException in Appointment object if parameters are invalid format
     */
    public Appointment add(LocalDateTime date, String description) {
        return add(new Appointment(date, description)); // super method. Adds to appointmentMap HashMap using unique ID
    }

    /**
     * Adds an appointment object mapped to its unique id in storage.
     * appointment date is set to the current system time
     * @param description - Description of the appointment (Must be non-null, no more than 50 characters)
     * @throws IllegalArgumentException in Appointment object if parameters are invalid format
     */
    public void add(String description) {
        Appointment appointment = new Appointment(description); // create object with specified parameters
        add(appointment); // super method. Adds to appointmentMap HashMap using unique ID
    }

    /**
     * Reads stored CSV contents from a file to this storage object.
     * Uses {@code ServiceFileUtility} to read a specified CSV file then convert each line to
     * a appointment and return a list of those appointments. Then, the list is added to this object.
     * Should convert contents to the object and add with {@code add} or {@code addAll}
     * Passes a prototype appointment object to use for creation of new objects with (fromCsv)
     * @param filePath the file to be read from into this object
     */
    @Override
    public void addFromFile(String filePath) {
        ServiceFileUtility<Appointment> fileUtil = new ServiceFileUtility<>(filePath,
                new Appointment("p"));
        addAll(fileUtil.readFromFile());
    }

    /**
     * Writes stored objects to a CSV file stored in filePath.
     * Uses {@code ServiceFileUtility} to convert Appointment objects into CSV format and writes
     * to the file.
     *
     * @param filePath the file to be written into using contents of this object
     */
    @Override
    public void writeToFile(String filePath) {
        ServiceFileUtility<Appointment> fileUtil = new ServiceFileUtility<>(filePath,
                new Appointment("p"));
        fileUtil.writeToFile(getAll());
    }

    // UPDATE APPOINTMENT FIELDS
    /**
     * Updates date of appointment with given id to given future date (Date class)
     * @param id Unique identifier of the appointment to update
     * @param date new appointment date (non-null, must be after current system time). Chance of exception if date is
     *             set to actual system time, such as with 'new Date()', use updateDate(String id) instead for setting
     *             to current time.
     * @throws IllegalArgumentException if appointment does not exist or date is invalid.
     */
    public void updateDate(String id, LocalDateTime date) {
        if (date == null) { // to avoid NullPointerException
            this.updateField(id, Appointment.Field.DATE, null); // purposefully throws IllegalArgumentException
        } else {
            this.updateField(id, Appointment.Field.DATE, date.toString());
        }
    }

    /**
     * Updates date of appointment with given id to current system time
     * @param id Unique identifier of the appointment to update
     * @throws IllegalArgumentException if appointment does not exist or date is invalid
     */
    public LocalDateTime updateDate(String id) {
        LocalDateTime current = LocalDateTime.now();

        // sets date to current system time and specifies it can't be before this time
        updateField(id, Appointment.Field.CURRENT_DATE, null);
        return current; // returns current (helpful in testing)
    }

    /**
     * Updates name of appointment with given id to firstName
     * @param id Unique identifier of the appointment to update
     * @param description new appointment description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if appointment does not exist or date is invalid
     */
    public void updateDescription(String id, String description) {
        updateField(id, Appointment.Field.DESCRIPTION, description);
    }
}
