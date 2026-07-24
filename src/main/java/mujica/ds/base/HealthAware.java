package mujica.ds.base;

import mujica.ds.bit.Bit;
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
        final Bit slot = new Bit();
        checkHealth(re -> slot.value = false);
        return slot.value;
    }
}
