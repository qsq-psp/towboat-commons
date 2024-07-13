package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

/**
 * Created on 2024/7/13.
 */
public class OutputString extends AbstractPreparedString implements CharSequence {

    @NotNull
    final String string;

    public OutputString(@NotNull String string) {
        super(PredictCharacteristic.computeFlags(string));
        this.string = string;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @Override
    @NotNull
    public String subSequence(int start, int end) {
        return string.substring(start, end);
    }

    @Override
    @NotNull
    public String toString() {
        return string;
    }

    @Override
    @NotNull
    public IntStream chars() {
        return string.chars();
    }

    @Override
    @NotNull
    public IntStream codePoints() {
        return string.codePoints();
    }

    @NotNull
    protected byte[] getBytes() {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    @NotNull
    protected char[] getChars() {
        return string.toCharArray();
    }
}
