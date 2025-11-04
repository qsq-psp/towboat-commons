package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/10/12.
 */
@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/10/12")
public interface JsonConsumer extends ClassifiedJsonConsumer {

    @Override
    void openArray();

    @Override
    void closeArray();

    @Override
    void openObject();

    @Override
    void closeObject();

    @Override
    void key(@NotNull String key);

    @Override
    void key(@NotNull FastString key);

    void optionalKey(@Nullable String key);

    void optionalKey(@Nullable FastString key);

    @Override
    void jsonValue(@NotNull String value);

    @Override
    void nullValue();

    @Override
    void booleanValue(boolean value);

    @Override
    void numberValue(long value);

    @Override
    void numberValue(@NotNull FastNumber value);

    @Override
    void stringValue(@NotNull String value);

    @Override
    void stringValue(@NotNull FastString value);
}
