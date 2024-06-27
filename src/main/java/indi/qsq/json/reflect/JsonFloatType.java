package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ParseHint;
import indi.um.json.api.SerializeFrom;
import indi.um.json.entity.JsonConstant;
import indi.um.util.text.HexCodec;
import indi.um.util.text.Quote;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/8/8.
 */
class JsonFloatType extends JsonFractionalType {

    private static final long serialVersionUID = 0x97C97348B35716F4L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFloatType.class);

    float min = Float.NEGATIVE_INFINITY;

    float max = Float.POSITIVE_INFINITY;

    @Override
    public String typeName() {
        return "float";
    }

    float clamp(float x) {
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
    public JsonFloatType clone() {
        final JsonFloatType that = new JsonFloatType();
        that.setJsonType(this);
        that.min = this.min;
        that.max = this.max;
        return that;
    }

    private static final int INTERESTED_CONFIG = SerializeFrom.ZERO_DECIMAL | SerializeFrom.INFINITE | SerializeFrom.NAN;

    /**
     * Almost same with indi.um.json.value.FractionalValueSerializer.serialize(String key, Number value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        float x = (float) value;
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
            } else if (Float.isInfinite(x)) {
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
            } else if (Float.isNaN(x)) {
                if (anySerializeConfig(SerializeFrom.NAN, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to undefined in {}", HexCodec.HEX_LOWER.hex0x32(x), js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.NAN, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to null in {}", HexCodec.HEX_LOWER.hex0x32(x), js);
                    }
                    serializeNull(key, jc);
                    return;
                }
            }
        }
        jc.optionalKey(key);
        jc.numberValue(x);
    }

    Object parseNumber(float value, JsonConverter jv) {
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
    protected Object parseNull(JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NULL)) {
            return null;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Parse null float value to undefined in {}", jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return value ? 1.0f : 0.0f;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard boolean value {} in {}", value, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        return parseNumber((float) value, jv);
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        return parseNumber((float) value, jv);
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        if (!anyParseConfig(ParseHint.ACCEPT_STRING)) {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard String value {} while parsing float in {}", Quote.DEFAULT.apply(value), jv);
            }
            return JsonConstant.UNDEFINED;
        }
        final Double doubleValue = parseUnits(value, jv);
        if (doubleValue != null) {
            return parseNumber((float) (double) doubleValue, jv);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        if (min != Float.NEGATIVE_INFINITY) {
            jc.key("min");
            jc.numberValue(min);
        }
        if (max != Float.POSITIVE_INFINITY) {
            jc.key("max");
            jc.numberValue(max);
        }
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 0x2f + Float.hashCode(min)) * 0x2f + Float.hashCode(max);
    }
}
