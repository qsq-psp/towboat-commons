package indi.qsq.json.reflect;

import indi.qsq.json.entity.JsonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created on 2022/7/6.
 */
class MethodHandleSetter extends Setter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleSetter.class);

    final MethodHandle handle;

    MethodHandleSetter(MethodHandle handle) {
        super();
        this.handle = handle;
    }

    @Override
    protected void invoke(Object self, Object value) {
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
