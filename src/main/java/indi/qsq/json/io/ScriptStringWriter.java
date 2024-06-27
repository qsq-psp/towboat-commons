package indi.um.json.io;

import indi.um.util.text.TypedString;
import org.jetbrains.annotations.NotNull;

/**
 * Created in webbiton on 2021/4/23, named FormattedJsonBlobBuilder.
 * Created on 2022/8/18.
 *
 * Difference:
 * If possible, key strings are not quoted
 * Accept +Infinity, -Infinity, and NaN value
 */
public class ScriptStringWriter extends JsonStringWriter {

    public ScriptStringWriter() {
        super();
    }

    protected boolean isIdentifier(String string) {
        final int length = string.length();
        if (length == 0) {
            return false;
        }
        int ch = string.charAt(0);
        if (!('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'A' || ch == '$' || ch == '_')) {
            return false;
        }
        for (int index = 1; index < length; index++) {
            ch = string.charAt(index);
            if (!('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || '0' <= ch && ch <= '9' || ch == '$' || ch == '_')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void key(@NotNull String key) {
        anyKey();
        if (isIdentifier(key)) {
            sb.append(key);
        } else {
            quoted(key);
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
            sb.append(key);
        } else if (key.isDirect(TypedString.DIRECT_JSON)) {
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
    public void numberValue(double value) {
        anyValue();
        if (mantissa < 0 || !Double.isFinite(value)) {
            sb.append(value); // Including +Infinity, -Infinity, and NaN value
        } else if (mantissa == 0) {
            sb.append(Math.round(value));
        } else {
            numberValueLoop(value);
        }
    }
}
