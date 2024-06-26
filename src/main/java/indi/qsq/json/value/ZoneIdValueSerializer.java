package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.zone.ZoneRules;

/**
 * Created on 2022/7/12.
 */
public class ZoneIdValueSerializer implements ValueSerializer<ZoneId>, JsonStructure {

    public static final ZoneIdValueSerializer INSTANCE = new ZoneIdValueSerializer();

    void serialize(String key, ZoneId value, JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("id");
        jc.stringValue(value.getId());
        final ZoneRules zoneRules = value.getRules();
        jc.key("fixedOffset");
        jc.booleanValue(zoneRules.isFixedOffset());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, ZoneId value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, ZoneId.systemDefault(), jc);
    }
}
