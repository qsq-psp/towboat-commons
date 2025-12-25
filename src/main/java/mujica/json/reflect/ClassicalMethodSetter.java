package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "MethodReflectSetter")
@CodeHistory(date = "2025/11/19")
class ClassicalMethodSetter extends Setter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassicalMethodGetter.class);

    @NotNull
    final Method method;

    ClassicalMethodSetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    protected void invoke(@Nullable Object self, @Nullable Object value) {
        try {
            method.invoke(self, value);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("{}", method, e);
        }
    }

    @NotNull
    @Override
    protected Setter tryUnreflect(@NotNull MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleSetter(lookup.unreflect(method));
        } catch (Exception e) {
            LOGGER.warn("{}", method, e);
            return this;
        }
    }
}
