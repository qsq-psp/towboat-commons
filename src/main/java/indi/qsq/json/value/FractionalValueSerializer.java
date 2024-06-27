package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.util.text.HexCodec;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/7/12.
 *
 * Designed for double and Double
 * Compatible with float, Float, BigDecimal, DoubleAccumulator, DoubleAdder
 */
public class FractionalValueSerializer implements ValueSerializer<Number> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FractionalValueSerializer.class);

    private static final int INTERESTED_CONFIG = SerializeFrom.ZERO_DECIMAL | SerializeFrom.INFINITE | SerializeFrom.NAN;

    /**
     * Almost same with indi.qsq.json.reflect.JsonDoubleType.serializeValue(String key, Object value, JsonConsumer jc, JsonSerializer js)
     */
    @Override
    public void serialize(String key, Number value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        final double x = value.doubleValue();
        if (cc.anySerializeConfig((INTERESTED_CONFIG << ConversionConfig.UNDEFINED_SHIFT) | INTERESTED_CONFIG)) {
            if (x == 0.0) {
                if (cc.anySerializeConfig(SerializeFrom.ZERO_DECIMAL, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize zero decimal to undefined in {}", js);
                    }
                    return;
                }
                if (cc.anySerializeConfig(SerializeFrom.ZERO_DECIMAL, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize zero decimal to null in {}", js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            } else if (Double.isInfinite(x)) {
                if (cc.anySerializeConfig(SerializeFrom.INFINITE, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize {} to undefined in {}", x, js);
                    }
                    return;
                }
                if (cc.anySerializeConfig(SerializeFrom.INFINITE, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize {} to null in {}", x, js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            } else if (Double.isNaN(x)) {
                if (cc.anySerializeConfig(SerializeFrom.NAN, true)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to undefined in {}", HexCodec.HEX_LOWER.hex0x64(x), js);
                    }
                    return;
                }
                if (cc.anySerializeConfig(SerializeFrom.NAN, false)) {
                    if (js.logEnabled()) {
                        LOGGER.debug("Serialize NaN {} to null in {}", HexCodec.HEX_LOWER.hex0x64(x), js);
                    }
                    jc.optionalKey(key);
                    jc.nullValue();
                    return;
                }
            }
        }
        jc.optionalKey(key);
        jc.numberValue(x);
    }
}
