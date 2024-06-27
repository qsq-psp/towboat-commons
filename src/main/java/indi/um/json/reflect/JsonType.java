package indi.um.json.reflect;

import indi.um.json.api.*;
import indi.um.json.entity.JsonArray;
import indi.um.json.entity.JsonConstant;
import indi.um.json.entity.JsonObject;
import indi.um.json.entity.RawNumber;
import indi.um.json.io.JsonStringWriter;
import indi.um.json.value.ArrayValueSerializer;
import indi.um.json.value.IterableValueSerializer;
import indi.um.json.value.MapValueSerializer;
import indi.um.util.reflect.ClassUtility;
import indi.um.util.text.TypedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 2022/6/5.
 */
class JsonType extends ConversionConfig implements FrameStructure {

    private static final long serialVersionUID = 0x41CDA8FAED8E4D97L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonType.class);

    Object specificToUndefined;

    Object specificToNull;

    JsonType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonType clone() {
        final JsonType that = new JsonType();
        that.setJsonType(this);
        return that;
    }

    void setJsonType(JsonType that) {
        setConversionConfig(that.serializeConfig, that.parseConfig);
        this.specificToUndefined = that.specificToUndefined;
        this.specificToNull = that.specificToNull;
    }

    /**
     * @param clazz may be null
     * @return true if need generic info
     */
    protected boolean linkCollectClass(@Nullable Class<?> clazz) {
        return false;
    }

    protected void linkCollectGenericType(Type genericType) {
        // pass
    }

    protected void linkSetConversionConfig(@NotNull ConversionConfig that) {
        setConversionConfig(that);
    }

    protected void linkCollectAnnotation(AnnotatedElement element, int layer) {
        if (layer == 0) {
            collectConfigs(element);
        }
        collectAnnotation(element, layer);
    }

    void collectConfigs(@NotNull AnnotatedElement element) {
        final SerializeToUndefined serializeToUndefined = element.getAnnotation(SerializeToUndefined.class);
        if (serializeToUndefined != null) {
            int config = serializeToUndefined.value();
            if ((config & SerializeFrom.SPECIFIC) != 0) {
                try {
                    specificToUndefined = (new JsonParser()).parseOrDereference(serializeToUndefined.specific());
                } catch (RuntimeException e) {
                    LOGGER.warn("Fail to parse specific value to undefined of {}", element, e); // Throwable is excluded from formats
                    config &= ~SerializeFrom.SPECIFIC;
                }
            }
            serializeConfig = (serializeConfig & ~(SerializeFrom.MASK << UNDEFINED_SHIFT)) | ((config & SerializeFrom.MASK) << UNDEFINED_SHIFT);
        }
        final SerializeToNull serializeToNull = element.getAnnotation(SerializeToNull.class);
        if (serializeToNull != null) {
            int config = serializeToNull.value();
            if ((config & SerializeFrom.SPECIFIC) != 0) {
                try {
                    specificToNull = (new JsonParser()).parseOrDereference(serializeToNull.specific());
                } catch (RuntimeException e) {
                    LOGGER.warn("Fail to parse specific value to null of {}", element, e); // Throwable is excluded from formats
                    config &= ~SerializeFrom.SPECIFIC;
                }
            }
            serializeConfig = (serializeConfig & ~SerializeFrom.MASK) | (config & SerializeFrom.MASK);
        }
        final ParseHint parseHint = element.getAnnotation(ParseHint.class);
        if (parseHint != null) {
            parseConfig = parseHint.value();
        }
    }

    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        // LOGGER.info("collectAnnotation {}", element);
        // pass
    }

    protected JsonType finishCollection() {
        return this;
    }

    void serialize(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        if (anySerializeConfig(((SerializeFrom.NULL | SerializeFrom.SPECIFIC) << UNDEFINED_SHIFT) | (SerializeFrom.NULL | SerializeFrom.SPECIFIC))) {
            if (value == null && anySerializeConfig((SerializeFrom.NULL << UNDEFINED_SHIFT) | SerializeFrom.NULL)) {
                if (anySerializeConfig(SerializeFrom.NULL, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize null to undefined in {}", js);
                    }
                } else {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize null to null in {}", js);
                    }
                    serializeNull(key, jc);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.SPECIFIC, true) && Objects.equals(specificToUndefined, value)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize specific {} to undefined in {}", value, js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.SPECIFIC, false) && Objects.equals(specificToNull, value)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize specific {} to null in {}", value, js);
                }
                serializeNull(key, jc);
                return;
            }
        }
        if (value != null) {
            serializeValue(key, value, jc, js);
        } else {
            serializeNull(key, jc);
        }
    }

    void serialize(TypedString key, Object value, JsonConsumer jc, JsonSerializer js) {
        if (anySerializeConfig(((SerializeFrom.NULL | SerializeFrom.SPECIFIC) << UNDEFINED_SHIFT) | (SerializeFrom.NULL | SerializeFrom.SPECIFIC))) {
            if (value == null && anySerializeConfig((SerializeFrom.NULL << UNDEFINED_SHIFT) | SerializeFrom.NULL)) {
                if (anySerializeConfig(SerializeFrom.NULL, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize null to undefined in {}", js);
                    }
                } else {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize null to null in {}", js);
                    }
                    serializeNull(key, jc);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.SPECIFIC, true) && Objects.equals(specificToUndefined, value)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize specific {} to undefined in {}", value, js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.SPECIFIC, false) && Objects.equals(specificToNull, value)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize specific {} to null in {}", value, js);
                }
                serializeNull(key, jc);
                return;
            }
        }
        if (value != null) {
            if (value instanceof JsonConstant) {
                if (js.logEnabled()) {
                    LOGGER.warn("Serialize JsonConstant {} in {}", value, js);
                }
                return;
            }
            if (key != null) {
                serializeValue(key.getString(), value, jc, js);
            } else {
                serializeValue(null, value, jc, js);
            }
        } else {
            serializeNull(key, jc);
        }
    }

    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        serializeObject(key, value, jc, this, js);
    }

    static void serializeObject(String key, Object value, JsonConsumer jc, ConversionConfig cc, JsonSerializer js) {
        if (value instanceof JsonStructure) {
            jc.optionalKey(key);
            ((JsonStructure) value).toJson(jc);
            return;
        }
        if (value instanceof Iterable) {
            IterableValueSerializer.INSTANCE.serialize(key, (Iterable<?>) value, jc, cc, js);
            return;
        }
        if (value instanceof Map) {
            MapValueSerializer.INSTANCE.serialize(key, (Map<?, ?>) value, jc, cc, js);
            return;
        }
        final Class<?> clazz = value.getClass();
        if (clazz.isArray()) {
            ArrayValueSerializer.INSTANCE.serialize(key, value, jc, cc, js);
            return;
        }
        ReflectClass.get(clazz).serialize(key, value, jc, cc, js);
    }

    void serializeNull(String key, JsonConsumer jc) {
        jc.optionalKey(key);
        jc.nullValue();
    }

    void serializeNull(TypedString key, JsonConsumer jc) {
        jc.optionalKey(key);
        jc.nullValue();
    }

    @Override
    public ConverterFrame frame(boolean isObject, JsonConverter jv) {
        return frame(null, Getter.INSTANCE, isObject, jv);
    }

    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        // in base JsonType, Object self is not used
        FrameStructure structure;
        if (isObject) {
            structure = new JsonObject();
        } else {
            structure = new JsonArray();
        }
        return structure.frame(isObject, jv);
    }

    ObjectFrame objectFrame(Object object) {
        return new ObjectFrame(object);
    }

    class ObjectFrame extends BottomFrame {

        protected ObjectFrame(Object object) {
            super(object);
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            return frame(object, SelfGetter.INSTANCE, isObject, jv);
        }

        @Override
        protected void nullValue(JsonConverter jv) {
            anyValue(parseNull(jv), jv);
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            anyValue(parseBoolean(value, jv), jv);
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            anyValue(parseNumber(value, jv), jv);
        }

        @Override
        protected void numberValue(double value, JsonConverter jv) {
            anyValue(parseNumber(value, jv), jv);
        }

        @Override
        protected void stringValue(String value, JsonConverter jv) {
            anyValue(parseString(value, jv), jv);
        }
    }

    protected Object parseNull(JsonConverter jv) {
        return null;
    }

    protected Object parseBoolean(boolean value, JsonConverter jv) {
        return value;
    }

    protected Object parseNumber(long value, JsonConverter jv) {
        return value;
    }

    protected Object parseNumber(double value, JsonConverter jv) {
        return value;
    }

    Object parseNumber(RawNumber value, JsonConverter jv) {
        try {
            if (value.value.indexOf('.') == -1) {
                parseNumber(value.longValue(), jv);
            } else {
                parseNumber(value.doubleValue(), jv);
            }
        } catch (ArithmeticException e) {
            if (jv.logEnabled()) {
                LOGGER.warn("");
            }
        }
        return JsonConstant.UNDEFINED;
    }

    protected Object parseString(String value, JsonConverter jv) {
        // System.out.println("JsonType.parseString " + value);
        return value;
    }

    protected Object parseAny(Object value, JsonConverter jv) {
        if (value == null) {
            return parseNull(jv);
        } else if (value instanceof String) {
            return parseString((String) value, jv);
        } else if (value instanceof Number) {
            String name = value.getClass().getSimpleName();
            if (name.startsWith("Double") || name.startsWith("Float")) {
                return parseNumber(((Number) value).doubleValue(), jv);
            } else {
                return parseNumber(((Number) value).longValue(), jv);
            }
        } else if (value instanceof Boolean) {
            return parseBoolean((Boolean) value, jv);
        } else {
            return value;
        }
    }

    public void toJsonEntries(@NotNull JsonConsumer jc) {
        jc.key("type");
        jc.stringValue(typeName());
        super.toJsonEntries(jc);
        jc.optionalObjectEntry("specificToUndefined", specificToUndefined);
        jc.optionalObjectEntry("specificToNull", specificToNull);
    }

    public String typeName() {
        return "any";
    }

    @Override
    public String toString() {
        return JsonStringWriter.stringify(ClassUtility.normalNonNull(this), this, null);
    }
}
