package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "MethodReflectGetter")
@CodeHistory(date = "2025/11/19")
class ClassicalMethodGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassicalMethodGetter.class);

    @NotNull
    final Method method;

    ClassicalMethodGetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    protected Object invoke(@Nullable Object self) {
        try {
            return method.invoke(self);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("{}", method, e);
            return super.invoke(self);
        }
    }

    @NotNull
    @Override
    protected Getter tryUnreflect(@NotNull MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleGetter(lookup.unreflect(method));
        } catch (Exception e) {
            LOGGER.warn("{}", method, e);
            return super.tryUnreflect(lookup);
        }
    }
}
