package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ParseHint;
import indi.um.json.api.SerializeFrom;
import indi.um.json.entity.JsonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created in webbiton on 2020/4/1, named RegistryByteField.
 * Created on 2022/8/8.
 */
class JsonByteType extends JsonIntegralType {

    private static final long serialVersionUID = 0x9312FA58B4A856D8L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonByteType.class);

    JsonByteType() {
        super();
    }

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        byte x = (byte) value;
        if (x == 0) {
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero byte to undefined in {}", js);
                }
                return;
            }
            if (anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero byte to null in {}", js);
                }
                serializeNull(key, jc);
                return;
            }
        }
        jc.optionalKey(key);
        jc.numberValue(x);
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return value ? 1 : 0;
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard boolean value {} while parsing byte in {}", value, jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.APPLY_CLAMP)) {
            return (byte) Math.max(Byte.MIN_VALUE, Math.min(value, Byte.MAX_VALUE));
        } else {
            if (anyParseConfig(ParseHint.APPLY_TRUNCATE)) {
                return (byte) value;
            }
            if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
                return (byte) value;
            } else {
                if (jv.logEnabled()) {
                    LOGGER.debug("Discard outbound byte value {} in {}", value, jv);
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
            LOGGER.debug("Fail to parse double value {} to byte in {}", value, jv);
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
    public String typeName() {
        return "byte";
    }
}
