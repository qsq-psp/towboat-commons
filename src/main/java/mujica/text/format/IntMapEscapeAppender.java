package mujica.text.format;

import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.HexEncoder;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/11")
public class IntMapEscapeAppender extends EscapeAppender {

    @AccessStructure(online = false, local = true)
    @NotNull
    protected final IntMap map;

    @NotNull
    protected final HexEncoder encoder;

    IntMapEscapeAppender(int flags, @NotNull IntMap map, @NotNull HexEncoder encoder) {
        super(flags);
        this.map = map;
        this.encoder = encoder;
    }

    @NotNull
    protected static IntMap createMap(int flags) {
        final IntMap map = new CompatibleIntMap();
        if ((flags & FLAG_UNICODE_CONTROL) != 0) {
            for (int ch = 0x0000; ch <= 0x001f; ch++) {
                map.putInt(ch, -1); // C0 controls
            }
            for (int ch = 0x007f; ch <= 0x009f; ch++) {
                map.putInt(ch, -1); // delete and C1 controls
            }
        }
        if ((flags & FLAG_UNICODE_FORMAT) != 0) {
            map.putInt(0x00ad, -1); // soft hyphen
            for (int ch = 0x0600; ch <= 0x0605; ch++) {
                map.putInt(ch, -1); // arabic subtending/supertending marks
            }
            map.putInt(0x061c, -1); // arabic format character
            map.putInt(0x06dd, -1); // arabic quranic annotation sign
            map.putInt(0x070f, -1); // syriac format control character
            map.putInt(0x0890, -1); // arabic extended B supertending currency symbols
            map.putInt(0x0891, -1); // arabic extended B supertending currency symbols
            map.putInt(0x08e2, -1); // arabic extended A quranic annotation sign
            map.putInt(0x180e, -1); // mongolian vowel separator
            for (int ch = 0x200b; ch <= 0x200f; ch++) {
                map.putInt(ch, -1); // general punctuation format characters
            }
            for (int ch = 0x202a; ch <= 0x202e; ch++) {
                map.putInt(ch, -1); // general punctuation format characters
            }
            for (int ch = 0x2060; ch <= 0x2064; ch++) {
                map.putInt(ch, -1); // general punctuation format characters
            }
            for (int ch = 0x2066; ch <= 0x206f; ch++) {
                map.putInt(ch, -1); // general punctuation format characters
            }
            map.putInt(0xfeff, -1); // zero width no-break space
            for (int ch = 0xfff9; ch <= 0xfffb; ch++) {
                map.putInt(ch, -1); // interlinear annotation
            }
        }
        if ((flags & FLAG_HORIZONTAL_WHITESPACE) != 0) {
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
        }
        if ((flags & FLAG_VERTICAL_WHITESPACE) != 0) {
            map.putInt(0x000a, 'R'); // line feed
            map.putInt(0x000b, 'R'); // vertical tabulation
            map.putInt(0x000c, 'R'); // form feed
            map.putInt(0x000d, 'R'); // carriage return
            map.putInt(0x0085, 'R');
            map.putInt(0x2028, 'R');
            map.putInt(0x2029, 'R');
        }
        if ((flags & FLAG_WHITESPACE) != 0) {
            for (int ch = 0x0009; ch <= 0x000d; ch++) {
                map.putInt(ch, 's');
            }
            map.putInt(0x0020, 's'); // space
        }
        if ((flags & FLAG_DIGIT) != 0) {
            for (int ch = '0'; ch <= '9'; ch++) {
                map.putInt(ch, 'd');
            }
        }
        if ((flags & FLAG_REGEX) != 0) {
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
        }
        if ((flags & FLAG_SURROGATE) != 0) {
            for (int ch = Character.MIN_SURROGATE; ch <= Character.MAX_SURROGATE; ch++) {
                map.putInt(ch, -1);
            }
        }
        if ((flags & FLAG_NUL) != 0) {
            map.putInt(CODE_NUL, '0');
        }
        if ((flags & FLAG_BEL) != 0) {
            map.putInt(CODE_BEL, 'a');
        }
        if ((flags & FLAG_BS) != 0) {
            map.putInt(CODE_BS, 'b');
        }
        if ((flags & FLAG_HT) != 0) {
            map.putInt(CODE_HT, 't');
        }
        if ((flags & FLAG_LF) != 0) {
            map.putInt(CODE_LF, 'n');
        }
        if ((flags & FLAG_VT) != 0) {
            map.putInt(CODE_VT, 'v');
        }
        if ((flags & FLAG_FF) != 0) {
            map.putInt(CODE_FF, 'f');
        }
        if ((flags & FLAG_CR) != 0) {
            map.putInt(CODE_CR, 'r');
        }
        if ((flags & FLAG_QUOT) != 0) {
            map.putInt(CODE_QUOT, '"');
        }
        if ((flags & FLAG_APOS) != 0) {
            map.putInt(CODE_APOS, '\'');
        }
        if ((flags & FLAG_SL) != 0) {
            map.putInt(CODE_SL, '/');
        }
        if ((flags & FLAG_BSL) != 0) {
            map.putInt(CODE_BSL, '\\');
        }
        if ((flags & FLAG_GA) != 0) {
            map.putInt(CODE_GA, '`');
        }
        return map;
    }

    @NotNull
    public static IntMapEscapeAppender create(int flags) {
        return new IntMapEscapeAppender(flags, createMap(flags), HexEncoder.LOWER_ENCODER);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount(string, startIndex, endIndex) == 0;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        int count = 0;
        for (int index = startIndex; index < endIndex; index++) {
            int value = map.getInt(string.charAt(index));
            if (value > 0) {
                count++;
            } else if (value < 0) {
                count += 5;
            }
        }
        return count;
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
                }
            } else if (value < 0) {
                distance += 5;
            }
        }
        return distance;
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getInt(key);
            if (value > 0) {
                out.append('\\').append((char) value);
            } else if (value < 0) {
                out.append('\\').append('u');
                HexEncoder.LOWER_ENCODER.hex16(key, out);
            } else {
                out.append(key);
            }
        }
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getInt(key);
            if (value > 0) {
                out.append('\\').append((char) value);
            } else if (value < 0) {
                out.append('\\').append('u');
                HexEncoder.LOWER_ENCODER.hex16(key, out);
            } else {
                out.append(key);
            }
        }
    }
}
