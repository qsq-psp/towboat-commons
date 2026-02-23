package mujica.text.format;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CodeHistory(date = "2026/1/9")
@ReferencePage(title = "Pattern", href = "https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/regex/Pattern.html")
public class EscapeAppender extends CharSequenceAppender {

    @ReferencePage(title = "Unicode Utilities: UnicodeSet", href = "https://util.unicode.org/UnicodeJsps/list-unicodeset.jsp?a=%5Cp%7BGeneral_Category%3DControl%7D&g=&i=")
    public static final int FLAG_UNICODE_CONTROL = 1 << 2;
    @ReferencePage(title = "Unicode Utilities: UnicodeSet", href = "https://util.unicode.org/UnicodeJsps/list-unicodeset.jsp?a=%5Cp%7BGeneral_Category%3DFormat%7D&g=&i=")
    public static final int FLAG_UNICODE_FORMAT = 1 << 3;
    @Name(value = "\\h", language = "regex")
    public static final int FLAG_HORIZONTAL_WHITESPACE = 1 << 4;
    @Name(value = "\\v", language = "regex")
    public static final int FLAG_VERTICAL_WHITESPACE = 1 << 5;
    @Name(value = "\\s", language = "regex")
    @Name(value = "\\p{Space}", language = "regex")
    public static final int FLAG_WHITESPACE = 1 << 6;
    @Name(value = "\\d", language = "regex")
    @Name(value = "\\p{Digit}", language = "regex")
    public static final int FLAG_DIGIT = 1 << 7;
    public static final int FLAG_REGEX = 1 << 8;
    public static final int FLAG_SURROGATE = 1 << 9;
    @Name(value = "null", language = "en")
    public static final int FLAG_NUL = 1 << 10, CODE_NUL = 0x00; // 0
    @Name(value = "bell", language = "en")
    public static final int FLAG_BEL = 1 << 11, CODE_BEL = 0x07; // 7
    @Name(value = "backspace", language = "en")
    public static final int FLAG_BS = 1 << 12, CODE_BS = 0x08; // 8
    @Name(value = "horizontal tabulation", language = "en")
    public static final int FLAG_HT = 1 << 13, CODE_HT = 0x09; // 9
    @Name(value = "line feed", language = "en")
    public static final int FLAG_LF = 1 << 14, CODE_LF = 0x0a; // 10
    @Name(value = "vertical tabulation", language = "en")
    public static final int FLAG_VT = 1 << 15, CODE_VT = 0x0b; // 11
    @Name(value = "form feed", language = "en")
    public static final int FLAG_FF = 1 << 16, CODE_FF = 0x0c; // 12
    @Name(value = "carriage return", language = "en")
    public static final int FLAG_CR = 1 << 17, CODE_CR = 0x0d; // 13
    @Name(value = "quotation mark", language = "en")
    public static final int FLAG_QUOT = 1 << 18, CODE_QUOT = 0x22; // 34
    @Name(value = "apostrophe", language = "en")
    public static final int FLAG_APOS = 1 << 19, CODE_APOS = 0x27; // 39
    @Name(value = "slash", language = "en")
    public static final int FLAG_SL = 1 << 20, CODE_SL = 0x2f; // 47
    @Name(value = "backslash", language = "en")
    public static final int FLAG_BSL = 1 << 21, CODE_BSL = 0x5c; // 92
    @Name(value = "grave accent", language = "en")
    public static final int FLAG_GA = 1 << 22, CODE_GA = 0x60; // 96


    protected final int flags;

    public EscapeAppender(int flags) {
        super();
        this.flags = flags;
    }

    protected int escapeCodePoint(int ch) {
        switch (ch) {
            case CODE_NUL:
                if ((flags & FLAG_NUL) != 0) {
                    return '0';
                }
                break;
            case CODE_BEL:
                if ((flags & FLAG_BEL) != 0) {
                    return 'a';
                }
                break;
            case CODE_BS:
                if ((flags & FLAG_BS) != 0) {
                    return 'b';
                }
                break;
            case CODE_HT:
                if ((flags & FLAG_HT) != 0) {
                    return 't';
                }
                break;
            case CODE_LF:
                if ((flags & FLAG_LF) != 0) {
                    return 'n';
                }
                break;
            case CODE_VT:
                if ((flags & FLAG_VT) != 0) {
                    return 'v';
                }
                break;
            case CODE_FF:
                if ((flags & FLAG_FF) != 0) {
                    return 'f';
                }
                break;
            case CODE_CR:
                if ((flags & FLAG_CR) != 0) {
                    return 'r';
                }
                break;
            case CODE_QUOT:
                if ((flags & FLAG_QUOT) != 0) {
                    return '"';
                }
                break;
            case CODE_APOS:
                if ((flags & FLAG_APOS) != 0) {
                    return '\'';
                }
                break;
            case CODE_SL:
                if ((flags & FLAG_SL) != 0) {
                    return '/';
                }
                break;
            case CODE_BSL:
                if ((flags & FLAG_BSL) != 0) {
                    return '\\';
                }
                break;
            case CODE_GA:
                if ((flags & FLAG_GA) != 0) {
                    return '`';
                }
                break;
        }
        return -1;
    }

    @Nullable
    protected String escapeSequence(int ch) {
        switch (ch) {
            case CODE_NUL:
                if ((flags & FLAG_NUL) != 0) {
                    return "\\0";
                }
                break;
            case CODE_BEL:
                if ((flags & FLAG_BEL) != 0) {
                    return "\\a";
                }
                break;
            case CODE_BS:
                if ((flags & FLAG_BS) != 0) {
                    return "\\b";
                }
                break;
            case CODE_HT:
                if ((flags & FLAG_HT) != 0) {
                    return "\\t";
                }
                break;
            case CODE_LF:
                if ((flags & FLAG_LF) != 0) {
                    return "\\n";
                }
                break;
            case CODE_VT:
                if ((flags & FLAG_VT) != 0) {
                    return "\\t";
                }
                break;
            case CODE_FF:
                if ((flags & FLAG_FF) != 0) {
                    return "\\f";
                }
                break;
            case CODE_CR:
                if ((flags & FLAG_CR) != 0) {
                    return "\\r";
                }
                break;
            case CODE_QUOT:
                if ((flags & FLAG_QUOT) != 0) {
                    return "\\\"";
                }
                break;
            case CODE_APOS:
                if ((flags & FLAG_APOS) != 0) {
                    return "\\'";
                }
                break;
            case CODE_SL:
                if ((flags & FLAG_SL) != 0) {
                    return "\\/";
                }
                break;
            case CODE_BSL:
                if ((flags & FLAG_BSL) != 0) {
                    return "\\\\";
                }
                break;
            case CODE_GA:
                if ((flags & FLAG_GA) != 0) {
                    return "\\`";
                }
                break;
        }
        return null;
    }

    @Override
    public boolean isApplicable(@NotNull CharSequence string, int startIndex, int endIndex) {
        return true;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return isIdentity(string, 0, string.length());
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            switch (string.charAt(index)) {
                case CODE_NUL:
                    if ((flags & FLAG_NUL) != 0) {
                        return false;
                    }
                    break;
                case CODE_BEL:
                    if ((flags & FLAG_BEL) != 0) {
                        return false;
                    }
                    break;
                case CODE_BS:
                    if ((flags & FLAG_BS) != 0) {
                        return false;
                    }
                    break;
                case CODE_HT:
                    if ((flags & FLAG_HT) != 0) {
                        return false;
                    }
                    break;
                case CODE_LF:
                    if ((flags & FLAG_LF) != 0) {
                        return false;
                    }
                    break;
                case CODE_VT:
                    if ((flags & FLAG_VT) != 0) {
                        return false;
                    }
                    break;
                case CODE_FF:
                    if ((flags & FLAG_FF) != 0) {
                        return false;
                    }
                    break;
                case CODE_CR:
                    if ((flags & FLAG_CR) != 0) {
                        return false;
                    }
                    break;
                case CODE_QUOT:
                    if ((flags & FLAG_QUOT) != 0) {
                        return false;
                    }
                    break;
                case CODE_APOS:
                    if ((flags & FLAG_APOS) != 0) {
                        return false;
                    }
                    break;
                case CODE_SL:
                    if ((flags & FLAG_SL) != 0) {
                        return false;
                    }
                    break;
                case CODE_BSL:
                    if ((flags & FLAG_BSL) != 0) {
                        return false;
                    }
                    break;
                case CODE_GA:
                    if ((flags & FLAG_GA) != 0) {
                        return false;
                    }
                    break;
            } // end of switch
        }
        return true;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string) {
        return deltaCharCount(string, 0, string.length());
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        int count = 0;
        for (int index = startIndex; index < endIndex; index++) {
            switch (string.charAt(index)) {
                case CODE_NUL:
                    if ((flags & FLAG_NUL) != 0) {
                        count++;
                    }
                    break;
                case CODE_BEL:
                    if ((flags & FLAG_BEL) != 0) {
                        count++;
                    }
                    break;
                case CODE_BS:
                    if ((flags & FLAG_BS) != 0) {
                        count++;
                    }
                    break;
                case CODE_HT:
                    if ((flags & FLAG_HT) != 0) {
                        count++;
                    }
                    break;
                case CODE_LF:
                    if ((flags & FLAG_LF) != 0) {
                        count++;
                    }
                    break;
                case CODE_VT:
                    if ((flags & FLAG_VT) != 0) {
                        count++;
                    }
                    break;
                case CODE_FF:
                    if ((flags & FLAG_FF) != 0) {
                        count++;
                    }
                    break;
                case CODE_CR:
                    if ((flags & FLAG_CR) != 0) {
                        count++;
                    }
                    break;
                case CODE_QUOT:
                    if ((flags & FLAG_QUOT) != 0) {
                        count++;
                    }
                    break;
                case CODE_APOS:
                    if ((flags & FLAG_APOS) != 0) {
                        count++;
                    }
                    break;
                case CODE_SL:
                    if ((flags & FLAG_SL) != 0) {
                        count++;
                    }
                    break;
                case CODE_BSL:
                    if ((flags & FLAG_BSL) != 0) {
                        count++;
                    }
                    break;
                case CODE_GA:
                    if ((flags & FLAG_GA) != 0) {
                        count++;
                    }
                    break;
            } // end of switch
        }
        return count;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string) {
        return charEditDistance(string, 0, string.length());
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        int distance = 0;
        for (int index = startIndex; index < endIndex; index++) {
            switch (string.charAt(index)) {
                case CODE_NUL:
                    if ((flags & FLAG_NUL) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_BEL:
                    if ((flags & FLAG_BEL) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_BS:
                    if ((flags & FLAG_BS) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_HT:
                    if ((flags & FLAG_HT) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_LF:
                    if ((flags & FLAG_LF) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_VT:
                    if ((flags & FLAG_VT) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_FF:
                    if ((flags & FLAG_FF) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_CR:
                    if ((flags & FLAG_CR) != 0) {
                        distance += 2;
                    }
                    break;
                case CODE_QUOT:
                    if ((flags & FLAG_QUOT) != 0) {
                        distance++;
                    }
                    break;
                case CODE_APOS:
                    if ((flags & FLAG_APOS) != 0) {
                        distance++;
                    }
                    break;
                case CODE_SL:
                    if ((flags & FLAG_SL) != 0) {
                        distance++;
                    }
                    break;
                case CODE_BSL:
                    if ((flags & FLAG_BSL) != 0) {
                        distance++;
                    }
                    break;
                case CODE_GA:
                    if ((flags & FLAG_GA) != 0) {
                        distance++;
                    }
                    break;
            } // end of switch
        } // end of for
        return distance;
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        write(string, 0, string.length(), out);
    }

    @Override
    public void write(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull ByteBuffer out) {
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int escape = escapeCodePoint(string.charAt(readIndex));
            if (escape != -1) {
                if (writeIndex < readIndex) {
                    out.put(string.subSequence(writeIndex, readIndex).toString().getBytes(StandardCharsets.UTF_8));
                }
                out.put((byte) '\\').put((byte) escape);
                writeIndex = readIndex + 1;
            }
        }
        if (writeIndex < endIndex) {
            out.put(string.subSequence(writeIndex, endIndex).toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        write(string, 0, string.length(), out);
    }

    @Override
    public void write(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull ByteBuf out) {
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int escape = escapeCodePoint(string.charAt(readIndex));
            if (escape != -1) {
                if (writeIndex < readIndex) {
                    out.writeCharSequence(string.subSequence(writeIndex, readIndex), StandardCharsets.UTF_8);
                }
                out.writeByte('\\').writeByte(escape);
                writeIndex = readIndex + 1;
            }
        }
        if (writeIndex < endIndex) {
            out.writeCharSequence(string.subSequence(writeIndex, endIndex), StandardCharsets.UTF_8);
        }
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        append(string, 0, string.length(), out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        LABEL:
        for (int index = startIndex; index < endIndex; index++) {
            char ch = string.charAt(index);
            switch (ch) {
                case CODE_NUL:
                    if ((flags & FLAG_NUL) != 0) {
                        out.append("\\0");
                        continue LABEL;
                    }
                    break;
                case CODE_BEL:
                    if ((flags & FLAG_BEL) != 0) {
                        out.append("\\a");
                        continue LABEL;
                    }
                    break;
                case CODE_BS:
                    if ((flags & FLAG_BS) != 0) {
                        out.append("\\b");
                        continue LABEL;
                    }
                    break;
                case CODE_HT:
                    if ((flags & FLAG_HT) != 0) {
                        out.append("\\t");
                        continue LABEL;
                    }
                    break;
                case CODE_LF:
                    if ((flags & FLAG_LF) != 0) {
                        out.append("\\n");
                        continue LABEL;
                    }
                    break;
                case CODE_VT:
                    if ((flags & FLAG_VT) != 0) {
                        out.append("\\v");
                        continue LABEL;
                    }
                    break;
                case CODE_FF:
                    if ((flags & FLAG_FF) != 0) {
                        out.append("\\f");
                        continue LABEL;
                    }
                    break;
                case CODE_CR:
                    if ((flags & FLAG_CR) != 0) {
                        out.append("\\r");
                        continue LABEL;
                    }
                    break;
                case CODE_QUOT:
                    if ((flags & FLAG_QUOT) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_APOS:
                    if ((flags & FLAG_APOS) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_SL:
                    if ((flags & FLAG_SL) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_BSL:
                    if ((flags & FLAG_BSL) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_GA:
                    if ((flags & FLAG_GA) != 0) {
                        out.append('\\');
                    }
                    break;
            } // end of switch
            out.append(ch);
        } // end of LABEL: for
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        append(string, 0, string.length(), out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        LABEL:
        for (int index = startIndex; index < endIndex; index++) {
            char ch = string.charAt(index);
            switch (ch) {
                case CODE_NUL:
                    if ((flags & FLAG_NUL) != 0) {
                        out.append("\\0");
                        continue LABEL;
                    }
                    break;
                case CODE_BEL:
                    if ((flags & FLAG_BEL) != 0) {
                        out.append("\\a");
                        continue LABEL;
                    }
                    break;
                case CODE_BS:
                    if ((flags & FLAG_BS) != 0) {
                        out.append("\\b");
                        continue LABEL;
                    }
                    break;
                case CODE_HT:
                    if ((flags & FLAG_HT) != 0) {
                        out.append("\\t");
                        continue LABEL;
                    }
                    break;
                case CODE_LF:
                    if ((flags & FLAG_LF) != 0) {
                        out.append("\\n");
                        continue LABEL;
                    }
                    break;
                case CODE_VT:
                    if ((flags & FLAG_VT) != 0) {
                        out.append("\\v");
                        continue LABEL;
                    }
                    break;
                case CODE_FF:
                    if ((flags & FLAG_FF) != 0) {
                        out.append("\\f");
                        continue LABEL;
                    }
                    break;
                case CODE_CR:
                    if ((flags & FLAG_CR) != 0) {
                        out.append("\\r");
                        continue LABEL;
                    }
                    break;
                case CODE_QUOT:
                    if ((flags & FLAG_QUOT) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_APOS:
                    if ((flags & FLAG_APOS) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_SL:
                    if ((flags & FLAG_SL) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_BSL:
                    if ((flags & FLAG_BSL) != 0) {
                        out.append('\\');
                    }
                    break;
                case CODE_GA:
                    if ((flags & FLAG_GA) != 0) {
                        out.append('\\');
                    }
                    break;
            } // end of switch
            out.append(ch);
        } // end of LABEL: for
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        print(string, 0, string.length(), out);
    }

    @Override
    public void print(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull PrintStream out) {
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            String escape = escapeSequence(string.charAt(readIndex));
            if (escape != null) {
                if (writeIndex < readIndex) {
                    out.print(string.subSequence(writeIndex, readIndex));
                }
                out.print(escape);
                writeIndex = readIndex + 1;
            }
        }
        if (writeIndex < endIndex) {
            out.print(string.subSequence(writeIndex, endIndex));
        }
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        addTokens(string, 0, string.length(), out);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull List<Object> out) {
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            String escape = escapeSequence(string.charAt(readIndex));
            if (escape != null) {
                if (writeIndex < readIndex) {
                    out.add(string.subSequence(writeIndex, readIndex).toString());
                }
                out.add(escape);
                writeIndex = readIndex + 1;
            }
        }
        if (writeIndex < endIndex) {
            out.add(string.subSequence(writeIndex, endIndex).toString());
        }
    }
}
