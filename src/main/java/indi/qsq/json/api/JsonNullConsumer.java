package indi.um.json.api;

import indi.um.json.entity.RawNumber;
import indi.um.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/6.
 */
public class JsonNullConsumer implements JsonConsumer {

    public static final JsonNullConsumer INSTANCE = new JsonNullConsumer();

    /**
     * Use INSTANCE, or create new
     */
    public JsonNullConsumer() {
        super();
    }

    @Override
    public void openArray() {
        // pass
    }

    @Override
    public void closeArray() {
        // pass
    }

    @Override
    public void openObject() {
        // pass
    }

    @Override
    public void closeObject() {
        // pass
    }

    @Override
    public void key(@NotNull String key) {
        // pass
    }

    @Override
    public void key(@NotNull TypedString key) {
        // pass
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        // pass
    }

    @Override
    public void nullValue() {
        // pass
    }

    @Override
    public void booleanValue(boolean value) {
        // pass
    }

    @Override
    public void numberValue(long value) {
        // pass
    }

    @Override
    public void numberValue(double value) {
        // pass
    }

    @Override
    public void numberValue(@NotNull RawNumber value) {
        // pass
    }

    @Override
    public void stringValue(@NotNull String value) {
        // pass
    }

    @Override
    public void objectValue(Object value) {
        // pass
    }
}
