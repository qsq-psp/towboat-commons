package mujica.json.io;

import mujica.algebra.discrete.BigConstants;
import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.ds.of_int.list.PublicIntList;
import mujica.io.codec.Base16Case;
import mujica.io.codec.UTF8PushPullDecoder;
import mujica.io.stream.OneBufferDataInputStream;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.ArrayList;

@CodeHistory(date = "2021/1/8", project = "webbiton", name = "JsonInputStreamReader")
@CodeHistory(date = "2022/8/13", project = "Ultramarine", name = "JsonInputStreamReader")
@CodeHistory(date = "2026/2/23")
public class JsonObjectInputStream extends OneBufferDataInputStream implements JsonSyncReader, ObjectInput {

    @NotNull
    private final StringBuilder sb = new StringBuilder();

    @NotNull
    private final UTF8PushPullDecoder decoder = new UTF8PushPullDecoder();

    private int flags;

    public JsonObjectInputStream(@NotNull InputStream in) {
        super(in);
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    public void skipJson() throws IOException {
        final int octet = super.readUnsignedByte();
        switch (octet) {
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            case '+': case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                skipJsonLiteral();
                break;
            case '"':
                skipJsonString(octet);
                break;
            case '\'':
                if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                    throw new IOException("apostrophe");
                }
                skipJsonString(octet);
                break;
            case '`':
                if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                    throw new IOException("grave accent");
                }
                skipJsonString(octet);
                break;
            case '{':
                skipJsonObject();
                break;
            case '[':
                skipJsonArray();
                break;
            default:
                throw new IOException("unrecognized " + octet);
        }
    }

    private void skipJsonLiteral() throws IOException {
        while (true) {
            int octet = read();
            if (!('0' <= octet && octet <= '9' || 'a' <= octet && octet <= 'z' || 'A' <= octet && octet <= 'Z'
                    || octet == '+' || octet == '-' || octet == '.')) {
                if (octet != -1) {
                    unread(octet);
                }
                break;
            }
        }
    }

    private void skipJsonString(int quoteChar) throws IOException {
        while (true) {
            int octet = super.readUnsignedByte();
            if (octet == '\\') {
                super.readUnsignedByte();
            } else if (octet == quoteChar) {
                break;
            }
        }
    }

    private void skipJsonObject() throws IOException {
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            switch (octet) {
                case '"':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    skipJsonString(octet);
                    break;
                case '\'':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                        throw new IOException("apostrophe");
                    }
                    skipJsonString(octet);
                    break;
                case '`':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                        throw new IOException("grave accent");
                    }
                    skipJsonString(octet);
                    break;
                case ',':
                    if (comma) {
                        throw new IOException("duplicate comma");
                    }
                    if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                        throw new IOException("leading comma");
                    }
                    comma = true;
                    skipGap();
                    continue LABEL;
                case '}':
                    if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                        throw new IOException("trailing comma");
                    }
                    break LABEL;
                default:
                    throw new IOException("unrecognized " + octet);
            }
            skipGap();
            octet = super.readUnsignedByte();
            if (octet != ':') {
                throw new IOException("expect colon actual " + octet);
            }
            skipGap();
            skipJson();
            first = false;
            comma = false;
        }
    }

    private void skipJsonArray() throws IOException {
        boolean first = true;
        boolean comma = false;
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            if (octet == ',') {
                if (comma) {
                    throw new IOException("duplicate comma");
                }
                if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new IOException("leading comma");
                }
                comma = true;
                continue;
            } else if (octet == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new IOException("trailing comma");
                }
                break;
            }
            if (comma || first) {
                unread(octet);
                skipJson();
                first = false;
                comma = false;
            } else {
                throw new IOException("missing comma");
            }
        }
    }

    private void skipGap() throws IOException {
        int octet;
        if ((flags & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            do {
                octet = super.readUnsignedByte();
            } while (octet <= 0x20);
        } else {
            while (true) {
                octet = super.readUnsignedByte();
                if (octet != '/') {
                    if (octet > 0x20) {
                        break;
                    }
                    continue;
                }
                octet = super.readUnsignedByte();
                if (octet == '/') {
                    if ((flags & FLAG_LINE_COMMENT) == 0) {
                        throw new IOException("line comment");
                    }
                    do {
                        octet = super.readUnsignedByte();
                    } while (octet != '\n');
                    unread(octet);
                } else if (octet == '*') {
                    if ((flags & FLAG_BLOCK_COMMENT) == 0) {
                        throw new IOException("block comment");
                    }
                    while (true) {
                        octet = super.readUnsignedByte();
                        if (octet == '*') {
                            octet = super.readUnsignedByte();
                            if (octet == '/') {
                                break;
                            } else {
                                unread(octet);
                            }
                        }
                    }
                } else {
                    throw new IOException("comment unrecognized " + octet);
                }
            }
        }
        unread(octet);
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        try {
            skipGap();
            readJson(jh);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void readJson(@NotNull JsonHandler jh) throws IOException {
        if ((flags & (FLAG_SKIP_VALUE | FLAG_SKIP_TO_BYTE_BUF)) != 0) {
            if ((flags & FLAG_SKIP_TO_BYTE_BUF) != 0) {
                teeToByteBuf(byteBuf -> {
                    skipJson();
                    jh.skipped(byteBuf.retain());
                });
            } else {
                skipJson();
                jh.skipped();
            }
            return;
        }
        final int octet = super.readUnsignedByte();
        switch (octet) {
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
                unread(octet);
                readJsonWord(jh);
                break;
            case '+':
                if ((flags & FLAG_PLUS_SIGN_NUMBER) == 0) {
                    throw new IOException("plus sign");
                }
                // no break here
            case '-':
                unread(octet);
                readJsonLiteral(jh);
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                unread(octet);
                readJsonNumber(jh);
                break;
            case '`':
                if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                    throw new IOException("grave accent");
                }
                jh.stringValue(readJsonString(octet));
                break;
            case '\'':
                if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                    throw new IOException("apostrophe");
                }
                // no break here
            case '"':
                jh.stringValue(readJsonString(octet));
                break;
            case '{':
                readJsonObject(jh);
                break;
            case '[':
                readJsonArray(jh);
                break;
            default:
                throw new IOException("unrecognized " + octet);
        }
    }

    private void readJsonWord(@NotNull JsonHandler jh) throws IOException {
        sb.delete(0, sb.length());
        while (true) {
            int octet = read();
            if ('a' <= octet && octet <= 'z' || 'A' <= octet && octet <= 'Z') {
                sb.append((char) octet);
            } else {
                if (octet != -1) {
                    unread(octet);
                }
                break;
            }
        }
        final String string = sb.toString();
        switch (string) {
            case "null":
                jh.nullValue();
                break;
            case "true":
                jh.booleanValue(true);
                break;
            case "false":
                jh.booleanValue(false);
                break;
            case "Infinity":
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new IOException("infinity");
                }
                if ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                    jh.numberValue(Double.POSITIVE_INFINITY);
                } else {
                    jh.numberValue(new FastNumber(string));
                }
            case "NaN":
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new IOException("not a number");
                }
                if ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                    jh.numberValue(Double.NaN);
                } else {
                    jh.numberValue(new FastNumber(string));
                }
                break;
            default:
                throw new IOException("unrecognized " + string);
        }
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private void readJsonLiteral(@NotNull JsonHandler jh) throws IOException {
        sb.delete(0, sb.length());
        boolean isFractional = (flags & FLAG_INTEGRAL_FORCE_TO_FRACTIONAL) != 0;
        while (true) {
            int octet = read();
            if ('0' <= octet && octet <= '9' || octet == '+' || octet == '-') {
                sb.append((char) octet);
            } else if ('a' <= octet && octet <= 'z' || 'A' <= octet && octet <= 'Z' || octet == '.') {
                sb.append((char) octet);
                isFractional = true;
            } else {
                if (octet != -1) {
                    unread(octet);
                }
                break;
            }
        }
        if (isFractional) {
            String string = sb.toString();
            LABEL:
            while ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                double value = Double.parseDouble(string);
                while (true) {
                    if (Double.isFinite(value)) {
                        break;
                    } else if (string.equals("+Infinity")) {
                        if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                            throw new IOException("positive infinity");
                        }
                        break;
                    } else if (string.equals("-Infinity")) {
                        if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                            throw new IOException("negative infinity");
                        }
                        break;
                    } else if ((flags & FLAG_FRACTIONAL_OVERFLOW_TO_RAW) == 0) {
                        break;
                    }
                    break LABEL;
                }
                jh.numberValue(value);
                return;
            }
            jh.numberValue(new FastNumber(string));
        } else {
            parseJsonInteger(jh);
        }
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private void readJsonNumber(@NotNull JsonHandler jh) throws IOException {
        sb.delete(0, sb.length());
        boolean isFractional = (flags & FLAG_INTEGRAL_FORCE_TO_FRACTIONAL) != 0;
        while (true) {
            int octet = read();
            if ('0' <= octet && octet <= '9' || octet == '+' || octet == '-') {
                sb.append((char) octet);
            } else if (octet == 'E' || octet == 'e' || octet == '.') {
                sb.append((char) octet);
                isFractional = true;
            } else {
                if (octet != -1) {
                    unread(octet);
                }
                break;
            }
        }
        if (isFractional) {
            String string = sb.toString();
            while ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                double value = Double.parseDouble(string);
                if ((flags & FLAG_FRACTIONAL_OVERFLOW_TO_RAW) == 0 || Double.isFinite(value)) {
                    jh.numberValue(value);
                    return;
                }
                break;
            }
            jh.numberValue(new FastNumber(string));
        } else {
            parseJsonInteger(jh);
        }
    }

    private void parseJsonInteger(@NotNull JsonHandler jh) {
        if ((flags & FLAG_INTEGRAL_FORCE_TO_RAW) != 0) {
            jh.numberValue(new FastNumber(sb.toString()));
            return;
        }
        final int length = sb.length();
        if (length < 10) {
            jh.numberValue(Integer.parseInt(sb, 0, length, 10));
        } else if (length < 19) {
            long longValue = Long.parseLong(sb, 0, length, 10);
            if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                jh.numberValue((int) longValue);
            } else {
                jh.numberValue(longValue);
            }
        } else {
            BigInteger bigValue = new BigInteger(sb.toString(), 10);
            if (BigConstants.MIN_LONG.compareTo(bigValue) <= 0 && bigValue.compareTo(BigConstants.MAX_LONG) <= 0) {
                long longValue = bigValue.longValue();
                if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                    jh.numberValue((int) longValue);
                } else {
                    jh.numberValue(longValue);
                }
            } else if ((flags & FLAG_INTEGRAL_OVERFLOW_TO_FRACTIONAL) != 0) {
                double doubleValue = bigValue.doubleValue();
                if ((flags & FLAG_FRACTIONAL_OVERFLOW_TO_RAW) == 0 || Double.isFinite(doubleValue)) {
                    jh.numberValue(doubleValue);
                } else {
                    jh.numberValue(new FastNumber(sb.toString()));
                }
            } else if ((flags & FLAG_INTEGRAL_OVERFLOW_TO_RAW) != 0) {
                jh.numberValue(new FastNumber(sb.toString()));
            } else {
                jh.numberValue(bigValue);
            }
        }
    }

    @NotNull
    private String readJsonString(@DataType("u8") int quoteChar) throws IOException {
        sb.delete(0, sb.length());
        decoder.reset();
        while (true) {
            int octet = super.readUnsignedByte();
            if (octet == '\\') {
                decoder.finishPush(sb);
                octet = super.readUnsignedByte();
                switch (octet) {
                    case '\r':
                        octet = super.readUnsignedByte();
                        if (octet != '\n') {
                            unread(octet);
                        }
                        break;
                    case '\n':
                        break;
                    case '"':
                    case '\\':
                    case '/':
                        sb.append((char) octet);
                        break;
                    case '\'':
                        if (octet == quoteChar) {
                            sb.append('\'');
                        } else {
                            throw new IOException("escape apostrophe");
                        }
                        break;
                    case '`':
                        if (octet == quoteChar) {
                            sb.append('`');
                        } else {
                            throw new IOException("escape grave accent");
                        }
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'u':
                        sb.append((char) readHex16());
                        break;
                    default:
                        throw new IOException("escape " + octet);
                }
                continue;
            }
            if (octet == quoteChar) {
                decoder.finishPush(sb);
                break;
            }
            decoder.push((byte) octet, sb);
        }
        return sb.toString();
    }

    private int readHex16() throws IOException {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = super.readUnsignedByte();
            if ('0' <= digit && digit <= '9') {
                digit -= '0';
            } else if ('A' <= digit && digit <= 'Z') {
                digit -= Base16Case.UPPER_CONSTANT;
            } else if ('a' <= digit && digit <= 'z') {
                digit -= Base16Case.LOWER_CONSTANT;
            } else {
                throw new IOException("hex digit " + digit);
            }
            value |= digit << shift;
        }
        return value;
    }

    private void readJsonObject(@NotNull JsonHandler jh) throws IOException {
        jh.openObject();
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            String key;
            switch (octet) {
                case '"':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    key = readJsonString(octet);
                    break;
                case '\'':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                        throw new IOException("apostrophe");
                    }
                    key = readJsonString(octet);
                    break;
                case '`':
                    if (!first && !comma) {
                        throw new IOException("missing comma");
                    }
                    if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                        throw new IOException("grave accent");
                    }
                    key = readJsonString(octet);
                    break;
                case ',':
                    if (comma) {
                        throw new IOException("duplicate comma");
                    }
                    if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                        throw new IOException("leading comma");
                    }
                    comma = true;
                    skipGap();
                    continue LABEL;
                case '}':
                    if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                        throw new IOException("trailing comma");
                    }
                    break LABEL;
                default:
                    throw new IOException("unrecognized " + octet);
            }
            skipGap();
            octet = super.readUnsignedByte();
            if (octet != ':') {
                throw new IOException("expect colon actual " + octet);
            }
            jh.stringKey(key);
            skipGap();
            readJson(jh);
            first = false;
            comma = false;
        }
        jh.closeObject();
    }

    private void readJsonArray(@NotNull JsonHandler jh) throws IOException {
        jh.openArray();
        boolean first = true;
        boolean comma = false;
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            if (octet == ',') {
                if (comma) {
                    throw new IOException("duplicate comma");
                }
                if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new IOException("leading comma");
                }
                comma = true;
                continue;
            } else if (octet == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new IOException("trailing comma");
                }
                break;
            }
            if (comma || first) {
                unread(octet);
                readJson(jh);
                first = false;
                comma = false;
            } else {
                throw new IOException("missing comma");
            }
        }
        jh.closeArray();
    }

    @Override
    public boolean readBoolean() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        while (true) {
            int octet = super.readUnsignedByte();
            if ('a' <= octet && octet <= 'z') {
                sb.append((char) octet);
            } else {
                break;
            }
        }
        return Boolean.parseBoolean(sb.toString());
    }

    @Override
    public byte readByte() throws IOException {
        final int value = readInt();
        if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
            return (byte) value;
        } else {
            throw new IOException();
        }
    }

    @Override
    public int readUnsignedByte() throws IOException {
        final int value = readInt();
        if (0 <= value && value < (1 << Byte.SIZE)) {
            return value;
        } else {
            throw new IOException();
        }
    }

    @Override
    public short readShort() throws IOException {
        final int value = readInt();
        if (Short.MIN_VALUE <= value && value <= Short.MAX_VALUE) {
            return (short) value;
        } else {
            throw new IOException();
        }
    }

    @Override
    public int readUnsignedShort() throws IOException {
        final int value = readInt();
        if (0 <= value && value < (1 << Short.SIZE)) {
            return value;
        } else {
            throw new IOException();
        }
    }

    @Override
    public char readChar() throws IOException {
        final int value = readInt();
        if (Character.MIN_VALUE <= value && value <= Character.MAX_VALUE) {
            return (char) value;
        } else {
            throw new IOException();
        }
    }

    @Override
    public int readInt() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        int octet = super.readUnsignedByte();
        if (octet == '-') {
            sb.append('-');
            octet = super.readUnsignedByte();
        } else if (octet == '+') {
            if ((flags & FLAG_PLUS_SIGN_NUMBER) == 0) {
                throw new IOException("plus sign");
            }
            sb.append('+');
            octet = super.readUnsignedByte();
        }
        while ('0' <= octet && octet <= '9') {
            sb.append((char) octet);
            octet = super.readUnsignedByte();
        }
        unread(octet);
        return Integer.parseInt(sb, 0, sb.length(), 10);
    }

    @NotNull
    public int[] readIntArray1D() throws IOException {
        skipGap();
        if (super.readUnsignedByte() != '[') {
            throw new IOException("open array");
        }
        CopyOnResizeIntList intList = null;
        boolean comma = false;
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            if (octet == ',') {
                if (comma) {
                    throw new IOException("duplicate comma");
                }
                if (intList == null && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new IOException("leading comma");
                }
                comma = true;
                continue;
            } else if (octet == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new IOException("trailing comma");
                }
                break;
            }
            if (comma || intList == null) {
                unread(octet);
                if (intList == null) {
                    intList = new CopyOnResizeIntList(null);
                }
                intList.offerLast(readInt());
                comma = false;
            } else {
                throw new IOException("missing comma");
            }
        }
        if (intList == null) {
            return PublicIntList.EMPTY.array;
        } else {
            return intList.toIntArray();
        }
    }

    @NotNull
    public int[][] readIntArray2D() throws IOException {
        skipGap();
        if (super.readUnsignedByte() != '[') {
            throw new IOException("open array");
        }
        ArrayList<int[]> list = null;
        boolean comma = false;
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            if (octet == ',') {
                if (comma) {
                    throw new IOException("duplicate comma");
                }
                if (list == null && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new IOException("leading comma");
                }
                comma = true;
                continue;
            } else if (octet == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new IOException("trailing comma");
                }
                break;
            }
            if (comma || list == null) {
                unread(octet);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(readIntArray1D());
                comma = false;
            } else {
                throw new IOException("missing comma");
            }
        }
        if (list == null) {
            return new int[0][0];
        } else {
            return list.toArray(new int[0][0]);
        }
    }

    @Override
    public long readUnsignedInt() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        while (true) {
            int octet = super.readUnsignedByte();
            if ('0' <= octet && octet <= '9') {
                sb.append((char) octet);
            } else {
                unread(octet);
                break;
            }
        }
        return 0xffffffffL & Integer.parseUnsignedInt(sb, 0, sb.length(), 10);
    }

    @Override
    public long readLong() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        int octet = super.readUnsignedByte();
        if (octet == '-') {
            sb.append('-');
            octet = super.readUnsignedByte();
        } else if (octet == '+') {
            if ((flags & FLAG_PLUS_SIGN_NUMBER) == 0) {
                throw new IOException("plus sign");
            }
            sb.append('+');
            octet = super.readUnsignedByte();
        }
        while ('0' <= octet && octet <= '9') {
            sb.append((char) octet);
            octet = super.readUnsignedByte();
        }
        unread(octet);
        return Long.parseLong(sb, 0, sb.length(), 10);
    }

    @Override
    public float readFloat() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        while (true) {
            char ch = (char) super.readUnsignedByte();
            if ("0123456789.+-Ee".indexOf(ch) != -1) {
                sb.append(ch);
            } else {
                unread(ch);
                break;
            }
        }
        return Float.parseFloat(sb.toString());
    }

    @Override
    public double readDouble() throws IOException {
        skipGap();
        sb.delete(0, sb.length());
        while (true) {
            char ch = (char) super.readUnsignedByte();
            if ("0123456789.+-Ee".indexOf(ch) != -1) {
                sb.append(ch);
            } else {
                unread(ch);
                break;
            }
        }
        return Double.parseDouble(sb.toString());
    }

    @NotNull
    public String readString() throws IOException {
        skipGap();
        final int octet = super.readUnsignedByte();
        if (octet == '"') {
            return readJsonString('"');
        }
        if (octet == '\'') {
            if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                throw new IOException("apostrophe");
            }
            return readJsonString('\'');
        }
        if (octet == '`') {
            if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                throw new IOException("grave accent");
            }
            return readJsonString('`');
        }
        throw new IOException("open string");
    }

    @NotNull
    public String[] readStringArray() throws IOException {
        skipGap();
        if (super.readUnsignedByte() != '[') {
            throw new IOException("open array");
        }
        ArrayList<String> list = null;
        boolean comma = false;
        while (true) {
            skipGap();
            int octet = super.readUnsignedByte();
            if (octet == ',') {
                if (comma) {
                    throw new IOException("duplicate comma");
                }
                if (list == null && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new IOException("leading comma");
                }
                comma = true;
                continue;
            } else if (octet == ']') {
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new IOException("trailing comma");
                }
                break;
            }
            if (comma || list == null) {
                unread(octet);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(readString());
                comma = false;
            } else {
                throw new IOException("missing comma");
            }
        }
        if (list == null) {
            return new String[0];
        } else {
            return list.toArray(new String[0]);
        }
    }

    @Override
    public Object readObject() throws IOException {
        return null;
    }
}
