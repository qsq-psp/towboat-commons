package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "MethodReflectGetter")
@CodeHistory(date = "2025/11/19")
class ClassicalMethodGetter extends Getter {

    @NotNull
    final Method method;

    ClassicalMethodGetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    protected Object get(Object self) throws Throwable {
        return method.invoke(self);
    }

    @NotNull
    @Override
    protected Getter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return new MethodHandleGetter(lookup.unreflect(method));
    }
}
