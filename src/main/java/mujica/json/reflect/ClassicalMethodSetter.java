package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "MethodReflectSetter")
@CodeHistory(date = "2025/11/19")
class ClassicalMethodSetter extends Setter {

    @NotNull
    final Method method;

    ClassicalMethodSetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    protected void set(Object self, @Nullable Object value) throws Throwable {
        method.invoke(self, value);
    }

    @NotNull
    @Override
    protected Setter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return new MethodHandleSetter(lookup.unreflect(method));
    }

    @NotNull
    @Override
    ReflectedTransformer bind(@NotNull JsonContextTransformer<?> instance) {
        return new ClassicalMethodTransformer(method, instance);
    }
}
