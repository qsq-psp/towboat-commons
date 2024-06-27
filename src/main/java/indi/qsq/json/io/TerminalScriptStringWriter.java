package indi.qsq.json.io;

import indi.qsq.util.io.TerminalStyle;
import indi.qsq.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/8/18.
 */
public class TerminalScriptStringWriter extends ScriptStringWriter {

    public final TerminalStyle stringKeyStyle = (new TerminalStyle()).setMode(TerminalStyle.DisplayMode.UNDERLINE);

    public final TerminalStyle stringValueStyle = (new TerminalStyle()).setForeground(TerminalStyle.DisplayColor.MAGENTA);

    public final TerminalStyle numberValueStyle = (new TerminalStyle()).setForeground(TerminalStyle.DisplayColor.BLUE);

    public final TerminalStyle booleanValueStyle = (new TerminalStyle()).setForeground(TerminalStyle.DisplayColor.CYAN);

    public final TerminalStyle escapeSequenceStyle = (new TerminalStyle()).setForeground(TerminalStyle.DisplayColor.RED);

    public TerminalScriptStringWriter() {
        super();
    }

    @Override
    public void key(@NotNull String key) {
        anyKey();
        if (isIdentifier(key)) {
            sb.append(stringKeyStyle.getSequence()).append(key).append(TerminalStyle.RESET);
        } else {
            quoted(key, stringKeyStyle.getSequence());
        }
        if (indent >= 0) {
            sb.append(": ");
        } else {
            sb.append(':');
        }
    }

    @Override
    public void key(@NotNull TypedString key) {
        anyKey();
        if (key.isDirect(TypedString.DIRECT_URI | TypedString.DIRECT_XML_NAME)) {
            sb.append(stringKeyStyle.getSequence()).append(key.getString()).append(TerminalStyle.RESET);
        } else if (key.isDirect(TypedString.DIRECT_JSON)) {
            sb.append('"').append(stringKeyStyle.getSequence()).append(key.getString()).append(TerminalStyle.RESET).append('"');
        } else {
            quoted(key.getString(), stringKeyStyle.getSequence());
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
        sb.append(booleanValueStyle.getSequence()).append(value).append(TerminalStyle.RESET);
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        sb.append(numberValueStyle.getSequence()).append(value).append(TerminalStyle.RESET);
    }

    @Override
    public void numberValue(double value) {
        anyValue();
        sb.append(numberValueStyle.getSequence());
        if (mantissa < 0 || !Double.isFinite(value)) {
            sb.append(value);
        } else if (mantissa == 0) {
            sb.append(Math.round(value));
        } else {
            numberValueLoop(value);
        }
        sb.append(TerminalStyle.RESET);
    }

    @Override
    public void stringValue(@NotNull String value) {
        anyValue();
        quoted(value, stringValueStyle.getSequence());
    }

    private void quoted(String string, String style) {
        sb.append('"').append(style);
        final String escape = escapeSequenceStyle.getSequence();
        final int length = string.length();
        for (int index = 0; index < length; index++) {
            int ch = string.charAt(index);
            switch (ch) {
                case '\b':
                    sb.append(escape).append("\\b").append(style);
                    break;
                case '\t':
                    sb.append(escape).append("\\t").append(style);
                    break;
                case '\n':
                    sb.append(escape).append("\\n").append(style);
                    break;
                case '\f':
                    sb.append(escape).append("\\f").append(style);
                    break;
                case '\r':
                    sb.append(escape).append("\\r").append(style);
                    break;
                case '\\':
                    sb.append(escape).append("\\\\").append(style);
                    break;
                case '"':
                    sb.append(escape).append("\\\"").append(style);
                    break;
                default:
                    if (ch < 0x20) {
                        sb.append(escape).append("\\x");
                        hexDigit(ch >> 4);
                        hexDigit(ch);
                        sb.append(style);
                    } else if (Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE) {
                        sb.append(escape).append("\\u");
                        hexDigit(ch >> 12);
                        hexDigit(ch >> 8);
                        hexDigit(ch >> 4);
                        hexDigit(ch);
                        sb.append(style);
                    } else {
                        sb.append((char) ch);
                    }
                    break;
            }
        }
        sb.append(TerminalStyle.RESET).append('"');
    }

    private void hexDigit(int ch) {
        ch &= 0xf;
        if (ch < 0xa) {
            sb.append((char) ('0' + ch));
        } else {
            sb.append((char) (hexCase + ch));
        }
    }
}
