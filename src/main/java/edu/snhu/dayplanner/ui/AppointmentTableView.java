package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import tornadofx.control.DateTimePicker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;


/**
 * A {@code TableView} subclass for displaying and editing {@code Appointment} objects.
 * <p>
 * provides a UI for adding, editing, and removing appointments using an interactive data table with "Description" and
 * "Date" fields.
 *
 */
public class AppointmentTableView extends TableView<Appointment, Appointment.Field> {
    private final List<Appointment.Field> fields;
    private final TriConsumer<Appointment,Appointment.Field, String> onEdit;
    private final BiConsumer<Appointment, Node> onRemove;
    private Button addButton;

    /**
     * Constructs a new {@code AppointmentTableView} instance.
     *
     * @param fields   The list of fields to display and manage in the table.
     * @param onRemove A handler to call when a row's remove button is clicked.
     * @param onEdit   A handler to call when a field is edited.
     */
    public AppointmentTableView(List<Appointment.Field> fields, BiConsumer<Appointment, Node> onRemove, TriConsumer<Appointment, Appointment.Field, String> onEdit) {
        super(fields, onRemove, onEdit);
        this.fields = fields;
        this.onEdit = onEdit;
        this.onRemove = onRemove;
    }

    /**
     * Creates a new row for adding an appointment, including input fields for description and date-time.
     * @return An {@code HBox} containing the input fields and an add button.
     */
    @Override
    protected HBox createNewEntryRow() {
        HBox entryRow = new HBox();

        TextField descriptionInput = new TextField();
        descriptionInput.setPromptText(Appointment.Field.DESCRIPTION.toString());

        DateTimePicker timePicker = new DateTimePicker();
        addButton = new Button("+");

        entryRow.getChildren().addAll(descriptionInput, timePicker, addButton);
        return entryRow;

    }

    /**
     * Returns the text within the new entry inputs as a List of string
     * @return List containing each entry as text from the new entry row (description, Date time (in ms)
     */
    public List<String> getNewEntryInput() {
        List<String> inputs = new ArrayList<>();

        // adds input from the DESCRIPTION input field to list
        inputs.add(((TextField)(getNewEntryRow().getChildren().getFirst())).getText());

        // gets the time in MS from the time picker and adds to list
        inputs.add(String.valueOf(
                ((DateTimePicker)getNewEntryRow().getChildren().get(1)).getDateTimeValue().toString()));

        return inputs;
    }

    /**
     * Creates a data row for an {@code Appointment} object.
     * Each row includes editable fields for description and date, along with a remove button.
     *
     * @param appointment The {@code Appointment} to display in the row.
     */
    @Override
    public void createDataRow(final Appointment appointment) {
        HBox dataRow = new HBox(); // holds each element of row

        // set fields and listeners
        TextField descriptionField = new TextField(appointment.getFieldValue(fields.getFirst()));
        descriptionField.textProperty().addListener(
                (observable, oldValue, newValue) -> onEdit.accept(appointment, Appointment.Field.DESCRIPTION, newValue));

        // get appointment date and set date field with that value.
        DateTimePicker dateField = new DateTimePicker();
        dateField.setDateTimeValue(LocalDateTime.parse(appointment.getFieldValue(Appointment.Field.DATE)));

        // set listener to send date time in (ms) to event handler
        dateField.getEditor().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("Date changed: " + newValue); //TODO: remove debug
                    onEdit.accept(appointment, Appointment.Field.DATE, dateField.getDateTimeValue().toString());
        });

        Button removeButton = new Button("X");
        removeButton.setOnAction(e -> onRemove.accept(appointment, dataRow));

        dataRow.getChildren().addAll(descriptionField, dateField, removeButton);
        getTableDataView().getChildren().add(dataRow);
    }

    /**
     * Retrieves the add button for the new entry row.
     * @return The add button.
     */
    @Override
    public Button getAddButton() {
        return addButton;
    }
}
