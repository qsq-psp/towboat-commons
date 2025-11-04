package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@CodeHistory(date = "2025/4/18")
public class Base16EncodeOutputStream extends FilterOutputStream implements Base16StreamingEncoder {

    private int alphabetOffset;

    protected Base16EncodeOutputStream(@NotNull OutputStream out, @MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset) {
        super(out);
        this.alphabetOffset = alphabetOffset;
    }

    public Base16EncodeOutputStream(@NotNull OutputStream out) {
        this(out, Base16Case.LOWER);
    }

    @Override
    public boolean isUpperCase() {
        return alphabetOffset == UPPER;
    }

    @Override
    public void setUpperCase(boolean upper) {
        if (upper) {
            alphabetOffset = UPPER;
        } else {
            alphabetOffset = LOWER;
        }
    }

    @Override
    public void write(int octet) throws IOException {
        encode(octet >> 4);
        encode(octet);
    }

    @Override
    public void write(@NotNull byte[] array, int offset, int length) throws IOException {
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            write(array[offset]);
        }
    }

    private void encode(int value) throws IOException {
        value &= 0xf;
        if (value < 0xa) {
            value += '0';
        } else {
            value += alphabetOffset;
        }
        out.write(value);
    }
}
