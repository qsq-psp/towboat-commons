package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.value.ClassValueSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

/**
 * Created on 2022/6/4.
 */
class ConstructorReflectGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructorReflectGetter.class);

    @NotNull
    final Constructor<?> constructor;

    ConstructorReflectGetter(@NotNull Constructor<?> constructor) {
        super();
        this.constructor = constructor;
    }

    @Override
    public Object invoke(Object self, JsonConverter jv) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("", e);
            return super.invoke(self, jv);
        }
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    @Override
    protected Getter unreflect(MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleGetter(lookup.unreflectConstructor(constructor));
        } catch (Exception e) {
            LOGGER.warn("{}", constructor, e);
            return this;
        }
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.openObject();
        jc.key("type");
        jc.stringValue("ConstructorReflectGetter");
        jc.key("parameters");
        jc.openArray();
        for (Class<?> parameterClass : constructor.getParameterTypes()) {
            ClassValueSerializer.INSTANCE.serialize(null, parameterClass, jc);
        }
        jc.closeArray();
        ClassValueSerializer.INSTANCE.serialize("class", constructor.getDeclaringClass(), jc);
        jc.closeObject();
    }
}
