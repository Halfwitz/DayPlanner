package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.contactservice.Contact;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents the Contact section user interface for managing contacts in the Day Planner Application. This class
 * renders a view with a screen title, a labelled table of contacts with edit and remove controls, and input fields
 * for adding new contacts.
 * <p>
 * This class defines the layout and each elements style using JavaFX components, and can dynamically update the
 * contact table using provided data. Logic is handled by the {@code ContactController} class. </p>
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class ContactView {
    private final VBox root;
    private final VBox tableView;
    private final VBox tableDataView;
    private HBox newContactRow;


    private TextField newFirstName;
    private TextField newLastName;
    private TextField newPhoneNumber;
    private TextField newAddress;
    private Button addButton;

    private Button saveButton;

    private final BiConsumer<Contact, Node> onRemove;
    private final TriConsumer<Contact, Contact.Field, String> onEdit;

    /**
     * Initializes this object to set up the layout with each element as a child of the {@code root} Vbox. Responsible
     * for creating the screen header, and empty contact table view with elements for a row of column headers, table
     * data, and a row for adding new contacts to the table data.
     * @param onRemove the method implementation that handles logic for when a contact is removed from the table.
     * @param onEdit the method implementation that handles logic for when a contact field is edited in the table.
     *               Should use Contact as reference to edited field, and Contact.Field as the field that is being
     *               modified, and String as the new value.
     *
     */
    public ContactView(BiConsumer<Contact, Node> onRemove, TriConsumer<Contact, Contact.Field, String> onEdit) {
        // set event listeners
        this.onRemove = onRemove;
        this.onEdit = onEdit;

        root = new VBox();

        // create view heading
        Label header = new Label("CONTACTS");
        tableDataView = new VBox();
        tableView = new VBox();

        // Create table head row
        HBox colHead = new HBox(
                new Label("First Name"),
                new Label("Last Name"),
                new Label("Phone Number"),
                new Label("Address"),
                new Label("Remove")
        );

        // Create ADD button row
        newFirstName = new TextField("first");
        newLastName = new TextField("last");
        newPhoneNumber = new TextField("phone");
        newAddress = new TextField("address");
        addButton = new Button("+");

        HBox addRow = new HBox(
                newFirstName,
                newLastName,
                newPhoneNumber,
                newAddress,
                addButton
        );

        saveButton = new Button("SAVE CHANGES");

        tableView.getChildren().addAll(colHead, tableDataView, addRow);
        root.getChildren().addAll(header, tableView, saveButton);

    }

    /**
     * Clears existing data in the contact table and repopulates using the provided contacts. Each contact row uses the
     * provided {@code onRemove} implementation responsible for removing the contact from the table and the service,
     * provided by {@code ContactController}. Also called by {@code ContactController.getView}
     * @param contacts list used to populate contacts table with row for each contact.
     */
    public void updateTable(List<Contact> contacts) {
        tableDataView.getChildren().clear();

        for (Contact contact : contacts) {
            createContactRow(contact);
        }
    }

    /**
     * Adds a row to the contact table prefilled with the values of each contact's field. Each field is a separate
     * {@code TextField} object.
     * <p>
     * Responsible for setting the action listener to the remove button and editable text fields using methods in
     * {@code ContactController}.
     * </p>
     * @param contact used to prefill each field with data from this.
     */
    public void createContactRow(Contact contact) {
        HBox row = new HBox(); // holds each element of thw row

        // create remove button with action using onRemove listener provided to constructor
        Button remove = new Button("X");
        remove.setOnAction(event -> onRemove.accept(contact, row));

        // add contact data fields and remove button to row
        // initialize contact editable fields
        TextField firstNameField = new TextField(contact.getFirstName());
        TextField lastNameField = new TextField(contact.getLastName());
        TextField phoneNumberField = new TextField(contact.getPhone());
        TextField addressField = new TextField(contact.getAddress());

        // set listeners on each field to use onEdit method when edited
        firstNameField.textProperty().addListener(
                (observable, oldVal, newVal) -> onEdit.accept(contact, Contact.Field.FIRST_NAME, newVal)
        );
        lastNameField.textProperty().addListener(
                (observable, oldVal, newVal) -> onEdit.accept(contact, Contact.Field.LAST_NAME, newVal)
        );
        phoneNumberField.textProperty().addListener(
                (observable, oldVal, newVal) -> onEdit.accept(contact, Contact.Field.PHONE_NUMBER, newVal)
        );
        addressField.textProperty().addListener(
                (observable, oldVal, newVal) -> onEdit.accept(contact, Contact.Field.ADDRESS, newVal)
        );

        // add each field and row remove button to row, and append to table data view
        row.getChildren().addAll(
                firstNameField,
                lastNameField,
                phoneNumberField,
                addressField,
                remove
        );
        tableDataView.getChildren().add(row);
    }

    /**
     * Removes a row HBox node from the data table view. Called by {@code ContactController.handleRemoveContact} which
     * is triggered by the remove buttons.
     * @param row A Node referencing one of the rows contained within the data table.
     */
    public void removeRow(Node row) {
        tableDataView.getChildren().remove(row);
    }

    /**
     * Return the parent VBox node containing each element of the Contacts screen.
     * @return root node displaying Contacts title, contact data table, and new contact fields.
     */
    public Parent getView() {
        return root;
    }

    /**
     * @return reference to new contact first name field element.
     */
    public TextField getNewFirstName() {
        return newFirstName;
    }
    /**
     * @return reference to new contact last name field element.
     */
    public TextField getNewLastName() {
        return newLastName;
    }
    /**
     * @return reference to new contact phone number field element.
     */
    public TextField getNewPhoneNumber() {
        return newPhoneNumber;
    }
    /**
     * @return reference to new contact address field element.
     */
    public TextField getNewAddress() {
        return newAddress;
    }
    /**
     * @return reference to new contact add button element.
     */
    public Button getAddButton() {
        return addButton;
    }

    /**
     * @return reference to save contact first name field element.
     */
    public Button getSaveButton() {
        return saveButton;
    }
}
