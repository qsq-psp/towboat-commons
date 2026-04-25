package mujica.json.io;

import mujica.algebra.discrete.BigConstants;
import mujica.io.codec.Base16Case;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.TypePreference;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2021/4/11", project = "webbiton", name = "JsonCharSequenceReader")
@CodeHistory(date = "2021/12/30", project = "infrastructure", name = "CharSequenceReader")
@CodeHistory(date = "2022/6/5", project = "Ultramarine", name = "JsonCharSequenceReader")
@CodeHistory(date = "2026/2/28")
public class JsonCharSequenceReader implements JsonSyncReader {

    @NotNull
    private final CharSequence in;

    private int flags;

    public JsonCharSequenceReader(@NotNull CharSequence in) {
        super();
        this.in = in;
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    private int skipGap(@Index(of = "in") int index) {
        int ch;
        if ((flags & (FLAG_LINE_COMMENT | FLAG_BLOCK_COMMENT)) == 0) {
            while (true) {
                ch = in.charAt(index);
                if (ch > 0x20) {
                    break;
                }
                index++;
            }
        } else {
            while (true) {
                ch = in.charAt(index);
                if (ch != '/') {
                    if (ch > 0x20) {
                        break;
                    }
                    index++;
                    continue;
                }
                index++;
                ch = in.charAt(index++);
                if (ch == '/') {
                    if ((flags & FLAG_LINE_COMMENT) == 0) {
                        throw new RuntimeException("line comment");
                    }
                    do {
                        ch = in.charAt(index++);
                    } while (ch != '\n');
                } else if (ch == '*') {
                    if ((flags & FLAG_BLOCK_COMMENT) == 0) {
                        throw new RuntimeException("block comment");
                    }
                    while (true) {
                        ch = in.charAt(index++);
                        if (ch == '*') {
                            ch = in.charAt(index);
                            if (ch == '/') {
                                index++;
                                break;
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("comment unrecognized " + ch);
                }
            }
        }
        return index;
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        read(jh, 0);
    }

    public int read(@NotNull JsonHandler jh, @Index(of = "in") int index) {
        return readJson(jh, skipGap(index));
    }

    private int readJson(@NotNull JsonHandler jh, @Index(of = "in") int index) {
        final int ch = in.charAt(index);
        switch (ch) {
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
                return readJsonWord(jh, index);
            case '+':
                if ((flags & FLAG_PLUS_SIGN_NUMBER) == 0) {
                    throw new RuntimeException("plus sign");
                }
                // no break here
            case '-':
                return readJsonLiteral(jh, index);
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                return readJsonNumber(jh, index);
            case '`':
                if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                    throw new RuntimeException("grave accent");
                }
                return readJsonString(jh, index + 1, ch, true);
            case '\'':
                if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                    throw new RuntimeException("apostrophe");
                }
                // no break here
            case '"':
                return readJsonString(jh, index + 1, ch, true);
            case '{':
                return readJsonObject(jh, index + 1);
            case '[':
                return readJsonArray(jh, index + 1);
            default:
                throw new RuntimeException("unrecognized " + ch);
        }
    }

    private int readJsonWord(@NotNull JsonHandler jh, @Index(of = "in") int startIndex) {
        final int length = in.length();
        int endIndex = startIndex + 1;
        while (endIndex < length) {
            int ch = in.charAt(endIndex);
            if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z') {
                endIndex++;
            } else {
                break;
            }
        }
        final String string = in.subSequence(startIndex, endIndex).toString();
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
                    throw new RuntimeException("infinity");
                }
                jh.numberValue(Double.POSITIVE_INFINITY);
            case "NaN":
                if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                    throw new RuntimeException("not a number");
                }
                jh.numberValue(Double.NaN);
                break;
            default:
                throw new RuntimeException("unrecognized " + string);
        }
        return endIndex;
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private int readJsonLiteral(@NotNull JsonHandler jh, @Index(of = "in") int startIndex) {
        final int length = in.length();
        int endIndex = startIndex + 1;
        boolean isFractional = jh.testTypePreference(TypePreference.FLAG_INTEGRAL_FORCE_TO_FRACTIONAL);
        while (endIndex < length) {
            int ch = in.charAt(endIndex);
            if ('0' <= ch && ch <= '9' || ch == '+' || ch == '-') {
                endIndex++;
            } else if ('a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '.') {
                endIndex++;
                isFractional = true;
            } else {
                break;
            }
        }
        if (isFractional) {
            String string = in.subSequence(startIndex, endIndex).toString();
            LABEL:
            while (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_FORCE_TO_RAW)) {
                double value = Double.parseDouble(string);
                while (true) {
                    if (Double.isFinite(value)) {
                        break;
                    } else if (string.equals("+Infinity")) {
                        if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                            throw new RuntimeException("positive infinity");
                        }
                        break;
                    } else if (string.equals("-Infinity")) {
                        if ((flags & FLAG_INFINITY_NAN_EXTENSION) == 0) {
                            throw new RuntimeException("negative infinity");
                        }
                        break;
                    } else if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_OVERFLOW_TO_RAW)) {
                        break;
                    }
                    break LABEL;
                }
                jh.numberValue(value);
                return endIndex;
            }
            jh.numberValue(new FastNumber(string));
        } else {
            parseJsonInteger(jh, in.subSequence(startIndex, endIndex));
        }
        return endIndex;
    }

    private int readJsonNumber(@NotNull JsonHandler jh, @Index(of = "in") int startIndex) {
        final int length = in.length();
        int endIndex = startIndex + 1;
        boolean isFractional = jh.testTypePreference(TypePreference.FLAG_INTEGRAL_FORCE_TO_FRACTIONAL);
        while (endIndex < length) {
            int octet = in.charAt(endIndex);
            if ('0' <= octet && octet <= '9' || octet == '+' || octet == '-') {
                endIndex++;
            } else if (octet == 'E' || octet == 'e' || octet == '.') {
                endIndex++;
                isFractional = true;
            } else {
                break;
            }
        }
        if (isFractional) {
            String string = in.subSequence(startIndex, endIndex).toString();
            if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_FORCE_TO_RAW)) {
                double value = Double.parseDouble(string);
                if (!jh.testTypePreference(TypePreference.FLAG_FRACTIONAL_OVERFLOW_TO_RAW) || Double.isFinite(value)) {
                    jh.numberValue(value);
                    return endIndex;
                }
            }
            jh.numberValue(new FastNumber(string));
        } else {
            parseJsonInteger(jh, in.subSequence(startIndex, endIndex));
        }
        return endIndex;
    }

    private void parseJsonInteger(@NotNull JsonHandler jh, @NotNull CharSequence string) {
        if (jh.testTypePreference(TypePreference.FLAG_INTEGRAL_FORCE_TO_RAW)) {
            jh.numberValue(new FastNumber(string.toString()));
            return;
        }
        final int length = string.length();
        if (length < 10) {
            jh.numberValue(Integer.parseInt(string, 0, length, 10));
        } else if (length < 19) {
            long longValue = Long.parseLong(string, 0, length, 10);
            if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
                jh.numberValue((int) longValue);
            } else {
                jh.numberValue(longValue);
            }
        } else {
            BigInteger bigValue = new BigInteger(string.toString(), 10);
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
                    jh.numberValue(new FastNumber(string.toString()));
                }
            } else if (jh.testTypePreference(TypePreference.FLAG_INTEGRAL_OVERFLOW_TO_RAW)) {
                jh.numberValue(new FastNumber(string.toString()));
            } else {
                jh.numberValue(bigValue);
            }
        }
    }

    private int readJsonString(@NotNull JsonHandler jh, @Index(of = "in") int startIndex, int quoteChar, boolean isValue) {
        int endIndex = startIndex;
        StringBuilder sb = null;
        while (true) {
            int ch = in.charAt(endIndex);
            if (ch == '\\') {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                if (startIndex < endIndex) {
                    sb.append(in, startIndex, endIndex);
                }
                endIndex++;
                ch = in.charAt(endIndex++);
                switch (ch) {
                    case '\r':
                        ch = in.charAt(endIndex++);
                        if (ch == '\n') {
                            break;
                        }
                    case '\n':
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '\'':
                        if (ch == quoteChar) {
                            sb.append('\'');
                        } else {
                            throw new RuntimeException("escape apostrophe");
                        }
                        break;
                    case '`':
                        if (ch == quoteChar) {
                            sb.append('`');
                        } else {
                            throw new RuntimeException("escape grave accent");
                        }
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '/':
                        sb.append('/');
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
                        sb.append((char) readHex16(endIndex));
                        endIndex += 4;
                        break;
                    default:
                        throw new RuntimeException("escape " + ch);
                }
                startIndex = endIndex;
                continue;
            }
            if (ch == quoteChar) {
                break;
            }
            endIndex++;
        }
        if (sb != null) {
            if (startIndex < endIndex) {
                sb.append(in, startIndex, endIndex);
            }
            if (isValue) {
                jh.stringValue(sb);
            } else {
                jh.stringKey(sb.toString());
            }
        } else {
            if (isValue) {
                jh.stringValue(in.subSequence(startIndex, endIndex));
            } else {
                jh.stringKey(in.subSequence(startIndex, endIndex).toString());
            }
        }
        return endIndex + 1;
    }

    private int readHex16(@Index(of = "in") int index) {
        int value = 0;
        for (int shift = 12; shift >= 0; shift -= 4) {
            int digit = in.charAt(index++);
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

    private int readJsonObject(@NotNull JsonHandler jh, @Index(of = "in") int index) {
        jh.openObject();
        boolean first = true;
        boolean comma = false;
        LABEL:
        while (true) {
            index = skipGap(index);
            int ch = in.charAt(index++);
            switch (ch) {
                case '"':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    index = readJsonString(jh, index, ch, false);
                    break;
                case '\'':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_APOSTROPHE_QUOTE_STRING) == 0) {
                        throw new RuntimeException("apostrophe");
                    }
                    index = readJsonString(jh, index, ch, false);
                    break;
                case '`':
                    if (!first && !comma) {
                        throw new RuntimeException("missing comma");
                    }
                    if ((flags & FLAG_GRAVE_ACCENT_QUOTE_STRING) == 0) {
                        throw new RuntimeException("grave accent");
                    }
                    index = readJsonString(jh, index, ch, false);
                    break;
                case ',':
                    if (comma) {
                        throw new RuntimeException("duplicate comma");
                    }
                    if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                        throw new RuntimeException("leading comma");
                    }
                    comma = true;
                    index = skipGap(index);
                    continue LABEL;
                case '}':
                    if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                        throw new RuntimeException("trailing comma");
                    }
                    break LABEL;
                default:
                    throw new RuntimeException("unrecognized " + ch);
            }
            index = skipGap(index);
            ch = in.charAt(index++);
            if (ch != ':') {
                throw new RuntimeException("expect colon actual " + ch);
            }
            index = skipGap(index);
            index = readJson(jh, index);
            first = false;
            comma = false;
        }
        jh.closeObject();
        return index;
    }

    private int readJsonArray(@NotNull JsonHandler jh, @Index(of = "in") int index) {
        jh.openArray();
        boolean first = true;
        boolean comma = false;
        while (true) {
            index = skipGap(index);
            int ch = in.charAt(index);
            if (ch == ',') {
                index++;
                if (comma) {
                    throw new RuntimeException("duplicate comma");
                }
                if (first && (flags & FLAG_LEADING_COMMA) == 0) {
                    throw new RuntimeException("leading comma");
                }
                comma = true;
                continue;
            } else if (ch == ']') {
                index++;
                if (comma && (flags & FLAG_TRAILING_COMMA) == 0) {
                    throw new RuntimeException("trailing comma");
                }
                break;
            }
            if (comma || first) {
                index = readJson(jh, index);
                first = false;
                comma = false;
            } else {
                throw new RuntimeException("missing comma");
            }
        }
        jh.closeArray();
        return index;
    }
}
