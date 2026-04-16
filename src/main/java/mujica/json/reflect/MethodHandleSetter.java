package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;

@CodeHistory(date = "2022/7/6", project = "Ultramarine")
@CodeHistory(date = "2025/12/15")
class MethodHandleSetter extends Setter {

    @NotNull
    final MethodHandle handle;

    MethodHandleSetter(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    protected void set(@Nullable Object self, @Nullable Object value) throws Throwable {
        handle.invoke(self, value);
    }

    @NotNull
    @Override
    ReflectedTransformer bind(@NotNull JsonContextTransformer<?> instance) {
        return new MethodHandleTransformer(handle.bindTo(instance));
    }
}
