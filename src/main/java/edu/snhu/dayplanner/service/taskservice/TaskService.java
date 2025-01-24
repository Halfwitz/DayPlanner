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
package service.taskservice;

import java.util.HashMap;
import java.util.Map;

public class TaskService {
    /**
     * Container for set of entities, maps entity id to entity object.
     * (Use entity extensions (Task, Contact, Appointment))
     */
    private final Map<String, Task> entityMap = new HashMap<>();

    /**
     * Adds a task object mapped to its unique id in storage.
     * @param name - Name of task
     * @param description - Description of task
     * @throws IllegalArgumentException in Task object if parameters are invalid format
     */
    public void add(String name, String description) {
        Task entity = new Task(name, description);
        add(entity);
    }

    /**
     * Adds an object to the service storage, mapped to its id.
     * @param object object to add to service.
     */
    protected void add(Task object) {
        entityMap.put(object.getId(), object);
    }

    /**
     * Removes object of type T with given id from contacts map
     * @param object object to be removed from list
     * @throws IllegalArgumentException if contact does not exist
     * @return object that was removed
     */
    public Task delete(Task object) {
        return entityMap.remove(object.getId());
    }

    /**
     * Removes object of type T with given id from service map
     * @param id identifier of object to be removed from service map
     * @throws IllegalArgumentException if object does not exist
     * @return object of type T that was removed
     */
    public Task delete(String id) {
        return delete(getEntityById(id));
    }

    // UPDATE TASK FIELDS
    /**
     * Update specified string field implemented in updatedField method implemented from Entity
     * @param id Unique identifier of the object to delete
     * @param value new value to change specified field to
     * @throws IllegalArgumentException if object does not exist or field string is invalid
     */
    public void updateEntityField(String id, String fieldName, String value) {
        Task entity = getEntityById(id); // throws exception if entity not found
        entity.updateField(fieldName, value); // throws exception if field name or value invalid
    }

    /**
     * Updates name of task with given id to firstName
     * @param id Unique identifier of the task to update
     * @param name new task name (non-null, <= 10 chars)
     * @throws IllegalArgumentException if task does not exist or firstName is invalid
     */
    public void updateName(String id, String name) {
        updateEntityField(id, "name", name);
    }

    /**
     * Updates name of task with given id to firstName
     * @param id Unique identifier of the task to update
     * @param description new task description (non-null, <= 50 chars)
     * @throws IllegalArgumentException if task does not exist or firstName is invalid
     */
    public void updateDescription(String id, String description) {
        updateEntityField(id, "description", description);
    }

    /**
     * Return an entity of type T from the stored map
     * @param id the unique id used to identity entity in map
     * @return entity of type T associated with given id key from map
     * @throws IllegalArgumentException if entity with specified id can't be found
     */
    public Task getEntityById(String id) {
        Task entity = entityMap.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("Object with ID [" + id + "] does not exist");
        }
        return entity;
    }
}
