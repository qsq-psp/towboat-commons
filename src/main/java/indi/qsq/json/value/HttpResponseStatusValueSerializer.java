package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/8/18.
 */
@SuppressWarnings("unused")
public class HttpResponseStatusValueSerializer implements ValueSerializer<HttpResponseStatus> {

    @Override
    public void serialize(String key, HttpResponseStatus value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.numberValue(value.code());
    }
}
