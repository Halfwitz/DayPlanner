package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents the Appointment section user interface for managing appointments in the Day Planner Application. This class
 * renders a view with a screen title, a labelled table of appointments with edit and remove controls, and input fields
 * for adding new appointments.
 * <p>
 * This class defines the layout and each elements style using JavaFX components, and can dynamically update the
 * appointment table using provided data. Logic is handled by the {@code AppointmentController} class. </p>
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class AppointmentView
{
    private final VBox root;
    private final AppointmentTableView dataTable;
    private final VBox tableView;
    private final Button addButton;
    private final Button saveButton;

    private final BiConsumer<Appointment, Node> onRemove;
    private final TriConsumer<Appointment, Appointment.Field, String> onEdit;

    /**
     * Initializes this object to set up the layout with each element as a child of the {@code root} Vbox. Responsible
     * for creating the screen header, and empty appointment table view with elements for a row of column headers, table
     * data, and a row for adding new appointments to the table data.
     * @param onRemove the method implementation that handles logic for when a appointment is removed from the table.
     * @param onEdit the method implementation that handles logic for when a appointment field is edited in the table.
     *               Should use Appointment as reference to edited field, and Appointment.Field as the field that is being
     *               modified, and String as the new value.
     *
     */
    public AppointmentView(BiConsumer<Appointment, Node> onRemove, TriConsumer<Appointment, Appointment.Field, String> onEdit) {
        // set event listeners
        this.onRemove = onRemove;
        this.onEdit = onEdit;

        root = new VBox();
        // create view heading
        Label header = new Label("APPOINTMENTS");

        // initialize components and data table with columns for each field in Appointment.Field
        List<Appointment.Field> fields = new ArrayList<>();
        fields.add(Appointment.Field.DATE);
        fields.add(Appointment.Field.DESCRIPTION);
        dataTable =  new AppointmentTableView(fields, onRemove, onEdit);

        tableView = dataTable.getView();
        addButton = dataTable.getAddButton();
        saveButton = new Button("SAVE CHANGES");

        root.getChildren().addAll(header, tableView, saveButton);

    }

    /**
     * Return the parent VBox node containing each element of the Appointments screen.
     * @return root node displaying Appointments title, appointment data table, and new appointment fields.
     */
    public Parent getView() {
        return root;
    }

    public AppointmentTableView getDataTable() {
        return dataTable;
    }

    /**
     * @return reference to new appointment add button element.
     */
    public Button getAddButton() {
        return addButton;
    }

    /**
     * @return reference to save appointment first name field element.
     */
    public Button getSaveButton() {
        return saveButton;
    }
}
