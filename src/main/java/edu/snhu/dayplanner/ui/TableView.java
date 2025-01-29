package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.Entity;
import edu.snhu.dayplanner.service.contactservice.Contact;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
/**
 * A reusable TableView class for displaying and editing entities with customizable fields and actions.
 * It provides functionality for dynamically adding, editing, and removing rows, as well as handling user input.
 * @param <T> the type of entity (must extend {@code Entity}).
 * @param <F> the type of the entity's field enumeration
 */
public class TableView<T extends Entity<F>, F extends Enum<F>> {
    private final List<F> fields;
    private final VBox tableView;
    private final VBox tableDataView;
    private final HBox newEntryRow;
    private Button addButton;

    private final BiConsumer<T, Node> onRemove;
    private final TriConsumer<T, F, String> onEdit;

    /**
     * Constructs a new {@code TableView} instance.
     *
     * @param fields   The list of fields to display and manage in the table.
     * @param onRemove A handler to call when a row's remove button is clicked.
     * @param onEdit   A handler to call when a field is edited.
     */
    public TableView(
            List<F> fields,
            BiConsumer<T, Node> onRemove,
            TriConsumer<T, F, String> onEdit
    ) {
        this.fields = fields;
        // set event listeners
        this.onRemove = onRemove;
        this.onEdit = onEdit;

        tableView = new VBox();
        tableDataView = new VBox();

        // create table head row with label from each entry in fields
        HBox colHeaderRow = new HBox();
        for (F field : fields) {

            colHeaderRow.getChildren().add(new Label(field.name()));
        }

        // create ADD button row
        newEntryRow = createNewEntryRow();

        // add tableView to a scrollable component and fit to width
        ScrollPane scrollPane = new ScrollPane(tableDataView);
        scrollPane.setFitToWidth(true);

        tableView.getChildren().addAll(colHeaderRow, scrollPane, newEntryRow);
    }

    /**
     * Creates a row for entering new data, including input fields for each field and an add button.
     * @return An {@code HBox} containing the new entry row components.
     */
    protected HBox createNewEntryRow() {
        HBox entryRow = new HBox();

        for (F field : fields) {
            TextField inputField = new TextField();
            inputField.setPromptText(field.name());
            entryRow.getChildren().add(inputField);
        }
        addButton = new Button("+");
        entryRow.getChildren().add(addButton);

        return entryRow;

    }

    /**
     * Updates the table with a new set of data. Clears existing rows and repopulates the table with rows
     * corresponding to the provided objects.
     * @param objects The list of entities to display in the table.
     */
    public void updateTable(List<T> objects) {
        tableDataView.getChildren().clear();

        for (T object : objects) {
            createDataRow(object);
        }
    }

    /**
     * Creates a data row for an entity, including editable fields and a remove button.
     * Responsible for assigning actions to data row actions (remove and edit)
     * @param object The entity to display in the row.
     */
    public void createDataRow(T object) {
        HBox dataRow = new HBox(); // holds each element of row

        // Create editable text fields for each field in the object
        for (F field : fields) {
            // prefill with data from field
            TextField inputField = new TextField(object.getFieldValue(field));
            // set listeners on each field to use onEdit method when edited
            inputField.textProperty().addListener(
                    (observable, oldValue, newValue) ->
                        onEdit.accept(object, field, newValue)
            );
            dataRow.getChildren().add(inputField);
        }

        // create remove button with action using onRemove listener provided to constructor
        Button removeButton = new Button("X");
        removeButton.setOnAction(e -> onRemove.accept(object, dataRow));
        dataRow.getChildren().add(removeButton);

        tableDataView.getChildren().addAll(dataRow);
    }

    /**
     * Removes a row from the table.
     * Should be called by the onRemove handler
     * @param row The {@code Node} representing the row to remove.
     */
    public void removeRow(Node row) {
        tableDataView.getChildren().remove(row);
    }

    /**
     * Returns the text within the new entry inputs as a List of string
     * @return List containing each entry as text from the new entry row
     */
    public List<String> getNewEntryInput() {
        List<String> inputs = new ArrayList<>();
        for (Node field : newEntryRow.getChildren()) {
            if (field instanceof TextField) {
                inputs.add(((TextField) field).getText());
            }
        }
        return inputs;
    }

    /**
     * Clears the text in each new row input field
     */
    public void clearNewEntryInput() {
        for (Node field : newEntryRow.getChildren()) {
            if (field instanceof TextField) {
                ((TextField) field).clear();
            }
        }
    }

    /**
     * Returns the row for entering new data.
     *
     * @return The {@code HBox} containing the new entry row.
     */
    protected HBox getNewEntryRow() {
        return newEntryRow;
    }

    /**
     * Returns the container for data rows.
     *
     * @return The {@code VBox} containing the data rows.
     */
    protected VBox getTableDataView() {
        return tableDataView;
    }

    /**
     * @return reference to new contact add button element.
     */
    public Button getAddButton() {
        return addButton;
    }
    /**
     * Return the parent VBox node containing each element of the data table.
     * @return root data table node reference
     */
    public VBox getView() {
        return tableView;
    }
}
