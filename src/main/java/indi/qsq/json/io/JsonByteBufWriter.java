package indi.um.json.io;

import indi.um.json.api.JsonStructure;
import indi.um.util.ds.HeapIntBuf;
import indi.um.util.io.ByteBufWriter;
import indi.um.util.io.Utf8Utility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

/**
 * Created in webbiton on 2020/12/16, named JsonBlobBuilder.
 * Recreated in va on 2021/9/19, named JsonWriter.
 * Recreated in infrastructure on 2021/12/30, named ByteBufWriter.
 * Recreated on 2022/6/15.
 *
 * JsonByteBufWriter do not have autoFlush() method any more
 */
public class JsonByteBufWriter extends ByteBufWriter implements Writer {

    public static ByteBuf marshal(@NotNull JsonStructure js) {
        final ByteBuf byteBuf = Unpooled.buffer();
        try {
            js.toJson(new JsonByteBufWriter(byteBuf));
            return byteBuf;
        } catch (Throwable e) {
            byteBuf.release();
            throw e;
        }
    }

    private final HeapIntBuf stack = new HeapIntBuf();

    private int hexCase = LOWER;

    private int mantissa = -1;

    public JsonByteBufWriter(ByteBuf byteBuf) {
        super(byteBuf);
        start();
    }

    public void start() {
        stack.removeAll();
        stack.writeInt(STATE_START);
    }

    public void restart() {
        clear();
        start();
    }

    @Override
    public void setHexCase(boolean upper) {
        this.hexCase = upper ? UPPER : LOWER;
    }

    @Override
    public void setMantissa(int count) {
        mantissa = count;
    }

    @Override
    public void setIndent(int count) {
        throw new UnsupportedOperationException();
    }

    private void anyValue() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_START:
                stack.writeInt(STATE_END);
                break;
            case STATE_ARRAY:
                save();
                cache[position++] = ',';
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

    private void jsonString(String content) {
        save();
        cache[position++] = '"';
        final int length = content.length();
        for (int i = 0; i < length; i++) {
            int ch = content.charAt(i);
            if (ch < 0x00A0) {
                switch (ch) {
                    case '"':
                        cache[position++] = '\\';
                        cache[position++] = '"';
                        break;
                    case '\\':
                        cache[position++] = '\\';
                        cache[position++] = '\\';
                        break;
                    case 0x8:
                        cache[position++] = '\\';
                        cache[position++] = 'b';
                        break;
                    case 0x9:
                        cache[position++] = '\\';
                        cache[position++] = 't';
                        break;
                    case 0xa:
                        cache[position++] = '\\';
                        cache[position++] = 'n';
                        break;
                    case 0xc:
                        cache[position++] = '\\';
                        cache[position++] = 'f';
                        break;
                    case 0xd:
                        cache[position++] = '\\';
                        cache[position++] = 'r';
                        break;
                    default:
                        if (ch < 0x20 || ch >= 0x7F) {
                            hex16(ch);
                        } else {
                            cache[position++] = (byte) ch;
                        }
                        break;
                }
            } else if (ch <= 0x07FF) {
                position = Utf8Utility.encodeB2(cache, position, ch);
            } else if (Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE) {
                hex16(ch);
            } else {
                position = Utf8Utility.encodeB3(cache, position, ch);
            }
            if ((i & SAVE_MASK) == SAVE_MASK) {
                save();
            }
        }
        cache[position++] = '"';
    }

    private void hex16(int ch) {
        save();
        cache[position++] = '\\';
        cache[position++] = 'u';
        for (int i = 12; i >= 0; i -= 4) {
            int nibble = 0xf & (ch >> i);
            if (nibble < 0xa) {
                nibble += '0';
            } else {
                nibble += hexCase;
            }
            cache[position++] = (byte) nibble;
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
        characters(name);
        save();
        cache[position++] = '(';
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
        save();
        cache[position++] = ')';
    }

    @Override
    public void openArray() {
        anyValue();
        stack.writeInt(STATE_NEW_ARRAY);
        save();
        cache[position++] = '[';
    }

    @Override
    public void closeArray() {
        final int state = stack.popInt();
        if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
            save();
            cache[position++] = ']';
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void arrayValue() {
        anyValue();
        save();
        cache[position++] = '[';
        cache[position++] = ']';
    }

    @Override
    public void openObject() {
        anyValue();
        stack.writeInt(STATE_NEW_OBJECT);
        save();
        cache[position++] = '{';
    }

    @Override
    public void closeObject() {
        final int state = stack.popInt();
        if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
            save();
            cache[position++] = '}';
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void objectValue() {
        anyValue();
        save();
        cache[position++] = '{';
        cache[position++] = '}';
    }

    @Override
    public void key(@NotNull String key) {
        final int state = stack.popInt();
        switch (state) {
            case STATE_OBJECT:
                save();
                cache[position++] = ',';
            case STATE_NEW_OBJECT:
                break;
            default:
                stack.writeInt(state);
                throw new IllegalStateException(toString());
        }
        stack.writeInt(STATE_KEY);
        jsonString(key);
        cache[position++] = ':';
    }

    @Override
    public void jsonValue(@NotNull CharSequence json) {
        anyValue();
        characters(json);
    }

    @Override
    public void nullValue() {
        anyValue();
        shortAscii("null");
    }

    @Override
    public void booleanValue(boolean value) {
        anyValue();
        shortAscii(Boolean.toString(value));
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        shortAscii(Long.toString(value));
    }

    @Override
    public void numberValue(double value) {
        anyValue();
        if (Double.isFinite(value)) {
            if (mantissa < 0) {
                shortAscii(Double.toString(value));
            } else if (mantissa == 0) {
                shortAscii(Long.toString(Math.round(value)));
            } else {
                decimal(value, mantissa);
            }
        } else {
            throw new IllegalArgumentException("double value " + value);
        }
    }

    @Override
    public void stringValue(@NotNull String value) {
        anyValue();
        jsonString(value);
    }

    @Override
    public boolean checksStructure() {
        return true;
    }

    @Override
    public void stringify(StringBuilder sb) {
        sb.append('[')
                .append(content()).append(", pos = ").append(position)
                .append(", stack = ");
        stack.stringifyReadable(sb, WRITER_STATE::forKey);
        sb.append(", hexCase = ").append(HEX_CASE.forKey(hexCase))
                .append(", mantissa = ").append(mantissa)
                .append(']');
    }
}
