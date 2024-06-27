package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.SerializeFrom;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Created on 2022/7/25.
 */
public class IterableValueSerializer implements ValueSerializer<Iterable<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IterableValueSerializer.class);

    public static final IterableValueSerializer INSTANCE = new IterableValueSerializer();

    @Override
    public void serialize(String key, Iterable<?> value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        if (js.add(value)) {
            try {
                Iterator<?> iterator = value.iterator();
                boolean first = true;
                while (iterator.hasNext()) {
                    if (first) {
                        jc.optionalKey(key);
                        jc.openArray();
                        first = false;
                    }
                    js.serialize(null, iterator.next(), jc, cc);
                }
                if (first) {
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
                } else {
                    jc.closeArray();
                }
            } finally {
                js.remove(value);
            }
        } else {
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic iterable to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic iterable to undefined in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
            throw new IllegalArgumentException("Cyclic iterable");
        }
    }
}
