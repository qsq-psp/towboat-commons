package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/4/19")
public class Base16DecodeInputStream extends FilterInputStream implements Base16StreamingCodec {

    public Base16DecodeInputStream(@NotNull InputStream in) {
        super(in);
    }

    @Override
    @DataType("u8+{-1}")
    public int read() throws IOException {
        return decode(true) | decode(false);
    }

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

    private int decode(boolean shift) throws IOException {
        int digit = in.read();
        if ('0' <= digit && digit <= '9') {
            digit -= '0';
        } else if ('a' <= digit && digit <= 'f') {
            digit -= Base16Case.LOWER;
        } else if ('A' <= digit && digit <= 'F') {
            digit -= Base16Case.UPPER;
        } else if (digit == -1) {
            return -1;
        } else {
            throw new BadCodeException(Quote.DEFAULT.apply((byte) digit) + " not in [0-9A-Fa-f]");
        }
        if (shift) {
            digit <<= 4;
        }
        return digit;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public long skip(long n) throws IOException {
        long m;
        if (n <= (Long.MAX_VALUE >> 1)) {
            m = in.skip(n << 1);
        } else {
            m = in.skip(n);
            if (m != 0) {
                m += in.skip(n);
            }
        }
        if ((m & 1) != 0) {
            in.read();
        }
        return m >> 1;
    }

    @Override
    public int available() throws IOException {
        return in.available() >> 1;
    }

    @Override
    public synchronized void mark(int readAheadLimit) {
        in.mark(Math.multiplyExact(readAheadLimit, 2));
    }
}
