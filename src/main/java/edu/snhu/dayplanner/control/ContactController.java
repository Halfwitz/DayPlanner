package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.contactservice.ContactService;
import edu.snhu.dayplanner.ui.ContactView;

import java.util.List;

/**
 * Manages the logic for the Contact section of the Day Planner Application. This handles user interactions,
 * coordinating between {@code ContactService} and {@code ContactView}, and provides listener methods for handling
 * adding, removing, and updating contact entries depending on which GUI elements are used.
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class ContactController extends EntityController<Contact, Contact.Field, ContactView>
{
    private static final String CSV_FILE_PATH = "data/contacts.csv";

    /**
     * Initializes a controller instance that sets up the initial {@code ContactService} list and initializes a new
     * {@code ContactView}. Configures event handlers for adding, removing, and editing contacts.
     */
    public ContactController() {
        super(new ContactService(), CSV_FILE_PATH, ContactView::new);
    }

    /**
     * Uses the supplied input to create a new Contact. Each entry in input
     * is retrieved in order from the fields of {@code ContactView}'s data table new entry field.
     *
     * @param input the list of input that should be values for contact's fields
     * @return The contact created using input arguments
     */
    @Override
    public Contact createEntityFromData(List<String> input) {
        if (input.size() < 4 || (input.contains(""))) {
            throw new IllegalArgumentException("Not enough fields to add a Contact");
        }

        return new Contact(input.get(0), input.get(1), input.get(2), input.get(3));
    }

}