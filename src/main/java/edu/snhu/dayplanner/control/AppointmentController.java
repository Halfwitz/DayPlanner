package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import edu.snhu.dayplanner.service.appointmentservice.AppointmentService;
import edu.snhu.dayplanner.service.taskservice.Task;
import edu.snhu.dayplanner.ui.AppointmentView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

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
public class AppointmentController extends EntityController<Appointment, Appointment.Field, AppointmentView> {
    private static final String CSV_FILE_PATH = "data/appointments.csv";

    /**
     * Initializes an {@code AppointmentController} instance.
     * Loads initial appointment data from a CSV file, initializes the {@code AppointmentView},
     * and sets up event handlers for adding, editing, and removing appointments.
     */
    public AppointmentController() {
        super(new AppointmentService(), CSV_FILE_PATH, AppointmentView::new);
    }

    /**
     * Uses the supplied input to create a new entity of type T. Each entry in input
     * is retrieved in order from the fields of {@code EntityView}'s data table new entry field.
     *
     * @param input the list of input that should be values for entity's fields
     * @return The entity created using input arguments
     */
    @Override
    public Appointment createEntityFromData(List<String> input) {
        if (input.size() < 2 || input.contains("")) {
            throw new IllegalArgumentException("Not enough fields to add an appointment");
        }

        LocalDateTime date = LocalDateTime.parse(input.get(0));
        String description = input.get(1);
        return new Appointment(date, description);
    }

    /**
     * Attempts to extract the text input from the given {@code Node}.
     * Returns the TextField or DateTimePicker input string if possible, else returns "";
     * @param inputNode node containing input to be extracted (TextField or DateTimePicker only)
     * @return The string extracted from the inputNode, or "" if nothing could be extracted.
     */
    @Override
    public String getInputFrom(Node inputNode) {
        if (inputNode instanceof DateTimePicker) {
            return ((DateTimePicker) inputNode).getDateTimeValue().toString();
        }
        return super.getInputFrom(inputNode);
    }
}