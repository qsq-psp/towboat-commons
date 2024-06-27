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
import java.util.Map;

/**
 * Created on 2022/7/30.
 */
public class MapValueSerializer implements ValueSerializer<Map<?,?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapValueSerializer.class);

    public static final MapValueSerializer INSTANCE = new MapValueSerializer();

    @Override
    public void serialize(String key, Map<?, ?> value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        if (js.add(value)) {
            try {
                Iterator<? extends Map.Entry<?, ?>> iterator = value.entrySet().iterator();
                boolean first = true;
                while (iterator.hasNext()) {
                    if (first) {
                        jc.optionalKey(key);
                        jc.openObject();
                        first = false;
                    }
                    Map.Entry<?,?> entry = iterator.next();
                    js.serialize(entry.getKey().toString(), entry.getValue(), jc, cc);
                }
                if (first) {
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_OBJECT, true)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty map to undefined in {}", js);
                        }
                        return;
                    }
                    if (cc.anySerializeConfig(SerializeFrom.EMPTY_OBJECT, false)) {
                        if (js.logEnabled()) {
                            LOGGER.debug("Serialize empty map to null in {}", js);
                        }
                        jc.optionalKey(key);
                        jc.nullValue();
                        return;
                    }
                    jc.optionalKey(key);
                    jc.openObject();
                }
                jc.closeObject();
            } finally {
                js.remove(value);
            }
        } else {
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic map to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.CYCLIC_OBJECT, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize cyclic map to undefined in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
            throw new IllegalArgumentException("Cyclic map");
        }
    }
}
