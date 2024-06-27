package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.attribute.FileTime;

/**
 * Created on 2022/9/10.
 */
public class FileTimeValueSerializer implements ValueSerializer<FileTime> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTimeValueSerializer.class);

    @Override
    public void serialize(String key, FileTime value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        long time = value.toMillis();
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
}
