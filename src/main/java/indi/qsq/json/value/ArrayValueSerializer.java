package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;

/**
 * Created on 2022/7/13.
 */
public class ArrayValueSerializer implements ValueSerializer<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayValueSerializer.class);

    public static final ArrayValueSerializer INSTANCE = new ArrayValueSerializer();

    @Override
    public void serialize(String key, Object value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        if (js.add(value)) {
            try {
                int length = Array.getLength(value);
                if (length != 0) {
                    jc.optionalKey(key);
                    jc.openArray();
                    for (int i = 0; i < length; i++) {
                        js.thisObject = value;
                        js.serialize(null, Array.get(value, i), jc, cc);
                    }
                    jc.closeArray();
                } else {
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_ARRAY, true)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty array to undefined in {}", js);
                        }
                        return;
                    }
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_ARRAY, false)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty array to null in {}", js);
                        }
                        jc.optionalKey(key);
                        jc.nullValue();
                        return;
                    }
                    jc.optionalKey(key);
                    jc.arrayValue();
                }
            } finally {
                js.remove(value);
            }
        } else {
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic array to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic array to undefined in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
            throw new IllegalArgumentException("Cyclic array");
        }
    }
}
