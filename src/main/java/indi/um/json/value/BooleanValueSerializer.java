package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.reflect.JsonSerializer;
import indi.um.json.api.SerializeFrom;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/7/12.
 *
 * Designed for boolean and Boolean
 */
public class BooleanValueSerializer implements ValueSerializer<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooleanValueSerializer.class);

    public static final BooleanValueSerializer INSTANCE = new BooleanValueSerializer();

    /**
     * Almost same with indi.um.json.reflect.JsonBooleanType.serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    public void serialize(String key, Boolean value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        if (value) {
            jc.optionalKey(key);
            jc.booleanValue(true);
        } else {
            if (cc.anySerializeConfig(SerializeFrom.FALSE, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize false boolean to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.FALSE, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize false boolean to null in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
            jc.optionalKey(key);
            jc.booleanValue(false);
        }
    }
}
