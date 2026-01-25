package mujica.text.format;

import mujica.io.codec.Base16Case;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2026/1/10.
 */
public class PatternEscapeAppender extends EscapeAppender {

    protected static void convertFlag(@NotNull char[] chars, int index, int code) {
        chars[index++] = '\\';
        chars[index++] = 'u';
        for (int shift = 12; shift >= 0; shift -= 4) {
            int nibble = 0xf & (code >> shift);
            if (nibble < 0xa) {
                nibble += '0';
            } else {
                nibble += Base16Case.LOWER;
            }
            chars[index++] = (char) nibble;
        }
    }

    @NotNull
    protected static Pattern createPattern(int flags) {
        final int bitCount = Integer.bitCount(flags);
        if (bitCount == 0) {
            throw new IllegalArgumentException();
        }
        int index = 6 * bitCount;
        if (bitCount != 1) {
            index += 2;
        }
        final char[] patternChars = new char[index];
        if (bitCount != 1) {
            patternChars[index++] = '[';
        }
        if ((flags & FLAG_NUL) != 0) {
            convertFlag(patternChars, index, CODE_NUL);
            index += 6;
        }
        if ((flags & FLAG_BEL) != 0) {
            convertFlag(patternChars, index, CODE_BEL);
            index += 6;
        }
        if (bitCount != 1) {
            patternChars[index++] = ']';
        }
        return Pattern.compile(new String(patternChars, 0, index));
    }

    @NotNull
    protected final Pattern pattern;

    public PatternEscapeAppender(int flags) {
        super(flags);
        pattern = createPattern(flags);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return !pattern.matcher(string).find();
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        return isIdentity(string.subSequence(startIndex, endIndex));
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string) {
        int count = 0;
        final Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string) {
        int distance = 0;
        final Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (string.charAt(matcher.start()) < 0x20) { // the space character
                distance += 2;
            } else {
                distance++;
            }
        }
        return distance;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        while (matcher.find()) {
            String escape = escapeSequence(string.charAt(matcher.start()));
            if (escape == null) {
                continue;
            }
            if (index < matcher.start()) {
                out.append(string, index, matcher.start());
            }
            out.append(escape);
            index = matcher.end();
        }
        if (index < string.length()) {
            out.append(string, index, string.length());
        }
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        while (matcher.find()) {
            String escape = escapeSequence(string.charAt(matcher.start()));
            if (escape == null) {
                continue;
            }
            if (index < matcher.start()) {
                out.append(string, index, matcher.start());
            }
            out.append(escape);
            index = matcher.end();
        }
        if (index < string.length()) {
            out.append(string, index, string.length());
        }
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        while (matcher.find()) {
            String escape = escapeSequence(string.charAt(matcher.start()));
            if (escape == null) {
                continue;
            }
            if (index < matcher.start()) {
                out.print(string.subSequence(index, matcher.start()));
            }
            out.print(escape);
            index = matcher.end();
        }
        if (index < string.length()) {
            out.print(string.subSequence(index, string.length()));
        }
    }

    @Override
    public void print(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull PrintStream out) {
        print(string.subSequence(startIndex, endIndex), out);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        while (matcher.find()) {
            String escape = escapeSequence(string.charAt(matcher.start()));
            if (escape == null) {
                continue;
            }
            if (index < matcher.start()) {
                out.add(string.subSequence(index, matcher.start()).toString());
            }
            out.add(escape);
            index = matcher.end();
        }
        if (index < string.length()) {
            out.add(string.subSequence(index, string.length()).toString());
        }
    }

    @Override
    public void addTokens(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull List<Object> out) {
        addTokens(string.subSequence(startIndex, endIndex), out);
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string) {
        return pattern.matcher(string).replaceAll(result ->
                Objects.requireNonNullElse(escapeSequence(string.charAt(result.start())), "")
        ); // use StringBuilder internally
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string, int startIndex, int endIndex) {
        return apply(string.subSequence(startIndex, endIndex));
    }
}
