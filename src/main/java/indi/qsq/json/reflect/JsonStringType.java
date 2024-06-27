package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.entity.JsonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created in infrastructure on 2021/12/31.
 * Recreated on 2022/8/6.
 */
class JsonStringType extends JsonType {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStringType.class);

    JsonStringType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonStringType clone() {
        final JsonStringType that = new JsonStringType();
        that.setJsonType(this);
        return that;
    }

    private static final int INTERESTED_CONFIG = SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING;

    @Override
    protected void serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js) {
        final String string = (String) value;
        if (anySerializeConfig((INTERESTED_CONFIG << UNDEFINED_SHIFT) | INTERESTED_CONFIG)) {
            if (string.isEmpty()) {
                if (anySerializeConfig(SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize empty string to undefined in {}", js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize empty string to null in {}", js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            } else if (string.isBlank()) {
                if (anySerializeConfig(SerializeFrom.BLANK_STRING, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize blank string to undefined in {}", js);
                    }
                    return;
                }
                if (anySerializeConfig(SerializeFrom.BLANK_STRING, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize blank string to null in {}", js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            }
        }
        jc.optionalKey(key);
        jc.stringValue(string);
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        if (!isObject && anyParseConfig(ParseHint.ACCEPT_ARRAY)) {
            return new ConcatFrame();
        } else {
            return ConverterFrame.INSTANCE;
        }
    }

    class ConcatFrame extends ConverterFrame {

        final StringBuilder sb = new StringBuilder();

        @Override
        protected Object finish(JsonParser jp) {
            return sb.toString();
        }

        @Override
        protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
            return ConverterFrame.INSTANCE;
        }

        @Override
        protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
            // pass
        }

        @Override
        protected void booleanValue(boolean value, JsonConverter jv) {
            final Object object = parseBoolean(value, jv);
            if (object instanceof String) {
                sb.append((String) object);
            } else if (jv.logEnabled()) {
                LOGGER.warn("Skip boolean value in string array in {}", jv);
            }
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            final Object object = parseNumber(value, jv);
            if (object instanceof Long) {
                sb.append(object);
            } else if (jv.logEnabled()) {
                LOGGER.warn("Skip number value in string array in {}", jv);
            }
        }

        @Override
        protected void numberValue(double value, JsonConverter jv) {
            final Object object = parseNumber(value, jv);
            if (object instanceof Double) {
                sb.append(object);
            } else if (jv.logEnabled()) {
                LOGGER.warn("Skip number value in string array in {}", jv);
            }
        }
    }

    @Override
    protected Object parseBoolean(boolean value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_BOOLEAN)) {
            return Boolean.toString(value);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(long value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            return Long.toString(value);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    protected Object parseNumber(double value, JsonConverter jv) {
        if (anyParseConfig(ParseHint.ACCEPT_NUMBER)) {
            return Double.toString(value);
        } else {
            return JsonConstant.UNDEFINED;
        }
    }

    @Override
    public String typeName() {
        return "string";
    }
}
