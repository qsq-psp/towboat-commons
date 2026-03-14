package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.UncheckedIOException;

@CodeHistory(date = "2025/3/16")
@FunctionalInterface
public interface IORunnable {

    void run() throws IOException;

    @NotNull
    default Runnable uncheck() {
        return () -> {
            try {
                run();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    @NotNull
    static IORunnable check(@NotNull Runnable action) {
        return () -> {
            try {
                action.run();
            } catch (UncheckedIOException e) {
                throw e.getCause();
            }
        };
    }

    @NotNull
    default Runnable upgrade() {
        return () -> {
            try {
                run();
            } catch (IOException e) {
                throw new IOError(e);
            }
        };
    }

    @NotNull
    static IORunnable downgrade(@NotNull Runnable action) {
        return () -> {
            try {
                action.run();
            } catch (IOError e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw e;
            }
        };
    }

    @NotNull
    default Runnable ignore() {
        return () -> {
            try {
                run();
            } catch (IOException ignore) {}
        };
    }
}
