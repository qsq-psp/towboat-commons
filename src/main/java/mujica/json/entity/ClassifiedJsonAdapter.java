package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/11/7.
 */
@CodeHistory(date = "2025/11/7")
public abstract class ClassifiedJsonAdapter implements ClassifiedJsonConsumer {

    @Override
    public abstract void openArray();

    @Override
    public abstract void closeArray();

    @Override
    public abstract void openObject();

    @Override
    public abstract void closeObject();

    @Override
    public abstract void stringKey(@NotNull String key);

    @Override
    public void stringKey(@NotNull FastString key) {
        stringKey(key.string);
    }

    @Override
    public abstract void jsonValue(@NotNull String value);

    @Override
    public void nullValue() {
        jsonValue("null");
    }

    @Override
    public void booleanValue(boolean value) {
        jsonValue(Boolean.toString(value));
    }

    @Override
    public void numberValue(long value) {
        jsonValue(Long.toString(value));
    }

    @Override
    public void numberValue(double value) {
        jsonValue(Double.toString(value));
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        jsonValue(value.value);
    }

    @Override
    public void stringValue(@NotNull String value) {
        jsonValue(Quote.JSON.apply(value));
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        jsonValue('"' + value.string + '"');
    }
}
