package edu.snhu.dayplanner.ui;

import edu.snhu.dayplanner.service.contactservice.Contact;
import edu.snhu.dayplanner.service.taskservice.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * Represents the Task section user interface for managing tasks in the Day Planner Application. This class
 * renders a view with a screen title, a labelled table of tasks with edit and remove controls, and input fields
 * for adding new tasks.
 * <p>
 * This class defines the layout and each elements style using JavaFX components, and can dynamically update the
 * task table using provided data. Logic is handled by the {@code TaskController} class. </p>
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class TaskView extends EntityView<Task, Task.Field> {

    /**
     * Initializes this object to set up the layout with each element as a child of the {@code root} Vbox. Responsible
     * for creating the screen header, and empty task table view with elements for a row of column headers, table
     * data, and a row for adding new tasks to the table data.
     * @param onRemove the method implementation that handles logic for when a task is removed from the table.
     * @param onEdit the method implementation that handles logic for when a task field is edited in the table.
     *               Should use Task as reference to edited field, and Task.Field as the field that is being
     *               modified, and String as the new value.
     *
     */
    public TaskView(BiConsumer<Task, Node> onRemove, TriConsumer<Task, Task.Field, Node> onEdit) {
        super("TASKS", Arrays.asList(Task.Field.values()), onRemove, onEdit);

    }


}
