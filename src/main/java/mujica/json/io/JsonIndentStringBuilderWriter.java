package mujica.json.io;

import mujica.json.container.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.ds.of_char.sanitizer.CharSequenceAppender;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2021/4/23", project = "webbiton", name = "FormattedJsonBlobBuilder")
@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "ScriptStringWriter")
@CodeHistory(date = "2026/4/23")
@DirectSubclass({JsonTerminalStringBuilderWriter.class})
public class JsonIndentStringBuilderWriter extends JsonStringBuilderWriter { // write for human

    @Nullable
    protected String indent;

    @NotNull
    public JsonIndentStringBuilderWriter setIndent(@Nullable String indent) {
        this.indent = indent;
        return this;
    }

    public JsonIndentStringBuilderWriter(@NotNull StringBuilder sb) {
        super(sb);
        setFlags(ConfigFlags.ESCAPE_EXTRA | ConfigFlags.COMMA_SPACE | ConfigFlags.COLON_SPACE);
    }

    public JsonIndentStringBuilderWriter() {
        super();
        setFlags(ConfigFlags.ESCAPE_EXTRA | ConfigFlags.COMMA_SPACE | ConfigFlags.COLON_SPACE);
    }

    @SuppressWarnings("StringRepeatCanBeUsed")
    protected void beforeClose() {
        if (indent != null) {
            sb.append(System.lineSeparator());
            for (int count = stack.intLength() - 2; count > 0; count--) {
                sb.append(indent);
            }
        }
    }

    @SuppressWarnings("StringRepeatCanBeUsed")
    protected void afterOpen() {
        if (indent != null) {
            sb.append(System.lineSeparator());
            for (int count = stack.intLength() - 1; count > 0; count--) {
                sb.append(indent);
            }
        }
    }

    @SuppressWarnings("StringRepeatCanBeUsed")
    @Override
    protected void appendComma() {
        if (indent != null) {
            sb.append(',').append(System.lineSeparator());
            for (int count = stack.intLength(); count > 0; count--) {
                sb.append(indent);
            }
        } else if ((flags & ConfigFlags.COMMA_SPACE) != 0) {
            sb.append(", ");
        } else {
            sb.append(',');
        }
    }

    @Override
    public void openArray() {
        super.openArray();
        afterOpen();
    }

    @Override
    public void closeArray() {
        beforeClose();
        super.closeArray();
    }

    @Override
    public void openObject() {
        super.openObject();
        afterOpen();
    }

    @Override
    public void closeObject() {
        beforeClose();
        super.closeObject();
    }

    @Override
    public void openJsonp(@NotNull CharSequence name) {
        super.openJsonp(name);
        afterOpen();
    }

    @Override
    public void closeJsonp() {
        beforeClose();
        super.closeJsonp();
    }

    @Override
    public void stringKey(@NotNull String key) {
        anyKey();
        if ((flags & ConfigFlags.NO_QUOTE_PROPER_KEY) != 0 && PROPER_KEY.matcher(key).matches()) {
            sb.append(key);
        } else {
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
        }
        if ((flags & ConfigFlags.COLON_SPACE) != 0) {
            sb.append(": ");
        } else {
            sb.append(':');
        }
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        anyKey();
        if ((flags & ConfigFlags.NO_QUOTE_FAST_KEY) != 0 || (flags & ConfigFlags.NO_QUOTE_PROPER_KEY) != 0 && PROPER_KEY.matcher(key.toString()).matches()) {
            sb.append(key.toString());
            if ((flags & ConfigFlags.COLON_SPACE) != 0) {
                sb.append(": ");
            } else {
                sb.append(':');
            }
        } else {
            sb.append('"').append(key.toString())
                    .append((flags & ConfigFlags.COLON_SPACE) != 0 ? "\": " : "\":");
        }
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

    @Override
    public void numberValue(float value) {
        if ((flags & ConfigFlags.INFINITY_TO_NULL) != 0 && Float.isInfinite(value)) {
            nullValue();
        } else if ((flags & ConfigFlags.NAN_TO_NULL) != 0 && Float.isNaN(value)) {
            nullValue();
        } else {
            anyValue();
            if (value > 0.0f && (flags & ConfigFlags.PLUS_SIGN) != 0) {
                sb.append('+');
            }
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
            if (value > 0.0 && (flags & ConfigFlags.PLUS_SIGN) != 0) {
                sb.append('+');
            }
            sb.append(value);
        }
    }
}
