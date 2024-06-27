package indi.um.json.reflect;

import indi.um.json.api.*;
import indi.um.json.entity.JsonConstant;
import indi.um.util.text.HexCodec;
import indi.um.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;

/**
 * Created in webbiton on 2020/12/24.
 * Created on 2022/6/11.
 */
class JsonDoubleType extends JsonFractionalType {

    private static final long serialVersionUID = 0x77A477923E741366L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDoubleType.class);

    double min = Double.NEGATIVE_INFINITY;

    double max = Double.POSITIVE_INFINITY;

    JsonDoubleType() {
        super();
    }

    double clamp(double x) {
        if (x > max) {
            x = max;
        }
        if (x < min) {
            x = min;
        }
        return x;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonDoubleType clone() {
        final JsonDoubleType that = new JsonDoubleType();
        that.setJsonType(this);
        that.min = this.min;
        that.max = this.max;
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        final DoubleValue doubleValue = element.getAnnotation(DoubleValue.class);
        if (doubleValue != null) {
            min = doubleValue.min();
            max = doubleValue.max();
            if (min > max) {
                LOGGER.warn("Bad double interval [{}, {}] in {}", min, max, element);
            }
        }
    }

    private static final int INTERESTED_CONFIG = SerializeFrom.ZERO_DECIMAL | SerializeFrom.INFINITE | SerializeFrom.NAN;

    /**
     * Almost same with indi.um.json.value.FractionalValueSerializer.serialize(String key, Number value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        double x = (double) value;
        x = clamp(x);
        if (anySerializeConfig((INTERESTED_CONFIG << UNDEFINED_SHIFT) | INTERESTED_CONFIG)) {
            if (x == 0.0) {
                if (anySerializeConfig(SerializeFrom.ZERO_DECIMAL, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize zero decimal to undefined in {}", js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.ZERO_DECIMAL, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize zero decimal to null in {}", js);
                    }
                    serializeNull(key, jc);
                    return;
                }
            } else if (Double.isInfinite(x)) {
                if (anySerializeConfig(SerializeFrom.INFINITE, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize {} to undefined in {}", x, js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.INFINITE, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize {} to null in {}", x, js);
                    }
                    serializeNull(key, jc);
                    return;
                }
            } else if (Double.isNaN(x)) {
                if (anySerializeConfig(SerializeFrom.NAN, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to undefined in {}", HexCodec.HEX_LOWER.hex0x64(x), js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.NAN, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to null in {}", HexCodec.HEX_LOWER.hex0x64(x), js);
                    }
                    serializeNull(key, jc);
                    return;
                }
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
                LOGGER.debug("Parse null double value to undefined in {}", jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return value ? 1.0 : 0.0;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard boolean value {} in {}", value, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        return parseNumber((double) value, jv);
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        if (min <= value && value <= max) {
            return value;
        } else if (anyParseConfig(ParseHint.APPLY_CLAMP)) {
            if (value == value) {
                return clamp(value);
            } else {
                return min;
            }
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard outbound value {} of [{}, {}] in {}", value, min, max, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        if (!anyParseConfig(ParseHint.ACCEPT_STRING)) {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard String value {} while parsing double in {}", Quote.DEFAULT.apply(value), jv);
            }
            return JsonConstant.UNDEFINED;
        }
        final Double doubleValue = parseUnits(value, jv);
        if (doubleValue != null) {
            return parseNumber(doubleValue, jv);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        if (min != Double.NEGATIVE_INFINITY) {
            jc.key("min");
            jc.numberValue(min);
        }
        if (max != Double.POSITIVE_INFINITY) {
            jc.key("max");
            jc.numberValue(max);
        }
    }

    @Override
    public String typeName() {
        return "double";
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 0x2f + Double.hashCode(min)) * 0x2f + Double.hashCode(max);
    }
}
