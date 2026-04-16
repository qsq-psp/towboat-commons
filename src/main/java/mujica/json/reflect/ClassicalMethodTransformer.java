package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@CodeHistory(date = "2026/4/9")
class ClassicalMethodTransformer extends ReflectedTransformer {

    @NotNull
    final Method method;

    @NotNull
    final Object instance;

    ClassicalMethodTransformer(@NotNull Method method, @NotNull Object instance) {
        super();
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        try {
            method.invoke(instance, in, out, context);
        } catch (Throwable e) {
            context.getLogger().error("", e);
        }
    }
}
