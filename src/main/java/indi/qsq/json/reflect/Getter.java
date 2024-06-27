package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.json.io.JsonStringWriter;
import indi.qsq.util.reflect.ClassUtility;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

/**
 * Created on 2022/6/5.
 */
class Getter implements JsonStructure {

    static final Getter INSTANCE = new Getter();

    Getter() {
        super();
    }

    protected Object invoke(Object self, JsonConverter jv) {
        return JsonConstant.UNDEFINED; // represents error
    }

    /**
     * For reflect constructor only
     */
    protected Class<?> type() {
        return null;
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    protected Getter unreflect(MethodHandles.Lookup lookup) {
        return this;
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.stringValue(getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return JsonStringWriter.stringify(ClassUtility.normalNonNull(this), this, null);
    }
}
