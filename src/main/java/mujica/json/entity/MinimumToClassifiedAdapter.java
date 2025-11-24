package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/6")
public class MinimumToClassifiedAdapter implements ClassifiedJsonConsumer {

    @NotNull
    protected final MinimumJsonConsumer jc;

    public MinimumToClassifiedAdapter(@NotNull MinimumJsonConsumer jc) {
        super();
        this.jc = jc;
    }

    @Override
    public void openArray() {
        jc.openArray();
    }

    @Override
    public void closeArray() {
        jc.closeArray();
    }

    @Override
    public void openObject() {
        jc.openObject();
    }

    @Override
    public void closeObject() {
        jc.closeObject();
    }

    @Override
    public void stringKey(@NotNull String key) {
        jc.stringKey(key);
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        jc.stringKey(key.string);
    }

    @Override
    public void jsonValue(@NotNull String value) {
        jc.jsonValue(value);
    }

    @Override
    public void nullValue() {
        jc.jsonValue("null");
    }

    @Override
    public void booleanValue(boolean value) {
        jc.jsonValue(Boolean.toString(value));
    }

    @Override
    public void numberValue(long value) {
        jc.jsonValue(Long.toString(value));
    }

    @Override
    public void numberValue(double value) {
        jc.jsonValue(Double.toString(value));
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        jc.jsonValue(value.value);
    }

    @Override
    public void stringValue(@NotNull String value) {
        jc.jsonValue(Quote.JSON.apply(value));
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        jc.jsonValue('"' + value.string + '"');
    }
}
