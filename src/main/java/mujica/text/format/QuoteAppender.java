package mujica.text.format;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CodeHistory(date = "2026/1/8")
public class QuoteAppender extends CharSequenceAppender {

    @NotNull
    protected final String prefix;
    
    @NotNull
    protected final String suffix;

    protected final int deltaCharCount;

    public QuoteAppender(@NotNull String prefix, @NotNull String suffix) {
        super();
        this.prefix = prefix;
        this.suffix = suffix;
        deltaCharCount = prefix.length() + suffix.length();
        if (deltaCharCount <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isApplicable(@NotNull CharSequence string) {
        return string.toString().contains(suffix);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return false;
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        return false;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string) {
        return deltaCharCount;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string) {
        return deltaCharCount;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount;
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        out.put(prefix.getBytes(StandardCharsets.UTF_8));
        out.put(string.toString().getBytes(StandardCharsets.UTF_8));
        out.put(suffix.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull ByteBuf out) {
        out.writeCharSequence(prefix, StandardCharsets.UTF_8);
        out.writeCharSequence(string, StandardCharsets.UTF_8);
        out.writeCharSequence(suffix, StandardCharsets.UTF_8);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        out.append(prefix).append(string).append(suffix);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        out.append(prefix).append(string, startIndex, endIndex).append(suffix);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        out.append(prefix).append(string).append(suffix);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        out.append(prefix).append(string, startIndex, endIndex).append(suffix);
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(prefix);
        out.print(string);
        out.print(suffix);
    }

    @Override
    public void print(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull PrintStream out) {
        out.print(prefix);
        out.print(string.subSequence(startIndex, endIndex));
        out.print(suffix);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        out.add(prefix);
        out.add(string.toString());
        out.add(suffix);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull List<Object> out) {
        out.add(prefix);
        out.add(string.subSequence(startIndex, endIndex).toString());
        out.add(suffix);
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string) {
        return prefix + string + suffix;
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string, int startIndex, int endIndex) {
        return prefix + string.subSequence(startIndex, endIndex) + suffix;
    }
}
