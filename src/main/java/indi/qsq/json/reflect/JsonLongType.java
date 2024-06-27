package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.LongValue;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.entity.JsonConstant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;

/**
 * Created in webbiton on 2020/12/24, named RegistryLongField.
 * Recreated in infrastructure on 2022/1/5.
 * Recreated on 2022/7/31.
 */
class JsonLongType extends JsonIntegralType {

    private static final long serialVersionUID = 0x203531829AA1BA76L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonLongType.class);

    long min = Long.MIN_VALUE;

    long max = Long.MAX_VALUE;

    JsonLongType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonType clone() {
        final JsonLongType that = new JsonLongType();
        that.setJsonType(this);
        that.min = this.min;
        that.max = this.max;
        return that;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        final LongValue longValue = element.getAnnotation(LongValue.class);
        if (longValue != null) {
            min = longValue.min();
            max = longValue.max();
            if (min > max) {
                LOGGER.warn("Bad long interval [{}, {}] in {}", min, max, element);
            }
        }
    }

    /**
     * Almost same with indi.qsq.json.value.IntegralValueSerializer.serialize(String key, Number value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        long x = (long) value;
        x = Math.max(min, Math.min(x, max));
        if (x == 0L) {
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero long to undefined in {}", js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero long to null in {}", js);
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
                LOGGER.debug("Parse null long value to undefined in {}", jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return value ? 1L : 0L;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard boolean value {} while parsing long in {}", value, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.APPLY_CLAMP)) {
            return Math.max(min, Math.min(value, max));
        } else {
            if (min <= value && value <= max) {
                return value;
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
            LOGGER.debug("Fail to parse double value {} to long in {}", value, jv);
        }
        return JsonConstant.UNDEFINED;
    }

    @Override
    protected Object parseString(String value, JsonConverter jc) {
        if (!anyParseConfig(ParseHint.ACCEPT_STRING)) {
            return JsonConstant.UNDEFINED;
        }
        final Long longValue = parseUnits(value, jc);
        if (longValue != null) {
            return parseNumber(longValue, jc);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        if (min != Long.MIN_VALUE) {
            jc.key("min");
            jc.numberValue(min);
        }
        if (max != Long.MAX_VALUE) {
            jc.key("max");
            jc.numberValue(max);
        }
    }

    @Override
    public String typeName() {
        return "long";
    }

    @Override
    public int hashCode() {
        return (super.hashCode() * 0x2f + Long.hashCode(min)) * 0x2f + Long.hashCode(max);
    }
}
