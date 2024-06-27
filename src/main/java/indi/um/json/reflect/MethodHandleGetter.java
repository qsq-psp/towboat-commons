package indi.um.json.reflect;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;

/**
 * Created on 2022/6/19.
 */
class MethodHandleGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleGetter.class);

    @NotNull
    final MethodHandle handle;

    MethodHandleGetter(@NotNull MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    public Object invoke(Object self, JsonConverter jv) {
        try {
            return handle.invoke(self);
        } catch (Throwable e) {
            LOGGER.warn("", e);
            return super.invoke(self, jv);
        }
    }
}
