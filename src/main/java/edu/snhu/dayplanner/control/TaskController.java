package edu.snhu.dayplanner.control;

import edu.snhu.dayplanner.service.taskservice.Task;
import edu.snhu.dayplanner.service.taskservice.TaskService;
import edu.snhu.dayplanner.ui.TaskView;
import java.util.List;

/**
 * Manages the logic for the Task section of the Day Planner Application. This handles user interactions,
 * coordinating between {@code TaskService} and {@code TaskView}, and provides listener methods for handling
 * adding, removing, and updating task entries depending on which GUI elements are used.
 *
 * @author Michael Lorenz
 * @version 1.0, 1/25/2025
 */
public class TaskController extends EntityController<Task, Task.Field, TaskView> {
    private static final String CSV_FILE_PATH = "data/tasks.csv";

    /**
     * Initializes a controller instance that sets up the initial {@code TaskService} list and initializes a new
     * {@code TaskView}. Configures event handlers for adding, removing, and editing tasks.
     */
    public TaskController() {
        super(new TaskService(), CSV_FILE_PATH, TaskView::new);
    }

    /**
     * Uses the supplied input to create a new Task. Each entry in input
     * is retrieved in order from the fields of {@code TaskView}'s data table new entry field.
     *
     * @param input the list of input that should be values for task's fields
     * @return The task created using input arguments
     */
    @Override
    public Task createEntityFromData(List<String> input) {
        if (input.size() < 2 || input.contains("")) {
            throw new IllegalArgumentException("Not enough fields to add a Task");
        }

        return new Task(input.get(0), input.get(1));
    }
}