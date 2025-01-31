package edu.snhu.dayplanner.service;

public class EntityNode<T extends Entity<F>, F extends Enum<F>> {
    public String key;
    public F fieldType;
    public T value;
    public EntityNode<T, F> left, right;

    public EntityNode(T value, F fieldType) {
        this.fieldType = fieldType;
        this.value = value;

        key = value.getFieldValue(fieldType);
    }

    @Override
    public String toString() {
        return "EntityNode{" + fieldType + " Node: " + value;
    }
}
