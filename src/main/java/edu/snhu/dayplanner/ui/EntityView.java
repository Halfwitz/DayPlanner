package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.Entity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Provides common UI/layout elements for managing entities in a day planner, responsible for
 * setting up a header label, a data table, an add button, and a save changes button.
 * @param <T> The entity type (Task, Contact, Appointment)
 * @param <F> The field enum belonging to the entity, used to create fields in the data table.
 */
public abstract class EntityView<T extends Entity<F>, F extends Enum<F>> {
    private final VBox root; // layout root container
    private final TableView<T, F> dataTable; // for displaying/adding/editing/removing entities
    private final VBox tableView; // the component containing the dataTable
    private final SearchView<F> searchView;
    private final Button addButton; // from within tableView, used to add entries to dataTable
    private final Button saveButton; // for saving changes by writing to file
    private final Label addEntityErrorLabel;

    /**
     * Constructs an entity management screen with a heading and data table for given fields.
     *
     * @param headingText Text to display as section header, section title ("TASKS")
     * @param fields The fields to display and manage in data table ("NAME, DESCRIPTION, etc.")
     * @param onRemove A handler called when remove button for a row in the data table is clicked.
     * @param onEdit A handler called when a row's field in data table is edited
     */
    public EntityView(String headingText,
                      List<F> fields,
                      BiConsumer<T, Node> onRemove,
                      TriConsumer<T, F, Node> onEdit) {
        // setup root layout / style
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #fff");

        // create searchbar view
        searchView = new SearchView<>(fields);


        // create heading
        Label header = new Label(headingText);
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-font-style: italic;");

        // create data table with fields
        dataTable = createDataTable(fields, onRemove, onEdit);
        tableView = dataTable.getView();
        tableView.setMaxWidth(700);

        // get data table references
        addButton = dataTable.getAddButton(); // used to add new entry row to table
        addEntityErrorLabel = dataTable.getAddEntryErrorLabel(); // used to display errors when adding new entity

        // Create and style save button for outputting table data to file
        saveButton = new Button("SAVE CHANGES");
        saveButton.setStyle("-fx-background-color: #00AA00; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-border-radius: 5px; -fx-font-size: 16px;");
        VBox.setMargin(saveButton, new Insets(40));

        // Add components to root
        root.getChildren().addAll(header, searchView.getView(), tableView, saveButton);
    }

    /**
     * Used to create the underlying datatable, override to return specialized tables (AppointmentDataTable)
     */
    protected TableView<T,F> createDataTable(List<F> fields, BiConsumer<T, Node> onRemove, TriConsumer<T,F, Node> onEdit) {
        return new TableView<>(fields, onRemove, onEdit);
    }

    /**
     * Return the parent VBox node containing each element of the Entity screen.
     * @return root node displaying title, data table, new entry fields, and save button.
     */
    public Parent getView() {
        return root;
    }

    public TableView<T, F> getDataTable() {
        return dataTable;
    }

    /**
     * @return reference to new contact add button element.
     */
    public Button getAddButton() {
        return addButton;
    }

    /**
     * @return reference to save button for outputting data table changes.
     */
    public Button getSaveButton() {
        return saveButton;
    }

    /**
     * @return reference to error label for displaying errors in adding a new entry
     */
    public Label getAddEntityErrorLabel() { return addEntityErrorLabel; }

    public SearchView<F> getSearchView() {
        return searchView;
    }


}
