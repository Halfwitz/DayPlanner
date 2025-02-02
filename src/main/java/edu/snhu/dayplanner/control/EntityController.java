package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.Entity;
import edu.snhu.dayplanner.service.Service;
import edu.snhu.dayplanner.ui.EntityView;
import edu.snhu.dayplanner.ui.EntityViewFactory;
import edu.snhu.dayplanner.ui.SearchView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.*;

public abstract class EntityController<T extends Entity<F>, F extends Enum<F>,V extends EntityView<T, F>> {

    private final Service<T, F> service;
    private final String CSV_FILE_PATH;
    private final EntityView<T, F> entityView;
    private final SearchView<F> searchView;

    private final Map<T, HashSet<Node>> invalidEntityInputs = new HashMap<>();

    public EntityController(Service<T, F> service,
                            String CSV_FILE_PATH,
                            EntityViewFactory<T, F, V> viewFactory) {
        this.service = service;
        this.CSV_FILE_PATH = CSV_FILE_PATH;

        // import entities from specified file path
        service.addFromFile(CSV_FILE_PATH);

        // construct a new EntityView using the factory and pass event handlers
        entityView = viewFactory.createView(this::handleRemoveEntity, this::handleEditEntity);

        searchView = entityView.getSearchView();

        // attach component actions to implementation methods
        entityView.getAddButton().setOnAction(e -> handleAddEntity());
        entityView.getSaveButton().setOnAction(e->handleSaveEntities());
        entityView.getSaveButton().setVisible(false); // invisible by default

        searchView.getSearchButton().setOnAction(e -> handleSearch());
    }

    /*---Overrideable/Abstract METHODS---*/
    /**
     * Attempts to extract the text input from the given {@code Node}.
     * Can be overwritten for subclasses to handle input nodes that are not {@code TextField}.
     * @param inputNode
     * @return The string extracted from the inputNode, or "" if nothing could be extracted.
     */
    public String getInputFrom(Node inputNode) {
        String input = "";

        if (inputNode instanceof TextField) {
            input = ((TextField) inputNode).getText();
        }
        return input;
    }

    /**
     * Uses the supplied input to create a new entity of type T. Each entry in input
     * is retrieved in order from the fields of {@code EntityView}'s data table new entry field.
     * @param input the list of input that should be values for entity's fields
     * @return The entity created using input arguments
     */
    public abstract T createEntityFromData(List<String> input);

    /*---EVENT LISTENERS---*/
    private void handleSearch() {
        TextField  searchField = searchView.getSearchField();
        ComboBox<F> fieldBox = searchView.getFieldBox();
        List<T> results;
        // get list from search parameters
        if (searchField.getText().isEmpty()) {
            results = service.getAll();
        } else {
            results = new ArrayList<>(service.entityTrie.searchAllWithPrefix(searchField.getText(), fieldBox.getValue()));
            System.out.println(service.entityTrie);

        }

        entityView.getDataTable().updateTable(results);

    }

    /**
     * Called when the data table's add button is clicked.
     * Retrieves input from the data table new entry row, creates an entity with the input, adds it as a new row, and
     * clears new entry fields.
     */
    private void handleAddEntity() {
        try {
            List<String> input = entityView.getDataTable().getNewEntryInput();

            // create entity from input and add to service storage if input is valid
            T entity = createEntityFromData(input);
            service.add(entity);

            // update data table and clear input forms
            entityView.getDataTable().createDataRow(entity);
            entityView.getDataTable().clearNewEntryInput();

            // Set any error label to invisible and set changes to true
            setHasChanges(true);
            updateLabel(entityView.getAddEntityErrorLabel(), "", false);
        } catch (IllegalArgumentException e) {
            // display exception message to user
            updateLabel(entityView.getAddEntityErrorLabel(), e.getMessage(), true);
        }
    }

    /**
     * Handles editing an entity's specified field within {@code Service} service if field is edited in
     * data table row.
     * This is called for each change to an existing row's input fields, the value must be extracted in
     * getInputFrom(Node)
     *
     * @param entity    the entity to modify
     * @param field      the entity's field to be updated.
     * @param inputField the Node containing input for the new value. (must be TextField)
     */
    private void handleEditEntity(T entity, F field, Node inputField) {
        try { // Update entity's specified field with new input
            service.updateField(entity.getId(), field, getInputFrom(inputField).trim());

            removeInvalidInput(entity, inputField); // removes status as "invalid" from Map

            setHasChanges(true);
            setEditErrorMessage(inputField, "", false);
        } catch (IllegalArgumentException e) { // New input is invalid, display error
            addInvalidInput(entity, inputField); // Add this input Node to invalid nodes for this entity
            setHasChanges(true);
            setEditErrorMessage(inputField, e.getMessage(), true);
        }
    }

    private void addInvalidInput(T entity, Node inputField) {
        // Adds inputField if not present to invalid inputs map values.
        invalidEntityInputs.computeIfAbsent(entity, e -> new HashSet<>()).add(inputField);
    }

    /**
     * Mark a node as 'valid' by removing it from the invalid input map values.
     * @param entity Key used to lookup set of invalid nodes
     * @param inputField The node that is no longer invalid.
     */
    private void removeInvalidInput(T entity, Node inputField) {
        Set<Node> invalidNodes = invalidEntityInputs.get(entity);
        if (invalidNodes != null) {
            invalidNodes.remove(inputField);
            if (invalidNodes.isEmpty()) {
                invalidEntityInputs.remove(entity);
            }
        }
    }

    /**
     * Listener that handles removing entity from the {@code Service} and {@code entityView } data table.
     * This method is called by each data table row's delete button.
     * Enables save button if no input errors are present in the data table.
     *
     * @param entity to be removed from {@code Service}
     * @param tableRow row being removed from {@code EntityView} data table
     */
    private void handleRemoveEntity(T entity, Node tableRow) {
        service.delete(entity);
        entityView.getDataTable().removeRow(tableRow);
        // remove errors the row may have from invalidFields
        invalidEntityInputs.remove(entity);
        setHasChanges(true);
    }

    /**
     *
     */
    private void handleSaveEntities() {
        service.writeToFile(CSV_FILE_PATH);
        handleSearch();
        setHasChanges(false);
    }

    /*---UTILITY METHODS---*/
    private void setHasChanges(boolean isChanged) {
        boolean hasErrors = !invalidEntityInputs.isEmpty();
        // enable save button if change is made and no input errors exist
        entityView.getSaveButton().setVisible(isChanged && !hasErrors);
    }

    private void setEditErrorMessage(Node inputField, String errorMessage, boolean hasError) {
        Label errorLabel = getFieldErrorLabel(inputField);

        if (errorLabel != null) {
            updateLabel(errorLabel, errorMessage, hasError);
            // change border color of inputField signifying error present (red) or not (green)
            inputField.setStyle(hasError? "-fx-border-color: #ff0000" : "-fx-border-color: #00BB00" );
        }
    }

    /**
     * Retrieves the {@code Label} if it is attached as the last child of the parent Node.
     * (Each data table field in {@TableView } is a child of a VBox containing a label formatted
     * for displaying errors below each field.)
     * @param inputField - Node stored as a sibling to the Label component.
     * @return Label retrieved from the last element of inputField's parent, or null if nonexistent
     */
    private Label getFieldErrorLabel(Node inputField) {
        if (inputField.getParent() instanceof VBox parent) {
            if (!parent.getChildren().isEmpty()) {
                Node last = parent.getChildren().getLast();
                if (last instanceof Label) {
                    return (Label) last;
                }
            }
        }

        return null;
    }

    /**
     * Updates the provided {@code Label} to the specified message and visibility
     * @param label exact label being updated
     * @param message Messages to be displayed by the label
     * @param visible sets visibility of label
     */
    private void updateLabel(Label label, String message, boolean visible) {
        if (visible) {
            label.setText(message);
            label.setVisible(true);
            label.setManaged(true);
        } else {
            label.setVisible(false);
            label.setManaged(false);
        }
    }

    /**
     * Updates entire table with data stored from the service and returns the root View
     * containing the entity management screen.
     *
     * @return the {@code Parent} node containing the {@code EntityView } view elements
     */
    public Parent getView() {
        entityView.getDataTable().updateTable(service.getAll());
        return entityView.getView();

    }

}
