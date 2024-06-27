package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.SerializeFrom;
import indi.um.json.entity.JsonConstant;
import indi.um.util.text.IdentifierFormat;
import indi.um.util.text.IdentifierReformat;
import indi.um.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;

/**
 * Created on 2022/8/12.
 */
class JsonEnumType extends JsonType {

    private static final long serialVersionUID = 0x43E916A99D6CCE1DL;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonEnumType.class);

    final HashMap<Object, Object> map = new HashMap<>();

    Object defaultValue; // Actual type : Enum

    JsonEnumType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonEnumType clone() {
        final JsonEnumType that = new JsonEnumType();
        that.setJsonType(this);
        that.map.putAll(this.map);
        return that;
    }

    @Override
    protected boolean linkCollectClass(Class<?> clazz) {
        map.put(Enum.class, clazz);
        return false;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        final Class<?> clazz = (Class<?>) map.get(Enum.class);
        map.remove(Enum.class);
        if (clazz == null) {
            return;
        }
        final IdentifierReformat reformat = element.getAnnotation(IdentifierReformat.class);
        try {
            Enum<?>[] values = (Enum<?>[]) clazz.getMethod("values").invoke(null); // java arrays are covariant
            for (Enum<?> value : values) {
                String name = value.name();
                String reformatted = IdentifierFormat.reformat(name, reformat);
                map.put(reformatted, value);
                map.put(value, reformatted);
            }
        } catch (Throwable e) {
            LOGGER.warn("AnyEnum values reflection from {}", element, e);
        }
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        if (anySerializeConfig((SerializeFrom.DEFAULT_ENUM << UNDEFINED_SHIFT) | SerializeFrom.DEFAULT_ENUM) && defaultValue == value) {
            if (anySerializeConfig(SerializeFrom.DEFAULT_ENUM, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize default enum {} to undefined in {}", value, js);
                }
            } else {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize default enum {} to undefined in {}", value, js);
                }
                serializeNull(key, jc);
            }
            return;
        }
        final Object string = map.get(value);
        if (string instanceof String) {
            jc.optionalKey(key);
            jc.stringValue((String) string);
        } else if (anySerializeConfig((SerializeFrom.OUT_ENUM << UNDEFINED_SHIFT) | SerializeFrom.OUT_ENUM)) {
            if (anySerializeConfig(SerializeFrom.OUT_ENUM, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize out enum {} to undefined in {}", value, js);
                }
            } else {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize out enum {} to null in {}", value, js);
                }
                serializeNull(key, jc);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        Object parsed = map.get(value); // Actual type : enum
        if (parsed != null) {
            return parsed;
        }
        parsed = defaultValue; // Actual type : enum
        if (parsed != null) {
            return parsed;
        }
        if (jv.logEnabled()) {
            LOGGER.debug("Fail to parse string value {} to enum in {}", Quote.DEFAULT.apply(value), jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("size");
        jc.numberValue(map.size());
    }

    @Override
    public String typeName() {
        return "enum";
    }
}
