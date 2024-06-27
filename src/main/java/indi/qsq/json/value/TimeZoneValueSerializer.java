package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

/**
 * Created on 2022/7/12.
 */
public class TimeZoneValueSerializer implements ValueSerializer<TimeZone>, JsonStructure {

    public static final TimeZoneValueSerializer INSTANCE = new TimeZoneValueSerializer();

    void serialize(String key, TimeZone value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("rawOffset");
        jc.numberValue(value.getRawOffset());
        jc.key("id");
        jc.stringValue(value.getID());
        ZoneIdValueSerializer.INSTANCE.serialize("zoneId", value.toZoneId(), jc);
        jc.key("displayName");
        jc.stringValue(value.getDisplayName());
        jc.key("dstSavings");
        jc.numberValue(value.getDSTSavings());
        jc.key("useDaylight");
        jc.booleanValue(value.useDaylightTime());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, TimeZone value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, TimeZone.getDefault(), jc);
    }
}
