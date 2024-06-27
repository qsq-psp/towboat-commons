package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import io.netty.handler.codec.http.HttpMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/9/6.
 */
@SuppressWarnings("unused")
public class HttpMethodValueSerializer implements ValueSerializer<HttpMethod> {

    @Override
    public void serialize(String key, HttpMethod value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.stringValue(value.name());
    }
}
