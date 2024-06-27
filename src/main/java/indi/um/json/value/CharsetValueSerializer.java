package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

/**
 * Created on 2023/4/20.
 */
public class CharsetValueSerializer implements ValueSerializer<Charset>, JsonStructure {

    public static final CharsetValueSerializer INSTANCE = new CharsetValueSerializer();

    void serialize(String key, Charset value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("name");
        jc.stringValue(value.name());
        jc.key("aliases");
        jc.stringsValue(value.aliases());
        jc.key("registered");
        jc.booleanValue(value.isRegistered());
        jc.key("canEncode");
        jc.booleanValue(value.canEncode());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Charset value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Charset.defaultCharset(), jc);
    }
}
