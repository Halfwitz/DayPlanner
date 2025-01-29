package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import edu.snhu.dayplanner.service.appointmentservice.AppointmentService;
import edu.snhu.dayplanner.ui.AppointmentView;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Manages the logic for the Appointment section of the Day Planner Application. This handles user interactions,
 * coordinating between {@code AppointmentService} and {@code AppointmentView}, and provides listener methods for handling
 * adding, removing, and updating appointment entries depending on which GUI elements are used.
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class AppointmentController
{
    private final AppointmentService appointments;
    private final AppointmentView appointmentView;

    private static final String CSV_FILE_PATH = "data/appointments.csv";

    /**
     * Initializes an {@code AppointmentController} instance.
     * Loads initial appointment data from a CSV file, initializes the {@code AppointmentView},
     * and sets up event handlers for adding, editing, and removing appointments.
     */
    public AppointmentController() {
        appointments = new AppointmentService();
        appointments.addFromFile(CSV_FILE_PATH);

        appointmentView = new AppointmentView(this::handleRemoveAppointment, this::handleEditAppointment);
        appointmentView.getAddButton().setOnAction(event -> handleAddAppointment());
        appointmentView.getSaveButton().setOnAction(event -> handleSaveAppointments());
    }

    /**
     * Handles adding a new appointment to the system.
     * Validates user inputs, creates a new appointment, adds it to the service and table view,
     * and clears input fields. If the input is invalid, logs the error stack trace.
     * <p>
     * TODO: Implement UI feedback for invalid input.
     */
    private void handleAddAppointment() {
        try {
            // returns input in order of the fields {description, date string}
            List<String> input = appointmentView.getDataTable().getNewEntryInput();
            System.out.println(input);

            // Parse the date and create a new appointment (throws IllegalArgumentException for invalid input
            LocalDateTime date = LocalDateTime.parse(input.get(0));

            // add appointment to appointments using input strings,
            Appointment newAppointment = appointments.add(date, input.get(1));

            // add new row to table and clear input forms.
            appointmentView.getDataTable().clearNewEntryInput();
            appointmentView.getDataTable().createDataRow(newAppointment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Logs the error
        }
    }

    /**
     * Handles removing an appointment from the system.
     * Deletes the appointment from the {@code AppointmentService} and removes the corresponding row in the table view.
     *
     * @param appointment the appointment to remove
     * @param tableRow    the corresponding table row node to remove from view
     *
     * FIXME: Handle cases where the appointment might not exist in the service.
     */
    private void handleRemoveAppointment(Appointment appointment, Node tableRow) {
        appointments.delete(appointment);
        appointmentView.getDataTable().removeRow(tableRow);
    }

    /**
     * Handles editing an appointment's field.
     * Updates the specified field of an appointment in the {@code AppointmentService} based on user input.
     * Logs any invalid input errors.
     *
     * @param appointment the appointment to modify
     * @param field       the field to update (Valid fields are DATE and DESCRIPTION)
     * @param newValue    the new value for the field
     *
     * FIXME: Provide user feedback for invalid input.
     */
    private void handleEditAppointment(Appointment appointment, Appointment.Field field, String newValue) {
        System.out.println(newValue);
        try {
            appointments.updateField(appointment.getId(), field, newValue.trim());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getClass() + e.getMessage());
        }
    }

    /**
     * Saves all appointments to the CSV file.
     * Called when the user clicks the save button in the AppointmentView.
     */
    private void handleSaveAppointments() {
        appointments.writeToFile(CSV_FILE_PATH);
    }

    /**
     * Refreshes the appointment table view and returns the root node containing the Appointment UI elements.
     * Used by the {@code NavController} to display the Appointment screen.
     *
     * @return the root node containing the Appointment view elements
     */
    public Parent getView() {
        appointmentView.getDataTable().updateTable(appointments.getAll());
        return appointmentView.getView();
    }
}