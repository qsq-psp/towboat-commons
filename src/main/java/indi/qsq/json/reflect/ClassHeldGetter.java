package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.value.ClassValueSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Created on 2022/8/27.
 */
class ClassHeldGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHeldGetter.class);

    @NotNull
    final Class<?> clazz;

    public ClassHeldGetter(@NotNull Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object self, JsonConverter jv) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("{}", clazz, e);
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
            return new MethodHandleGetter(lookup.unreflectConstructor(clazz.getConstructor()));
        } catch (Exception e) {
            LOGGER.warn("{}", clazz, e);
            return this;
        }
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.openObject();
        jc.key("type");
        jc.stringValue("ClassHeldGetter");
        ClassValueSerializer.INSTANCE.serialize("class", clazz, jc);
        jc.closeObject();
    }
}
