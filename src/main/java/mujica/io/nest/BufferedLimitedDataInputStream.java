package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2025/9/16")
public class BufferedLimitedDataInputStream extends BufferedLimitedInputStream implements LimitedDataInput {

    public BufferedLimitedDataInputStream(@NotNull InputStream in, int initialCapacity) {
        super(in, initialCapacity);
    }

    public BufferedLimitedDataInputStream(@NotNull InputStream in) {
        super(in);
    }

    @NotNull
    @Override
    public Behavior getBehavior() {
        return behavior;
    }

    @Override
    public void setBehavior(@NotNull Behavior newBehavior) {
        behavior = newBehavior;
    }

    @Override
    public long getRemaining() {
        return remaining;
    }

    @Override
    public long setRemaining(long newRemaining) {
        final long oldRemaining = remaining;
        remaining = newRemaining;
        return oldRemaining;
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
    public int skipBytes(int n) throws IOException {
        if (n < 0) {
            return 0;
        }
        if (fill()) {
            n = Math.min(n, count - pos);
            pos += n;
            remaining -= n;
            return n;
        } else {
            return 0;
        }
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        fill(1);
        return buf[pos++];
    }

    @Override
    public int readUnsignedByte() throws IOException {
        fill(1);
        return 0xff & buf[pos++];
    }

    @Override
    public short readShort() throws IOException {
        fill(2);
        int value = buf[pos++] << 8;
        value |= 0xff & (buf[pos++]);
        return (short) value;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        fill(2);
        int value = (0xff & buf[pos++]) << 8;
        value |= 0xff & (buf[pos++]);
        return value;
    }

    @Override
    public char readChar() throws IOException {
        fill(2);
        int value = (0xff & buf[pos++]) << 8;
        value |= 0xff & (buf[pos++]);
        return (char) value;
    }

    @Override
    public int readInt() throws IOException {
        fill(4);
        int value = buf[pos++] << 24;
        value |= (0xff & buf[pos++]) << 16;
        value |= (0xff & buf[pos++]) << 8;
        value |= 0xff & buf[pos++];
        return value;
    }

    @Override
    public long readLong() throws IOException {
        fill(8);
        long value = 0L;
        for (int shift = 56; shift >= 0; shift -= 8) {
            value |= (0xffL & buf[pos++]) << shift;
        }
        return value;
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        final int length = readUnsignedShort();
        fill(length);
        final String string = new String(buf, pos, length, StandardCharsets.UTF_8);
        pos += length;
        return string;
    }

    @Override
    public String toString() {
        return "BufferedLimitedDataInputStream[pos = " + pos + ", count = " + count + ", remaining = " + remaining + "]";
    }
}
