package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

import java.io.IOException;

@CodeHistory(date = "2025/4/7")
@Stable(date = "2025/8/18")
public interface IOIterator<E> {

    boolean hasNext() throws IOException;

    E next() throws IOException;

    @SuppressWarnings("RedundantThrows")
    default void remove() throws IOException {
        throw new UnsupportedOperationException();
    }
}
