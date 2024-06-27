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
 * Designed for long and Long
 * Compatible with byte, Byte, short, Short, int, Integer, AtomicInteger, AtomicLong, LongAccumulator, LongAdder
 */
public class IntegralValueSerializer implements ValueSerializer<Number> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralValueSerializer.class);

    @Override
    public void serialize(String key, Number value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        final long x = value.longValue();
        if (x == 0) {
            if (cc.anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero integral to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero integral to null in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
        }
        jc.optionalKey(key);
        jc.numberValue(x);
    }
}
