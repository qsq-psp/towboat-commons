package mujica.ds;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2025/9/12")
public interface HealthAware {

    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "java.util.LinkedList.dataStructureInvariants()")
    default void checkHealth() throws RuntimeException {
        checkHealth(re -> {throw re;});
    }

    default boolean isHealthy() {
        final PublicBooleanSlot slot = new PublicBooleanSlot();
        checkHealth(re -> slot.value = false);
        return slot.value;
    }
}
