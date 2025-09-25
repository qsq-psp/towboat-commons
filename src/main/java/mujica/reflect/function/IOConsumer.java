package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.util.function.Consumer;

@CodeHistory(date = "2024/6/6", project = "UltraIO")
@CodeHistory(date = "2025/3/4")
@FunctionalInterface
public interface IOConsumer<T> {

    /**
     * @throws IOException the general class of exceptions produced by failed or interrupted I/O operations.
     */
    void accept(T t) throws IOException;

    @NotNull
    default Consumer<T> ignore() {
        return (t) -> {
            try {
                accept(t);
            } catch (IOException ignore) {}
        };
    }

    @NotNull
    default Consumer<T> upgrade() {
        return (t) -> {
            try {
                accept(t);
            } catch (IOException e) {
                throw new IOError(e);
            }
        };
    }

    @NotNull
    default IOConsumer<T> andThen(@NotNull Consumer<? super T> after) {
        return (T t) -> {accept(t); after.accept(t); };
    }
}
