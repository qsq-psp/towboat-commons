package mujica.json.io;

import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.sanitizer.CharSequenceAppender;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2021/4/23", project = "webbiton", name = "FormattedJsonBlobBuilder")
@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "ScriptStringWriter")
@CodeHistory(date = "2026/4/23")
public class JsonIndentStringBuilderWriter extends JsonStringBuilderWriter { // write for human

    public JsonIndentStringBuilderWriter(@NotNull StringBuilder sb) {
        super(sb);
        setFlags(ConfigFlags.ESCAPE_EXTRA | ConfigFlags.COMMA_SPACE | ConfigFlags.COLON_SPACE);
    }

    public JsonIndentStringBuilderWriter() {
        super();
        setFlags(ConfigFlags.ESCAPE_EXTRA | ConfigFlags.COMMA_SPACE | ConfigFlags.COLON_SPACE);
    }

    @Override
    protected void appendComma() {
        if ((flags & ConfigFlags.COMMA_SPACE) != 0) {
            sb.append(", ");
        } else {
            sb.append(',');
        }
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
        if ((flags & ConfigFlags.COLON_SPACE) != 0) {
            sb.append(": ");
        } else {
            sb.append(':');
        }
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        anyKey();
        sb.append('"').append(key.toString())
                .append((flags & ConfigFlags.COLON_SPACE) != 0 ? "\": " : "\":");
    }

    @Override
    public void numberValue(int value) {
        anyValue();
        if (value > 0 && (flags & ConfigFlags.PLUS_SIGN) != 0) {
            sb.append('+');
        }
        sb.append(value);
    }

    @Override
    public void numberValue(long value) {
        anyValue();
        if (value > 0 && (flags & ConfigFlags.PLUS_SIGN) != 0) {
            sb.append('+');
        }
        sb.append(value);
    }
}
