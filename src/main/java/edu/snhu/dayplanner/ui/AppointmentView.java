package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import javafx.scene.Node;
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
public class AppointmentView extends EntityView<Appointment, Appointment.Field>
{
    /**
     * Initializes this object to set up the layout with each element as a child of the {@code root} Vbox. Responsible
     * for creating the screen header, and empty appointment table view with elements for a row of column headers, table
     * data, and a row for adding new appointments to the table data.
     *
     * @param onRemove the method implementation that handles logic for when a appointment is removed from the table.
     * @param onEdit   the method implementation that handles logic for when a appointment field is edited in the table.
     *                 Should use Appointment as reference to edited field, and Appointment.Field as the field that is being
     *                 modified, and String as the new value.
     */
    public AppointmentView(BiConsumer<Appointment, Node> onRemove, TriConsumer<Appointment, Appointment.Field, Node> onEdit) {
        super("APPOINTMENTS",
                Arrays.asList(Appointment.Field.DATE, Appointment.Field.DESCRIPTION),
                onRemove, onEdit);
    }

    /**
     * Called during super construction to get the view used for the data table
     */
    @Override
    protected TableView<Appointment, Appointment.Field> createDataTable(List<Appointment.Field> fields, BiConsumer<Appointment, Node> onRemove, TriConsumer<Appointment, Appointment.Field, Node> onEdit) {
        return new AppointmentTableView(fields, onRemove, onEdit);
    }

}
