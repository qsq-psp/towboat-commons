package indi.qsq.json.io;

import indi.qsq.json.api.JsonStructure;
import indi.qsq.util.text.Quote;
import indi.qsq.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/8/18.
 */
public class UriJsonStringWriter extends SimpleJsonStringWriter {

    public static String stringify(JsonStructure js) {
        final UriJsonStringWriter sw = new UriJsonStringWriter();
        js.toJson(sw);
        return sw.get();
    }

    private int hexCase;

    private String comma, colon, rectOpen, rectClose, curlyOpen, curlyClose;

    public UriJsonStringWriter() {
        super();
        setHexCase(false);
    }

    /**
     * @param x from 0x00 to 0x7f
     */
    private String encodeAscii(int x) {
        int hx = 0xf & x;
        if (hx < 0xa) {
            hx += '0';
        } else {
            hx += hexCase;
        }
        return "%" + ('0' + (0x7 & (x >> 4))) + ((char) hx);
    }

    @Override
    public void setHexCase(boolean upper) {
        this.hexCase = upper ? UPPER : LOWER;
        comma = encodeAscii(',');
        colon = encodeAscii(':');
        rectOpen = encodeAscii('[');
        rectClose = encodeAscii(']');
        curlyOpen = encodeAscii('{');
        curlyClose = encodeAscii('}');
    }

    protected void anyKey() {
        final int state = stack.popInt();
        switch (state) {
            case STATE_OBJECT:
                sb.append(comma);
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
                sb.append(comma);
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
        sb.append(rectOpen);
    }

    @Override
    public void closeArray() {
        final int state = stack.popInt();
        if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
            sb.append(rectClose);
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void arrayValue() {
        anyValue();
        sb.append(rectOpen).append(rectClose);
    }

    @Override
    public void openObject() {
        anyValue();
        stack.writeInt(STATE_NEW_OBJECT);
        sb.append(curlyOpen);
    }

    @Override
    public void closeObject() {
        final int state = stack.popInt();
        if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
            sb.append(curlyClose);
        } else {
            stack.writeInt(state);
            throw new IllegalStateException(toString());
        }
    }

    @Override
    public void key(@NotNull String key) {
        anyKey();
        Quote.JSON.append(sb, key);
        sb.append(colon);
    }

    @Override
    public void key(@NotNull TypedString key) {
        anyKey();
        if (key.isDirect(TypedString.DIRECT_URI | TypedString.DIRECT_JSON)) {
            sb.append('"').append(key.getString()).append('"');
        } else {
            quoted(key.getString());
        }
        sb.append(':');
    }

    private void quoted(String string) {
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
                /*
                case ' ':
                    sb.append('+');
                    break;
                */
                case ',':
                    sb.append(comma);
                    break;
                case ':':
                    sb.append(colon);
                    break;
                case '[':
                    sb.append(rectOpen);
                    break;
                case ']':
                    sb.append(rectClose);
                    break;
                case '{':
                    sb.append(curlyOpen);
                    break;
                case '}':
                    sb.append(curlyClose);
                    break;
                case '%':
                case '/':
                case '?':
                case '#':
                case '@':
                case '!':
                case '$':
                case '&':
                case '\'':
                case '(':
                case ')':
                case '*':
                case '+':
                case ';':
                case '=':
                case 0x7f:
                    sb.append('%');
                    hexDigit(ch >> 4);
                    hexDigit(ch);
                    break;
                default:
                    if (ch <= 0x20) {
                        sb.append('%');
                        hexDigit(ch >> 4);
                        hexDigit(ch);
                    } else if (ch < 0x0800) {
                        int x = 0xc0 | ((ch >> 6) & 0x1f);
                        sb.append('%');
                        hexDigit(x >> 4);
                        hexDigit(x);
                        x = 0x80 | (ch & 0x3f);
                        sb.append('%');
                        hexDigit(x >> 4);
                        hexDigit(x);
                    } else if (Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE) {
                        sb.append("\\u");
                        hexDigit(ch >> 12);
                        hexDigit(ch >> 8);
                        hexDigit(ch >> 4);
                        hexDigit(ch);
                    } else {
                        int x = 0xe0 | ((ch >> 12) & 0x0f);
                        sb.append('%');
                        hexDigit(x >> 4);
                        hexDigit(x);
                        x = 0x80 | ((ch >> 6) & 0x3f);
                        sb.append('%');
                        hexDigit(x >> 4);
                        hexDigit(x);
                        x = 0x80 | (ch & 0x3f);
                        sb.append('%');
                        hexDigit(x >> 4);
                        hexDigit(x);
                    }
                    break;
            }
        }
        sb.append('"');
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
        sb.append(", hexCase = ").append(hexCase);
    }
}
