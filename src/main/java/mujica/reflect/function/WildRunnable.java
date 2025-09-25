package mujica.reflect.function;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/3/16.
 */
@FunctionalInterface
public interface WildRunnable {
    
    void run() throws Throwable;

    @NotNull
    default Runnable ignoreChecked() {
        return () -> {
            try {
                run();
            } catch (Error | RuntimeException error) {
                throw error;
            } catch (Throwable ignore) {}
        };
    }

    @NotNull
    default Runnable ignoreException() {
        return () -> {
            try {
                run();
            } catch (Error error) {
                throw error;
            } catch (Throwable ignore) {}
        };
    }

    @NotNull
    default Runnable ignoreAll() {
        return () -> {
            try {
                run();
            } catch (Throwable ignore) {}
        };
    }
}
