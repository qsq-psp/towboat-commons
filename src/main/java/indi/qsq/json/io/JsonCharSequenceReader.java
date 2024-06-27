package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.entity.RawNumber;
import indi.qsq.util.ds.*;
import indi.qsq.util.reflect.ClassUtility;
import indi.qsq.util.text.Quote;
import io.netty.util.internal.InternalThreadLocalMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created in webbiton on 2021/4/11.
 * Created on 2022/6/5.
 */
public class JsonCharSequenceReader implements RecursiveReader {

    private final CharSequence string;

    private int pos;

    private JsonConsumer jc;

    private int config;

    private Consumer<RecursiveReader> callback;

    public JsonCharSequenceReader(CharSequence string) {
        super();
        this.string = string;
    }

    @Override
    public void config(int config) {
        this.config = config;
    }

    @Override
    public void read(@NotNull JsonConsumer jc) {
        this.jc = jc;
        this.callback = null;
        skipGap();
        readValue();
    }

    @Override
    public void skip(@NotNull Consumer<RecursiveReader> callback) {
        this.callback = callback;
    }

    @Override
    @SuppressWarnings("DefaultAnnotationParam")
    public String raw(@Index(inclusive = true) int fromIndex, @Index(inclusive = false) int toIndex) {
        return string.subSequence(fromIndex, toIndex).toString();
    }

    @Override
    public void setPosition(int pos) {
        this.pos = pos;
    }

    @Override
    public int getPosition() {
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
        switch (string.charAt(pos)) {
            case '{':
                skipObject();
                break;
            case '[':
                skipArray();
                break;
            case '"':
                skipString(false);
                break;
            case '\'':
                skipString(true);
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
                skipNumber();
                break;
            case 't':
                skipChars("true");
                break;
            case 'f':
                skipChars("false");
                break;
            case 'n':
                skipChars("null");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void readValue() {
        switch (string.charAt(pos)) {
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
                skipChars("true");
                jc.booleanValue(true);
                break;
            case 'f':
                skipChars("false");
                jc.booleanValue(false);
                break;
            case 'n':
                skipChars("null");
                jc.nullValue();
                break;
            default:
                throw new IllegalArgumentException("pos = " + pos);
        }
    }

    private void skipObject() {
        pos++;
        skipGap();
        boolean looped = false;
        LOOP:
        while (true) {
            int ch = string.charAt(pos);
            switch (ch) {
                case '}':
                    if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                        throw new IllegalArgumentException();
                    }
                    pos++;
                    break LOOP;
                case '"':
                    skipString(false);
                    break;
                case '\'':
                    skipString(true);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            skipGap();
            if (string.charAt(pos) != ':') {
                throw new IllegalArgumentException();
            }
            pos++;
            skipGap();
            skipValue();
            skipGap();
            ch = string.charAt(pos++);
            if (ch == ',') {
                skipGap();
                looped = true;
            } else if (ch == '}') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private void readObject() {
        jc.openObject();
        pos++;
        skipGap();
        boolean looped = false;
        LOOP:
        while (true) {
            int ch = string.charAt(pos);
            String key;
            switch (ch) {
                case '}':
                    if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                        throw new IllegalArgumentException();
                    }
                    pos++;
                    break LOOP;
                case '"':
                    key = readString(false);
                    break;
                case '\'':
                    key = readString(true);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            skipGap();
            if (string.charAt(pos) != ':') {
                throw new IllegalArgumentException();
            }
            pos++;
            skipGap();
            jc.key(key);
            meetValue();
            skipGap();
            ch = string.charAt(pos++);
            if (ch == ',') {
                skipGap();
                looped = true;
            } else if (ch == '}') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        jc.closeObject();
    }

    private void skipArray() {
        pos++;
        skipGap();
        boolean looped = false;
        while (true) {
            if (string.charAt(pos) == ']') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                pos++;
                break;
            }
            skipValue();
            skipGap();
            int ch = string.charAt(pos++);
            if (ch == ',') {
                skipGap();
                looped = true;
            } else if (ch == ']') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private void readArray() {
        jc.openArray();
        pos++;
        skipGap();
        boolean looped = false;
        while (true) {
            if (string.charAt(pos) == ']') {
                if (looped && (config & FLAG_TRAILING_COMMA) == 0) {
                    throw new IllegalArgumentException();
                }
                pos++;
                break;
            }
            meetValue();
            skipGap();
            int ch = string.charAt(pos++);
            if (ch == ',') {
                skipGap();
                looped = true;
            } else if (ch == ']') {
                break;
            } else {
                throw new IllegalArgumentException();
            }
        }
        jc.closeArray();
    }

    private int skipString(boolean singleQuote) {
        int quote;
        if (singleQuote) {
            if ((config & FLAG_SINGLE_QUOTE_STRING) == 0) {
                throw new IllegalArgumentException();
            }
            quote = '\'';
        } else {
            quote = '"';
        }
        if (string.charAt(pos) == quote) {
            pos++;
        } else {
            throw new IllegalArgumentException();
        }
        int escapeCount = 0;
        while (true) {
            int ch = string.charAt(pos++);
            if (ch == quote) {
                break;
            } else if (ch == '\\') {
                escapeCount++;
                pos++;
            }
        }
        return escapeCount;
    }

    private String readString(boolean singleQuote) {
        int start = pos + 1;
        if (skipString(singleQuote) == 0) {
            return string.subSequence(start, pos - 1).toString();
        } else {
            return readEscapedString(start, pos, singleQuote);
        }
    }

    private String readEscapedString(int pos, int lim, boolean singleQuote) {
        StringBuilder sb = new StringBuilder(lim - pos);
        LOOP:
        while (pos < lim) {
            int ch = string.charAt(pos++);
            switch (ch) {
                case '"':
                    if (singleQuote) {
                        sb.append('"');
                        break;
                    } else {
                        break LOOP;
                    }
                case '\'':
                    if (singleQuote) {
                        break LOOP;
                    } else {
                        sb.append('\'');
                        break;
                    }
                case '\\':
                    if (pos == lim) {
                        throw new IndexOutOfBoundsException();
                    }
                    ch = string.charAt(pos++);
                    switch (ch) {
                        case '"':
                        case '\'':
                        case '\\':
                        case '/':
                            sb.append((char) ch);
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
                            if (pos + 4 <= lim) {
                                sb.append((char) Integer.parseInt(string, pos, pos + 4, 16));
                                pos += 4;
                            } else {
                                throw new IndexOutOfBoundsException();
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                default:
                    sb.append((char) ch);
                    break;
            }
        }
        return sb.toString();
    }

    private void skipNumber() {
        while ("0123456789-.Ee".indexOf(string.charAt(pos)) != -1) {
            pos++;
        }
    }

    private void readNumber() {
        final int start = pos;
        boolean decimal = false;
        try {
            LOOP:
            while (true) {
                switch (string.charAt(pos)) {
                    case '.':
                    case 'E':
                    case 'e':
                        decimal = true;
                    case '0': case '1': case '2': case '3': case '4':
                    case '5': case '6': case '7': case '8': case '9':
                    case '-':
                        pos++;
                        break;
                    default:
                        break LOOP;
                }
            }
        } catch (IndexOutOfBoundsException ignore) {}
        if (decimal) {
            String string = raw(start, pos);
            if ((config & FLAG_RAW_DECIMAL) == 0) {
                jc.numberValue(Double.parseDouble(string));
            } else {
                jc.numberValue(new RawNumber(string));
            }
        } else {
            jc.numberValue(Long.parseLong(string, start, pos, 10));
        }
    }

    private void skipGap() {
        if ((config & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            while (string.charAt(pos) <= ' ') {
                pos++;
            }
        } else {
            skipGapOrComment();
        }
    }

    private void skipGapOrComment() {
        while (true) {
            int ch = string.charAt(pos);
            if (ch <= ' ') {
                pos++;
                continue;
            }
            if (ch == '/') {
                ch = string.charAt(pos + 1);
                if (ch == '/') {
                    if ((config & FLAG_LINE_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    int i = pos + 2;
                    while (string.charAt(i) != '\n') {
                        i++;
                    }
                    pos = i + 1;
                } else if (ch == '*') {
                    if ((config & FLAG_BLOCK_COMMENT) == 0) {
                        throw new IllegalArgumentException();
                    }
                    int i = pos + 2;
                    while (!(string.charAt(i) == '*' && string.charAt(i + 1) == '/')) {
                        i++;
                    }
                    pos = i + 2;
                } else {
                    throw new IllegalArgumentException();
                }
                continue;
            }
            break;
        }
    }

    private void skipChars(String string) {
        final int length = string.length();
        for (int i = 0; i < length; i++) {
            if (this.string.charAt(pos) != string.charAt(i)) {
                throw new IllegalArgumentException();
            }
            pos++;
        }
    }

    @Override
    public void stringifyNeighbors(StringBuilder sb, int before, int after) {
        before = Math.max(0, pos - before);
        after = Math.min(pos + after, string.length());
        Quote.DEFAULT.append(sb, string.toString(), before, after);
    }

    public void stringify(StringBuilder sb) {
        sb.append("[config = ");
        READER_FLAG.stringify(sb, config, " | ");
        sb.append(", pos = ").append(pos);
        if (callback != null) {
            sb.append(", callback = ").append(callback);
        }
        sb.append(']');
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(ClassUtility.normal(this)); // for possible further extensions
        stringify(sb);
        return sb.toString();
    }

    /**
     * @throws IllegalArgumentException includes NumberFormatException
     */
    public static int[] readIntArray1(CharSequence string) throws IllegalArgumentException {
        final int limit = string.length() - 1;
        if (limit <= 0 || string.charAt(0) != '[' || string.charAt(limit) != ']') {
            throw new IllegalArgumentException();
        }
        if (limit == 1) {
            return IntArray.Nullish.EMPTY;
        }
        final HeapIntBuf intBuf = new HeapIntBuf();
        int i0 = -1;
        for (int i1 = 1; i1 < limit; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9' || ch == '-') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                intBuf.writeInt(Integer.parseInt(string, i0, i1, 10));
                i0 = -1;
            }
        }
        if (i0 != -1) {
            intBuf.writeInt(Integer.parseInt(string, i0, limit, 10));
        }
        return intBuf.toIntArray();
    }

    /**
     * @throws IllegalArgumentException includes NumberFormatException
     */
    public static long[] readLongArray1(CharSequence string) throws IllegalArgumentException {
        final int limit = string.length() - 1;
        if (limit <= 0 || string.charAt(0) != '[' || string.charAt(limit) != ']') {
            throw new IllegalArgumentException();
        }
        if (limit == 1) {
            return LongArray.Nullish.EMPTY;
        }
        final ArrayList<Long> longList = InternalThreadLocalMap.get().arrayList();
        int i0 = -1;
        for (int i1 = 1; i1 < limit; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9' || ch == '-') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                longList.add(Long.parseLong(string, i0, i1, 10));
                i0 = -1;
            }
        }
        if (i0 != -1) {
            longList.add(Long.parseLong(string, i0, limit, 10));
        }
        return LongArray.Box.unbox(longList);
    }

    /**
     * @throws IllegalArgumentException includes NumberFormatException
     */
    public static int[][] readIntArray2(CharSequence string) throws IllegalArgumentException {
        final int limit = string.length() - 1;
        if (limit <= 0 || string.charAt(0) != '[' || string.charAt(limit) != ']') {
            throw new IllegalArgumentException();
        }
        if (limit == 1) {
            return IntMatrix.EMPTY;
        }
        final ArrayList<int[]> list = InternalThreadLocalMap.get().arrayList();
        final HeapIntBuf intBuf = new HeapIntBuf();
        int i0 = -2;
        for (int i1 = 1; i1 < limit; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9' || ch == '-') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else {
                if (i0 >= 0) {
                    intBuf.writeInt(Integer.parseInt(string, i0, i1, 10));
                    i0 = -1;
                }
                if (ch == '[') {
                    if (i0 == -2) {
                        i0 = -1;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    intBuf.removeAll();
                } else if (ch == ']') {
                    if (i0 == -1) {
                        i0 = -2;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    list.add(intBuf.toIntArray());
                }
            }
        }
        if (i0 != -2) {
            throw new IllegalArgumentException();
        }
        return list.toArray(IntMatrix.EMPTY);
    }

    /**
     * @throws IllegalArgumentException includes NumberFormatException
     */
    public static long[][] readLongArray2(CharSequence string) throws IllegalArgumentException {
        final int limit = string.length() - 1;
        if (limit <= 0 || string.charAt(0) != '[' || string.charAt(limit) != ']') {
            throw new IllegalArgumentException();
        }
        final ArrayList<long[]> list = new ArrayList<>();
        final ArrayList<Long> longList = InternalThreadLocalMap.get().arrayList();
        int i0 = -2;
        for (int i1 = 1; i1 < limit; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9' || ch == '-') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else {
                if (i0 >= 0) {
                    longList.add(Long.parseLong(string, i0, i1, 10));
                    i0 = -1;
                }
                if (ch == '[') {
                    if (i0 == -2) {
                        i0 = -1;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    longList.clear();
                } else if (ch == ']') {
                    if (i0 == -1) {
                        i0 = -2;
                    } else {
                        throw new IllegalArgumentException();
                    }
                    list.add(LongArray.Box.unbox(longList));
                }
            }
        }
        if (i0 != -2) {
            throw new IllegalArgumentException();
        }
        return list.toArray(new long[0][]);
    }

    public static int readCoarseInt(CharSequence string, int fallback) {
        final int length = string.length();
        int i0 = 0;
        while (i0 < length) {
            int ch = string.charAt(i0);
            if ('0' <= ch && ch <= '9') {
                break;
            }
            i0++;
        }
        int i1 = i0 + 1;
        while (i1 < length) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                i1++;
            } else {
                break;
            }
        }
        if (i1 > length) {
            return fallback;
        }
        if (i0 > 0 && string.charAt(i0 - 1) == '-') {
            i0--;
        }
        try {
            return Integer.parseInt(string, i0, i1, 10);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static long readCoarseLong(CharSequence string, long fallback) {
        final int length = string.length();
        int i0 = 0;
        while (i0 < length) {
            int ch = string.charAt(i0);
            if ('0' <= ch && ch <= '9') {
                break;
            }
            i0++;
        }
        int i1 = i0 + 1;
        while (i1 < length) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                i1++;
            } else {
                break;
            }
        }
        if (i1 > length) {
            return fallback;
        }
        if (i0 > 0 && string.charAt(i0 - 1) == '-') {
            i0--;
        }
        try {
            return Long.parseLong(string, i0, i1, 10);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static int readCoarseUnsignedInt(CharSequence string, int fallback) {
        final int length = string.length();
        int i0 = 0;
        while (i0 < length) {
            int ch = string.charAt(i0);
            if ('0' <= ch && ch <= '9') {
                break;
            }
            i0++;
        }
        int i1 = i0 + 1;
        while (i1 < length) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                i1++;
            } else {
                break;
            }
        }
        if (i1 > length) {
            return fallback;
        }
        try {
            return Integer.parseUnsignedInt(string, i0, i1, 10);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static long readCoarseUnsignedLong(CharSequence string, long fallback) {
        final int length = string.length();
        int i0 = 0;
        while (i0 < length) {
            int ch = string.charAt(i0);
            if ('0' <= ch && ch <= '9') {
                break;
            }
            i0++;
        }
        int i1 = i0 + 1;
        while (i1 < length) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                i1++;
            } else {
                break;
            }
        }
        if (i1 > length) {
            return fallback;
        }
        try {
            return Long.parseUnsignedLong(string, i0, i1, 10);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static int[] readCoarseIntArray(CharSequence string) {
        final int length = string.length();
        if (length <= 2) { // at most one number
            int value = readCoarseInt(string, 100);
            if (value == 100) {
                return IntArray.Nullish.EMPTY;
            } else {
                return new int[] {value};
            }
        }
        final HeapIntBuf intBuf = new HeapIntBuf();
        int i0 = -1;
        for (int i1 = 0; i1 < length; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                if (i0 > 0 && string.charAt(i0 - 1) == '-') {
                    i0--;
                }
                try {
                    intBuf.writeInt(Integer.parseInt(string, i0, i1, 10));
                } catch (NumberFormatException ignore) {}
                i0 = -1;
            }
        }
        if (i0 != -1) {
            try {
                intBuf.writeInt(Integer.parseInt(string, i0, length, 10));
            } catch (NumberFormatException ignore) {}
        }
        return intBuf.toIntArray();
    }

    public static long[] readCoarseLongArray(CharSequence string) {
        final int length = string.length();
        if (length <= 2) { // at most one number
            long value = readCoarseLong(string, 100);
            if (value == 100) {
                return LongArray.Nullish.EMPTY;
            } else {
                return new long[] {value};
            }
        }
        final ArrayList<Long> longList = InternalThreadLocalMap.get().arrayList();
        int i0 = -1;
        for (int i1 = 0; i1 < length; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                if (i0 > 0 && string.charAt(i0 - 1) == '-') {
                    i0--;
                }
                try {
                    longList.add(Long.parseLong(string, i0, i1, 10));
                } catch (NumberFormatException ignore) {}
                i0 = -1;
            }
        }
        if (i0 != -1) {
            try {
                longList.add(Long.parseLong(string, i0, length, 10));
            } catch (NumberFormatException ignore) {}
        }
        return LongArray.Box.unbox(longList);
    }

    public static int[] readCoarseUnsignedIntArray(CharSequence string) {
        final int length = string.length();
        if (length <= 2) { // at most one number
            int value = readCoarseUnsignedInt(string, 100);
            if (value == 100) {
                return IntArray.Nullish.EMPTY;
            } else {
                return new int[] {value};
            }
        }
        final HeapIntBuf intBuf = new HeapIntBuf();
        int i0 = -1;
        for (int i1 = 0; i1 < length; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                try {
                    intBuf.writeInt(Integer.parseUnsignedInt(string, i0, i1, 10));
                } catch (NumberFormatException ignore) {}
                i0 = -1;
            }
        }
        if (i0 != -1) {
            try {
                intBuf.writeInt(Integer.parseUnsignedInt(string, i0, length, 10));
            } catch (NumberFormatException ignore) {}
        }
        return intBuf.toIntArray();
    }

    public static long[] readCoarseUnsignedLongArray(CharSequence string) {
        final int length = string.length();
        if (length <= 2) { // at most one number
            long value = readCoarseUnsignedLong(string, 100);
            if (value == 100) {
                return LongArray.Nullish.EMPTY;
            } else {
                return new long[] {value};
            }
        }
        final ArrayList<Long> longList = InternalThreadLocalMap.get().arrayList();
        int i0 = -1;
        for (int i1 = 0; i1 < length; i1++) {
            int ch = string.charAt(i1);
            if ('0' <= ch && ch <= '9') {
                if (i0 == -1) {
                    i0 = i1;
                }
            } else if (i0 != -1) {
                try {
                    longList.add(Long.parseUnsignedLong(string, i0, i1, 10));
                } catch (NumberFormatException ignore) {}
                i0 = -1;
            }
        }
        if (i0 != -1) {
            try {
                longList.add(Long.parseUnsignedLong(string, i0, length, 10));
            } catch (NumberFormatException ignore) {}
        }
        return LongArray.Box.unbox(longList);
    }
}
