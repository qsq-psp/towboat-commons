package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created on 2026/3/31.
 */
@CodeHistory(date = "2026/3/31")
public interface IOIterable<E> {

    @NotNull
    IOIterator<E> iterator() throws IOException;

    default void forEach(@NotNull Consumer<? super E> action) throws IOException {
        for (IOIterator<E> i = iterator(); i.hasNext(); ) {
            action.accept(i.next());
        }
    }

    default void forEach(@NotNull IOConsumer<? super E> action) throws IOException {
        for (IOIterator<E> i = iterator(); i.hasNext(); ) {
            action.accept(i.next());
        }
    }
}
