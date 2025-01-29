package edu.snhu.dayplanner.ui;
/**
 * Represents an operation that accepts three input arguments and returns no result.
 * This is a functional interface, similar to {@link java.util.function.BiConsumer}, but with three parameters.*
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 */
public interface TriConsumer<T, U, V> {
    /**
     * Performs the operation on the given arguments
     * @param t input argument
     * @param u input argument
     * @param v input argument
     */
    void accept (T t, U u, V v);
}
