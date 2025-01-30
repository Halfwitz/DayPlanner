package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.taskservice.Task;
import edu.snhu.dayplanner.service.taskservice.TaskService;
import edu.snhu.dayplanner.ui.TaskView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Manages the logic for the Task section of the Day Planner Application. This handles user interactions,
 * coordinating between {@code TaskService} and {@code TaskView}, and provides listener methods for handling
 * adding, removing, and updating task entries depending on which GUI elements are used.
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class TaskController
{
    private static final String CSV_FILE_PATH = "data/tasks.csv";
    private final TaskService tasks;
    private final TaskView taskView;

    /**
     * Initializes a controller instance that sets up the initial {@code TaskService} list and initializes a new
     * {@code TaskView}. Configures event handlers for adding, removing, and editing tasks.
     */
    public TaskController() {
        tasks = new TaskService();
        tasks.addFromFile(CSV_FILE_PATH);

        taskView = new TaskView(this::handleRemoveTask, this::handleEditTask);
        taskView.getAddButton().setOnAction(event -> handleAddTask());
        taskView.getSaveButton().setOnAction(event -> handleSaveTasks());
    }

    /**
     * Handles adding a new task to the {@code taskView} table and {@code tasks} service.
     * If inputs in taskView TextFields are correctly formatted, creates a new row in the table with action listeners
     * for edit or remove actions and clears input fields. Else, it catches an {@code IllegalArgumentException} and logs
     * to console, TODO: additional UI messages for this error should be indicated to user.
     */
    private void handleAddTask() {
        try {
            // retrieve input field references
            List<String> input = taskView.getDataTable().getNewEntryInput();

            // add task to tasks using input strings, throws IllegalArgumentException for invalid input
            Task newTask = tasks.add(input.get(0), input.get(1));

            // add new row to table and clear input forms.
            taskView.getDataTable().clearNewEntryInput();
            taskView.getDataTable().createDataRow(newTask);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles removing a task from the {@code tasks} service and task table view. This method is called by
     * each Task row's inline delete button created in the view. The method is passed as paramater in
     * {@code TaskView} constructors using {{@code BiConsumer} interface}
     *
     * @param task
     * @param tableRow
     */
    private void handleRemoveTask(Task task, Node tableRow) {
        tasks.delete(task); // FIXME: likely throws error if task does not exist. Unsure if this case is possible yet.
        taskView.getDataTable().removeRow(tableRow);
    }

    /**
     * Handles editing a task's specified field within the {@code tasks} service if task field is edited in
     * table view.
     * This is called by each Task row's text fields upon changes. An IllegalArgumentException is caught for invalid
     * input and logs an error. FIXME: Should also indicate the input issue to the User
     *
     * @param task       the task to modify
     * @param field      the Task.Field to be updated
     * @param inputField the Node containing input for the new value. (Must be TextField
     */
    private void handleEditTask(Task task, Task.Field field, Node inputField) {
        String newValue = "";
        if (inputField instanceof TextField) {
            newValue = ((TextField) inputField).getText();
        }

        try {
            tasks.updateField(task.getId(), field, newValue.trim());
            inputField.setStyle("-fx-border-color: #005500");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getClass() + e.getMessage());
            System.out.println("Invalid input: " + e.getMessage());
            inputField.setStyle("-fx-border-color: #ff0000");
        }
    }

    /**
     * Saves all tasks to the CSV file.
     * Invoked when the user clicks the save button in the AppointmentView.
     */
    private void handleSaveTasks() {
        tasks.writeToFile(CSV_FILE_PATH);
    }

    /**
     * Refreshes the task table view and returns the root node containing the Task screen elements. Used in
     * {@code NavController} to overlay the Task screen in the layout.
     *
     * @return the {@code Parent} node containing the {Task view elements}
     */
    public Parent getView() {
        taskView.getDataTable().updateTable(tasks.getAll());
        return taskView.getView();
    }
}