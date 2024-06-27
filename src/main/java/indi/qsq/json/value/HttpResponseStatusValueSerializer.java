package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
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
