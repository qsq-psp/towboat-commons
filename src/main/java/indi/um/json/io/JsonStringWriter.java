package indi.um.json.io;

import indi.um.json.api.JsonStructure;
import indi.um.util.text.StringifyMethod;
import indi.um.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

/**
 * Created in collectord on 2020/8/30, named JsonWriter.
 * Created on 2022/6/4.
 */
public class JsonStringWriter extends SimpleJsonStringWriter {

    public static String stringify(JsonStructure structure) {
        final JsonStringWriter stringWriter = new JsonStringWriter();
        structure.toJson(stringWriter);
        return stringWriter.get();
    }

    public static String stringify(String prefix, JsonStructure structure, String suffix) {
        final JsonStringWriter stringWriter = new JsonStringWriter();
        if (prefix != null) {
            stringWriter.sb.append(prefix);
        }
        structure.toJson(stringWriter);
        if (suffix != null) {
            stringWriter.sb.append(suffix);
        }
        return stringWriter.get();
    }

    public static <E extends JsonStructure> String stringify(StringifyMethod<E> prefix, E structure, StringifyMethod<E> suffix) {
        final JsonStringWriter stringWriter = new JsonStringWriter();
        if (prefix != null) {
            prefix.stringify(stringWriter.sb, structure);
        }
        structure.toJson(stringWriter);
        if (suffix != null) {
            suffix.stringify(stringWriter.sb, structure);
        }
        return stringWriter.get();
    }

    protected int hexCase = LOWER;

    protected int mantissa = -1;

    protected int indent = -1;

    public JsonStringWriter() {
        super();
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
        indent = count;
    }

    private boolean appendIndent() {
        if (indent < 0) {
            return false;
        }
        if (indent == 0) {
            return true;
        }
        sb.append("\r\n");
        final int depth = stack.writerIndex() - 1;
        if (depth > 0) {
            int count = indent * depth;
            while (count >= 4) {
                sb.append("    ");
                count -= 4;
            }
            while (count >= 1) {
                sb.append(' ');
                count--;
            }
        }
        return false;
    }

    @Override
    protected void anyKey() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_OBJECT:
                stack.writeInt(STATE_KEY);
                sb.append(',');
                if (appendIndent()) {
                    sb.append(' ');
                }
                break;
            case STATE_NEW_OBJECT:
                stack.writeInt(STATE_KEY);
                appendIndent();
                break;
            default:
                stack.writeInt(state);
                throw new IllegalStateException(toString());
        }
    }

    @Override
    protected void anyValue() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_START:
                stack.writeInt(STATE_END);
                break;
            case STATE_ARRAY:
                stack.writeInt(STATE_ARRAY);
                sb.append(',');
                if (appendIndent()) {
                    sb.append(' ');
                }
                break;
            case STATE_NEW_ARRAY:
                stack.writeInt(STATE_ARRAY);
                appendIndent();
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
        if (state == STATE_NEW_ARRAY) {
            sb.append(']');
        } else if (state == STATE_ARRAY) {
            appendIndent();
            sb.append(']');
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void arrayValue() {
        anyValue();
        sb.append("[]");
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
        if (state == STATE_NEW_OBJECT) {
            sb.append('}');
        } else if (state == STATE_OBJECT) {
            appendIndent();
            sb.append('}');
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void objectValue() {
        anyValue();
        sb.append("{}");
    }

    @Override
    public void key(@NotNull String key) {
        anyKey();
        quoted(key);
        if (indent >= 0) {
            sb.append(": ");
        } else {
            sb.append(':');
        }
    }

    @Override
    public void key(@NotNull TypedString key) {
        anyKey();
        if (key.isDirect(TypedString.DIRECT_JSON)) {
            sb.append('"').append(key.getString()).append('"');
        } else {
            quoted(key.getString());
        }
        if (indent >= 0) {
            sb.append(": ");
        } else {
            sb.append(':');
        }
    }

    @Override
    public void booleanValue(boolean value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        sb.append(value);
    }

    @Override
    public void numberValue(double value) {
        anyValue();
        if (Double.isFinite(value)) {
            if (mantissa < 0) {
                sb.append(value);
            } else if (mantissa == 0) {
                sb.append(Math.round(value));
            } else {
                numberValueLoop(value);
            }
        } else {
            throw new IllegalArgumentException("double value " + value);
        }
    }

    protected void numberValueLoop(double value) {
        final long integral = (long) value;
        if (integral == 0 && value < 0.0) {
            sb.append("-0");
        } else {
            sb.append(integral);
        }
        value = Math.abs(value - integral);
        int wi = -1;
        for (int ri = 0; ri < mantissa; ri++) {
            value *= 10;
            int digit = (int) value;
            value -= digit;
            if (digit != 0) {
                while (wi < ri) {
                    sb.append(wi == -1 ? '.' : '0');
                    wi++;
                }
                sb.append((char) ('0' + digit));
                wi++;
            }
        }
    }

    @Override
    public void stringValue(@NotNull String value) {
        anyValue();
        quoted(value);
    }

    @Override
    public boolean checksStructure() {
        return true;
    }

    protected void quoted(String string) {
        sb.append('"');
        final int length = string.length();
        for (int index = 0; index < length; index++) {
            int ch = string.charAt(index);
            switch (ch) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                default:
                    if (ch < 0x20) {
                        appendEscapeX(ch);
                    } else if (Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE) {
                        appendEscapeU(ch);
                    } else {
                        sb.append((char) ch);
                    }
                    break;
            }
        }
        sb.append('"');
    }

    private void appendEscapeX(int ch) {
        sb.append("\\u00");
        hexDigit(ch >> 4);
        hexDigit(ch);
    }

    private void appendEscapeU(int ch) {
        sb.append("\\u");
        hexDigit(ch >> 12);
        hexDigit(ch >> 8);
        hexDigit(ch >> 4);
        hexDigit(ch);
    }

    private void hexDigit(int ch) {
        ch &= 0xf;
        if (ch < 0xa) {
            sb.append((char) ('0' + ch));
        } else {
            sb.append((char) (hexCase + ch));
        }
    }

    @Override
    public void stringify(StringBuilder sb) {
        super.stringify(sb);
        sb.append(", hexCase = ").append(HEX_CASE.forKey(hexCase)).append(", mantissa = ").append(mantissa);
    }
}
