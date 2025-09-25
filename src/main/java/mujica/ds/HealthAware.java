package mujica.ds;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2025/9/12.
 */
@CodeHistory(date = "2025/9/12")
public interface HealthAware {

    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    default void checkHealth() throws RuntimeException {
        checkHealth(re -> {throw re;});
    }

    default boolean isHealthy() {
        final PublicBooleanSlot slot = new PublicBooleanSlot();
        checkHealth(re -> slot.value = false);
        return slot.value;
    }
}
