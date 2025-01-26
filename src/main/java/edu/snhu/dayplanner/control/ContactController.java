package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.contactservice.ContactService;
import edu.snhu.dayplanner.ui.ContactView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

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
    ContactService contacts;
    ContactView contactView;

    /**
     * Initializes a controller instance that sets up the initial {@code ContactService} list and initializes a new
     * {@code ContactView}. Configures event handlers for adding, removing, and editing contacts.
     */
    public ContactController() {
        // TODO: replace contacts initialization with file I/O
        contacts = new ContactService();
        contacts.add("Jon", "Sno", "1112223456", "The North");
        contacts.add("Michael", "L", "0001112222", "Home");
        contacts.add("first", "last", "1234567890", "addy");
        contacts.add("Frank", "Lin", "1234567890", "West");
        contacts.add("Michael", "L", "0001112222", "Home");
        contacts.add("fire", "truck", "1234567890", "weewoo");
        contacts.add("Jon", "Sno", "1112223456", "The North");
        contacts.add("Brandy", "L", "0001112222", "Home");
        contacts.add("Flippo", "last", "1234567890", "addy");
        contacts.add("Meliodas", "Lin", "1234567890", "West");
        contacts.add("Jeramish", "L", "0001112222", "Home");
        contacts.add("Escanor", "truck", "1234567890", "weewoo");

        contactView = new ContactView(this::handleRemoveContact, this::handleEditContact);
        contactView.getAddButton().setOnAction(event -> handleAddContact());
    }

    /**
     * Handles adding a new contact to the {@code contactView} table and {@code contacts} service.
     * If inputs in contactView TextFields are correctly formatted, creates a new row in the table with action listeners
     * for edit or remove actions and clears input fields. Else, it catches an {@code IllegalArgumentException} and logs
     * to console, TODO: additional UI messages for this error should be indicated to user.
     */
    private void handleAddContact() {
        try {
            // retrieve input field references
            TextField firstNameField = contactView.getNewFirstName();
            TextField lastNameField = contactView.getNewLastName();
            TextField phoneField = contactView.getNewPhoneNumber();
            TextField addressField = contactView.getNewAddress();

            // add contact to contacts using input strings, throws IllegalArgumentException for invalid input
            Contact c = contacts.add(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            );

            // add new row to table and clear input forms.
            contactView.createContactRow(c);
            firstNameField.clear();
            lastNameField.clear();
            phoneField.clear();
            addressField.clear();

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
        contactView.removeRow(tableRow);
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
            contacts.updateEntityField(contact.getId(), field, newValue.trim());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getClass() + e.getMessage());
        }
    }

    /**
     * Refreshes the contact table view and returns the root node containing the Contact screen elements. Used in
     * {@code NavController} to overlay the Contact screen in the layout.
     * @return the {@code Parent} node containing the {Contact view elements}
     */
    public Parent getView() {
        contactView.updateTable(contacts.getAllContacts());
        return contactView.getView();
    }
}