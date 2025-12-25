package mujica.io.codec;

import mujica.math.algebra.discrete.ClampedMath;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/4/18")
public class Base16EncodeInputStream extends FilterInputStream implements Base16StreamingEncoder {

    private int alphabetOffset;

    private int nibble = -1;

    private int markNibble = -1;

    protected Base16EncodeInputStream(@NotNull InputStream in, @MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset) {
        super(in);
        this.alphabetOffset = alphabetOffset;
    }

    public Base16EncodeInputStream(@NotNull InputStream in) {
        this(in, Base16Case.LOWER);
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
    @DataType("u8+{-1}")
    public int read() throws IOException {
        int digit;
        if (nibble == -1) {
            int octet = in.read();
            if (octet == -1) {
                return -1;
            }
            digit = encode(octet >> 4);
            nibble = octet;
        } else {
            digit = encode(nibble);
            nibble = -1;
        }
        return digit;
    }

    /**
     * Needed only in FilterInputStream, not FilterOutputStream
     */
    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        int count = 0;
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            int value = read();
            if (value == -1) {
                if (count == 0) {
                    count = -1;
                }
                break;
            }
            array[offset] = (byte) value;
            count++;
        }
        return count;
    }

    private int encode(int value) {
        value &= 0xf;
        if (value < 0xa) {
            value += '0';
        } else {
            value += alphabetOffset;
        }
        return value;
    }

    @Override
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0L;
        }
        long m = 0;
        if (nibble != -1) {
            m++;
            n--;
        }
        m += in.skip(n >> 1) << 1;
        if ((n & 1L) != 0) {
            nibble = in.read();
        } else {
            nibble = -1;
        }
        if (nibble != -1) {
            m++;
        }
        return m;
    }

    @Override
    public int available() throws IOException {
        final ClampedMath math = ClampedMath.INSTANCE;
        int count = math.multiply(in.available(), 2);
        if (nibble != -1) {
            count = math.increment(count);
        }
        return count;
    }

    @Override
    public synchronized void mark(int readAheadLimit) {
        in.mark(readAheadLimit >> 1);
        markNibble = nibble;
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        nibble = markNibble;
    }
}
