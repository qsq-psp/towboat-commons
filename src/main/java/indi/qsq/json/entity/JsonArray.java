package indi.qsq.json.entity;

import indi.qsq.json.api.FrameStructure;
import indi.qsq.json.reflect.ConverterFrame;
import indi.qsq.json.reflect.JsonConverter;
import indi.qsq.json.reflect.JsonParser;
import indi.qsq.json.reflect.MergeFrame;
import indi.qsq.util.ds.TruncateList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created on 2022/6/4.
 */
public class JsonArray extends TruncateList<Object> implements FrameStructure {

    private static final long serialVersionUID = 0x326E01ADA4BA3C92L;

    @NotNull
    public static JsonArray of(@Nullable Object obj) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add(obj);
        return jsonArray;
    }

    @SuppressWarnings({"ManualArrayToCollectionCopy", "UseBulkOperation"})
    @NotNull
    public static JsonArray of(Object... objects) {
        final JsonArray jsonArray = new JsonArray();
        for (Object obj : objects) {
            jsonArray.add(obj);
        }
        return jsonArray;
    }

    @NotNull
    public static JsonArray requireNonNull(@Nullable JsonArray obj) {
        if (obj == null) {
            obj = new JsonArray();
        }
        return obj;
    }

    @NotNull
    public static JsonArray requireWrapped(@Nullable Object obj) {
        if (obj instanceof JsonArray) {
            return (JsonArray) obj;
        }
        return of(obj);
    }

    @NotNull
    public static JsonArray requireUpdated(@Nullable Collection<?> list) {
        if (list instanceof JsonArray) {
            return (JsonArray) list;
        }
        final JsonArray jsonArray = new JsonArray();
        if (list != null) {
            jsonArray.addAll(list);
        }
        return jsonArray;
    }

    public JsonArray() {
        super();
    }

    public boolean getBooleanValue(int index) {
        return (Boolean) get(index);
    }

    public int getIntValue(int index) {
        return (Integer) get(index);
    }

    public long getLongValue(int index) {
        return (Long) get(index);
    }

    public double getDoubleValue(int index) {
        return (Double) get(index);
    }

    public String getStringValue(int index) {
        return (String) get(index);
    }

    public String getString(int index) {
        final Object obj = get(index);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    @NotNull
    public String[] toStringArray() {
        final int length = size();
        final String[] array = new String[length];
        for (int index = 0; index < length; index++) {
            array[index] = String.valueOf(get(index)); // handles null
        }
        return array;
    }

    @NotNull
    public JsonArray flat(Class<? extends Iterable<?>> filter, int depth) {
        final JsonArray out = new JsonArray();
        flat(this, out, filter, depth);
        return out;
    }

    public static void flat(@NotNull Iterable<?> in, @NotNull JsonArray out, @NotNull Class<? extends Iterable<?>> filter, int depth) {
        if (depth > 0) {
            depth--;
            for (Object item : in) {
                if (filter.isInstance(item)) {
                    flat((Iterable<?>) item, out, filter, depth);
                } else {
                    out.add(item);
                }
            }
        } else if (in instanceof Collection<?>) {
            out.addAll((Collection<?>) in);
        } else {
            for (Object item : in) {
                out.add(item);
            }
        }
    }

    @NotNull
    public JsonArray flatMap(@NotNull Function<Object, Iterable<?>> function) {
        final JsonArray out = new JsonArray();
        for (Object item0 : this) {
            for (Object item1 : function.apply(item0)) {
                out.add(item1);
            }
        }
        return out;
    }

    public Object construct(Class<?> clazz) throws ReflectiveOperationException {
        CONSTRUCTORS:
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() != size()) {
                continue;
            }
            Class<?>[] formalParameters = constructor.getParameterTypes();
            int length = formalParameters.length;
            for (int index = 0; index < length; index++) {
                Object value = get(index);
                if (value == null || value.getClass() != formalParameters[index]) {
                    continue CONSTRUCTORS;
                }
            }
            return constructor.newInstance(toArray());
        }
        throw new InstantiationException();
    }

    @Override
    public ConverterFrame frame(boolean isObject, JsonConverter jv) {
        if (isObject) {
            return ConverterFrame.INSTANCE;
        } else {
            return new ArrayIndexFrame();
        }
    }

    class ArrayIndexFrame extends MergeFrame {

        @Override
        protected Object finish(JsonParser jp) {
            return JsonArray.this;
        }

        @Override
        protected void anyValue(Object value, JsonConverter jv) {
            if (value == JsonConstant.UNDEFINED) {
                return;
            }
            add(value);
        }
    }
}
