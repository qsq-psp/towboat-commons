package mujica.json.io;

import io.netty.buffer.Unpooled;
import mujica.io.codec.Base16Case;
import mujica.io.stream.OIO;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/3/25")
public class JsonByteBufferReader implements JsonSyncReader {

    @NotNull
    private final ByteBuffer byteBuffer;

    @NotNull
    private CharBuffer charBuffer;

    @NotNull
    private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    private int flags;

    public JsonByteBufferReader(@NotNull ByteBuffer byteBuffer) {
        super();
        this.byteBuffer = byteBuffer;
        this.charBuffer = CharBuffer.allocate(Math.min(OIO.BUFFER_SIZE, byteBuffer.remaining()));
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    private void skipJson() {
        //
    }

    private void skipWord(@NotNull String word) {
        final int n = word.length();
        for (int i = 1; i < n; i++) { // char at 0 is already read
            if (byteBuffer.get() != word.charAt(i)) {
                throw new RuntimeException("word " + word);
            }
        }
    }

    private void skipLiteral() {
        while (byteBuffer.hasRemaining()) {
            int x = byteBuffer.get();
            if ('0' <= x && x <= '9' || 'a' <= x && x <= 'z' || 'A' <= x && x <= 'Z' || x == '+' || x == '-' || x == '.') {
                continue;
            }
            byteBuffer.position(byteBuffer.position() - 1);
            break;
        }
    }

    private void skipJsonString(@DataType("u8") int quoteChar) {
        while (true) {
            int x = byteBuffer.get();
            if (x == quoteChar) {
                break;
            } else if (x == '\\') {
                byteBuffer.get();
            }
        }
    }

    private void skipJsonObject() {
        //
    }

    private void skipJsonArray() {
        //
    }

    private void skipGap() {
        int x;
        if ((flags & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            do {
                x = byteBuffer.get();
            } while (isWhiteSpace(x));
        } else {
            while (true) {
                x = byteBuffer.get();
                if (x != '/') {
                    if (isWhiteSpace(x)) {
                        continue;
                    } else {
                        break;
                    }
                }
                x = byteBuffer.get();
                if (x == '/') {
                    if ((flags & FLAG_LINE_COMMENT) == 0) {
                        throw new RuntimeException("line comment");
                    }
                    do {
                        x = byteBuffer.get();
                    } while (x != '\n');
                    byteBuffer.position(byteBuffer.position() - 1);
                } else if (x == '*') {
                    if ((flags & FLAG_BLOCK_COMMENT) == 0) {
                        throw new RuntimeException("block comment");
                    }
                    while (true) {
                        x = byteBuffer.get();
                        if (x == '*') {
                            x = byteBuffer.get();
                            if (x == '/') {
                                break;
                            } else {
                                byteBuffer.position(byteBuffer.position() - 1);
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("comment unrecognized " + x);
                }
            }
        }
        byteBuffer.position(byteBuffer.position() - 1);
    }

    private static boolean isWhiteSpace(int x) {
        return x == ' ' || x == '\n' || x == '\r' || x == '\t';
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        skipGap();
        readJson(jh);
    }

    private void readJson(@NotNull JsonHandler jh) {
        // gap already skipped now
        if ((flags & (FLAG_SKIP_VALUE | FLAG_SKIP_TO_BYTE_BUF)) != 0) {
            if ((flags & FLAG_SKIP_TO_BYTE_BUF) != 0) {
                int start = byteBuffer.position();
                skipJson();
                int position = byteBuffer.position();
                int limit = byteBuffer.limit();
                ByteBuffer slice = byteBuffer.position(start).limit(position).slice();
                byteBuffer.limit(limit).position(position);
                jh.skippedValue(Unpooled.wrappedBuffer(slice));
            } else {
                skipJson();
                jh.skippedValue();
            }
            return;
        }
        final int x = byteBuffer.get();
        switch (x) {
            case 'I':
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new RuntimeException("infinity");
                }
                skipWord("Infinity");
                if ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                    jh.numberValue(Double.POSITIVE_INFINITY);
                } else {
                    jh.numberValue(new FastNumber("Infinity"));
                }
                break;
            case 'N':
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new RuntimeException("not a number");
                }
                skipWord("NaN");
                if ((flags & FLAG_FRACTIONAL_FORCE_TO_RAW) == 0) {
                    jh.numberValue(Double.NaN);
                } else {
                    jh.numberValue(new FastNumber("NaN"));
                }
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
                // todo
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

    @NotNull
    protected String readJsonString(@DataType("u8") int quoteChar) {
        int start = byteBuffer.position();
        skipJsonString(quoteChar);
        {
            int length = byteBuffer.position() - start;
            if (charBuffer.capacity() < length) {
                charBuffer = CharBuffer.allocate(length);
            } else {
                charBuffer.clear();
            }
        }
        byteBuffer.position(start);
        while (true) {
            int value = byteBuffer.get();
            if (value == quoteChar) {
                break;
            }
            if (value == '\\') {
                int end = byteBuffer.position();
                value = byteBuffer.get();
                switch (value) {
                    case '\r':
                        value = byteBuffer.get();
                        if (value != '\n') {
                            byteBuffer.position(byteBuffer.position() - 1);
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
                        value = readHex16();
                        break;
                }
                decode(start, end);
                start = byteBuffer.position();
                if (value != -1) {
                    charBuffer.put((char) value);
                }
            }
        }
        decode(start, byteBuffer.position() - 1);
        return charBuffer.flip().toString();
    }

    private int readHex16() {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = byteBuffer.get();
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

    private void decode(int start, int end) {
        if (start >= end) {
            return;
        }
        final int position = byteBuffer.position();
        final int limit = byteBuffer.limit();
        try {
            byteBuffer.position(start).limit(end);
            CoderResult coderResult = decoder.decode(byteBuffer, charBuffer, true);
            assert !coderResult.isOverflow();
        } finally {
            byteBuffer.limit(limit).position(position);
        }
    }

    private void readJsonObject(@NotNull JsonHandler jh) {
        jh.openObject();
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            skipGap();
            int x = byteBuffer.get();
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
            x = byteBuffer.get();
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
            int x = byteBuffer.get();
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
                byteBuffer.position(byteBuffer.position() - 1);
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
