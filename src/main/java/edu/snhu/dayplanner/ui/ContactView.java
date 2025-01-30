package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.contactservice.Contact;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Arrays;
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
public class ContactView extends EntityView<Contact, Contact.Field>
{
    /**
     * Initializes this object to set up the layout with each element as a child of the {@code root} Vbox. Responsible
     * for creating the screen header, and empty contact table view with elements for a row of column headers, table
     * data, and a row for adding new contacts to the table data.
     *
     * @param onRemove the method implementation that handles logic for when a contact is removed from the table.
     * @param onEdit   the method implementation that handles logic for when a contact field is edited in the table.
     *                 Should use Contact as reference to edited field, and Contact.Field as the field that is being
     *                 modified, and String as the new value.
     */
    public ContactView(BiConsumer<Contact, Node> onRemove, TriConsumer<Contact, Contact.Field, Node> onEdit) {
        super("CONTACTS", Arrays.asList(Contact.Field.values()), onRemove, onEdit);
    }
}