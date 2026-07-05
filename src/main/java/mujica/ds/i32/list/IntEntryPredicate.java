package mujica.ds.i32.list;

@FunctionalInterface
public interface IntEntryPredicate {

    boolean test(int key, int value);
}
