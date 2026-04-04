package mujica.text.sanitizer;

import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.ds.of_int.list.CopyOnWriteIntList;
import mujica.ds.of_int.list.IntAscendingBubbleSort;
import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.ds.of_int.map.IntMap;
import mujica.ds.of_int.map.IterableIntMap;
import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.HexadecimalAppender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2026/1/11")
public class IntMapEscapeAppender extends CharSequenceAppender {

    private static final int HEX2 = 0x20000 | 'x';

    private static final int HEX4 = 0x40000 | 'u';

    @AccessStructure(online = false, local = true)
    @NotNull
    protected final IntMap map;

    @NotNull
    protected final HexadecimalAppender hex;

    IntMapEscapeAppender(@NotNull IntMap map, @NotNull HexadecimalAppender hex) {
        super();
        this.map = map;
        this.hex = hex;
    }

    public IntMapEscapeAppender() {
        this(new CompatibleIntMap(), new HexadecimalAppender(false, true));
    }

    @NotNull
    private IntMapEscapeAppender escapeControl(int value) {
        for (int ch = 0x0000; ch <= 0x001f; ch++) {
            map.putInt(ch, value); // C0 controls
        }
        for (int ch = 0x007f; ch <= 0x009f; ch++) {
            map.putInt(ch, value); // delete and C1 controls
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
        map.putInt(0x00ad, value); // soft hyphen
        for (int ch = 0x0600; ch <= 0x0605; ch++) {
            map.putInt(ch, HEX4); // arabic subtending / supertending marks
        }
        map.putInt(0x061c, HEX4); // arabic format character
        map.putInt(0x06dd, HEX4); // arabic quranic annotation sign
        map.putInt(0x070f, HEX4); // syriac format control character
        map.putInt(0x0890, HEX4); // arabic extended B supertending currency symbols
        map.putInt(0x0891, HEX4); // arabic extended B supertending currency symbols
        map.putInt(0x08e2, HEX4); // arabic extended A quranic annotation sign
        map.putInt(0x180e, HEX4); // mongolian vowel separator
        for (int ch = 0x200b; ch <= 0x200f; ch++) {
            map.putInt(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x202a; ch <= 0x202e; ch++) {
            map.putInt(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x2060; ch <= 0x2064; ch++) {
            map.putInt(ch, HEX4); // general punctuation format characters
        }
        for (int ch = 0x2066; ch <= 0x206f; ch++) {
            map.putInt(ch, HEX4); // general punctuation format characters
        }
        map.putInt(0xfeff, HEX4); // zero width no-break space; byte order mark
        for (int ch = 0xfff9; ch <= 0xfffb; ch++) {
            map.putInt(ch, HEX4); // interlinear annotation
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
        map.putInt(0xfffc, HEX4);
        map.putInt(0xfffd, HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeHorizontalWhitespaceH() {
        map.putInt(0x0009, 'h'); // horizontal tabulation
        map.putInt(0x0020, 'h'); // space
        map.putInt(0x00a0, 'h'); // no-break space
        map.putInt(0x1680, 'h');
        map.putInt(0x180e, 'h');
        for (int ch = 0x2000; ch < 0x200a; ch++) {
            map.putInt(ch, 'h');
        }
        map.putInt(0x202f, 'h');
        map.putInt(0x205f, 'h');
        map.putInt(0x3000, 'h');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeVerticalWhitespaceR() {
        map.putInt(0x000a, 'R'); // line feed
        map.putInt(0x000b, 'R'); // vertical tabulation
        map.putInt(0x000c, 'R'); // form feed
        map.putInt(0x000d, 'R'); // carriage return
        map.putInt(0x0085, 'R');
        map.putInt(0x2028, 'R');
        map.putInt(0x2029, 'R');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeWhitespaceU() {
        map.putInt(0x0009, HEX4); // horizontal tabulation
        map.putInt(0x000a, HEX4); // line feed
        map.putInt(0x000b, HEX4); // vertical tabulation
        map.putInt(0x000c, HEX4); // form feed
        map.putInt(0x000d, HEX4); // carriage return
        map.putInt(0x0020, HEX4); // space
        map.putInt(0x0085, HEX4);
        map.putInt(0x00a0, HEX4); // no-break space
        map.putInt(0x1680, HEX4);
        map.putInt(0x180e, HEX4);
        for (int ch = 0x2000; ch < 0x200a; ch++) {
            map.putInt(ch, HEX4);
        }
        map.putInt(0x2028, HEX4);
        map.putInt(0x2029, HEX4);
        map.putInt(0x202f, HEX4);
        map.putInt(0x205f, HEX4);
        map.putInt(0x3000, HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeDigitD() {
        for (int ch = '0'; ch <= '9'; ch++) {
            map.putInt(ch, 'd');
        }
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeRegex() {
        map.putInt('$', '$');
        map.putInt('(', '(');
        map.putInt(')', ')');
        map.putInt('*', '*');
        map.putInt('+', '+');
        map.putInt('.', '.');
        map.putInt('?', '?');
        map.putInt('[', '[');
        map.putInt('\\', '\\');
        map.putInt(']', ']');
        map.putInt('|', '|');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeNTR() {
        map.putInt('\n', 'n');
        map.putInt('\t', 't');
        map.putInt('\r', 'r');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeZero() {
        map.putInt(0x00, '0');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBell() {
        map.putInt(0x07, 'a');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackspace() {
        map.putInt('\b', 'b');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeVerticalTabulation() {
        map.putInt(0x0b, 'v');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeFormFeed() {
        map.putInt('\f', 'f');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeEndOfFile() {
        map.putInt(0x1a, 'Z');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeQuotationMark() {
        map.putInt('"', '"');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeApostrophe() {
        map.putInt('\'', '\'');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeGraveAccent() { // also named backtick
        map.putInt('`', '`');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeSlash() {
        map.putInt('/', '/');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlash() {
        map.putInt('\\', '\\');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlashX() {
        map.putInt('\\', HEX2);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeBackSlashU() {
        map.putInt('\\', HEX4);
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapePercent() {
        map.putInt('%', '%');
        return this;
    }

    @NotNull
    public IntMapEscapeAppender escapeUnderscore() {
        map.putInt('_', '_');
        return this;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return isIdentity(string, 0, string.length());
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            int value = map.getInt(string.charAt(index));
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
            int value = map.getInt(string.charAt(index));
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
            int value = map.getInt(key);
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
            int value = map.getInt(key);
            if (value > 0) {
                if (value == HEX2) {
                    int position = out.length();
                    hex.acceptByte((byte) key, out);
                    out.setCharAt(position, '\\');
                } else if (value == HEX4) {
                    int position = out.length();
                    hex.acceptCharacter(key, out);
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

    public static void main(String[] args) {
        final IntMapEscapeAppender appender = new IntMapEscapeAppender();
        appender.escapeFormatU();
        final IntList intList = new CopyOnResizeIntList(null);
        ((IterableIntMap) appender.map).forEachKey(intList::offerLast);
        intList.sort(IntAscendingBubbleSort.INSTANCE);
        for (int ch : intList) {
            System.out.printf("case 0x%04x: ", ch);
        }
    }
}
