package indi.qsq.json.io;

import indi.qsq.json.api.JsonStructure;
import indi.qsq.util.ds.HeapIntBuf;
import indi.qsq.util.reflect.ClassUtility;
import indi.qsq.util.text.Quote;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/6/4.
 */
public class SimpleJsonStringWriter implements Writer {

    public static String stringify(JsonStructure js) {
        final SimpleJsonStringWriter sw = new SimpleJsonStringWriter();
        js.toJson(sw);
        return sw.get();
    }

    protected final StringBuilder sb = new StringBuilder();

    protected final HeapIntBuf stack = new HeapIntBuf();

    public SimpleJsonStringWriter() {
        super();
        start();
    }

    @Override
    public void setHexCase(boolean upper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMantissa(int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIndent(int count) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        sb.setLength(0); // clear output buffer
        start();
    }

    public void start() {
        stack.removeAll();
        stack.writeInt(STATE_START);
    }

    public void checkEnd() {
        if (stack.writerIndex() != 1 || stack.getInt(0) != STATE_END) {
            throw new IllegalStateException(toString());
        }
    }

    public String get() {
        checkEnd();
        return sb.toString();
    }

    protected void anyKey() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_OBJECT:
                sb.append(',');
                // no break here
            case STATE_NEW_OBJECT:
                break;
            default:
                stack.writeInt(state);
                throw new IllegalStateException(toString());
        }
        stack.writeInt(STATE_KEY);
    }

    protected void anyValue() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_START:
                stack.writeInt(STATE_END);
                break;
            case STATE_ARRAY:
                sb.append(',');
                // no break here
            case STATE_NEW_ARRAY:
                stack.writeInt(STATE_ARRAY);
                break;
            case STATE_KEY:
                stack.writeInt(STATE_OBJECT);
                break;
            default:
                stack.writeInt(state);
                throw new IllegalStateException(toString());
        }
    }

    @Override
    public void openArray() {
        anyValue();
        stack.writeInt(STATE_NEW_ARRAY);
        sb.append('[');
    }

    @Override
    public void closeArray() {
        final int state = stack.popInt();
        if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
            sb.append(']');
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void openObject() {
        anyValue();
        stack.writeInt(STATE_NEW_OBJECT);
        sb.append('{');
    }

    @Override
    public void closeObject() {
        final int state = stack.popInt();
        if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
            sb.append('}');
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    public void openJsonp(CharSequence name) {
        final int state = stack.popInt();
        if (state != STATE_START) {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
        stack.writeInt(STATE_JSONP);
        stack.writeInt(STATE_START);
        sb.append(name).append('(');
    }

    public void closeJsonp() {
        int state = stack.popInt();
        if (state != STATE_END) {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
        state = stack.popInt();
        if (state != STATE_JSONP) {
            stack.writeInt(state);
            stack.writeInt(STATE_END);
            throw new IllegalStateException(toString());
        }
        stack.writeInt(STATE_END);
        sb.append(')');
    }

    @Override
    public void key(@NotNull String key) {
        anyKey();
        Quote.JSON.append(sb, key);
        sb.append(':');
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        anyValue();
        sb.append(json);
    }

    public void stringify(StringBuilder sb) {
        sb.append("length = ").append(this.sb.length()).append(", stack = ");
        stack.stringifyReadable(sb, WRITER_STATE::forKey);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ClassUtility.normal(this)).append('[');
        stringify(sb);
        return sb.append(']').toString();
    }
}
