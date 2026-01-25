package mujica.ds.of_int.list;

@FunctionalInterface
public interface IntEntryPredicate {

    boolean test(int key, int value);
}
