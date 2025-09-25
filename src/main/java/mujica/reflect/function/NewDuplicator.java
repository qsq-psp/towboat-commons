package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/5/9")
@FunctionalInterface
@Deprecated
public interface NewDuplicator<T> {

    /**
     * @return the copy, calling new internally
     */
    T duplicate(T original); // returns the copy
}
