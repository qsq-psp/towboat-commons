package mujica.io.compress;

import mujica.io.hash.Adler32;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/10/27", name = "TowboatZlibInputStream")
@CodeHistory(date = "2025/11/12")
@Stable(date = "2025/11/28")
@ReferencePage(title = "ZLIB Compressed Data Format Specification version 3.3", href = "https://www.rfc-editor.org/rfc/rfc1950.html")
public class WrapperZlibInputStream extends FilterInputStream {

    @NotNull
    public static WrapperZlibInputStream create(@NotNull InputStream in, @NotNull AbstractInflateInputStream.Constructor2 constructor) throws IOException {
        final int head = (in.read() << Byte.SIZE) | in.read();
        if (head == -1) { // any of the two bytes is -1
            throw new EOFException();
        }
        if (head % 31 != 0) {
            throw new CompressAlgorithmException("header check");
        }
        if ((head & 0x0f00) != 0x0800) {
            throw new CompressAlgorithmException("compression method = " + ((head >> 8) & 0xf));
        }
        int compressionInfo = (head >> 12) & 0xf;
        if (compressionInfo >= 8) {
            throw new CompressAlgorithmException("compression info = " + compressionInfo);
        }
        if ((head & 0x0020) != 0) {
            throw new CompressAlgorithmException("preset dictionary");
        }
        // compression level is ignored
        return new WrapperZlibInputStream(constructor.apply(in, 1 << (compressionInfo + 8)));
    }

    private Adler32 adler32;

    private WrapperZlibInputStream(@NotNull AbstractInflateInputStream in) {
        super(in);
        adler32 = new Adler32();
        adler32.start();
    }

    private void check() throws IOException {
        final int expected = adler32.getAsInt();
        adler32 = null;
        ((AbstractInflateInputStream) in).trailingBytesMode();
        int actual = 0;
        for (int shift = Integer.SIZE - Byte.SIZE; shift >= 0; shift -= Byte.SIZE) {
            int octet = in.read();
            if (octet == -1) {
                throw new EOFException();
            }
            actual |= octet << shift;
        }
        if (expected != actual) {
            throw new CompressAlgorithmException("0x" + Integer.toHexString(expected) + " 0x" + Integer.toHexString(actual));
        }
    }

    @Override
    @DataType("u8+{-1}")
    public int read() throws IOException {
        if (adler32 != null) {
            int value = in.read();
            if (value != -1) {
                adler32.update((byte) value);
                return value;
            } else {
                check();
            }
        }
        return -1;
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (adler32 != null) {
            length = in.read(array, offset, length);
            if (length != -1) {
                adler32.update(array, offset, length);
                return length;
            } else {
                check();
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "WrapperZlibInputStream[in = " + in + "]";
    }
}
