package mujica.text.format;

import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/11.
 */
@CodeHistory(date = "2026/1/11")
public class IntMapEscapeAppender extends EscapeAppender {

    @NotNull
    protected static IntMap createMap(int flags) {
        final IntMap map = new CompatibleIntMap();
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
    protected final IntMap map;

    public IntMapEscapeAppender(int flags) {
        super(flags);
        map = createMap(flags);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount(string, startIndex, endIndex) == 0;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        int count = 0;
        for (int index = startIndex; index < endIndex; index++) {
            if (map.getInt(string.charAt(index)) != 0) {
                count++;
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
            if (value != 0) {
                if (key == value) {
                    distance++;
                } else {
                    distance += 2;
                }
            }
        }
        return distance;
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getInt(key);
            if (value == 0) {
                out.append(key);
            } else {
                out.append('\\').append((char) value);
            }
        }
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            int value = map.getInt(key);
            if (value == 0) {
                out.append(key);
            } else {
                out.append('\\').append((char) value);
            }
        }
    }
}
