package mujica.io.stream;

import mujica.io.function.Base16Case;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on 2025/4/19.
 */
public class Base16DecodeOutputStream extends FilterOutputStream implements Base16StreamingCodec {

    private int nibble = -1;

    public Base16DecodeOutputStream(@NotNull OutputStream out) {
        super(out);
    }

    @Override
    public void write(int digit) throws IOException {
        if (nibble == -1) {
            nibble = decode(digit);
        } else {
            out.write((nibble << 4) | decode(digit));
            nibble = -1;
        }
    }

    @Override
    public void write(@NotNull byte[] array, int offset, int length) throws IOException {
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            write(array[offset]);
        }
    }

    private int decode(int digit) throws IOException {
        digit &= 0xff;
        if ('0' <= digit && digit <= '9') {
            return digit - '0';
        }
        if ('a' <= digit && digit <= 'f') {
            return digit - Base16Case.LOWER;
        }
        if ('A' <= digit && digit <= 'F') {
            return digit - Base16Case.UPPER;
        }
        throw new BadCodeException(Quote.DEFAULT.apply((byte) digit) + " not in [0-9A-Fa-f]");
    }

    @Override
    public void close() throws IOException {
        if (nibble != -1) {
            throw new IOException();
        }
        out.close();
    }
}
