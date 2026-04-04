package mujica.json.io;

import io.netty.buffer.ByteBuf;
import mujica.io.codec.Base16Case;
import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2026/3/31.
 */
@CodeHistory(date = "2026/3/31")
public class JsonCharStreamWriter extends JsonStreamWriter {

    @NotNull
    protected final Writer out;

    public JsonCharStreamWriter(@NotNull Writer out) {
        super();
        this.out = out;
    }

    protected void anyKey() throws IOException {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_OBJECT:
                out.write(',');
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

    protected void anyValue() throws IOException {
        final int state = stack.removeLast();
        switch (state) {
            case STATE_START:
                stack.offerLast(STATE_END);
                break;
            case STATE_ARRAY:
                out.write(',');
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

    private void escapeString(@NotNull String string) throws IOException {
        final int length = string.length();
        int start = 0;
        LABEL:
        for (int index = 0; index < length; index++) {
            int ch = string.charAt(index);
            int action;
            switch (ch) {
                case '\b':
                    action = 'b';
                    break;
                case '\t':
                    action = 't';
                    break;
                case '\n':
                    action = 'n';
                    break;
                case '\f':
                    action = 'f';
                    break;
                case '\r':
                    action = 'r';
                    break;
                case '\\':
                case '"':
                    action = ch;
                    break;
                default:
                    continue LABEL;
            }
            if (start < index) {
                out.write(string, start, index - start);
            }
            out.write('\\');
            out.write(action);
            start = index + 1;
        }
        if (start < length) {
            out.write(string, start, length - start);
        }
    }

    private void escapeStringExtra(@NotNull String string) throws IOException {
        final int length = string.length();
        int start = 0;
        LABEL:
        for (int index = 0; index < length; index++) {
            int ch = string.charAt(index);
            int action;
            switch (ch) {
                case 0x00: case 0x01: case 0x02: case 0x03: case 0x04:
                case 0x05: case 0x06: case 0x07: case 0x0b: case 0x0e:
                case 0x0f: case 0x10: case 0x11: case 0x12: case 0x13:
                case 0x14: case 0x15: case 0x16: case 0x17: case 0x18:
                case 0x19: case 0x1a: case 0x1b: case 0x1c: case 0x1d:
                case 0x1e: case 0x1f: case 0x7f: case 0x80: case 0x81:
                case 0x82: case 0x83: case 0x84: case 0x85: case 0x86:
                case 0x87: case 0x88: case 0x89: case 0x8a: case 0x8b:
                case 0x8c: case 0x8d: case 0x8e: case 0x8f: case 0x90:
                case 0x91: case 0x92: case 0x93: case 0x94: case 0x95:
                case 0x96: case 0x97: case 0x98: case 0x99: case 0x9a:
                case 0x9b: case 0x9c: case 0x9d: case 0x9e: case 0x9f:
                case 0x00ad: case 0x0600: case 0x0601: case 0x0602: case 0x0603:
                case 0x0604: case 0x0605: case 0x061c: case 0x06dd: case 0x070f:
                case 0x0890: case 0x0891: case 0x08e2: case 0x180e: case 0x200b:
                case 0x200c: case 0x200d: case 0x200e: case 0x200f: case 0x202a:
                case 0x202b: case 0x202c: case 0x202d: case 0x202e: case 0x2060:
                case 0x2061: case 0x2062: case 0x2063: case 0x2064: case 0x2066:
                case 0x2067: case 0x2068: case 0x2069: case 0x206a: case 0x206b:
                case 0x206c: case 0x206d: case 0x206e: case 0x206f: case 0xfeff:
                case 0xfff9: case 0xfffa: case 0xfffb: case 0xfffc: case 0xfffd:
                    action = 0;
                    break;
                case '\b':
                    action = 'b';
                    break;
                case '\t':
                    action = 't';
                    break;
                case '\n':
                    action = 'n';
                    break;
                case '\f':
                    action = 'f';
                    break;
                case '\r':
                    action = 'r';
                    break;
                case '\\':
                case '"':
                    action = ch;
                    break;
                default:
                    continue LABEL;
            }
            if (start < index) {
                out.write(string, start, index - start);
            }
            out.write('\\');
            if (action != 0) {
                out.write(action);
            } else {
                out.write('u');
                for (int shift = 12; shift >= 0; shift -= 4) {
                    int digit = 0xf & (ch >> shift);
                    if (digit < 0xa) {
                        out.write('0' + digit);
                    } else if ((flags & FLAG_UPPERCASE_HEX) != 0) {
                        out.write(Base16Case.UPPER_CONSTANT + digit);
                    } else {
                        out.write(Base16Case.LOWER_CONSTANT + digit);
                    }
                }
            }
            start = index + 1;
        }
        if (start < length) {
            out.write(string, start, length - start);
        }
    }

    @Override
    public void openArray() {
        try {
            anyValue();
            stack.offerLast(STATE_NEW_ARRAY);
            out.write('[');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void closeArray() {
        final int state = stack.removeLast();
        try {
            if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
                out.write(']');
            } else {
                stack.offerLast(state);
                throwState();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void openObject() {
        try {
            anyValue();
            stack.offerLast(STATE_NEW_OBJECT);
            out.write('{');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void closeObject() {
        final int state = stack.removeLast();
        try {
            if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
                out.write('}');
            } else {
                stack.offerLast(state);
                throwState();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
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
        try {
            out.write(name.toString());
            out.write('(');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
        try {
            out.write(')');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void stringKey(@NotNull String key) {
        try {
            anyKey();
            out.write('"');
            if ((flags & FLAG_ESCAPE_EXTRA) == 0) {
                escapeString(key);
            } else {
                escapeStringExtra(key);
            }
            out.write('"');
            out.write(':');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        try {
            anyKey();
            out.write('"');
            out.write(key.string);
            out.write('"');
            out.write(':');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void skippedValue(@NotNull ByteBuf value) {
        try {
            out.write(value.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            value.release();
        }
    }

    @Override
    public void nullValue() {
        try {
            anyValue();
            out.write("null");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void booleanValue(boolean value) {
        try {
            anyValue();
            out.write(String.valueOf(value));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(int value) {
        try {
            anyValue();
            out.write(String.valueOf(value));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(long value) {
        try {
            anyValue();
            out.write(String.valueOf(value));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(float value) {
        try {
            anyValue();
            if (Float.isFinite(value)) {
                String string = String.valueOf(value);
                if ((flags & FLAG_UPPERCASE_E) != 0) {
                    string = string.toUpperCase();
                } else {
                    string = string.toLowerCase();
                }
                out.write(string);
            } else if (Float.isNaN(value)) {
                if ((flags & FLAG_NAN_TO_NULL) != 0) {
                    out.write("null");
                } else {
                    out.write("NaN");
                }
            } else {
                if ((flags & FLAG_INFINITY_TO_NULL) != 0) {
                    out.write("null");
                } else if (value > 0.0f) {
                    out.write("Infinity");
                } else {
                    out.write("-Infinity");
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(double value) {
        try {
            anyValue();
            if (Double.isFinite(value)) {
                String string = String.valueOf(value);
                if ((flags & FLAG_UPPERCASE_E) != 0) {
                    string = string.toUpperCase();
                } else {
                    string = string.toLowerCase();
                }
                out.write(string);
            } else if (Double.isNaN(value)) {
                if ((flags & FLAG_NAN_TO_NULL) != 0) {
                    out.write("null");
                } else {
                    out.write("NaN");
                }
            } else {
                if ((flags & FLAG_INFINITY_TO_NULL) != 0) {
                    out.write("null");
                } else if (value > 0.0f) {
                    out.write("Infinity");
                } else {
                    out.write("-Infinity");
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(@NotNull BigInteger value) {
        try {
            anyValue();
            out.write(value.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        try {
            anyValue();
            out.write(value.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        try {
            anyValue();
            out.write('"');
            if ((flags & FLAG_ESCAPE_EXTRA) == 0) {
                escapeString(value.toString());
            } else {
                escapeStringExtra(value.toString());
            }
            out.write('"');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        try {
            anyValue();
            out.write('"');
            out.write(value.string);
            out.write('"');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void emptyArrayValue() {
        try {
            anyValue();
            out.write("[]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void emptyObjectValue() {
        try {
            anyValue();
            out.write("{}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
