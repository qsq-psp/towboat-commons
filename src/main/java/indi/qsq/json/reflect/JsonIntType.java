package indi.um.json.reflect;

import indi.um.json.api.*;
import indi.um.json.entity.JsonConstant;
import indi.um.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;

/**
 * Created in webbiton on 2020/12/24, named RegistryIntField.
 * Recreated in va on 2021/12/24, named JsonIntField.
 * Recreated in infrastructure on 2021/12/31.
 * Recreated on 2022/6/10.
 */
class JsonIntType extends JsonIntegralType {

    private static final long serialVersionUID = 0x9044DFC48C3175EFL;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonIntType.class);

    int min = Integer.MIN_VALUE;

    int max = Integer.MAX_VALUE;

    JsonIntType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonIntType clone() {
        final JsonIntType that = new JsonIntType();
        that.setJsonType(this);
        that.min = this.min;
        that.max = this.max;
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        final IntValue intValue = element.getAnnotation(IntValue.class);
        if (intValue != null) {
            collectAnnotation(intValue);
        }
    }

    void collectAnnotation(IntValue intValue) {
        min = intValue.min();
        max = intValue.max();
        if (min > max) {
            LOGGER.warn("Bad int interval [{}, {}]", min, max);
        }
    }

    /**
     * Almost same with indi.um.json.value.IntegralValueSerializer.serialize(String key, Number value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        int x = (int) value;
        x = Math.max(min, Math.min(x, max));
        if (x == 0) {
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero integer to undefined in {}", js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero integer to null in {}", js);
                }
                serializeNull(key, jc);
                return;
            }
        }
        jc.optionalKey(key);
        jc.numberValue(x);
    }

    @Override
    protected Object parseNull(JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NULL)) {
            return null;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Parse null int value to undefined in {}", jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return value ? 1 : 0;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard boolean value {} while parsing integer in {}", value, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.APPLY_CLAMP)) {
            return (int) Math.max(min, Math.min(value, max));
        } else {
            if (anyParseConfig(ParseHint.APPLY_TRUNCATE)) {
                value = (int) value;
            }
            if (min <= value && value <= max) {
                return (int) value;
            } else {
                if (jv.logEnabled()) {
                    LOGGER.debug("Discard outbound value {} of [{}, {}] in {}", value, min, max, jv);
                }
                return JsonConstant.UNDEFINED;
            }
        }
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
            LOGGER.debug("Fail to parse double value {} to int in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        if (!anyParseConfig(ParseHint.ACCEPT_STRING)) {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard String value {} while parsing integer in {}", Quote.DEFAULT.apply(value), jv);
            }
            return JsonConstant.UNDEFINED;
        }
        final Long longValue = parseUnits(value, jv);
        if (longValue != null) {
            return parseNumber(longValue, jv);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        if (min != Integer.MIN_VALUE) {
            jc.key("min");
            jc.numberValue(min);
        }
        if (max != Integer.MAX_VALUE) {
            jc.key("max");
            jc.numberValue(max);
        }
    }

    @Override
    public String typeName() {
        return "int";
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 0x2f + min) * 0x2f + max;
    }
}
