package mujica.json.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Created on 2026/4/5.
 */
@CodeHistory(date = "2026/4/5")
public class JsonByteBufWriter extends JsonWriter implements ByteBufHolder {

    @NotNull
    protected final ByteBuf data;

    public JsonByteBufWriter(@NotNull ByteBuf data) {
        super();
        this.data = data;
    }

    @Override
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(data);
    }

    @Override
    public ByteBufHolder copy() {
        return replace(data.copy());
    }

    @Override
    public ByteBufHolder duplicate() {
        return replace(data.duplicate());
    }

    @Override
    public ByteBufHolder retainedDuplicate() {
        return replace(data.retainedDuplicate());
    }

    @Override
    public ByteBufHolder replace(ByteBuf content) {
        return new DefaultByteBufHolder(content);
    }

    @Override
    public int refCnt() {
        return data.refCnt();
    }

    @Override
    public ByteBufHolder retain() {
        data.retain();
        return this;
    }

    @Override
    public ByteBufHolder retain(int increment) {
        data.retain(increment);
        return this;
    }

    @Override
    public ByteBufHolder touch() {
        data.touch();
        return this;
    }

    @Override
    public ByteBufHolder touch(Object hint) {
        data.touch(hint);
        return this;
    }

    @Override
    public boolean release() {
        return data.release();
    }

    @Override
    public boolean release(int decrement) {
        return data.release(decrement);
    }

    @Override
    public void reset() {
        super.reset(); // reset stack
        data.clear();
    }

    protected void anyKey() {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_OBJECT:
                data.writeByte(',');
                // no break here
            case STATE_NEW_OBJECT:
                break;
            default:
                stack.offerLast(state);
                throwState();
                break; // never
        }
        stack.offerLast(STATE_KEY);
    }

    protected void anyValue() {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_START:
                stack.offerLast(STATE_END);
                break;
            case STATE_ARRAY:
                data.writeByte(',');
                // no break here
            case STATE_NEW_ARRAY:
                stack.offerLast(STATE_ARRAY);
                break;
            case STATE_KEY:
                stack.offerLast(STATE_OBJECT);
                break;
            default:
                stack.offerLast(state);
                throwState();
                break; // never
        }
    }

    protected boolean undoKey() {
        int index = data.writerIndex();
        int ch = data.getUnsignedByte(--index);
        if (ch != ':') {
            throw new IllegalStateException();
        }
        ch = data.getUnsignedByte(--index);
        if (ch != '"') {
            throw new IllegalStateException();
        }
        while (true) {
            ch = data.getUnsignedByte(--index);
            if (ch == '"') {
                ch = data.getUnsignedByte(--index);
                if (ch != '\\') {
                    break;
                }
            }
        }
        if (ch == '{') {
            data.writerIndex(index + 1);
            return true;
        } else if (ch == ',') {
            data.writerIndex(index);
            return false;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void openArray() {
        anyValue();
        stack.offerLast(STATE_NEW_ARRAY);
        data.writeByte('[');
    }

    @Override
    public void closeArray() {
        final int state = stack.removeLast();
        if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
            data.writeByte(']');
        } else {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    public void openObject() {
        anyValue();
        stack.offerLast(STATE_NEW_OBJECT);
        data.writeByte('{');
    }

    @Override
    public void closeObject() {
        final int state = stack.removeLast();
        if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
            data.writeByte('}');
        } else {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    public void openJsonp(@NotNull CharSequence name) {
        final int state = stack.removeLast();
        if (state != STATE_START) {
            stack.offerLast(state);
            throwState();
        }
        stack.offerLast(STATE_JSONP);
        stack.offerLast(STATE_START);
        data.writeCharSequence(name, StandardCharsets.UTF_8);
        data.writeByte('(');
    }

    @Override
    public void closeJsonp() {
        int state = stack.removeLast();
        if (state != STATE_END) {
            stack.offerLast(state);
            throwState();
        }
        state = stack.removeLast();
        if (state != STATE_JSONP) {
            stack.offerLast(state);
            stack.offerLast(STATE_END);
            throwState();
        }
        stack.offerLast(STATE_END);
        data.writeByte(')');
    }

    @Override
    public void stringKey(@NotNull String key) {
        anyKey();
        CharSequenceAppender appender;
        if ((flags & ConfigFlags.ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            if ((flags & ConfigFlags.UPPERCASE_HEX) == 0) {
                appender = CharSequenceAppender.Json.EXTRA_LOWER;
            } else {
                appender = CharSequenceAppender.Json.EXTRA_UPPER;
            }
        }
        appender.write(key, data);
        data.writeByte(':');
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        anyKey();
        data.writeByte('"').writeCharSequence(key.toString(), StandardCharsets.US_ASCII);
        data.writeByte('"').writeByte(':');
    }

    @Override
    public void skippedValue() { // undo key if there is a key
        final int state = stack.removeLast();
        switch (state) {
            case STATE_START:
                stack.offerLast(STATE_END);
                break;
            case STATE_NEW_ARRAY:
            case STATE_ARRAY:
                stack.offerLast(state);
                break;
            case STATE_KEY:
                if (undoKey()) {
                    stack.offerLast(STATE_NEW_OBJECT);
                } else {
                    stack.offerLast(STATE_OBJECT);
                }
                break;
            default:
                stack.offerLast(state);
                throwState();
                break; // never
        }
    }

    @Override
    public void skippedValue(@NotNull ByteBuf value) {
        try {
            data.writeBytes(value);
        } finally {
            value.release();
        }
    }

    @Override
    public void nullValue() {
        anyValue();
        data.writeCharSequence("null", StandardCharsets.US_ASCII);
    }

    @Override
    public void booleanValue(boolean value) {
        anyValue();
        data.writeCharSequence(String.valueOf(value), StandardCharsets.US_ASCII);
    }

    static final int[] INT_DECIMAL = {-1, -10, -100, -1000, -10000, -100000, -1000000, -10000000, -100000000, -1000000000};

    @Override
    public void numberValue(int value) {
        anyValue();
        if (value > 0) { // move to Base10Appender
            value = -value;
        } else if (value < 0) {
            data.writeByte('-');
        } else {
            data.writeByte('0');
            return;
        }
        int index = INT_DECIMAL.length - 1;
        while (value > INT_DECIMAL[index]) {
            index--;
        }
        while (index >= 0) {
            int magnitude = INT_DECIMAL[index];
            int digit = value / magnitude;
            value -= magnitude * digit;
            data.writeByte('0' + digit);
            index--;
        }
    }

    static final long[] LONG_DECIMAL = {-1L, -10L, -100L, -1000L, -10000L, -100000L, -1000000L, -10000000L, -100000000L,
            -1000000000L, -10000000000L, -100000000000L, -1000000000000L, -10000000000000L, -100000000000000L,
            -1000000000000000L, -10000000000000000L, -100000000000000000L, -1000000000000000000L};

    @Override
    public void numberValue(long value) {
        anyValue();
        if (value > 0) { // move to Base10Appender
            value = -value;
        } else if (value < 0) {
            data.writeByte('-');
        } else {
            data.writeByte('0');
            return;
        }
        int index = LONG_DECIMAL.length - 1;
        while (value > LONG_DECIMAL[index]) {
            index--;
        }
        while (index >= 0) {
            long magnitude = LONG_DECIMAL[index];
            long digit = value / magnitude;
            value -= magnitude * digit;
            data.writeByte('0' + (int) digit);
            index--;
        }
    }

    @Override
    public void numberValue(float value) {
        if ((flags & ConfigFlags.INFINITY_TO_NULL) != 0 && Float.isInfinite(value)) {
            nullValue();
        } else if ((flags & ConfigFlags.NAN_TO_NULL) != 0 && Float.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            data.writeCharSequence(String.valueOf(value), StandardCharsets.US_ASCII);
        }
    }

    @Override
    public void numberValue(double value) {
        if ((flags & ConfigFlags.INFINITY_TO_NULL) != 0 && Double.isInfinite(value)) {
            nullValue();
        } else if ((flags & ConfigFlags.NAN_TO_NULL) != 0 && Double.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            data.writeCharSequence(String.valueOf(value), StandardCharsets.US_ASCII);
        }
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        anyValue();
        CharSequenceAppender appender;
        if ((flags & ConfigFlags.ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            if ((flags & ConfigFlags.UPPERCASE_HEX) == 0) {
                appender = CharSequenceAppender.Json.EXTRA_LOWER;
            } else {
                appender = CharSequenceAppender.Json.EXTRA_UPPER;
            }
        }
        appender.write(value, data);
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        anyValue();
        data.writeByte('"').writeCharSequence(value.toString(), StandardCharsets.US_ASCII);
        data.writeByte('"');
    }
}
