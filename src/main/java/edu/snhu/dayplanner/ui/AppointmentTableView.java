package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.appointmentservice.Appointment;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final TriConsumer<Appointment,Appointment.Field, Node> onEdit;
    private final BiConsumer<Appointment, Node> onRemove;

    /**
     * Constructs a new {@code AppointmentTableView} instance.
     *
     * @param fields   The list of fields to display and manage in the table.
     * @param onRemove A handler to call when a row's remove button is clicked.
     * @param onEdit   A handler to call when a field is edited.
     */
    public AppointmentTableView(List<Appointment.Field> fields,
                                BiConsumer<Appointment, Node> onRemove,
                                TriConsumer<Appointment, Appointment.Field, Node> onEdit) {
        super(fields, onRemove, onEdit);
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

        // Create Date-Time Field
        DateTimePicker timePicker = new DateTimePicker();
        timePicker.setMinWidth(80);
        timePicker.setPromptText("YYYY-MM-DD HH:MM");
        HBox.setHgrow(timePicker, Priority.ALWAYS);

        // Create Description Field
        TextField descriptionInput = new TextField();
        descriptionInput.setMinWidth(80);
        descriptionInput.setPromptText(Appointment.Field.DESCRIPTION.toString());
        HBox.setHgrow(descriptionInput, Priority.ALWAYS);

        // Add time, description, and add button to row
        entryRow.getChildren().addAll(timePicker, descriptionInput, getAddButton());
        return entryRow;
    }

    /**
     * Returns the text within the new entry inputs as a List of string
     * @return List containing each entry as text from the new entry row (description, Date time (in ms)
     */
    @Override
    public List<String> getNewEntryInput() {
        List<String> inputs = new ArrayList<>();
        // gets the time in MS from the time picker and adds to list
        inputs.add(((DateTimePicker)getNewEntryRow().getChildren().getFirst()).getDateTimeValue().toString());
        // adds input from the DESCRIPTION input field to list
        inputs.add(((TextField)getNewEntryRow().getChildren().get(1)).getText());

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
        setRowStyle(dataRow);

        // set fields and listeners
        TextField descriptionField = new TextField(appointment.getFieldValue(Appointment.Field.DESCRIPTION));
        VBox descFieldBox = wrapWithErrorLabel(descriptionField);
        HBox.setHgrow(descFieldBox, Priority.ALWAYS);

        descriptionField.textProperty().addListener(
                (observable, oldValue, newValue) -> onEdit.accept(appointment, Appointment.Field.DESCRIPTION, descriptionField));

        // get appointment date and set date field with that value.
        DateTimePicker dateField = new DateTimePicker();
        VBox dateFieldBox = wrapWithErrorLabel(dateField);
        HBox.setHgrow(dateFieldBox, Priority.ALWAYS);
        dateField.setDateTimeValue(LocalDateTime.parse(appointment.getFieldValue(Appointment.Field.DATE)));

        // set listener to send date time string to event handler
        dateField.dateTimeValueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    onEdit.accept(appointment, Appointment.Field.DATE, dateField);
                });
        dateField.getEditor().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        LocalDateTime parsed = LocalDateTime.parse(newValue, DateTimeFormatter.ofPattern(dateField.getFormat()));
                        dateField.setDateTimeValue(parsed);
                    } catch (Exception ignored) {
                    }
        });

        Button removeButton = new Button("X");
        setButtonStyle(removeButton, "#dc3545");
        removeButton.setOnAction(e -> onRemove.accept(appointment, dataRow));

        dataRow.getChildren().addAll(dateFieldBox, descFieldBox, removeButton);
        getTableDataView().getChildren().add(dataRow);
    }

}
