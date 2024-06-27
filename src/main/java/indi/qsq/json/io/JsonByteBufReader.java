package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.entity.RawNumber;
import indi.qsq.util.ds.Index;
import indi.qsq.util.reflect.ClassUtility;
import indi.qsq.util.text.HexCase;
import indi.qsq.util.text.Quote;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.function.Consumer;

/**
 * Created on 2022/8/12.
 */
public class JsonByteBufReader extends DefaultByteBufHolder implements RecursiveReader {

    private static final int NONE = -1;

    private int last;

    private JsonConsumer jc;

    private int config;

    private ByteBuf cache;

    private Consumer<RecursiveReader> callback;

    private int storedSurrogate, storedIndex;

    public JsonByteBufReader(ByteBuf data) {
        super(data);
    }

    @Override
    public void config(int config) {
        this.config = config;
    }

    @Override
    public void read(@NotNull JsonConsumer jc) {
        this.jc = jc;
        try {
            last = NONE;
            callback = null;
            skipGap();
            readValue();
        } finally {
            if (cache != null) {
                cache.release();
                cache = null;
            }
        }
    }
    @Override
    public void skip(@NotNull Consumer<RecursiveReader> callback) {
        this.callback = callback;
    }

    @Override
    @SuppressWarnings("DefaultAnnotationParam")
    public String raw(@Index(inclusive = true) int fromIndex, @Index(inclusive = false) int toIndex) {
        return content().toString(fromIndex, toIndex - fromIndex, StandardCharsets.UTF_8);
    }

    @Override
    public void setPosition(int pos) {
        content().readerIndex(pos);
        last = NONE;
    }

    @Override
    public int getPosition() {
        int pos = content().readerIndex();
        if (last != NONE) {
            pos--;
        }
        return pos;
    }

    private void meetValue() {
        final Consumer<RecursiveReader> callback = this.callback;
        if (callback != null) {
            this.callback = null;
            skipValue();
            callback.accept(this);
        } else {
            readValue();
        }
    }

    private void skipValue() {
        switch (last) {
            case '{':
                skipObject();
                break;
            case '[':
                skipArray();
                break;
            case '"':
                skipString(content(), false);
                break;
            case '\'':
                skipString(content(), true);
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
                skipNumber(content());
                break;
            case 't':
                skipAscii("true");
                break;
            case 'f':
                skipAscii("false");
                break;
            case 'n':
                skipAscii("null");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void readValue() {
        switch (last) {
            case '{':
                readObject();
                break;
            case '[':
                readArray();
                break;
            case '"':
                jc.stringValue(readString(false));
                break;
            case '\'':
                jc.stringValue(readString(true));
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
                readNumber();
                break;
            case 't':
                skipAscii("true");
                jc.booleanValue(true);
                break;
            case 'f':
                skipAscii("false");
                jc.booleanValue(false);
                break;
            case 'n':
                skipAscii("null");
                jc.nullValue();
                break;
            default:
                throw new IllegalArgumentException("pos = " + content().readerIndex());
        }
    }

    private void skipObject() {
        last = NONE;
        skipGap();
        boolean looped = false;
        while (true) {
            if (last == '"') {
                skipString(content(), false);
            } else if (last == '\'') {
                skipString(content(), true);
            } else if (last == '}') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            } else {
                throw new IllegalArgumentException();
            }
            skipGap();
            if (last != ':') {
                throw new IllegalArgumentException();
            }
            last = NONE;
            skipGap();
            skipValue();
            skipGap();
            if (last == ',') {
                last = NONE;
                skipGap();
                looped = true;
            } else if (last == '}') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        last = NONE; // fixed at 2022/5/24
    }

    private void readObject() {
        jc.openObject();
        last = NONE;
        skipGap();
        boolean looped = false;
        while (true) {
            String key;
            if (last == '"') {
                key = readString(false);
            } else if (last == '\'') {
                key = readString(true);
            } else if (last == '}') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            } else {
                throw new IllegalArgumentException();
            }
            skipGap();
            if (last != ':') {
                throw new IllegalArgumentException();
            }
            last = NONE;
            skipGap();
            jc.key(key);
            meetValue();
            skipGap();
            if (last == ',') {
                last = NONE;
                skipGap();
                looped = true;
            } else if (last == '}') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        last = NONE;
        jc.closeObject();
    }

    private void skipArray() {
        last = NONE;
        skipGap();
        boolean looped = false;
        while (true) {
            if (last == ']') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            }
            skipValue();
            skipGap();
            if (last == ',') {
                last = NONE;
                skipGap();
                looped = true;
            } else if (last == ']') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        last = NONE;
    }

    private void readArray() {
        jc.openArray();
        last = NONE;
        skipGap();
        boolean looped = false;
        while (true) {
            if (last == ']') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                break;
            }
            meetValue();
            skipGap();
            if (last == ',') {
                last = NONE;
                skipGap();
                looped = true;
            } else if (last == ']') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        last = NONE;
        jc.closeArray();
    }

    private int skipString(ByteBuf data, boolean singleQuote) {
        int quote;
        if (singleQuote) {
            if ((config & FLAG_SINGLE_QUOTE_STRING) == 0) {
                throw new IllegalArgumentException();
            }
            quote = '\'';
        } else {
            quote = '"';
        }
        if (last != quote) {
            throw new IllegalArgumentException();
        }
        int escapeCount = 0;
        while (true) {
            int x = data.readByte();
            if (x == quote) {
                break;
            } else if (x == '\\') {
                escapeCount++;
                data.readByte();
            }
        }
        last = NONE;
        return escapeCount;
    }

    private String readString(boolean singleQuote) {
        final ByteBuf data = content();
        int start = data.readerIndex();
        if (skipString(data, singleQuote) == 0) {
            return data.toString(start, data.readerIndex() - 1 - start, StandardCharsets.UTF_8);
        } else {
            return readEscapedString(data, start, singleQuote);
        }
    }

    private String readEscapedString(ByteBuf data, int index, boolean singleQuote) {
        final ByteBuf cache = prepareCache();
        while (true) {
            int current = data.getByte(index++);
            switch (current) {
                case '"':
                    if (singleQuote) {
                        cache.writeByte('"');
                        break;
                    } else {
                        return readCache(cache);
                    }
                case '\'':
                    if (singleQuote) {
                        return readCache(cache);
                    } else {
                        cache.writeByte('\'');
                        break;
                    }
                case '\\':
                    current = data.getByte(index++);
                    switch (current) {
                        case '"':
                        case '\'':
                        case '\\':
                        case '/':
                            cache.writeByte(current);
                            break;
                        case 'b':
                            cache.writeByte('\b');
                            break;
                        case 'f':
                            cache.writeByte('\f');
                            break;
                        case 'n':
                            cache.writeByte('\n');
                            break;
                        case 'r':
                            cache.writeByte('\r');
                            break;
                        case 't':
                            cache.writeByte('\t');
                            break;
                        case 'u':
                            readUnicodeEscape(data, cache, index);
                            index += 4;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                default:
                    cache.writeByte(current);
                    break;
            }
        }
    }

    private ByteBuf prepareCache() {
        storedIndex = Integer.MAX_VALUE;
        ByteBuf cache = this.cache;
        if (cache != null) {
            cache.clear();
        } else {
            cache = Unpooled.buffer();
            this.cache = cache;
        }
        return cache;
    }

    private String readCache(ByteBuf cache) {
        if (storedIndex != Integer.MAX_VALUE) {
            System.out.println("JsonByteBufReader.readCache");
        }
        return cache.toString(StandardCharsets.UTF_8);
    }

    private void readUnicodeEscape(ByteBuf data, ByteBuf cache, int index) {
        final int ch = readHex16(data, index);
        if (Character.MIN_SURROGATE <= ch && ch <= Character.MAX_SURROGATE) {
            if (ch < Character.MIN_LOW_SURROGATE) { // ch is high surrogate
                storedSurrogate = ch;
                storedIndex = index;
            } else if (storedIndex + 6 == index) {
                writeCodePoint(cache, ((storedSurrogate << 10) + ch) + (Character.MIN_SUPPLEMENTARY_CODE_POINT
                        - (Character.MIN_HIGH_SURROGATE << 10) - Character.MIN_LOW_SURROGATE));
                storedIndex = Integer.MAX_VALUE;
            } else {
                System.out.println("JsonByteBufReader.readUnicodeEscape");
            }
        } else {
            writeCodePoint(cache, ch);
        }
    }

    private int readHex16(ByteBuf data, int base) {
        int ch = 0;
        for (int offset = 0; offset < 4; offset++) {
            ch <<= 4;
            int current = data.getByte(base + offset);
            if ('0' <= current && current <= '9') {
                ch |= (current - '0');
            } else if ('a' <= current && current <= 'f') { // lower cases used more frequently
                ch |= (current - HexCase.LOWER);
            } else if ('A' <= current && current <= 'F') {
                ch |= (current - HexCase.UPPER);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return ch;
    }

    private void writeCodePoint(ByteBuf cache, int cp) {
        if (cp <= 0x007f && cp != 0) {
            cache.writeByte(cp);
        } else if (cp <= 0x07ff) {
            cache.writeByte(0xc0 | ((cp >> 6) & 0x1f));
            cache.writeByte(0xc0 | (cp & 0x3f));
        } else if (cp <= 0xffff) {
            cache.writeByte(0xe0 | ((cp >> 12) & 0x0f));
            cache.writeByte(0x80 | ((cp >> 6) & 0x3f));
            cache.writeByte(0x80 | (cp & 0x3f));
        } else {
            cache.writeByte(0xf0 | ((cp >> 18) & 0x07));
            cache.writeByte(0x80 | ((cp >> 12) & 0x3f));
            cache.writeByte(0x80 | ((cp >> 6) & 0x3f));
            cache.writeByte(0x80 | (cp & 0x3f));
        }
    }

    private boolean skipNumber(ByteBuf data) {
        boolean decimal = false;
        while (true) {
            int last = data.readByte();
            switch (last) {
                case '.':
                case 'e':
                case 'E':
                    decimal = true;
                    break;
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                case '-':
                    break;
                default:
                    this.last = last;
                    return decimal;
            }
        }
    }

    private void readNumber() {
        final ByteBuf data = content();
        final int start = data.readerIndex();
        final boolean decimal = skipNumber(data);
        final String string = data.toString(start - 1, data.readerIndex() - start, StandardCharsets.UTF_8);
        if (decimal) {
            if ((config & FLAG_RAW_DECIMAL) == 0) {
                jc.numberValue(Double.parseDouble(string));
            } else {
                jc.numberValue(new RawNumber(string));
            }
        } else {
            jc.numberValue(Long.parseLong(string));
        }
    }

    private void skipGap() {
        final ByteBuf data = content();
        if (last == NONE) {
            last = 0xff & data.readByte();
        }
        if ((config & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            while (last <= 0x20) {
                last = 0xff & data.readByte();
            }
        } else {
            skipGapOrComment(data);
        }
    }

    private void skipGapOrComment(ByteBuf data) {
        while (true) {
            if (last <= 0x20) {
                last = data.readByte();
                continue;
            }
            if (last == '/') {
                int current = data.readByte();
                if (current == '/') {
                    if ((config & FLAG_LINE_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    do {
                        current = data.readByte();
                    } while (current != '\n');
                } else if (current == '*') {
                    if ((config & FLAG_BLOCK_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    LOOP:
                    while (true) {
                        current = data.readByte();
                        while (current == '*') {
                            current = data.readByte();
                            if (current == '/') {
                                break LOOP;
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
                last = data.readByte();
                continue;
            }
            break;
        }
    }

    private void skipAscii(String string) {
        final ByteBuf data = content();
        final int length = string.length();
        for (int i = 0; i < length; i++) {
            if (last == NONE) {
                last = 0xff & data.readByte();
            }
            if (last != string.charAt(i)) {
                throw new InputMismatchException();
            }
            last = NONE;
        }
    }

    @Override
    public void stringifyNeighbors(StringBuilder sb, int before, int after) {
        final ByteBuf data = content();
        before = Math.max(0, data.readerIndex() - before);
        after = Math.min(data.readerIndex() + after, data.writerIndex());
        Quote.DEFAULT.append(sb, data.toString(before, after - before, StandardCharsets.UTF_8));
    }

    public void stringify(StringBuilder sb) {
        sb.append("[config = ");
        READER_FLAG.stringify(sb, config, " | ");
        sb.append(", last = ");
        if (last == NONE) {
            sb.append("none");
        } else {
            Quote.ONE.append(sb, (char) last);
        }
        if (callback != null) {
            sb.append(", callback = ").append(callback);
        }
        sb.append(", storedSurrogate = ").append(storedSurrogate);
        sb.append(", storedIndex = ").append(storedIndex);
        sb.append(']');
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ClassUtility.normal(this)); // for possible further extensions
        stringify(sb);
        return sb.toString();
    }
}
