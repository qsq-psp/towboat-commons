package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CodeHistory(date = "2026/1/10")
public class PatternEscapeAppender extends CharSequenceAppender {

    @NotNull
    protected final Pattern pattern; // add slash before each match

    public PatternEscapeAppender(@NotNull Pattern pattern) {
        super();
        this.pattern = pattern;
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
            distance++;
        }
        return distance;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    protected boolean appendOnMatch(@NotNull CharSequence string, @NotNull StringBuilder out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        boolean matches = false;
        while (matcher.find()) {
            int start = matcher.start();
            if (index < start) {
                out.append(string, index, start);
            }
            out.append('\\');
            index = start;
            matches = true;
        }
        if (matches) {
            if (index < string.length()) {
                out.append(string, index, string.length());
            }
            return true;
        }
        return false;
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        pattern.matcher(string).appendReplacement(out, "\\\\$1");
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    protected boolean appendOnMatch(@NotNull CharSequence string, @NotNull StringBuffer out) {
        final Matcher matcher = pattern.matcher(string);
        int index = 0;
        boolean matches = false;
        while (matcher.find()) {
            int start = matcher.start();
            if (index < start) {
                out.append(string, index, start);
            }
            out.append('\\');
            index = start;
            matches = true;
        }
        if (matches) {
            if (index < string.length()) {
                out.append(string, index, string.length());
            }
            return true;
        }
        return false;
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        pattern.matcher(string).appendReplacement(out, "\\\\$1");
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(pattern.matcher(string).replaceAll("\\\\$1")); // use StringBuilder internally
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
            if (index < matcher.start()) {
                out.add(string.subSequence(index, matcher.start()).toString());
            }
            out.add("\\" + matcher.group());
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
}
