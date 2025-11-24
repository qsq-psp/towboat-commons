package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected Object invoke(@Nullable Object self) {
        try {
            return method.invoke(self);
        } catch (ReflectiveOperationException e) {
            // logger
            return super.invoke(self);
        }
    }
}
