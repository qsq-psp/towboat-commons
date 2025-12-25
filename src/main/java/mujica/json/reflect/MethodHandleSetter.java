package mujica.json.reflect;

import mujica.json.entity.JsonConstant;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;

@CodeHistory(date = "2022/7/6", project = "Ultramarine")
@CodeHistory(date = "2025/12/15")
class MethodHandleSetter extends Setter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleSetter.class);

    @NotNull
    final MethodHandle handle;

    MethodHandleSetter(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    protected void invoke(@Nullable Object self, @Nullable Object value) {
        if (value == JsonConstant.UNDEFINED) {
            return;
        }
        try {
            handle.invoke(self, value);
        } catch (Throwable e) {
            LOGGER.warn("", e);
        }
    }
}
