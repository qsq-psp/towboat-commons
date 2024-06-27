package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.entity.JsonConstant;
import indi.um.json.value.ClassValueSerializer;
import indi.um.util.reflect.ClassUtility;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * Created on 2022/6/19.
 */
class MethodReflectGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodReflectGetter.class);

    @NotNull
    final Method method;

    MethodReflectGetter(@NotNull Method method) {
        super();
        this.method = method;
    }

    @Override
    public Object invoke(Object self, JsonConverter jv) {
        try {
            return method.invoke(self);
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("", e);
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Class<?> type() {
        return method.getReturnType();
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    @Override
    protected Getter unreflect(MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleGetter(lookup.unreflect(method));
        } catch (Exception e) {
            LOGGER.warn("{}", method, e);
            return this;
        }
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.openObject();
        jc.key("type");
        jc.stringValue(ClassUtility.normalNonNull(this));
        jc.key("method");
        jc.stringValue(method.getName());
        ClassValueSerializer.INSTANCE.serialize("class", method.getDeclaringClass(), jc);
        jc.closeObject();
    }

    static class WithParserParameter extends MethodReflectGetter {

        WithParserParameter(Method method) {
            super(method);
        }

        @Override
        public Object invoke(Object self, JsonConverter jv) {
            try {
                return method.invoke(self, jv);
            } catch (ReflectiveOperationException e) {
                LOGGER.warn("", e);
                return JsonConstant.UNDEFINED;
            }
        }
    }
}
