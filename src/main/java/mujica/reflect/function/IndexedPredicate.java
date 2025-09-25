package mujica.reflect.function;

import mujica.reflect.modifier.Index;

/**
 * Created on 2025/3/9.
 */
@FunctionalInterface
public interface IndexedPredicate<C, T> {

    boolean test(T item, @Index(of = "container") int index, C container);
}
