package mujica.reflect.function;

@FunctionalInterface
public interface IntEntryPredicate {

    boolean test(int key, int value);
}
