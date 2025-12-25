package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;

@CodeHistory(date = "2022/6/19", project = "Ultramarine")
@CodeHistory(date = "2025/12/15")
class MethodHandleGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleGetter.class);

    @NotNull
    final MethodHandle handle;

    MethodHandleGetter(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    protected Object invoke(@Nullable Object self) {
        try {
            return handle.invoke(self);
        } catch (Throwable e) {
            LOGGER.warn("", e);
            return super.invoke(self);
        }
    }
}
