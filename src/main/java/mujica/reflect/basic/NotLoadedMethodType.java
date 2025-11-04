package mujica.reflect.basic;

import mujica.ds.HealthAware;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.util.function.Consumer;

@CodeHistory(date = "2025/9/10")
public interface NotLoadedMethodType<F extends TypeDescriptor.OfField<F>, M extends NotLoadedMethodType<F, M>> extends HealthAware, TypeDescriptor.OfMethod<F, M> {

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    String toRealSourceString(@NotNull String methodName);

    @NotNull
    String toSourceString(@NotNull String methodName);

    @NotNull
    String toBytecodeString();

    @NotNull
    MethodType toMethodType() throws ClassNotFoundException;

    @NotNull
    SourceMethodType toSourceMethodType();

    @NotNull
    BytecodeMethodType toBytecodeMethodType();
}
