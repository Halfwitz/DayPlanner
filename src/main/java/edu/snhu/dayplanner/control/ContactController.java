package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.contactservice.ContactService;
import edu.snhu.dayplanner.ui.ContactView;

import javafx.scene.Node;
import javafx.scene.Parent;
import java.util.List;

/**
 * Manages the logic for the Contact section of the Day Planner Application. This handles user interactions,
 * coordinating between {@code ContactService} and {@code ContactView}, and provides listener methods for handling
 * adding, removing, and updating contact entries depending on which GUI elements are used.
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class ContactController
{
    private final ContactService contacts;
    private final ContactView contactView;

    private static final String CSV_FILE_PATH = "data/contacts.csv";

    /**
     * Initializes a controller instance that sets up the initial {@code ContactService} list and initializes a new
     * {@code ContactView}. Configures event handlers for adding, removing, and editing contacts.
     */
    public ContactController() {
        contacts = new ContactService();
        contacts.addFromFile(CSV_FILE_PATH);

        contactView = new ContactView(this::handleRemoveContact, this::handleEditContact);
        contactView.getAddButton().setOnAction(event -> handleAddContact());
        contactView.getSaveButton().setOnAction(event -> handleSaveContacts());
    }

    /**
     * Handles adding a new contact to the {@code contactView} table and {@code contacts} service.
     * If inputs in contactView TextFields are correctly formatted, creates a new row in the table with action listeners
     * for edit or remove actions and clears input fields. Else, it catches an {@code IllegalArgumentException} and logs
     * to console, TODO: additional UI messages for this error should be indicated to user.
     */
    private void handleAddContact() {
        try {
            List<String> input = contactView.getDataTable().getNewEntryInput();

            // add contact to contacts using input strings, throws IllegalArgumentException for invalid input
            Contact c = contacts.add(input.get(0), input.get(1), input.get(2), input.get(3));

            // add new row to table and clear input forms.
            contactView.getDataTable().createDataRow(c);
            contactView.getDataTable().clearNewEntryInput();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles removing a contact from the {@code contacts} service and contact table view. This method is called by
     * each Contact row's inline delete button created in the view. The method is passed as paramater in
     * {@code ContactView} constructors using {{@code BiConsumer} interface}
     * @param contact
     * @param tableRow
     */
    private void handleRemoveContact(Contact contact, Node tableRow) {
        contacts.delete(contact); // FIXME: throws error if contact does not exist. Unsure if this case is possible yet.
        contactView.getDataTable().removeRow(tableRow);
    }

    /**
     * Handles editing a contact's specified field within the {@code contacts} service if contact field is edited in
     * table view.
     * This is called by each Contact row's text fields upon changes. An IllegalArgumentException is caught for invalid
     * input and logs an error. FIXME: Should also indicate the input issue to the User
     * @param contact the contact to modify
     * @param field the Contact.Field to be updated
     * @param newValue the new value to update the contact field to.
     */
    private void handleEditContact(Contact contact, Contact.Field field, String newValue) {
        try {
            contacts.updateField(contact.getId(), field, newValue.trim());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getClass() + e.getMessage());
        }
    }

    private void handleSaveContacts() {
        contacts.writeToFile(CSV_FILE_PATH);
    }

    /**
     * Refreshes the contact table view and returns the root node containing the Contact screen elements. Used in
     * {@code NavController} to overlay the Contact screen in the layout.
     * @return the {@code Parent} node containing the {Contact view elements}
     */
    public Parent getView() {
        contactView.getDataTable().updateTable(contacts.getAll());
        return contactView.getView();
    }
}