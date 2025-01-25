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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentService {

    /**
     * Container for set of appointments, maps appointment id to appointment object.
     */
    private final Map<String, Appointment> appointmentMap = new HashMap<>();

    /**
     * Return an appointment of type T from the stored map
     * @param id the unique id used to identify appointment in map
     * @return appointment associated with given id key from map
     * @throws IllegalArgumentException if appointment with specified id can't be found
     */
    public Appointment getAppointmentById(String id) {
        Appointment appointment = appointmentMap.get(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Object with ID [" + id + "] does not exist");
        }
        return appointment;
    }

    /**
     * Removes object of type T with given id from contacts map
     * @param object object to be removed from list
     * @throws IllegalArgumentException if contact does not exist
     * @return object that was removed
     */
    public Appointment delete(Appointment object) {
        return appointmentMap.remove(object.getId());

    }

    /**
     * Removes object of type T with given id from service map
     * @param id identifier of object to be removed from service map
     * @throws IllegalArgumentException if object does not exist
     * @return object of type T that was removed
     */
    public Appointment delete(String id) {
        return delete(getAppointmentById(id));
    }

    /**
     * Adds an object to the service storage, mapped to its id.
     * @param object object to add to service.
     */
    protected void add(Appointment object) {
        appointmentMap.put(object.getId(), object);
    }

    /**
     * Adds an appointment object mapped to its unique id in storage.
     * @param date - Scheduled date of the appointment (Must not be non-null, in the past)
     * @param description - Description of the appointment (Must be non-null, no more than 50 characters)
     * @throws IllegalArgumentException in Appointment object if parameters are invalid format
     */
    public void add(Date date, String description) {
        Appointment appointment = new Appointment(date, description); // create object with specified parameters
        add(appointment); // super method. Adds to appointmentMap HashMap using unique ID
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

    // super class contains method for delete appointments per ID

    // UPDATE APPOINTMENT FIELDS
    /**
     * Updates date of appointment with given id to given future date (Date class)
     * @param id Unique identifier of the appointment to update
     * @param date new appointment date (non-null, must be after current system time). Chance of exception if date is
     *             set to actual system time, such as with 'new Date()', use updateDate(String id) instead for setting
     *             to current time.
     * @throws IllegalArgumentException if appointment does not exist or date is invalid.
     */
    public void updateDate(String id, Date date) {
        if (date == null) { // to avoid NullPointerException
            updateAppointmentField(id, "date", null); // purposefully throws IllegalArgumentException
        } else {
            updateAppointmentField(id, "date", String.valueOf(date.getTime()));
        }
    }

    /**
     * Updates date of appointment with given id to current system time
     * @param id Unique identifier of the appointment to update
     * @throws IllegalArgumentException if appointment does not exist or date is invalid
     */
    public Date updateDate(String id) {
        Date current = new Date();
        // sets date to current system time and specifies it can't be before this time
        updateAppointmentField(id, "date-now", null);
        return current; // returns current (helpful in testing)
    }

    /**
     * Updates name of appointment with given id to firstName
     * @param id Unique identifier of the appointment to update
     * @param description new appointment description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if appointment does not exist or date is invalid
     */
    public void updateDescription(String id, String description) {
        updateAppointmentField(id, "description", description);
    }

    /**
     * Update specified string field implemented in updatedField method implemented from Appointment
     * @param id Unique identifier of the object to delete
     * @param value new value to change specified field to
     * @throws IllegalArgumentException if object does not exist or field string is invalid
     */
    public void updateAppointmentField(String id, String fieldName, String value) {
        Appointment appointment = getAppointmentById(id); // throws exception if appointment not found
        appointment.updateField(fieldName, value); // throws exception if fieldname or value invalid
    }
}
