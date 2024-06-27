package indi.um.json.api;

import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/12.
 */
@FunctionalInterface
public interface ValueSerializer<T> {

    void serialize(String key, T value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js);
}
