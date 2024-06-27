package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.io.JsonStringWriter;
import indi.um.util.reflect.ClassUtility;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

/**
 * Created on 2022/6/5.
 */
class Setter implements JsonStructure {

    static final Setter INSTANCE = new Setter();

    protected void invoke(Object self, Object value) {
        // discard
    }

    /**
     * @param lookup not null
     * @return not this if unreflect successes
     */
    protected Setter unreflect(MethodHandles.Lookup lookup) {
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
