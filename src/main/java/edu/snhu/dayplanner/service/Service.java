package edu.snhu.dayplanner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Description:
 * This abstract class provides common functionality to use or implement in
 * each service class. Supports storage of type T (extends entity),
 * includes functions to add, remove, and update T objects.
 * @param <T> Type of object to be managed by this service.
 * @param <F> An enum implementation that should contain the fields of T (T.Field)
 */
public abstract class Service<T extends Entity<F>, F extends Enum<F>> {

    /**
     * Container for set of entities, maps entity id to entity object.
     * (Use entity extensions (Task, Contact, Appointment))
     */
    private final Map<String, T> entityMap = new HashMap<>();
    public final CompactTrie<T, F> entityTrie;

    protected Service(List<F> fields) {
        entityTrie = new CompactTrie<>(fields);
    }

    /**
     * Return an entity from the stored map
     * @param id the unique id used to identify entity in map
     * @return entity of type T associated with given id key from map
     * @throws IllegalArgumentException if entity with specified id can't be found
     */
    public T getById(String id) {
        T entity = entityMap.get(id);
        if (entity == null) {
            throw new IllegalArgumentException("Object with ID [" + id + "] does not exist");
        }
        return entity;
    }
    public List<T> getAll() {
        return new ArrayList<>(entityMap.values());
    }

    /**
     * Adds an object to the service storage, mapped to its id.
     * @param object object to add to service.
     */
    public T add(T object) {
        entityMap.put(object.getId(), object);
        entityTrie.insert(object);
        return object;
    }

    /**
     * Adds a list of objects to the service storage, mapped to their id.
     * @param objects list of objects to add to service
     */
    public void addAll(List<T> objects) {
        for (T object : objects) {
            add(object);
        }
    }

    /**
     * Reads stored contents from a file to storage in this storage object.
     * Should convert contents to the object and add with {@code add} or {@code addAll}
     * @param filePath the file to be written into
     */
    public abstract void addFromFile(String filePath);
    /**
     * Writes stored objects to a file stored in filePath
     * @param filePath the file to be written into
     */
    public abstract void writeToFile(String filePath);

    /**
     * Removes object of type T from storage.
     * @param object object to be removed
     * @return object that was removed
     * @throws IllegalArgumentException if object does not exist
     */
    public T delete(T object) {
        entityTrie.delete(object);
        return entityMap.remove(object.getId());
    }
    /**
     * Removes object of type T found with given id from storage
     * @param id identifier of object to be removed
     * @return object of type T that was removed
     * @throws IllegalArgumentException if object does not exist
     */
    public T delete(String id) {
        return delete(getById(id));
    }

    /**
     * Update specified field of object found with id to new value.
     * @param id Unique identifier of the object being updated
     * @param field specified field of object being modified, see {@code updateField} implementation in T
     * @param value new value to change specified field to value to
     * @throws IllegalArgumentException if object does not exist or value is invalid
     */
    public void updateField(String id, F field, String value) {
        T entity = getById(id);
        String oldValue = entity.getFieldValue(field);
        entity.updateField(field, value);
        entityTrie.update(entity, field, oldValue);
    }

}