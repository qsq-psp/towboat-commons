package indi.um.json.reflect;

import indi.um.json.entity.JsonConstant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * Created on 2022/6/19.
 */
class MethodReflectSetter extends Setter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodReflectSetter.class);

    @NotNull
    final Method method;

    MethodReflectSetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    protected void invoke(Object self, Object value) {
        if (value == JsonConstant.UNDEFINED) {
            return;
        }
        try {
            method.invoke(self, value);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("", e);
        }
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    @Override
    protected Setter unreflect(MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleSetter(lookup.unreflect(method));
        } catch (Exception e) {
            LOGGER.warn("{}", method, e);
            return this;
        }
    }
}
