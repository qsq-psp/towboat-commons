package mujica.reflect.basic;

import mujica.ds.HealthAware;
import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.TypeDescriptor;
import java.util.function.Consumer;

@CodeHistory(date = "2022/6/30", project = "infrastructure", name = "GuestType")
@CodeHistory(date = "2025/9/13")
public interface NotLoadedFieldType<F extends NotLoadedFieldType<F>> extends HealthAware, TypeDescriptor.OfField<F> {

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    void checkReturnTypeHealth(@NotNull Consumer<RuntimeException> consumer);

    default void checkReturnTypeHealth() throws RuntimeException {
        checkReturnTypeHealth(re -> {throw re;});
    }

    default boolean isReturnTypeHealthy() {
        final PublicBooleanSlot slot = new PublicBooleanSlot();
        checkReturnTypeHealth(re -> slot.value = false);
        return slot.value;
    }

    /**
     * @return the String returned by java.lang.Class.getName()
     */
    @NotNull
    String toClassGetName();

    @NotNull
    String toRealSourceString();

    @NotNull
    String toSourceString();

    @NotNull
    String toBytecodeString();

    @NotNull
    SourceFieldType toSourceFieldType();

    @NotNull
    BytecodeFieldType toBytecodeFieldType();
}
