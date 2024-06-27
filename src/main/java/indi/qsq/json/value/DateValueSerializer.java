package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created on 2022/7/18.
 */
@SuppressWarnings("unused")
public class DateValueSerializer implements ValueSerializer<Date>, JsonStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateValueSerializer.class);

    @Override
    public void serialize(String key, Date value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        final long time = value.getTime();
        if (time <= 0) { // zero or minus value are both meaningless
            if (cc.anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, true)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero time to undefined in {}", js);
                }
                return;
            }
            if (cc.anySerializeConfig(SerializeFrom.ZERO_INTEGRAL, false)) {
                if (js.logEnabled()) {
                    LOGGER.debug("Serialize zero time to null in {}", js);
                }
                jc.optionalKey(key);
                jc.nullValue();
                return;
            }
        }
        jc.optionalKey(key);
        jc.numberValue(time);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        // time now
        // do not create the Date Object; it's useless
        jc.numberValue(System.currentTimeMillis());
    }
}
