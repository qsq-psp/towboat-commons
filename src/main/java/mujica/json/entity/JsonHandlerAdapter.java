package mujica.json.entity;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2026/4/1")
public class JsonHandlerAdapter<H extends JsonHandler> extends JsonHandler {

    @NotNull
    protected H h;

    public JsonHandlerAdapter(@NotNull H h) {
        super();
        this.h = h;
    }

    protected void beforeValue() {
        // pass
    }

    protected void afterValue() {
        // pass
    }

    @Override
    public void openArray() {
        beforeValue();
        h.openArray();
    }

    @Override
    public void closeArray() {
        h.closeArray();
        afterValue();
    }

    @Override
    public void openObject() {
        beforeValue();
        h.openObject();
    }

    @Override
    public void closeObject() {
        h.closeObject();
        afterValue();
    }

    @Override
    public void stringKey(@NotNull String key) {
        h.stringKey(key);
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        h.stringKey(key);
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        beforeValue();
        h.simpleValue(value);
        afterValue();
    }

    @Override
    public void skippedValue() {
        beforeValue();
        h.skippedValue();
        afterValue();
    }

    @Override
    public void skippedValue(@NotNull ByteBuf value) {
        beforeValue();
        h.skippedValue(value);
        afterValue();
    }

    @Override
    public void nullValue() {
        beforeValue();
        h.nullValue();
        afterValue();
    }

    @Override
    public void numberValue(int value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void numberValue(long value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void numberValue(float value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void numberValue(double value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void numberValue(@NotNull BigInteger value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        beforeValue();
        h.numberValue(value);
        afterValue();
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        beforeValue();
        h.stringValue(value);
        afterValue();
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        beforeValue();
        h.stringValue(value);
        afterValue();
    }
}
