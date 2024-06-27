package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.value.ClassValueSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * Created on 2022/6/19.
 */
class FieldReflectGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldReflectGetter.class);

    @NotNull
    final Field field;

    FieldReflectGetter(@NotNull Field field) {
        super();
        this.field = field;
    }

    @Override
    public Object invoke(Object self, JsonConverter jv) {
        try {
            return field.get(self);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("", e);
            return super.invoke(self, jv);
        }
    }

    /**
     * For reflect constructor only
     */
    @Override
    protected Class<?> type() {
        return field.getType();
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    @Override
    protected Getter unreflect(MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleGetter(lookup.unreflectGetter(field));
        } catch (Exception e) {
            LOGGER.warn("{}", field, e);
            return this;
        }
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.openObject();
        jc.key("type");
        jc.stringValue("FieldReflectGetter");
        jc.key("field");
        jc.stringValue(field.getName());
        ClassValueSerializer.INSTANCE.serialize("class", field.getDeclaringClass(), jc);
        jc.closeObject();
    }
}
