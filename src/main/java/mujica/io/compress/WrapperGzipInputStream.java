package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.io.hash.SimpleIntSizedCRC;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/10/7")
@Stable(date = "2025/11/28")
@ReferencePage(title = "GZIP file format specification version 4.3", href = "https://www.rfc-editor.org/rfc/rfc1952.html")
public class WrapperGzipInputStream extends FilterInputStream {

    public static WrapperGzipInputStream create(@NotNull InputStream in, @NotNull AbstractInflateInputStream.Constructor1 constructor) throws IOException {
        {
            int id1 = in.read();
            if (id1 != 0x1f) { // EOF check included
                throw new IOException("ID1 = " + id1);
            }
        }
        {
            int id2 = in.read();
            if (id2 != 0x8b) { // EOF check included
                throw new IOException("ID1 = " + id2);
            }
        }
        {
            int compressionMethod = in.read();
            if (compressionMethod != 0x08) { // EOF check included
                throw new IOException("compression method = " + compressionMethod);
            }
        }
        final int flags = in.read();
        if ((flags & 0xe0) != 0) {
            throw new IOException("flags = " + flags);
        }
        in.skipNBytes(6);
        if ((flags & 0x04) != 0) { // extra
            skipExtraField(in);
        }
        if ((flags & 0x08) != 0) { // name
            skipZeroTerminated(in);
        }
        if ((flags & 0x10) != 0) { // comment
            skipZeroTerminated(in);
        }
        if ((flags & 0x02) != 0) { // header CRC
            in.skipNBytes(2); // never used
        }
        return new WrapperGzipInputStream(constructor.apply(in));
    }

    private static void skipExtraField(@NotNull InputStream in) throws IOException {
        final int length = in.read() | (in.read() << Byte.SIZE);
        if (length < 0) {
            throw new EOFException();
        }
        in.skipNBytes(length);
    }

    private static void skipZeroTerminated(@NotNull InputStream in) throws IOException {
        int octet;
        do {
            octet = in.read();
        } while (octet > 0);
        if (octet == -1) {
            throw new EOFException();
        }
    }

    private SimpleIntSizedCRC crc;

    private long uncompressedSize;

    private WrapperGzipInputStream(@NotNull AbstractInflateInputStream in) {
        super(in);
        crc = SimpleIntSizedCRC.crc32();
        crc.start();
    }

    private int readInt() throws IOException {
        int value = 0;
        for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
            int octet = in.read();
            if (octet == -1) {
                throw new EOFException();
            }
            value |= octet << shift;
        }
        return value;
    }

    private void check() throws IOException {
        final int crcValue = crc.getAsInt();
        crc = null;
        ((AbstractInflateInputStream) in).trailingBytesMode();
        if (crcValue != readInt()) {
            throw new CodecException();
        }
        if ((int) uncompressedSize != readInt()) {
            throw new CodecException();
        }
    }

    @Override
    @DataType("u8+{-1}")
    public int read() throws IOException {
        if (crc != null) {
            int value = in.read();
            if (value != -1) {
                crc.update((byte) value);
                uncompressedSize++;
                return value;
            } else {
                check();
            }
        }
        return -1;
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (crc != null) {
            length = in.read(array, offset, length);
            if (length != -1) {
                crc.update(array, offset, length);
                uncompressedSize += length;
                return length;
            } else {
                check();
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "WrapperGzipInputStream[in = " + in + "]";
    }
}
