package mujica.ds.generic.map;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2026/3/3.
 */
@CodeHistory(date = "2026/3/3")
public interface OverrideHashEqual<T> {

    int hashCode(T t);

    boolean equals(T a, T b);
}
