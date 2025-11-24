package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/12")
public interface ClassifiedJsonConsumer extends MinimumJsonConsumer {

    @Override
    void openArray();

    @Override
    void closeArray();

    @Override
    void openObject();

    @Override
    void closeObject();

    @Override
    void stringKey(@NotNull String key);

    void stringKey(@NotNull FastString key);

    @Override
    void jsonValue(@NotNull String value);

    void nullValue();

    void booleanValue(boolean value);

    void numberValue(long value);

    void numberValue(double value);

    void numberValue(@NotNull FastNumber value);

    void stringValue(@NotNull String value);

    void stringValue(@NotNull FastString value);
}
