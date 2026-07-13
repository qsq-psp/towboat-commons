package mujica.ds.text.sanitizer;

import mujica.ds.i32.map.JdkI32Map;
import mujica.ds.i32.map.I32Map;
import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.ds.text.number.Base16Appender;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/11")
public class IntMapEscapeAppender extends CharSequenceAppender {

    private static final int HEX2 = 0x20000 | 'x';

    private static final int HEX4 = 0x40000 | 'u';

    @AccessStructure(online = false, local = true)
    @NotNull
    protected final I32Map map;

    @NotNull
    protected final Base16Appender hex;

    IntMapEscapeAppender(@NotNull I32Map map, @NotNull Base16Appender hex) {
        super();
        this.map = map;
        this.hex = hex;
    }

    public IntMapEscapeAppender(@NotNull IntMapEscapeAppender that, boolean upperCase) {
        this(that.map, new Base16Appender(upperCase, true));
    }

    public IntMapEscapeAppender(boolean upperCase) {
        this(new JdkI32Map(), new Base16Appender(upperCase, true));
    }

    public IntMapEscapeAppender() {
        this(false);
    }

    @NotNull
    private IntMapEscapeAppender escapeControl(int value) {
        for (int ch = 0x0000; ch <= 0x001f; ch++) {
            map.putI32(ch, value); // C0 controls
        }
        for (int ch = 0x007f; ch <= 0x009f; ch++) {
            map.putI32(ch, value); // delete and C1 controls
        }
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeControlX() {
        return escapeControl(HEX2);
    }

    @NotNull
    public IntMapEscapeAppender escapeControlU() {
        return escapeControl(HEX4);
    }

    @NotNull
    private IntMapEscapeAppender escapeFormat(int value) {
        map.putI32(0x00ad, value); // soft hyphen
        for (int ch = 0x0600; ch <= 0x0605; ch++) {
            map.putI32(ch, HEX4); // arabic subtending / supertending marks
        }
        map.putI32(0x061c, HEX4); // arabic format character
        map.putI32(0x06dd, HEX4); // arabic quranic annotation sign
        map.putI32(0x070f, HEX4); // syriac format control character
        map.putI32(0x0890, HEX4); // arabic extended B supertending currency symbols
        map.putI32(0x0891, HEX4); // arabic extended B supertending currency symbols
        map.putI32(0x08e2, HEX4); // arabic extended A quranic annotation sign
        map.putI32(0x180e, HEX4); // mongolian vowel separator
        for (int ch = 0x200b; ch <= 0x200f; ch++) {
            map.putI32(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x202a; ch <= 0x202e; ch++) {
            map.putI32(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x2060; ch <= 0x2064; ch++) {
            map.putI32(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x2066; ch <= 0x206f; ch++) {
            map.putI32(ch, HEX4); // general punctuation format characters
        }
        map.putI32(0xfeff, HEX4); // zero width no-break space; byte order mark
        for (int ch = 0xfff9; ch <= 0xfffb; ch++) {
            map.putI32(ch, HEX4); // interlinear annotation
        }
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeFormatX() {
        return escapeFormat(HEX2);
    }

    @NotNull
    public IntMapEscapeAppender escapeFormatU() {
        return escapeFormat(HEX4);
    }

    @NotNull
    public IntMapEscapeAppender escapeReplacementU() {
        map.putI32(0xfffc, HEX4);
        map.putI32(0xfffd, HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeHorizontalWhitespaceH() {
        map.putI32(0x0009, 'h'); // horizontal tabulation
        map.putI32(0x0020, 'h'); // space
        map.putI32(0x00a0, 'h'); // no-break space
        map.putI32(0x1680, 'h');
        map.putI32(0x180e, 'h');
        for (int ch = 0x2000; ch < 0x200a; ch++) {
            map.putI32(ch, 'h');
        }
        map.putI32(0x202f, 'h');
        map.putI32(0x205f, 'h');
        map.putI32(0x3000, 'h');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeVerticalWhitespaceR() {
        map.putI32(0x000a, 'R'); // line feed
        map.putI32(0x000b, 'R'); // vertical tabulation
        map.putI32(0x000c, 'R'); // form feed
        map.putI32(0x000d, 'R'); // carriage return
        map.putI32(0x0085, 'R');
        map.putI32(0x2028, 'R');
        map.putI32(0x2029, 'R');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeWhitespaceU() {
        map.putI32(0x0009, HEX4); // horizontal tabulation
        map.putI32(0x000a, HEX4); // line feed
        map.putI32(0x000b, HEX4); // vertical tabulation
        map.putI32(0x000c, HEX4); // form feed
        map.putI32(0x000d, HEX4); // carriage return
        map.putI32(0x0020, HEX4); // space
        map.putI32(0x0085, HEX4);
        map.putI32(0x00a0, HEX4); // no-break space
        map.putI32(0x1680, HEX4);
        map.putI32(0x180e, HEX4);
        for (int ch = 0x2000; ch < 0x200a; ch++) {
            map.putI32(ch, HEX4);
        }
        map.putI32(0x2028, HEX4);
        map.putI32(0x2029, HEX4);
        map.putI32(0x202f, HEX4);
        map.putI32(0x205f, HEX4);
        map.putI32(0x3000, HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeDigitD() {
        for (int ch = '0'; ch <= '9'; ch++) {
            map.putI32(ch, 'd');
        }
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeRegex() {
        map.putI32('$', '$');
        map.putI32('(', '(');
        map.putI32(')', ')');
        map.putI32('*', '*');
        map.putI32('+', '+');
        map.putI32('.', '.');
        map.putI32('?', '?');
        map.putI32('[', '[');
        map.putI32('\\', '\\');
        map.putI32(']', ']');
        map.putI32('|', '|');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeNTR() {
        map.putI32('\n', 'n');
        map.putI32('\t', 't');
        map.putI32('\r', 'r');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeZero() {
        map.putI32(0x00, '0');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBell() {
        map.putI32(0x07, 'a');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackspace() {
        map.putI32('\b', 'b');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeVerticalTabulation() {
        map.putI32(0x0b, 'v');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeFormFeed() {
        map.putI32('\f', 'f');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeEndOfFile() {
        map.putI32(0x1a, 'Z');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeQuotationMark() {
        map.putI32('"', '"');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeApostrophe() {
        map.putI32('\'', '\'');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeGraveAccent() { // also named backtick
        map.putI32('`', '`');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeSlash() {
        map.putI32('/', '/');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlash() {
        map.putI32('\\', '\\');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlashX() {
        map.putI32('\\', HEX2);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlashU() {
        map.putI32('\\', HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapePercent() {
        map.putI32('%', '%');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeUnderscore() {
        map.putI32('_', '_');
        return this;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return isIdentity(string, 0, string.length());
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            int value = map.getI32(string.charAt(index));
            if (value > 0) {
                return false;
            }
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
            int value = map.getI32(string.charAt(index));
            if (value > 0) {
                count++;
                if (value == HEX2) {
                    count += 2;
                } else if (value == HEX4) {
                    count += 4;
                }
            }
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
            int key = string.charAt(index);
            int value = map.getI32(key);
            if (value > 0) {
                if (key == value) {
                    distance++;
                } else {
                    distance += 2;
                    if (value == HEX2) {
                        distance += 2;
                    } else if (value == HEX4) {
                        distance += 4;
                    }
                }
            }
        }
        return distance;
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        append(string, 0, string.length(), out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getI32(key);
            if (value > 0) {
                if (value == HEX2) {
                    int position = out.length();
                    hex.append((byte) key, out);
                    out.setCharAt(position, '\\');
                } else if (value == HEX4) {
                    int position = out.length();
                    hex.append(key, out);
                    out.setCharAt(position, '\\');
                    out.setCharAt(position + 1, 'u');
                } else {
                    out.append('\\').append((char) value);
                }
            } else {
                out.append(key);
            }
        }
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        append(string, 0, string.length(), out);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getI32(key);
            if (value > 0) {
                if (value == HEX2) {
                    int position = out.length();
                    hex.append((byte) key, out);
                    out.setCharAt(position, '\\');
                } else if (value == HEX4) {
                    int position = out.length();
                    hex.append(key, out);
                    out.setCharAt(position, '\\');
                    out.setCharAt(position + 1, 'u');
                } else {
                    out.append('\\').append((char) value);
                }
            } else {
                out.append(key);
            }
        }
    }
}
