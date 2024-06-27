package indi.qsq.json.io;

import indi.qsq.util.nio.AsyncByteBufConsumer;
import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.entity.StructureChecker;
import indi.qsq.json.entity.RawNumber;
import indi.qsq.util.text.HexCase;
import indi.qsq.util.text.Quote;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2022/6/5, named JsonCharStreamReader.
 * Renamed to JsonPushReader on 2023/4/23.
 * Renamed to JsonAsyncByteBufReader on 2023/4/29.
 */
public class JsonAsyncByteBufReader implements ReaderStates, ReaderFlags, AsyncByteBufConsumer {

    private static final char REPLACEMENT = '\ufffd'; // for decode failure

    private int token, hold, depth, config;
    
    private final StringBuilder sb = new StringBuilder();

    private final JsonConsumer jc;

    public JsonAsyncByteBufReader(JsonConsumer jc) {
        super();
        this.jc = StructureChecker.checked(jc);
    }

    @Override
    public void config(int config) {
        if (token == TOKEN_FREE) {
            this.config = config;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void start() {
        token = TOKEN_FREE;
        hold = HOLD_NOTHING;
        depth = 0;
        sb.setLength(0); // clear
    }

    /**
     * The content is from readerIndex (inclusive) to writerIndex (exclusive)
     * The consumer is responsible for releasing the byteBuf
     */
    @Override
    public void update(@NotNull ByteBuf data) {
        while (data.isReadable()) {
            switch (token) {
                case TOKEN_FREE:
                    free(data);
                    break;
                case TOKEN_WORD: 
                    word(data);
                    break;
                case TOKEN_INTEGRAL:
                case TOKEN_DECIMAL:
                    number(data);
                    break;
                case TOKEN_STRING_DOUBLE_QUOTE:
                case TOKEN_STRING_SINGLE_QUOTE:
                    stringState(data);
                    break;
                case TOKEN_STRING_DOUBLE_QUOTE_ESCAPE:
                case TOKEN_STRING_SINGLE_QUOTE_ESCAPE:
                    stringEscapeState(data);
                    break;
                case TOKEN_COMMENT_SLASH:
                    commentSlashState(data);
                    break;
                case TOKEN_LINE_COMMENT:
                    do {
                        if (data.readByte() == '\n') {
                            token = TOKEN_FREE;
                            break;
                        }
                    } while (data.isReadable());
                    break;
                case TOKEN_BLOCK_COMMENT:
                    do {
                        if (data.readByte() == '*') {
                            token = TOKEN_BLOCK_COMMENT_STAR;
                            break;
                        }
                    } while (data.isReadable());
                    break;
                case TOKEN_BLOCK_COMMENT_STAR:
                    do {
                        int x = data.readByte();
                        if (x == '/') {
                            token = TOKEN_FREE;
                            break;
                        } else if (x != '*') {
                            token = TOKEN_BLOCK_COMMENT;
                            break;
                        }
                    } while (data.isReadable());
                    break;
            }
        }
    }
    
    private void free(@NotNull ByteBuf data) {
        int x;
        while (true) {
            x = data.readUnsignedByte();
            if (x > 0x20) {
                break;
            }
            if (!data.isReadable()) {
                return;
            }
        }
        switch (x) {
            case '{':
                beforeValue();
                jc.openObject();
                depth++;
                break;
            case '}':
                beforeClose();
                jc.closeObject();
                depth--;
                break;
            case '[':
                beforeValue();
                jc.openArray();
                depth++;
                break;
            case ']':
                beforeClose();
                jc.closeArray();
                depth--;
                break;
            case ',':
                if (hold != HOLD_NOTHING) {
                    if (hold != HOLD_STRING) {
                        throw new IllegalArgumentException();
                    }
                    jc.stringValue(sb.toString());
                    sb.setLength(0); // clear
                }
                hold = HOLD_COMMA;
                break;
            case ':':
                if (hold != HOLD_STRING) {
                    throw new IllegalArgumentException();
                }
                jc.key(sb.toString());
                sb.setLength(0); // clear
                hold = HOLD_COLON;
                break;
            case 't': case 'f': case 'n':
                beforeValue();
                sb.append((char) x);
                token = TOKEN_WORD;
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
                beforeValue();
                sb.append((char) x);
                token = TOKEN_INTEGRAL;
                break;
            case '.':
                beforeValue();
                sb.append("0.");
                token = TOKEN_DECIMAL;
                break;
            case '"':
                beforeValue();
                token = TOKEN_STRING_DOUBLE_QUOTE;
                break;
            case '\'':
                if ((config & FLAG_SINGLE_QUOTE_STRING) == 0) {
                    throw new IllegalArgumentException(toString("Single quote string not allowed"));
                }
                beforeValue();
                token = TOKEN_STRING_SINGLE_QUOTE;
                break;
            case '/':
                if ((config & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
                    throw new IllegalArgumentException(toString("Comment not allowed"));
                }
                token = TOKEN_COMMENT_SLASH;
                break;
            default:
                throw new IllegalArgumentException(toString(Quote.DEFAULT.apply((byte) x)));
        }
    }
    
    private void word(@NotNull ByteBuf data) {
        final int x = data.readByte();
        if ('a' <= x && x <= 'z') {
            sb.append((char) x);
        } else {
            data.readerIndex(data.readerIndex() - 1); // unread one byte
            String word = sb.toString();
            switch (word) {
                case "null":
                    jc.nullValue();
                    break;
                case "true":
                    jc.booleanValue(true);
                    break;
                case "false":
                    jc.booleanValue(false);
                    break;
                default:
                    throw new IllegalArgumentException(word);
            }
            sb.setLength(0); // clear
            token = TOKEN_FREE;
        }
    }
    
    private void number(@NotNull ByteBuf data) {
        final int x = data.readByte();
        switch (x) {
            case '.':
            case 'E':
            case 'e':
                token = TOKEN_DECIMAL;
                // no break here
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                sb.append((char) x);
                break;
            default:
                if (token == TOKEN_DECIMAL) {
                    if ((config & FLAG_RAW_DECIMAL) == 0) {
                        jc.numberValue(Double.parseDouble(sb.toString()));
                    } else {
                        jc.numberValue(new RawNumber(sb.toString()));
                    }
                } else {
                    jc.numberValue(Long.parseLong(sb, 0, sb.length(), 10));
                }
                sb.setLength(0); // clear
                data.readerIndex(data.readerIndex() - 1); // unread one byte
                token = TOKEN_FREE;
                break;
        }
    }
    
    private void stringState(@NotNull ByteBuf data) {
        final int x = data.readUnsignedByte();
        if ((x & 0x80) == 0) {
            switch (HOLD_TYPE_MASK & hold) {
                case HOLD_NOTHING:
                    break;
                case HOLD_UTF8_3:
                    sb.append(REPLACEMENT);
                    // no break here
                case HOLD_UTF8_2:
                    sb.append(REPLACEMENT);
                    // no break here
                case HOLD_UTF8_1:
                    sb.append(REPLACEMENT);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            switch (x) {
                case '"':
                    if (token == TOKEN_STRING_DOUBLE_QUOTE) {
                        token = TOKEN_FREE;
                        hold = HOLD_STRING;
                    } else {
                        sb.append('"');
                    }
                    break;
                case '\'':
                    if (token == TOKEN_STRING_SINGLE_QUOTE) {
                        token = TOKEN_FREE;
                        hold = HOLD_STRING;
                    } else {
                        sb.append('\'');
                    }
                    break;
                case '\\':
                    assert token == TOKEN_STRING_DOUBLE_QUOTE || token == TOKEN_STRING_SINGLE_QUOTE;
                    token++; // escape
                    break;
                default:
                    sb.append((char) x);
                    break;
            }
        } else {
            switch (HOLD_TYPE_MASK & hold) {
                case HOLD_NOTHING:
                    if ((x & 0xe0) == 0xc0 || (x & 0xf0) == 0xe0 || (x & 0xf1) == 0xf0) {
                        hold = (x << 24) | HOLD_UTF8_1;
                    } else {
                        sb.append(REPLACEMENT);
                    }
                    break;
                case HOLD_UTF8_1:
                    if ((x & 0x40) == 0) {
                        if ((hold & 0xe0000000) == 0xc0000000) {
                            sb.append((char) (((hold & 0x1f000000) >> 18) | (x & 0x3f)));
                            hold = HOLD_NOTHING;
                        } else {
                            hold = (0xff000000 & hold) | (x << 16) | HOLD_UTF8_2;
                        }
                    } else {
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                        hold = HOLD_NOTHING;
                    }
                    break;
                case HOLD_UTF8_2:
                    if ((x & 0x40) == 0) {
                        if ((hold & 0xf0000000) == 0xe0000000) {
                            sb.append((char) (((hold & 0x0f000000) >> 12) | ((hold & 0x3f0000) >> 10) | (x & 0x3f)));
                            hold = HOLD_NOTHING;
                        } else {
                            hold = (0xffff0000 & hold) | (x << 8) | HOLD_UTF8_3;
                        }
                    } else {
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                        hold = HOLD_NOTHING;
                    }
                    break;
                case HOLD_UTF8_3:
                    if ((x & 0x40) == 0 && (hold & 0xf1000000) == 0xf0000000) {
                        int cp = ((hold & 0x07000000) >> 6) | ((hold & 0x3f0000) >> 10) | ((hold & 0x3f00) >> 2) | (x & 0x3f);
                        sb.append((char) (Character.MIN_HIGH_SURROGATE | ((cp >> 10) & 0x3ff)));
                        sb.append((char) (Character.MIN_LOW_SURROGATE | (cp & 0x3ff)));
                    } else {
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                        sb.append(REPLACEMENT);
                    }
                    hold = HOLD_NOTHING;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    
    private void stringEscapeState(@NotNull ByteBuf data) {
        final int x = data.readByte();
        switch (HOLD_TYPE_MASK & hold) { // extra state in variable hold (unicode nibble decode process)
            case HOLD_UNICODE_0:
                hold = (decodeNibble(x) << 16) | HOLD_UNICODE_1;
                break;
            case HOLD_UNICODE_1:
                hold = (0xf0000 & hold) | (decodeNibble(x) << 12) | HOLD_UNICODE_2;
                break;
            case HOLD_UNICODE_2:
                hold = (0xff000 & hold) | (decodeNibble(x) << 8) | HOLD_UNICODE_3;
                break;
            case HOLD_UNICODE_3:
                sb.append((char) (((hold & 0xfff00) >> 4) | decodeNibble(x)));
                assert token == TOKEN_STRING_DOUBLE_QUOTE_ESCAPE || token == TOKEN_STRING_SINGLE_QUOTE_ESCAPE;
                token--; // unescape
                hold = HOLD_NOTHING;
                break;
            default:
                assert token == TOKEN_STRING_DOUBLE_QUOTE_ESCAPE || token == TOKEN_STRING_SINGLE_QUOTE_ESCAPE;
                token--; // unescape
                switch (x) {
                    case '"':
                    case '\'':
                    case '\\':
                    case '/':
                        sb.append((char) x);
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
                        // no need to assert here
                        token++; // escape again
                        hold = HOLD_UNICODE_0;
                        break;
                }
                break;
        }
    }

    private int decodeNibble(int x) {
        if ('0' <= x && x <= '9') {
            return x - '0';
        } else if ('a' <= x && x <= 'z') { // lower cases used more frequently
            return x - HexCase.LOWER;
        } else if ('A' <= x && x <= 'Z') {
            return x - HexCase.UPPER;
        } else {
            throw new IllegalArgumentException(toString(Quote.DEFAULT.apply((byte) x)));
        }
    }
    
    private void commentSlashState(@NotNull ByteBuf data) {
        final int x = data.readByte();
        if (x == '/') {
            if ((config & FLAG_LINE_COMMENT) == 0) {
                throw new IllegalArgumentException(toString("Line comment not allowed"));
            }
            token = TOKEN_LINE_COMMENT;
        } else if (x == '*') {
            if ((config & FLAG_BLOCK_COMMENT) == 0) {
                throw new IllegalArgumentException(toString("Block comment not allowed"));
            }
            token = TOKEN_BLOCK_COMMENT;
        } else {
            throw new IllegalArgumentException(toString("Other comment not allowed"));
        }
    }

    private void beforeValue() {
        if (hold == HOLD_STRING) {
            throw new IllegalArgumentException(toString());
        }
        assert sb.length() == 0;
        hold = HOLD_NOTHING;
    }

    private void beforeClose() {
        if (hold != HOLD_NOTHING) {
            if (hold == HOLD_STRING) {
                jc.stringValue(sb.toString());
                sb.setLength(0); // clear
                hold = HOLD_NOTHING;
            }
            if (hold == HOLD_COMMA && (config & FLAG_TRAILING_COMMA) == 0) {
                throw new IllegalArgumentException(toString("Trailing comma not allowed"));
            }
        }
        if (depth <= 0) {
            throw new IllegalArgumentException(toString("Negative depth"));
        }
        hold = HOLD_NOTHING;
    }

    @Override
    public void finish() {
        if (depth != 0) {
            throw new IllegalArgumentException(toString("Nonzero depth"));
        }
        if (hold == HOLD_STRING) {
            jc.stringValue(sb.toString());
            sb.setLength(0); // clear
            hold = HOLD_NOTHING;
        }
    }

    public void stringify(@NotNull StringBuilder sb, @Nullable String error) {
        sb.append("[config = ");
        READER_FLAG.stringify(sb, config, " | ");
        sb.append(", token = ").append(TOKEN.forKey(token));
        sb.append(", hold = ").append(HOLD.forKey(0xff & hold));
        sb.append(", depth = ").append(depth);
        if (error != null) {
            sb.append(", error = ").append(error);
        }
        sb.append(']');
    }

    @NotNull
    private String toString(@Nullable String error) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        stringify(sb, error);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(null);
    }
}
