package mujica.json.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import mujica.algebra.discrete.BigConstants;
import mujica.io.codec.Base16Case;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.TypePreference;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2021/9/19", project = "va", name = "ByteBufReader")
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "ByteBufReader")
@CodeHistory(date = "2022/8/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/24")
public class JsonByteBufReader extends DefaultByteBufHolder implements JsonSyncSkipReader {

    protected int flags;

    public JsonByteBufReader(@NotNull ByteBuf data) {
        super(data);
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public void skip() {
        skipGap();
        skipJson();
    }

    private void skipJson() {
        final int x = content().readByte();
        switch (x) {
            case 'I':
                skipWord("Infinity");
                break;
            case 'N':
                skipWord("NaN");
                break;
            case 'f':
                skipWord("false");
                break;
            case 'n':
                skipWord("null");
                break;
            case 't':
                skipWord("true");
                break;
            case '+': case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                skipLiteral();
                break;
            case '"': case '\'': case '`':
                skipString(x);
                break;
            case '{':
                skipJsonObject();
                break;
            case '[':
                skipJsonArray();
                break;
            default:
                throw new RuntimeException("unrecognized " + x);
        }
    }

    private void skipWord(@NotNull String word) {
        final ByteBuf data = content();
        final int n = word.length();
        for (int i = 1; i < n; i++) { // char at 0 is already read
            if (data.readByte() != word.charAt(i)) {
                throw new RuntimeException("word " + word);
            }
        }
    }

    private void skipLiteral() {
        final ByteBuf data = content();
        while (data.isReadable()) {
            int x = data.readByte();
            if ('0' <= x && x <= '9' || 'a' <= x && x <= 'z' || 'A' <= x && x <= 'Z' || x == '+' || x == '-' || x == '.') {
                continue;
            }
            data.readerIndex(data.readerIndex() - 1);
            break;
        }
    }

    private void skipString(@DataType("u8") int quoteChar) {
        final ByteBuf data = content();
        while (true) {
            int x = data.readByte();
            if (x == '\\') {
                data.readByte();
            } else if (x == quoteChar) {
                break;
            }
        }
    }

    private void skipJsonObject() {
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            skipGap();
            int x = content().readByte();
            switch (x) {
                case '"':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    skipString(x);
                    break;
                case '\'':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                        throw new RuntimeException("apostrophe");
                    }
                    skipString(x);
                    break;
                case '`':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                        throw new RuntimeException("grave accent");
                    }
                    skipString(x);
                    break;
                case ',':
                    if (comma) {
                        throw new RuntimeException("duplicate comma");
                    }
                    if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                        throw new RuntimeException("leading comma");
                    }
                    comma = true;
                    skipGap();
                    continue LABEL;
                case '}':
                    if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                        throw new RuntimeException("trailing comma");
                    }
                    break LABEL;
                default:
                    throw new RuntimeException("unrecognized " + x);
            }
            skipGap();
            x = content().readByte();
            if (x != ':') {
                throw new RuntimeException("expect colon actual " + x);
            }
            skipGap();
            skipJson();
            first = false;
            comma = false;
        }
    }

    private void skipJsonArray() {
        boolean first = true;
        boolean comma = false;
        while (true) {
            skipGap();
            int x = content().readByte();
            if (x == ',') {
                if (comma) {
                    throw new RuntimeException("duplicate comma");
                }
                if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new RuntimeException("leading comma");
                }
                comma = true;
                continue;
            } else if (x == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new RuntimeException("trailing comma");
                }
                break;
            }
            if (comma || first) {
                ByteBuf data = content();
                data.readerIndex(data.readerIndex() - 1);
                skipJson();
                first = false;
                comma = false;
            } else {
                throw new RuntimeException("missing comma");
            }
        }
    }

    private void skipGap() {
        final ByteBuf data = content();
        int x;
        if ((flags & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            do {
                x = data.readUnsignedByte();
            } while (x <= 0x20);
        } else {
            while (true) {
                x = data.readUnsignedByte();
                if (x != '/') {
                    if (x > 0x20) {
                        break;
                    }
                    continue;
                }
                x = data.readUnsignedByte();
                if (x == '/') {
                    if ((flags & FLAG_LINE_COMMENT) == 0) {
                        throw new RuntimeException("line comment");
                    }
                    do {
                        x = data.readUnsignedByte();
                    } while (x != '\n');
                    data.readerIndex(data.readerIndex() - 1);
                } else if (x == '*') {
                    if ((flags & FLAG_BLOCK_COMMENT) == 0) {
                        throw new RuntimeException("block comment");
                    }
                    while (true) {
                        x = data.readUnsignedByte();
                        if (x == '*') {
                            x = data.readUnsignedByte();
                            if (x == '/') {
                                break;
                            } else {
                                data.readerIndex(data.readerIndex() - 1);
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("comment unrecognized " + x);
                }
            }
        }
        data.readerIndex(data.readerIndex() - 1);
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        skipGap();
        readJson(jh);
    }

    private void readJson(@NotNull JsonHandler jh) {
        // gap already skipped now
        final ByteBuf data = content();
        if (jh.testTypePreference(TypePreference.FLAG_SKIP_TO_BYTE_BUF)) {
            int startIndex = data.readerIndex();
            skipJson();
            jh.skippedValue(data.slice(startIndex, data.readerIndex() - startIndex));
        } else if (jh.testTypePreference(TypePreference.FLAG_SKIP_VALUE)) {
            skipJson();
            jh.skippedValue();
            return;
        }
        final int x = data.readUnsignedByte();
        switch (x) {
            case 'I':
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new RuntimeException("infinity");
                }
                skipWord("Infinity");
                jh.numberValue(new FastNumber("Infinity"));
                break;
            case 'N':
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new RuntimeException("not a number");
                }
                skipWord("NaN");
                jh.numberValue(new FastNumber("NaN"));
                break;
            case 'f':
                skipWord("false");
                jh.booleanValue(false);
                break;
            case 'n':
                skipWord("null");
                jh.nullValue();
                break;
            case 't':
                skipWord("true");
                jh.booleanValue(true);
                break;
            case '+': case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                readJsonLiteral(jh);
                break;
            case '`':
                if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                    throw new RuntimeException("grave accent");
                }
                jh.stringValue(readJsonString(x));
                break;
            case '\'':
                if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                    throw new RuntimeException("apostrophe");
                }
                // no break here
            case '"':
                jh.stringValue(readJsonString(x));
                break;
            case '{':
                readJsonObject(jh);
                break;
            case '[':
                readJsonArray(jh);
                break;
            default:
                throw new RuntimeException("unrecognized " + x);
        }
    }

    private void readJsonLiteral(@NotNull JsonHandler jh) {
        final ByteBuf data = content();
        final int startIndex = data.readerIndex() - 1;
        int endIndex = data.writerIndex();
        boolean isFractional = jh.testTypePreference(TypePreference.FLAG_INTEGRAL_FORCE_TO_FRACTIONAL);
        while (data.isReadable()) {
            int x = data.readByte();
            if ('0' <= x && x <= '9') {
                continue;
            }
            if ('a' <= x && x <= 'z' || 'A' <= x && x <= 'Z' || x == '+' || x == '-' || x == '.') {
                isFractional = true;
                continue;
            }
            endIndex = data.readerIndex() - 1;
            data.readerIndex(endIndex);
            break;
        }
        if (isFractional) {
            readJsonLiteral(jh, data.toString(startIndex, endIndex - startIndex, StandardCharsets.US_ASCII));
        } else {
            readJsonLiteral(jh, data, startIndex, endIndex);
        }
    }

    private void readJsonLiteral(@NotNull JsonHandler jh, @NotNull String string) {
        if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_FORCE_TO_RAW)) {
            double value = Double.parseDouble(string);
            if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_OVERFLOW_TO_RAW) || Double.isFinite(value)) {
                jh.numberValue(value);
                return;
            }
        }
        jh.numberValue(new FastNumber(string));
    }

    private void readJsonLiteral(@NotNull JsonHandler jh, @NotNull ByteBuf data, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (jh.testTypePreference(TypePreference.FLAG_INTEGRAL_FORCE_TO_RAW)) {
            jh.numberValue(new FastNumber(data.toString(startIndex, length, StandardCharsets.US_ASCII)));
            return;
        }
        if (length < 10) {
            boolean negative = false;
            int intValue = data.getByte(startIndex);
            if (intValue == '-') {
                negative = true;
                intValue = 0;
            } else if (intValue == '+') {
                intValue = 0;
            } else {
                intValue -= '0';
            }
            for (startIndex++; startIndex < endIndex; startIndex++) {
                intValue = intValue * 10 + (data.getByte(startIndex) - '0');
            }
            if (negative) {
                intValue = -intValue;
            }
            jh.numberValue(intValue);
        } else if (length < 19) {
            boolean negative = false;
            long longValue = data.getByte(startIndex);
            if (longValue == '-') {
                negative = true;
                longValue = 0L;
            } else if (longValue == '+') {
                longValue = 0L;
            } else {
                longValue -= '0';
            }
            for (startIndex++; startIndex < endIndex; startIndex++) {
                longValue = longValue * 10L + (data.getByte(startIndex) - '0');
            }
            if (negative) {
                longValue = -longValue;
            }
            if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                jh.numberValue((int) longValue);
            } else {
                jh.numberValue(longValue);
            }
        } else {
            String string = data.toString(startIndex, length, StandardCharsets.US_ASCII);
            BigInteger bigValue = new BigInteger(string, 10);
            if (BigConstants.MIN_LONG.compareTo(bigValue) <= 0 && bigValue.compareTo(BigConstants.MAX_LONG) <= 0) {
                long longValue = bigValue.longValue();
                if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                    jh.numberValue((int) longValue);
                } else {
                    jh.numberValue(longValue);
                }
            } else if (jh.testTypePreference(TypePreference.FLAG_INTEGRAL_OVERFLOW_TO_FRACTIONAL)) {
                double doubleValue = bigValue.doubleValue();
                if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_OVERFLOW_TO_RAW) || Double.isFinite(doubleValue)) {
                    jh.numberValue(doubleValue);
                } else {
                    jh.numberValue(new FastNumber(string));
                }
            } else if (jh.testTypePreference(TypePreference.FLAG_INTEGRAL_OVERFLOW_TO_RAW)) {
                jh.numberValue(new FastNumber(string));
            } else {
                jh.numberValue(bigValue);
            }
        }
    }

    @NotNull
    protected String readJsonString(@DataType("u8") int quoteChar) {
        final ByteBuf data = content();
        int startIndex = data.readerIndex();
        StringBuilder sb = null;
        while (true) {
            int value = data.readByte();
            if (value == quoteChar) {
                break;
            }
            if (value == '\\') {
                int endIndex = data.readerIndex() - 1;
                value = data.readByte();
                switch (value) {
                    case '\r':
                        value = data.readByte();
                        if (value != '\n') {
                            data.readerIndex(data.readerIndex() - 1);
                        }
                        // no break here
                        break;
                    case '\n':
                        value = -1;
                        break;
                    case '"':
                    case '\\':
                    case '/':
                        break;
                    case '\'':
                        if (value != quoteChar) {
                            throw new RuntimeException("escape apostrophe");
                        }
                        break;
                    case '`':
                        if (value != quoteChar) {
                            throw new RuntimeException("escape grave accent");
                        }
                        break;
                    case 'b':
                        value = '\b';
                        break;
                    case 'f':
                        value = '\f';
                        break;
                    case 'n':
                        value = '\n';
                        break;
                    case 'r':
                        value = '\r';
                        break;
                    case 't':
                        value = '\t';
                        break;
                    case 'u':
                        value = readHex16(data);
                        break;
                }
                if (sb == null) {
                    sb = new StringBuilder();
                }
                if (startIndex < endIndex) {
                    sb.append(data.toString(startIndex, endIndex - startIndex, StandardCharsets.UTF_8));
                }
                startIndex = data.readerIndex();
                if (value != -1) {
                    sb.append((char) value);
                }
            }
        }
        {
            int endIndex = data.readerIndex() - 1;
            if (sb == null) {
                return data.toString(startIndex, endIndex - startIndex, StandardCharsets.UTF_8);
            }
            if (startIndex < endIndex) {
                sb.append(data.toString(startIndex, endIndex - startIndex, StandardCharsets.UTF_8));
            }
        }
        return sb.toString();
    }

    private int readHex16(@NotNull ByteBuf data) {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = data.readByte();
            if ('0' <= digit && digit <= '9') {
                digit -= '0';
            } else if ('A' <= digit && digit <= 'Z') {
                digit -= Base16Case.UPPER_CONSTANT;
            } else if ('a' <= digit && digit <= 'z') {
                digit -= Base16Case.LOWER_CONSTANT;
            } else {
                throw new RuntimeException("hex digit " + digit);
            }
            value |= digit << shift;
        }
        return value;
    }

    private void readJsonObject(@NotNull JsonHandler jh) {
        jh.openObject();
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            skipGap();
            int x = content().readByte();
            String key;
            switch (x) {
                case '"':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    key = readJsonString(x);
                    break;
                case '\'':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                        throw new RuntimeException("apostrophe");
                    }
                    key = readJsonString(x);
                    break;
                case '`':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                        throw new RuntimeException("grave accent");
                    }
                    key = readJsonString(x);
                    break;
                case ',':
                    if (comma) {
                        throw new RuntimeException("duplicate comma");
                    }
                    if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                        throw new RuntimeException("leading comma");
                    }
                    comma = true;
                    skipGap();
                    continue LABEL;
                case '}':
                    if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                        throw new RuntimeException("trailing comma");
                    }
                    break LABEL;
                default:
                    throw new RuntimeException("unrecognized " + x);
            }
            skipGap();
            x = content().readByte();
            if (x != ':') {
                throw new RuntimeException("expect colon actual " + x);
            }
            jh.stringKey(key);
            skipGap();
            readJson(jh);
            first = false;
            comma = false;
        }
        jh.closeObject();
    }

    private void readJsonArray(@NotNull JsonHandler jh) {
        jh.openArray();
        boolean first = true;
        boolean comma = false;
        while (true) {
            skipGap();
            int x = content().readByte();
            if (x == ',') {
                if (comma) {
                    throw new RuntimeException("duplicate comma");
                }
                if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new RuntimeException("leading comma");
                }
                comma = true;
                continue;
            } else if (x == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new RuntimeException("trailing comma");
                }
                break;
            }
            if (comma || first) {
                ByteBuf data = content();
                data.readerIndex(data.readerIndex() - 1);
                readJson(jh);
                first = false;
                comma = false;
            } else {
                throw new RuntimeException("missing comma");
            }
        }
        jh.closeArray();
    }
}
