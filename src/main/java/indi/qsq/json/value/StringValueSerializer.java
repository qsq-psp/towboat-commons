package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/7/13.
 */
@SuppressWarnings("unused")
public class StringValueSerializer implements ValueSerializer<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringValueSerializer.class);

    private static final int INTERESTED_CONFIG = SerializeFrom.EMPTY_STRING | SerializeFrom.BLANK_STRING;

    @Override
    public void serialize(String key, Object value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        final String string = value.toString();
        if (cc.anySerializeConfig((INTERESTED_CONFIG << ConversionConfig.UNDEFINED_SHIFT) | INTERESTED_CONFIG)) {
            if (string.isEmpty()) {
                if (cc.anySerializeConfig(INTERESTED_CONFIG, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize empty string to undefined in {}", js);
                    }
                    return;
                }
                if (cc.anySerializeConfig(INTERESTED_CONFIG, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize empty string to null in {}", js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            } else if (string.isBlank()) {
                if (cc.anySerializeConfig(SerializeFrom.BLANK_STRING, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize blank string to undefined in {}", js);
                    }
                    return;
                }
                if (cc.anySerializeConfig(SerializeFrom.BLANK_STRING, false)) {
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
}
