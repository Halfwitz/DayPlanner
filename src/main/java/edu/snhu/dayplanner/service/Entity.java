package edu.snhu.dayplanner.service;
/**
 * This abstract class provides common functionality for objects managed within
 * a service class. Each instance has a unique ID (generated per subclass),
 * supports updates to specific fields via {@code updateField}, and ensures
 * field values meet defined constraints using {@code InputValidator}.
 * <p>
 * Classes extending {@code Entity} must implement methods for updating and retrieving
 * specific fields.
 */
public abstract class Entity<F extends Enum<F>> {
    protected final String id;
    protected final int ID_CHAR_LIMIT = 10;

    // set of object attributes to be implemented

    // initialize this object with a unique id (no more than 10 chars, required, non-null
    @SuppressWarnings("unchecked")
    protected Entity() {
        // casts to match expected parameter in generateId. This cast is safe because every subclass of Entity calls
        // generateId.
        Class<? extends Entity<?>> entityClass = (Class<? extends Entity<?>>) this.getClass();

        this.id = InputValidator.verifyNonNullWithinChars(IdGenerator.generateId(entityClass), 1, ID_CHAR_LIMIT);
    }

    /**
     * Updates a specific field of the object with the provided value.
     * (could implement using switch cases for handling each field in an entity)
     * @param field the enum constant of the field to update. Entity object should implement
     *              an enum storing each field name as a constant.
     * @param value the new value to set for the specified field.
     * @throws IllegalArgumentException if the fieldName is invalid or does not exist.
     */
    protected abstract void updateField(F field, String value);

    /**
     * Returns the correct value indicated by the selected field.
     * @param field should be an enum constant associated with a specific field of an object
     * @return return value of the field as a string.
     */
    public abstract String getFieldValue(final F field);
    public String getId() {
        return id;
    }
}