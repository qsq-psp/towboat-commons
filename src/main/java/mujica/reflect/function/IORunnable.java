package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;

@CodeHistory(date = "2025/3/16")
@FunctionalInterface
public interface IORunnable {

    void run() throws IOException;

    @NotNull
    default Runnable ignore() {
        return () -> {
            try {
                run();
            } catch (IOException ignore) {}
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
}
