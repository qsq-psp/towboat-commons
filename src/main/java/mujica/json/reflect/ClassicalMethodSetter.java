package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected void invoke(@Nullable Object self, @Nullable Object value) {
        try {
            method.invoke(self, value);
        } catch (ReflectiveOperationException e) {
            // logger
        }
    }
}
