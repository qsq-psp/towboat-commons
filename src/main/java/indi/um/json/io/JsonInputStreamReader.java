package indi.um.json.io;

import indi.um.json.api.JsonConsumer;
import indi.um.json.entity.RawNumber;
import indi.um.util.ds.HeapIntBuf;
import indi.um.util.ds.IntMatrix;
import indi.um.util.text.HexCase;
import indi.um.util.text.Quote;
import indi.um.util.ds.IntArray;
import indi.um.util.ds.LongArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.InternalThreadLocalMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created in webbiton on 2021/1/8.
 * Recreated on 2022/8/13.
 */
public class JsonInputStreamReader implements SyncReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonInputStreamReader.class);

    private static final int NONE = -1;

    private int last;

    private final InputStream is;

    private JsonConsumer jc;

    private int config;

    private ByteBuf cache;

    public JsonInputStreamReader(InputStream is) {
        super();
        this.is = is;
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
            skipGap();
            readValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (cache != null) {
                cache.release();
                cache = null;
            }
        }
    }

    private void readValue() throws IOException {
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
                if ((config & FLAG_SINGLE_QUOTE_STRING) == 0) {
                    throw new IllegalArgumentException();
                }
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
            case NONE:
                throw new EOFException();
            default:
                throw new IllegalArgumentException();
        }
    }

    private void readObject() throws IOException {
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
            readValue();
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

    private void readArray() throws IOException {
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
            readValue();
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

    private String readString(boolean singleQuote) throws IOException {
        ByteBuf cache = this.cache;
        if (cache != null) {
            cache.clear();
        } else {
            cache = Unpooled.buffer();
            this.cache = cache;
        }
        last = NONE;
        int ch0, ch1;
        ch0 = NONE;
        LOOP:
        while (true) {
            int current = is.read();
            switch (current) {
                case NONE:
                    throw new EOFException();
                case '"':
                    if (singleQuote) {
                        cache.writeByte('"');
                        break;
                    } else {
                        break LOOP;
                    }
                case '\'':
                    if (singleQuote) {
                        break LOOP;
                    } else {
                        cache.writeByte('\'');
                        break;
                    }
                case '\\':
                    current = is.read();
                    switch (current) {
                        case NONE:
                            throw new EOFException();
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
                            ch1 = readHex16();
                            if (Character.MIN_SURROGATE <= ch1 && ch1 <= Character.MAX_SURROGATE) {
                                if (ch1 < Character.MIN_LOW_SURROGATE) {
                                    ch0 = ch1;
                                    continue LOOP;
                                } else if (ch0 != NONE) {
                                    writeCodePoint(cache, ((ch0 << 10) + ch1) + (Character.MIN_SUPPLEMENTARY_CODE_POINT
                                            - (Character.MIN_HIGH_SURROGATE << 10) - Character.MIN_LOW_SURROGATE));
                                }
                            } else {
                                writeCodePoint(cache, ch1);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                default:
                    cache.writeByte(current);
                    break;
            }
            ch0 = NONE;
        }
        return cache.toString(StandardCharsets.UTF_8);
    }

    private int readHex16() throws IOException {
        int ch = 0;
        for (int offset = 0; offset < 4; offset++) {
            ch <<= 4;
            int current = is.read();
            if ('0' <= current && current <= '9') {
                ch |= (current - '0');
            } else if ('a' <= current && current <= 'f') {
                ch |= (current - HexCase.LOWER);
            } else if ('A' <= current && current <= 'F') {
                ch |= (current - HexCase.UPPER);
            } else if (current == NONE) {
                throw new EOFException();
            } else {
                throw new InputMismatchException();
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

    private void readNumber() throws IOException {
        ByteBuf cache = this.cache;
        if (cache != null) {
            cache.clear();
        } else {
            cache = Unpooled.buffer();
            this.cache = cache;
        }
        if (last == NONE) {
            last = is.read();
        }
        boolean decimal = false;
        LOOP:
        while (true) {
            switch (last) {
                case NONE:
                    throw new EOFException();
                case '.':
                case 'e':
                case 'E':
                    decimal = true;
                    // no break here
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                case '-':
                    cache.writeByte(last);
                    break;
                default:
                    break LOOP;
            }
            last = is.read();
        }
        final String string = cache.toString(StandardCharsets.US_ASCII);
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

    private void skipGap() throws IOException {
        if (last == NONE) {
            last = is.read();
        }
        if ((config & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            while (last <= 0x20) {
                if (last == NONE) {
                    throw new EOFException();
                }
                last = is.read();
            }
        } else {
            skipGapOrComment();
        }
    }

    private void skipGapOrComment() throws IOException {
        while (true) {
            if (last <= 0x20) {
                if (last == NONE) {
                    throw new EOFException();
                }
                last = is.read();
                continue;
            }
            if (last == '/') {
                int current = is.read();
                if (current == '/') {
                    if ((config & FLAG_LINE_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    do {
                        current = is.read();
                        if (current == NONE) {
                            throw new EOFException();
                        }
                    } while (current != '\n');
                    last = is.read();
                } else if (current == '*') {
                    if ((config & FLAG_BLOCK_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    LOOP:
                    while (true) {
                        current = is.read();
                        while (current == '*') {
                            current = is.read();
                            if (current == '/') {
                                break LOOP;
                            }
                        }
                        if (current == NONE) {
                            throw new EOFException();
                        }
                    }
                    last = is.read();
                } else if (current == NONE) {
                    throw new EOFException();
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                break;
            }
        }
    }

    private void skipAscii(String string) throws IOException {
        final int length = string.length();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                if (last == NONE) {
                    last = is.read();
                }
            } else {
                last = is.read();
            }
            if (last != string.charAt(i)) {
                if (last == NONE) {
                    throw new EOFException();
                } else {
                    throw new InputMismatchException();
                }
            }
        }
        last = NONE;
    }

    @Override
    public void stringifyNeighbors(StringBuilder sb, int before, int after) {
        if (after > 0 && is.markSupported()) {
            try {
                is.mark(4 * after);
                try {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    char[] buffer = new char[after];
                    int offset = 0;
                    while (offset < after) {
                        int count = isr.read(buffer, offset, after - offset);
                        if (count > 0) {
                            offset += count;
                        } else {
                            break;
                        }
                    }
                    Quote.DEFAULT.append(sb, new String(buffer, 0, offset));
                    return;
                } finally {
                    is.reset();
                }
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        sb.append("\"\"");
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
        sb.append(']');
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()); // for possible further extensions
        stringify(sb);
        return sb.toString();
    }

    public int[] readIntArray1() throws IOException {
        skipGap();
        if (last != '[') {
            throw new IOException();
        }
        last = NONE;
        skipGap();
        if (last == ']') {
            return IntArray.Nullish.EMPTY;
        }
        final HeapIntBuf intBuf = new HeapIntBuf();
        final ByteBuf cache = Unpooled.buffer();
        try {
            while (true) {
                cache.clear();
                while ('0' <= last && last <= '9' || last == '-') {
                    cache.writeByte(last);
                    last = is.read();
                }
                intBuf.writeInt(Integer.parseInt(cache.toString(StandardCharsets.US_ASCII)));
                skipGap();
                if (last != ',') {
                    if (last == ']') {
                        return intBuf.toIntArray();
                    } else {
                        throw new IOException();
                    }
                }
                last = NONE;
                skipGap();
            }
        } finally {
            cache.release();
        }
    }

    public long[] readLongArray1() throws IOException {
        skipGap();
        if (last != '[') {
            throw new IOException();
        }
        last = NONE;
        skipGap();
        if (last == ']') {
            return LongArray.Nullish.EMPTY;
        }
        final ArrayList<Long> longList = InternalThreadLocalMap.get().arrayList();
        final ByteBuf cache = Unpooled.buffer();
        try {
            while (true) {
                cache.clear();
                while ('0' <= last && last <= '9' || last == '-') {
                    cache.writeByte(last);
                    last = is.read();
                }
                longList.add(Long.parseLong(cache.toString(StandardCharsets.US_ASCII)));
                skipGap();
                if (last != ',') {
                    if (last == ']') {
                        return LongArray.Box.unbox(longList);
                    } else {
                        throw new IOException();
                    }
                }
                last = NONE;
                skipGap();
            }
        } finally {
            cache.release();
        }
    }

    public int[][] readIntArray2() throws IOException {
        skipGap();
        if (last != '[') {
            throw new IOException();
        }
        last = NONE;
        skipGap();
        if (last == ']') {
            return IntMatrix.EMPTY;
        }
        final ArrayList<int[]> list = InternalThreadLocalMap.get().arrayList();
        final HeapIntBuf intBuf = new HeapIntBuf();
        final ByteBuf cache = Unpooled.buffer();
        try {
            while (true) {
                if (last != '[') {
                    throw new IOException();
                }
                last = NONE;
                skipGap();
                if (last == ']') {
                    list.add(IntArray.Nullish.EMPTY);
                } else {
                    while (true) {
                        cache.clear();
                        while ('0' <= last && last <= '9' || last == '-') {
                            cache.writeByte(last);
                            last = is.read();
                        }
                        intBuf.writeInt(Integer.parseInt(cache.toString(StandardCharsets.US_ASCII)));
                        skipGap();
                        if (last != ',') {
                            if (last == ']') {
                                skipGap();
                                list.add(intBuf.toIntArray());
                                intBuf.removeAll();
                                break;
                            } else {
                                throw new IOException();
                            }
                        }
                        last = NONE;
                        skipGap();
                    }
                }
                last = NONE;
                skipGap();
                if (last != ',') {
                    if (last == ']') {
                        return list.toArray(IntMatrix.EMPTY);
                    } else {
                        throw new IOException();
                    }
                }
                last = NONE;
                skipGap();
            }
        } finally {
            cache.release();
        }
    }
}
