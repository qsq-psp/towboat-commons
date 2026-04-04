package mujica.json.io;

import io.netty.buffer.ByteBuf;
import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "StringWriter")
@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "SimpleJsonStringWriter")
@CodeHistory(date = "2026/1/6", name = "StringBuilderJsonWriter")
@CodeHistory(date = "2026/3/22")
public class JsonStringBuilderWriter extends JsonStringWriter {

    @NotNull
    protected final StringBuilder sb;

    public JsonStringBuilderWriter(@NotNull StringBuilder sb) {
        super();
        this.sb = sb;
    }

    public JsonStringBuilderWriter() {
        this(new StringBuilder());
    }

    @Override
    public void reset() {
        super.reset();
        sb.delete(0, sb.length());
    }

    @NotNull
    @Override
    public StringBuilder getCharSequence() {
        return sb;
    }

    protected void anyKey() {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_OBJECT:
                sb.append(',');
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
                sb.append(',');
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

    @Override
    public void openArray() {
        anyValue();
        stack.offerLast(STATE_NEW_ARRAY);
        sb.append('[');
    }

    @Override
    public void closeArray() {
        final int state = stack.removeLast();
        if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
            sb.append(']');
        } else {
            stack.offerLast(state);
            throwState();
        }
    }

    @Override
    public void openObject() {
        anyValue();
        stack.offerLast(STATE_NEW_OBJECT);
        sb.append('{');
    }

    @Override
    public void closeObject() {
        final int state = stack.removeLast();
        if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
            sb.append('}');
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
        sb.append(name).append('(');
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
        sb.append(')');
    }

    @Override
    public void stringKey(@NotNull String key) {
        anyKey();
        CharSequenceAppender appender;
        if ((flags & FLAG_ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            appender = CharSequenceAppender.Json.EXTRA;
        }
        appender.append(key, sb);
        sb.append(':');
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        anyKey();
        sb.append('"').append(key.string).append('"').append(':');
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
                // todo
                stack.offerLast(STATE_OBJECT); // or STATE_NEW_OBJECT
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
            sb.append(value.toString(StandardCharsets.UTF_8));
        } finally {
            value.release();
        }
    }

    @Override
    public void nullValue() {
        anyValue();
        sb.append("null");
    }

    @Override
    public void booleanValue(boolean value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(int value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(float value) {
        if ((flags & FLAG_INFINITY_TO_NULL) != 0 && Float.isInfinite(value)) {
            nullValue();
        } else if ((flags & FLAG_NAN_TO_NULL) != 0 && Float.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            sb.append(value);
        }
    }

    @Override
    public void numberValue(double value) {
        if ((flags & FLAG_INFINITY_TO_NULL) != 0 && Double.isInfinite(value)) {
            nullValue();
        } else if ((flags & FLAG_NAN_TO_NULL) != 0 && Double.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            sb.append(value);
        }
    }

    @Override
    public void numberValue(@NotNull BigInteger value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        anyValue();
        CharSequenceAppender appender;
        if ((flags & FLAG_ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            appender = CharSequenceAppender.Json.EXTRA;
        }
        appender.append(value, sb);
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        anyValue();
        sb.append('"').append(value.string).append('"');
    }

    @Override
    public void emptyArrayValue() {
        anyValue();
        sb.append('[').append(']');
    }

    @Override
    public void emptyObjectValue() {
        anyValue();
        sb.append('{').append('}');
    }
}
