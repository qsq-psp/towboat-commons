package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
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
