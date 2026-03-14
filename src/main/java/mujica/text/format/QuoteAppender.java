package mujica.text.format;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.collation.Bracket;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CodeHistory(date = "2026/1/8")
public class QuoteAppender extends CharSequenceAppender {

    @NotNull
    private final CharSequenceAppender center;

    @NotNull
    private String left;
    
    @NotNull
    private String right;

    private final int deltaCharCount;

    private boolean checkEnabled;

    public QuoteAppender(@NotNull CharSequenceAppender center, @NotNull String left, @NotNull String right) {
        super();
        this.center = center;
        this.left = left;
        this.right = right;
        deltaCharCount = left.length() + right.length();
        if (deltaCharCount <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public QuoteAppender(@NotNull String left, @NotNull String right) {
        this(new CharSequenceAppender(), left, right);
    }

    public QuoteAppender(@NotNull CharSequenceAppender center, @NotNull Bracket bracket) {
        this(center, bracket.left, bracket.right);
    }

    public QuoteAppender(@NotNull CharSequenceAppender center, @NotNull String around) {
        this(center, around, around);
    }

    @NotNull
    public String getLeft() {
        return left;
    }

    @NotNull
    public QuoteAppender setLeft(@NotNull String left) {
        this.left = left;
        return this;
    }

    @NotNull
    public String getRight() {
        return right;
    }

    @NotNull
    public QuoteAppender setRight(@NotNull String right) {
        this.right = right;
        return this;
    }

    public boolean isCheckEnabled() {
        return checkEnabled;
    }

    @NotNull
    public QuoteAppender setCheckEnabled(boolean checkEnabled) {
        this.checkEnabled = checkEnabled;
        return this;
    }

    @Override
    public boolean isApplicable(@NotNull CharSequence string) {
        if (checkEnabled && string.toString().contains(right)) {
            return false;
        }
        return center.isApplicable(string);
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
        return deltaCharCount + center.deltaCharCount(string);
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount + center.deltaCharCount(string, startIndex, endIndex);
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string) {
        return deltaCharCount + center.charEditDistance(string);
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        return deltaCharCount + center.charEditDistance(string, startIndex, endIndex);
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        out.put(left.getBytes(StandardCharsets.UTF_8));
        center.write(string, out);
        out.put(right.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        out.writeCharSequence(left, StandardCharsets.UTF_8);
        center.write(string, out);
        out.writeCharSequence(right, StandardCharsets.UTF_8);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        out.append(left);
        center.append(string, out);
        out.append(right);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuilder out) {
        out.append(left);
        center.append(string, startIndex, endIndex, out);
        out.append(right);
    }

    @Override
    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        out.append(left);
        center.append(string, out);
        out.append(right);
    }

    @Override
    public void append(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull StringBuffer out) {
        out.append(left);
        center.append(string, startIndex, endIndex, out);
        out.append(right);
    }

    @Override
    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(left);
        center.print(string, out);
        out.print(right);
    }

    @Override
    public void print(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull PrintStream out) {
        out.print(left);
        center.print(string, startIndex, endIndex, out);
        out.print(right);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        out.add(left);
        center.addTokens(string, out);
        out.add(right);
    }

    @Override
    public void addTokens(@NotNull CharSequence string, int startIndex, int endIndex, @NotNull List<Object> out) {
        out.add(left);
        center.addTokens(string, startIndex, endIndex, out);
        out.add(right);
    }
}
