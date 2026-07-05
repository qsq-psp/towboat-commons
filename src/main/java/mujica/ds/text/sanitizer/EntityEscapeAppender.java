package mujica.ds.text.sanitizer;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.IntPredicate;

@CodeHistory(date = "2019/5/17", project = "coo", name = "CharacterEntityEscape")
@CodeHistory(date = "2021/3/29", project = "webbiton", name = "HtmlEncoder")
@CodeHistory(date = "2026/3/18")
public class EntityEscapeAppender extends CharSequenceAppender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityEscapeAppender.class);

    @AccessStructure(online = false, local = true)
    @NotNull
    protected final HashMap<Character, String> map;

    public EntityEscapeAppender() {
        super();
        map = new HashMap<>();
        map.put('<', "&lt;");
        map.put('>', "&gt;");
        map.put('&', "&amp;");
        map.put('"', "&quot;");
    }

    public void load(@Nullable IntPredicate filter, boolean ifAbsent) {
        if (filter == null) {
            filter = ch -> true;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                EntityEscapeAppender.class.getResourceAsStream("entity-escape.txt"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                int spaceIndex = line.indexOf(' ');
                try {
                    int ch = Integer.parseUnsignedInt(line, 0, spaceIndex, 10);
                    if (ch <= Character.MAX_VALUE && filter.test(ch)) {
                        String str = '&' + line.substring(spaceIndex + 1) + ';';
                        if (ifAbsent) {
                            map.putIfAbsent((char) ch, str);
                        } else {
                            map.put((char) ch, str);
                        }
                    }
                } catch (RuntimeException e) {
                    LOGGER.warn("{}", line, e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            String value = map.get(string.charAt(index));
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        int count = 0;
        for (int index = startIndex; index < endIndex; index++) {
            String value = map.get(string.charAt(index));
            if (value != null) {
                count += value.length() - 1;
            }
        }
        return count;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        int distance = 0;
        for (int index = startIndex; index < endIndex; index++) {
            char key = string.charAt(index);
            String value = map.get(key);
            if (value != null) {
                distance += value.length();
                if (key == '&') {
                    distance--;
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
            String value = map.get(key);
            if (value != null) {
                out.append(value);
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
            String value = map.get(key);
            if (value != null) {
                out.append(value);
            } else {
                out.append(key);
            }
        }
    }
}
