package mujica.json.io;

import io.netty.buffer.ByteBuf;
import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/1/7", name = "StringBufferJsonWriter")
@CodeHistory(date = "2026/3/24")
public class JsonStringBufferWriter extends JsonStringWriter {

    @NotNull
    protected final StringBuffer sb;

    public JsonStringBufferWriter(@NotNull StringBuffer sb) {
        super();
        this.sb = sb;
    }

    public JsonStringBufferWriter() {
        this(new StringBuffer());
    }

    @NotNull
    @Override
    public StringBuffer getCharSequence() {
        return sb;
    }

    @Override
    public void reset() {
        super.reset(); // reset stack
        sb.delete(0, sb.length());
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

    protected boolean undoKey() {
        final int length = sb.length();
        int index = length;
        int ch = sb.charAt(--index);
        if (ch != ':') {
            throw new IllegalStateException();
        }
        ch = sb.charAt(--index);
        if (ch != '"') {
            throw new IllegalStateException();
        }
        while (true) {
            ch = sb.charAt(--index);
            if (ch == '"') {
                ch = sb.charAt(--index);
                if (ch != '\\') {
                    break;
                }
            }
        }
        if (ch == '{') {
            sb.delete(index + 1, length);
            return true;
        } else if (ch == ',') {
            sb.delete(index, length);
            return false;
        } else {
            System.out.println(sb);
            throw new IllegalStateException("ch = " + ch);
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
        if ((flags & ConfigFlags.ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            if ((flags & ConfigFlags.UPPERCASE_HEX) == 0) {
                appender = CharSequenceAppender.Json.EXTRA_LOWER;
            } else {
                appender = CharSequenceAppender.Json.EXTRA_UPPER;
            }
        }
        appender.append(key, sb);
        sb.append(':');
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        anyKey();
        sb.append('"').append(key.toString()).append('"').append(':');
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
        if ((flags & ConfigFlags.INFINITY_TO_NULL) != 0 && Float.isInfinite(value)) {
            nullValue();
        } else if ((flags & ConfigFlags.NAN_TO_NULL) != 0 && Float.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            sb.append(value);
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
        if ((flags & ConfigFlags.ESCAPE_EXTRA) == 0) {
            appender = CharSequenceAppender.Json.ESSENTIAL;
        } else {
            if ((flags & ConfigFlags.UPPERCASE_HEX) == 0) {
                appender = CharSequenceAppender.Json.EXTRA_LOWER;
            } else {
                appender = CharSequenceAppender.Json.EXTRA_UPPER;
            }
        }
        appender.append(value, sb);
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        anyValue();
        sb.append('"').append(value.toString()).append('"');
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
