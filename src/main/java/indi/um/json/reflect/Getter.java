package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.entity.JsonConstant;
import indi.um.json.io.JsonStringWriter;
import indi.um.util.reflect.ClassUtility;
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
