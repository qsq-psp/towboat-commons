package mujica.json.reflect;

import mujica.json.handler.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;

/**
 * Created on 2026/4/9.
 */
@CodeHistory(date = "2026/4/9")
class MethodHandleTransformer extends ReflectedTransformer {

    @NotNull
    final MethodHandle handle;

    MethodHandleTransformer(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        try {
            handle.invoke(in, out, context);
        } catch (Throwable e) {
            context.getLogger().error("", e);
        }
    }
}
