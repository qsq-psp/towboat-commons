package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2024/7/13.
 */
public class StrongOutputString extends OutputString {

    byte[] bytes;

    char[] chars;

    public StrongOutputString(@NotNull String string) {
        super(string);
    }

    @Override
    @NotNull
    public byte[] getBytes() {
        byte[] bytes = this.bytes;
        if (bytes == null) {
            bytes = super.getBytes();
            this.bytes = bytes;
        }
        return bytes;
    }

    @Override
    @NotNull
    public char[] getChars() {
        char[] chars = this.chars;
        if (chars == null) {
            chars = super.getChars();
            this.chars = chars;
        }
        return chars;
    }
}
