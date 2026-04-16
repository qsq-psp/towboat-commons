package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;

@CodeHistory(date = "2022/6/19", project = "Ultramarine")
@CodeHistory(date = "2025/12/15")
class MethodHandleGetter extends Getter {

    @NotNull
    final MethodHandle handle;

    MethodHandleGetter(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    protected Object get(@Nullable Object self) throws Throwable {
        return handle.invoke(self);
    }
}
