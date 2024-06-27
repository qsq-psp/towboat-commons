package indi.qsq.json.entity;

import indi.qsq.json.api.FrameStructure;
import indi.qsq.json.reflect.ConverterFrame;
import indi.qsq.json.reflect.JsonConverter;
import indi.qsq.json.reflect.JsonParser;
import indi.qsq.json.reflect.MergeFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * Created on 2022/6/4.
 */
public class JsonObject extends HashMap<String, Object> implements FrameStructure {

    private static final long serialVersionUID = 0x2C7F0FA3EA0712A5L;

    @NotNull
    public static JsonObject requireNotNull(@Nullable JsonObject obj) {
        if (obj == null) {
            obj = new JsonObject();
        }
        return obj;
    }

    public JsonObject() {
        super();
    }

    @Nullable
    public JsonObject getPrototype() {
        return null;
    }

    @Nullable
    public JsonObject getRootPrototype() {
        JsonObject object = this;
        while (true) {
            JsonObject prototype = object.getPrototype();
            if (prototype != null) {
                object = prototype;
            } else {
                return null;
            }
        }
    }

    public boolean getBooleanValue(@NotNull String key) throws ClassCastException {
        return (Boolean) get(key);
    }

    public boolean removeBooleanValue(@NotNull String key) throws ClassCastException {
        return (Boolean) remove(key);
    }

    public boolean getBoolean(@NotNull String key, boolean fallback) {
        final Object obj = get(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            if (obj instanceof Number) {
                return ((Number) obj).intValue() != 0;
            }
            if (obj instanceof JsonArray) {
                return ((JsonArray) obj).size() != 0;
            }
            return true;
        }
        return fallback;
    }

    public boolean removeBoolean(@NotNull String key, boolean fallback) {
        final Object obj = remove(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            if (obj instanceof Number) {
                return ((Number) obj).intValue() != 0;
            }
            if (obj instanceof JsonArray) {
                return ((JsonArray) obj).size() != 0;
            }
            return true;
        }
        return fallback;
    }

    public int getIntValue(@NotNull String key) {
        return (Integer) get(key);
    }

    public int getInt(@NotNull String key, int fallback) {
        final Object obj = get(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj ? 1 : 0;
            }
            if (obj instanceof Number) {
                return ((Number) obj).intValue();
            }
            if (obj instanceof JsonArray) {
                return ((JsonArray) obj).size();
            }
        }
        return fallback;
    }

    public long getLongValue(@NotNull String key) {
        return (Long) get(key);
    }

    public long getLong(@NotNull String key, long fallback) {
        final Object obj = get(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj ? 1L : 0L;
            }
            if (obj instanceof Number) {
                return ((Number) obj).longValue();
            }
            if (obj instanceof JsonArray) {
                return ((JsonArray) obj).size();
            }
        }
        return fallback;
    }

    public double getDoubleValue(@NotNull String key) {
        return (Double) get(key);
    }

    public double getDouble(@NotNull String key, double fallback) {
        final Object obj = get(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj ? 1L : 0L;
            }
            if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            }
        }
        return fallback;
    }

    public String getStringValue(@NotNull String key) throws ClassCastException {
        return (String) get(key);
    }

    public String removeStringValue(@NotNull String key) throws ClassCastException {
        return (String) remove(key);
    }

    public String getString(@NotNull String key, @Nullable String fallback) {
        final Object obj = get(key);
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return fallback;
        }
    }

    public String removeString(@NotNull String key, @Nullable String fallback) {
        final Object obj = remove(key);
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return fallback;
        }
    }

    @Nullable
    public String getToString(@NotNull String key) {
        final Object obj = get(key);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    public Object construct(Class<?> clazz) throws ReflectiveOperationException {
        CONSTRUCTORS:
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() != size()) {
                continue;
            }
            Parameter[] formalParameters = constructor.getParameters();
            for (Parameter parameter : formalParameters) {
                if (!containsKey(parameter.getName())) {
                    continue CONSTRUCTORS;
                }
            }
            int length = formalParameters.length;
            Object[] actualParameters = new Object[length];
            for (int index = 0; index < length; index++) {
                actualParameters[index] = get(formalParameters[index].getName());
            }
            return constructor.newInstance(actualParameters);
        }
        throw new InstantiationException();
    }

    @Override
    public ConverterFrame frame(boolean isObject, JsonConverter jv) {
        if (isObject) {
            return new ObjectNameFrame();
        } else {
            return new ObjectIndexFrame();
        }
    }

    class ObjectNameFrame extends MergeFrame {

        private String name;

        @Override
        protected Object finish(JsonParser jp) {
            return JsonObject.this;
        }

        @Override
        protected void key(String key, JsonParser jp) {
            this.name = key;
        }

        @Override
        protected void anyValue(Object value, JsonConverter jv) {
            final String key = this.name;
            if (key == null) {
                return;
            }
            this.name = null;
            if (value instanceof JsonConstant) {
                return;
            }
            put(key, value);
        }
    }

    class ObjectIndexFrame extends MergeFrame {

        private int index;

        @Override
        protected Object finish(JsonParser jp) {
            return JsonObject.this;
        }

        @Override
        protected void anyValue(Object value, JsonConverter jv) {
            if (!(value instanceof JsonConstant)) {
                put(Integer.toString(index), value);
            }
            index++; // A JsonConstant leaves a empty slot
        }
    }
}
