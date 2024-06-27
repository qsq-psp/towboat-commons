package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.util.text.Quote;
import indi.qsq.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * Created in webbiton on 2020/12/24, named RegistryStringToIntField.
 * Recreated in infrastructure on 2021/12/31.
 * Recreated on 2022/8/5.
 */
class JsonIntEnumType extends JsonIntegralType {

    private static final long serialVersionUID = 0x773DDE6D7B291DC4L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonIntEnumType.class);

    EnumMapping mapping;

    boolean copy;

    JsonIntEnumType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonIntEnumType clone() {
        final JsonIntEnumType that = new JsonIntEnumType();
        that.setJsonType(this);
        if (copy) {
            if (this.mapping != null) {
                that.mapping = new EnumMapping(this.mapping);
            }
        } else {
            that.mapping = this.mapping;
        }
        that.copy = this.copy;
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        if (this.mapping != null) {
            return;
        }
        final IntEnumValue intEnumValue = element.getAnnotation(IntEnumValue.class);
        if (intEnumValue != null) {
            collectAnnotation(intEnumValue);
            return;
        }
        final AnyEnum anyEnum = element.getAnnotation(AnyEnum.class);
        if (anyEnum != null) {
            collectAnnotation(anyEnum);
        }
    }

    void collectAnnotation(IntEnumValue intEnumValue) {
        final EnumMapping mapping = new EnumMapping(intEnumValue.caseInsensitive());
        final int[] integers = intEnumValue.integers();
        final String[] strings = intEnumValue.strings();
        final int length = Math.min(integers.length, strings.length);
        for (int index = 0; index < length; index++) {
            mapping.add(strings[index], integers[index]);
        }
        this.mapping = mapping;
    }

    void collectAnnotation(AnyEnum anyEnum) {
        final EnumMapping mapping = JsonParser.dereference(anyEnum.value(), EnumMapping.class);
        if (mapping != null) {
            if (anyEnum.copy()) {
                this.mapping = new EnumMapping(mapping);
                this.copy = true;
            } else {
                this.mapping = mapping;
                this.copy = false;
            }
        } else {
            LOGGER.warn("Fail to dereference {}", Quote.DEFAULT.apply(anyEnum.value()));
            this.mapping = new EnumMapping();
            this.copy = false;
        }
    }

    /**
     * @param key nullable
     * @param value actual type: Integer
     */
    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        if (anySerializeConfig((SerializeFrom.DEFAULT_ENUM << UNDEFINED_SHIFT) | SerializeFrom.DEFAULT_ENUM) && Objects.equals(mapping.getDefaultValue(), value)) {
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
        Object string = mapping.get(value);
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
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            if (anyParseConfig(ParseHint.APPLY_TRUNCATE) || Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
                int intValue = (int) value;
                if (anyParseConfig(ParseHint.ACCEPT_ENUM_OUT) || mapping.get(intValue) instanceof String) {
                    return intValue;
                } else if (jv.logEnabled()) {
                    LOGGER.debug("Discard out-map long value {} while parsing int-enum in {}", value, jv);
                }
            } else if (jv.logEnabled()) {
                LOGGER.debug("Discard outbound long value {} while parsing int-enum in {}", value, jv);
            }
        } else if (jv.logEnabled()) {
            LOGGER.debug("Discard long value {} while parsing int-enum in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        if (Double.isFinite(value)) {
            if (anyParseConfig(ParseHint.APPLY_ROUND_ZERO)) {
                return parseNumber((long) value, jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_FLOOR)) {
                return parseNumber((long) Math.floor(value), jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_CEIL)) {
                return parseNumber((long) Math.ceil(value), jv);
            }
            if (anyParseConfig(ParseHint.APPLY_ROUND_NEAR)) {
                return parseNumber(Math.round(value), jv);
            }
        }
        if (jv.logEnabled()) {
            LOGGER.debug("Fail to parse double value {} to int-enum in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        Object parsed = mapping.get(value); // Actual type : Integer
        if (parsed != null) {
            return parsed;
        }
        parsed = mapping.getDefaultValue(); // Actual type : Integer
        if (parsed != null) {
            return parsed;
        }
        if (jv.logEnabled()) {
            LOGGER.debug("Fail to parse string value {} to int-enum in {}", Quote.DEFAULT.apply(value), jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        EnumMapping map = this.mapping;
        if (map != null) {
            jc.key("map");
            map.toJson(jc);
        }
    }

    @Override
    public String typeName() {
        return "int-enum";
    }
}
