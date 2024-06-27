package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.entity.JsonArray;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.json.io.JsonStringWriter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

/**
 * Created in webbiton on 2021/3/22, named RegistryArrayField.
 * Recreated in infrastructure on 2021/12/31.
 * Recreated on 2022/7/23.
 */
class JsonArrayType extends JsonCollectionType {

    private static final long serialVersionUID = 0xFFC61BEFDC9ACE59L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonArrayType.class);

    Class<?> componentClass;

    JsonArrayType(JsonType componentType) {
        super();
        this.componentType = componentType;
    }

    /**
     * @param clazz may be null
     * @return true if need generic info
     */
    @Override
    protected boolean linkCollectClass(Class<?> clazz) {
        if (clazz != null) {
            componentClass = clazz.getComponentType();
        }
        return componentType.linkCollectClass(componentClass);
    }

    @Override
    protected void linkCollectGenericType(Type genericType) {
        LOGGER.info("linkCollectGenericType {} {}", this, genericType);
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        if (js.add(value)) {
            try {
                int length = Array.getLength(value);
                if (length != 0) {
                    jc.optionalKey(key);
                    jc.openArray();
                    for (int index = 0; index < length; index++) {
                        js.thisObject = value;
                        componentType.serialize((String) null, Array.get(value, index), jc, js);
                    }
                    jc.closeArray();
                } else {
                    if (anySerializeConfig(SerializeFrom.EMPTY_ARRAY, true)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty array to undefined in {}", js);
                        }
                        return;
                    }
                    if (anySerializeConfig(SerializeFrom.EMPTY_ARRAY, false)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty array to null in {}", js);
                        }
                        serializeNull(key, jc);
                        return;
                    }
                    jc.optionalKey(key);
                    jc.arrayValue();
                }
            } finally {
                js.remove(value);
            }
        } else {
            if (anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic array to undefined in {}", js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic array to undefined in {}", js);
                }
                serializeNull(key, jc);
                return;
            }
            throw new IllegalArgumentException("Cyclic array");
        }
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        if (isObject) {
            if (anyParseConfig(ParseHint.WRAP_SINGLE_VALUE)) {
                return new SingleValueFrame(componentType.frame(null, null, true, jv));
            }
            return ConverterFrame.INSTANCE;
        }
        final ArrayFrame frame = new ArrayFrame();
        if (anyParseConfig(ParseHint.APPEND_ARRAY)) {
            Object array = read.invoke(self, null);
            if (array != null && array.getClass().isArray()) {
                int length = Array.getLength(array);
                for (int index = 0; index < length; index++) {
                    frame.list.add(Array.get(array, index));
                }
            }
        }
        return frame;
    }

    /**
     * Created in webbiton on 2021/3/23, named JsonArrayLayer.
     * Recreated in infrastructure on 2022/3/24.
     * Recreated on 2022/7/23.
     */
    class ArrayFrame extends ConverterFrame {

        final JsonArray list = new JsonArray();

        @Override
        protected Object finish(JsonParser jp) {
            final int length = list.size();
            final Object array = Array.newInstance(componentClass, length);
            for (int index = 0; index < length; index++) {
                Array.set(array, index, list.get(index));
            }
            return array;
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            return componentType.frame(isObject, jv);
        }

        @Override
        protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
            add(value, jv);
        }

        @Override
        protected void nullValue(JsonConverter jv) {
            add(componentType.parseNull(jv), jv);
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            add(componentType.parseBoolean(value, jv), jv);
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            add(componentType.parseNumber(value, jv), jv);
        }

        @Override
        protected void numberValue(double value, JsonConverter jv) {
            add(componentType.parseNumber(value, jv), jv);
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            add(componentType.parseString(value, jv), jv);
        }

        void add(Object value, JsonConverter jv) {
            if (value instanceof JsonConstant) {
                if (jv.logEnabled()) {
                    LOGGER.warn("Unexpected value {} at array index {} while parsing {}", value, list.size(), jv);
                }
            } else {
                list.add(value);
            }
        }

        @Override
        public String toString() {
            return JsonStringWriter.stringify("JsonArrayType.Frame", JsonArrayType.this, null);
        }
    }

    @Override
    protected Object parseSingleValue(Object value, JsonConverter jv) {
        if (!anyParseConfig(ParseHint.WRAP_SINGLE_VALUE) || value == JsonConstant.UNDEFINED) {
            return JsonConstant.UNDEFINED;
        }
        final Object array = Array.newInstance(componentClass, 1);
        Array.set(array, 0, value);
        return array;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("componentClass");
        jc.stringValue(componentClass.getName());
    }

    @Override
    public String typeName() {
        return "array";
    }
}
