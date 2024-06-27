package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Created on 2022/7/19.
 */
public class LanguageRangeValueSerializer implements ValueSerializer<Locale.LanguageRange> {

    @Override
    public void serialize(String key, Locale.LanguageRange value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("range");
        jc.stringValue(value.getRange()); // range is always not null
        jc.key("weight");
        jc.numberValue(value.getWeight());
        jc.closeObject();
    }
}
