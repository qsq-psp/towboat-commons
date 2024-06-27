package indi.um.json.reflect;

import indi.um.json.entity.JsonConstant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * Created on 2022/6/19.
 */
class FieldReflectSetter extends Setter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldReflectSetter.class);

    @NotNull
    final Field field;

    FieldReflectSetter(@NotNull Field field) {
        super();
        this.field = field;
    }

    @Override
    protected void invoke(Object self, Object value) {
        if (value == JsonConstant.UNDEFINED) {
            return;
        }
        try {
            field.set(self, value);
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
            return new MethodHandleSetter(lookup.unreflectSetter(field));
        } catch (Exception e) {
            LOGGER.warn("{}", field, e);
            return this;
        }
    }
}
