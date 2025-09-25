package mujica.reflect.function;

/**
 * Created on 2025/6/25.
 */
@FunctionalInterface
public interface IntEntryPredicate {

    boolean test(int key, int value);
}
