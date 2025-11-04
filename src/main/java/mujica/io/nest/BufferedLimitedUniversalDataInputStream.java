package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2025/10/9.
 */
@CodeHistory(date = "2025/10/9")
public class BufferedLimitedUniversalDataInputStream extends BufferedLimitedInputStream implements LimitedUniversalDataInput {

    public BufferedLimitedUniversalDataInputStream(@NotNull InputStream in, int initialCapacity) {
        super(in, initialCapacity);
    }

    public BufferedLimitedUniversalDataInputStream(@NotNull InputStream in) {
        super(in);
    }

    @Override
    public void readFully(@NotNull byte[] array) throws IOException {
        readFully(array, 0, array.length);
    }

    @Override
    public void readFully(@NotNull byte[] array, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (length > remaining) {
            throw new EOFException("remaining"); // fail fast
        }
        while (length > 0) {
            int count = read(array, offset, length);
            if (count <= 0) {
                throw new EOFException();
            }
            offset += count;
            length -= count;
        }
    }

    @Override
    public void skipFully(int length) throws IOException {
        while (length > 0) {
            long count = skip(length);
            if (count <= 0) {
                throw new EOFException();
            }
            length -= count;
        }
        if (length < 0) {
            throw new IOException();
        }
    }

    @Override
    public byte readSignedByte() throws IOException {
        fill(1);
        return buf[pos++];
    }

    @Override
    public int readUnsignedByte() throws IOException {
        fill(1);
        return 0xff & buf[pos++];
    }

    @Override
    public short readIntelSignedShort() throws IOException {
        fill(2);
        int value = 0xff & buf[pos++];
        value |= buf[pos++] << 8;
        return (short) value;
    }

    @Override
    public short readNetworkSignedShort() throws IOException {
        fill(2);
        int value = buf[pos++] << 8;
        value |= 0xff & (buf[pos++]);
        return (short) value;
    }

    @Override
    public int readIntelUnsignedShort() throws IOException {
        fill(2);
        int value = 0xff & buf[pos++];
        value |= buf[pos++] << 8;
        return 0xffff & value;
    }

    @Override
    public int readNetworkUnsignedShort() throws IOException {
        fill(2);
        int value = buf[pos++] << 8;
        value |= 0xff & (buf[pos++]);
        return 0xffff & value;
    }

    @Override
    public char readIntelChar() throws IOException {
        fill(2);
        int value = 0xff & buf[pos++];
        value |= buf[pos++] << 8;
        return (char) value;
    }

    @Override
    public char readNetworkChar() throws IOException {
        fill(2);
        int value = buf[pos++] << 8;
        value |= 0xff & (buf[pos++]);
        return (char) value;
    }

    @Override
    public int readIntelSignedInt() throws IOException {
        fill(4);
        int value = 0xff & buf[pos++];
        value |= (0xff & buf[pos++]) << 8;
        value |= (0xff & buf[pos++]) << 16;
        value |= buf[pos++] << 24;
        return value;
    }

    @Override
    public int readNetworkSignedInt() throws IOException {
        fill(4);
        int value = buf[pos++] << 24;
        value |= (0xff & buf[pos++]) << 16;
        value |= (0xff & buf[pos++]) << 8;
        value |= 0xff & buf[pos++];
        return value;
    }

    @Override
    public long readIntelUnsignedInt() throws IOException {
        return 0xffffffffL & readIntelSignedInt();
    }

    @Override
    public long readNetworkUnsignedInt() throws IOException {
        return 0xffffffffL & readNetworkSignedInt();
    }

    @Override
    public long readIntelSignedLong() throws IOException {
        fill(8);
        long value = 0L;
        for (int shift = 0; shift < Long.SIZE; shift += Byte.SIZE) {
            value |= (0xffL & buf[pos++]) << shift;
        }
        return value;
    }

    @Override
    public long readNetworkSignedLong() throws IOException {
        fill(8);
        long value = 0L;
        for (int shift = Long.SIZE - Byte.SIZE; shift >= 0; shift -= Byte.SIZE) {
            value |= (0xffL & buf[pos++]) << shift;
        }
        return value;
    }

    @NotNull
    @Override
    public BigInteger readIntelUnsignedLong() throws IOException {
        return BigInteger.valueOf(readIntelSignedLong()).and(MASK64);
    }

    @NotNull
    @Override
    public BigInteger readNetworkUnsignedLong() throws IOException {
        return BigInteger.valueOf(readNetworkSignedLong()).and(MASK64);
    }

    @NotNull
    @Override
    public String readAscii(int length) throws IOException {
        fill(length);
        final String string = new String(buf, pos, length, StandardCharsets.US_ASCII);
        pos += length;
        return string;
    }

    @NotNull
    @Override
    public String readUtf8(int byteLength) throws IOException {
        fill(byteLength);
        final String string = new String(buf, pos, byteLength, StandardCharsets.UTF_8);
        pos += byteLength;
        return string;
    }

    @NotNull
    @Override
    public String readTrimmedAscii(int length) throws IOException {
        fill(length);
        int end = pos + length;
        while (pos < end) {
            if ((0xff & buf[end - 1]) < ' ') {
                end--;
            }
        }
        final String string = new String(buf, pos, end - pos, StandardCharsets.US_ASCII);
        pos += length;
        return string;
    }

    @NotNull
    @Override
    public String readTrimmedUtf8(int byteLength) throws IOException {
        fill(byteLength);
        int end = pos + byteLength;
        while (pos < end) {
            if ((0xff & buf[end - 1]) < ' ') {
                end--;
            }
        }
        final String string = new String(buf, pos, end - pos, StandardCharsets.UTF_8);
        pos += byteLength;
        return string;
    }

    @NotNull
    @Override
    public String readIntelUnicode(int charLength) throws IOException {
        fill(charLength << 1);
        final char[] chars = new char[charLength];
        for (int index = 0; index < charLength; index++) {
            int value = 0xff & buf[pos++];
            value |= buf[pos++] << 8;
            chars[index] = (char) value;
        }
        return new String(chars);
    }

    @NotNull
    @Override
    public String readNetworkUnicode(int charLength) throws IOException {
        fill(charLength << 1);
        final char[] chars = new char[charLength];
        for (int index = 0; index < charLength; index++) {
            int value = buf[pos++] << 8;
            value |= 0xff & (buf[pos++]);
            chars[index] = (char) value;
        }
        return new String(chars);
    }

    @NotNull
    @Override
    public byte[] readFully(int length) throws IOException {
        final byte[] array = new byte[length];
        readFully(array);
        return array;
    }

    @NotNull
    @Override
    public byte[] readZeroTerminatedBytes() throws IOException {
        return new byte[0];
    }

    @NotNull
    @Override
    public String readZeroTerminatedAscii() throws IOException {
        return "";
    }

    @NotNull
    @Override
    public String readZeroTerminatedUtf8() throws IOException {
        return "";
    }
}
