package mujica.text.format;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CodeHistory(date = "2026/1/7")
public class CharSequenceAppender {

    public boolean isApplicable(@NotNull CharSequence string) {
        return true;
    }

    public boolean isApplicable(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return isApplicable(string.subSequence(startIndex, endIndex));
    }

    public boolean isIdentity(@NotNull CharSequence string) {
        return true;
    }

    public boolean isIdentity(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return isIdentity(string.subSequence(startIndex, endIndex));
    }

    public int deltaCharCount(@NotNull CharSequence string) {
        return 0;
    }

    public int deltaCharCount(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    public int charEditDistance(@NotNull CharSequence string) {
        return 0;
    }

    public int charEditDistance(@NotNull CharSequence string,
                                @Index(of = "string") int startIndex,
                                @Index(of = "string", inclusive = false) int endIndex) {
        return deltaCharCount(string.subSequence(startIndex, endIndex));
    }

    public void write(@NotNull CharSequence string, @NotNull ByteBuffer out) {
        out.put(string.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void write(@NotNull CharSequence string,
                      @Index(of = "string") int startIndex,
                      @Index(of = "string", inclusive = false) int endIndex,
                      @NotNull ByteBuffer out) {
        write(string.subSequence(startIndex, endIndex), out);
    }

    public void write(@NotNull CharSequence string, @NotNull ByteBuf out) {
        out.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public void write(@NotNull CharSequence string,
                      @Index(of = "string") int startIndex,
                      @Index(of = "string", inclusive = false) int endIndex,
                      @NotNull ByteBuf out) {
        write(string.subSequence(startIndex, endIndex), out);
    }

    public void append(@NotNull CharSequence string, @NotNull StringBuilder out) {
        out.append(string);
    }

    public void append(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull StringBuilder out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    public void append(@NotNull CharSequence string, @NotNull StringBuffer out) {
        out.append(string);
    }

    public void append(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull StringBuffer out) {
        append(string.subSequence(startIndex, endIndex), out);
    }

    public void print(@NotNull CharSequence string, @NotNull PrintStream out) {
        out.print(string); // append is also OK
    }

    public void print(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull PrintStream out) {
        print(string.subSequence(startIndex, endIndex), out);
    }

    /**
     * @param out accepted types:
     *            String
     *            char[]
     *            Boolean
     *            Integer for CodePoint
     *            Character
     *            Long
     */
    public void addTokens(@NotNull CharSequence string, @NotNull List<Object> out) {
        out.add(string.toString());
    }

    public void addTokens(@NotNull CharSequence string,
                       @Index(of = "string") int startIndex,
                       @Index(of = "string", inclusive = false) int endIndex,
                       @NotNull List<Object> out) {
        addTokens(string.subSequence(startIndex, endIndex), out);
    }
}
