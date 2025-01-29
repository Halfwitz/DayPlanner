/******************************************************************************
 * Module Four Milestone
 * [TaskService.java]
 * Author: Michael Lorenz
 * - CS320 - Software Test, Automation QA
 * - Southern New Hampshire University
 * <p>--------------------------------------</p>
 * Description:
 * This class supports storing tasks. Allows functionalities to:
 * - add tasks with a unique ID, name, and description
 * - delete tasks per task ID
 * - update task fields per task ID (Name, Description)
 * Date: Due 9/29/2024
 * Modified: 10/09/2024 to remove outer package dependencies
 * Modified: 10/11/2024 to merge with superclass
 *****************************************************************************/
package edu.snhu.dayplanner.service.taskservice;

import edu.snhu.dayplanner.service.Service;
import edu.snhu.dayplanner.service.ServiceFileUtility;

public class TaskService extends Service<Task, Task.Field> {

    /**
     * Adds a task object mapped to its unique id in storage.
     * @param name Name of task
     * @param description Description of task
     * @return reference to newly created object with name and description
     * @throws IllegalArgumentException in Task object if parameters are invalid format
     */
    public Task add(String name, String description) {
        Task entity = new Task(name, description);
        return add(entity);
    }

    /**
     * Reads stored CSV contents from a file to this storage object.
     * Uses {@code ServiceFileUtility} to read a specified CSV file then convert each line to
     * a task and return a list of those tasks. Then, the list is added to this object.
     * Should convert contents to the object and add with {@code add} or {@code addAll}
     * Passes a prototype task object to use for creation of new objects with (fromCsv)
     * @param filePath the file to be read from into this object
     */
    @Override
    public void addFromFile(String filePath) {
        ServiceFileUtility<Task> fileUtil = new ServiceFileUtility<>(filePath,
                new Task("p", "p"));
        addAll(fileUtil.readFromFile());
    }

    /**
     * Writes stored objects to a CSV file stored in filePath.
     * Uses {@code ServiceFileUtility} to convert Task objects into CSV format and writes
     * to the file.
     *
     * @param filePath the file to be written into using contents of this object
     */
    @Override
    public void writeToFile(String filePath) {
        ServiceFileUtility<Task> fileUtil = new ServiceFileUtility<>(filePath,
                new Task("p", "p"));
        fileUtil.writeToFile(getAll());
    }

    // UPDATE TASK FIELDS
    /**
     * Updates name of task with given id to firstName
     * @param id Unique identifier of the task to update
     * @param name new task name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if task does not exist or firstName is invalid
     */
    public void updateName(String id, String name) {
        updateField(id, Task.Field.NAME, name);
    }

    /**
     * Updates name of task with given id to firstName
     * @param id Unique identifier of the task to update
     * @param description new task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if task does not exist or firstName is invalid
     */
    public void updateDescription(String id, String description) {
        updateField(id, Task.Field.DESCRIPTION, description);
    }
}
