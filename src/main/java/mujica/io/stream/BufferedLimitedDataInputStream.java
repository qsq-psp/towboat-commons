package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2025/9/16.
 */
@CodeHistory(date = "2025/9/16")
public class BufferedLimitedDataInputStream extends BufferedInputStream implements LimitedDataInput {

    protected long remaining = Long.MAX_VALUE;

    public BufferedLimitedDataInputStream(@NotNull InputStream in, int initialCapacity) {
        super(in, initialCapacity);
    }

    public BufferedLimitedDataInputStream(@NotNull InputStream in) {
        this(in, 4096);
    }

    private boolean fill() throws IOException {
        if (pos < count) {
            return true;
        }
        pos = 0;
        count = in.read(buf);
        return count > 0;
    }

    private void readFill(int size) throws IOException {
        if (remaining < size) {
            throw new EOFException();
        }
        int capacity = buf.length;
        if (pos + size > capacity) {
            if (size > capacity) {
                capacity = Math.min(capacity << 1, size);
                byte[] newBuf = new byte[capacity];
                if (pos != 0) {
                    System.arraycopy(buf, pos, newBuf, 0, count - pos);
                    count -= pos;
                    pos = 0;
                }
                buf = newBuf;
            } else {
                System.arraycopy(buf, pos, buf, 0, count - pos);
                count -= pos;
                pos = 0;
            }
        }
        // System.out.println("pos = " + pos + ", count = " + count + ", capacity = " + capacity + ", size = " + size);
        remaining -= size; // then size changed
        size = size + pos - count;
        while (size > 0) {
            int delta = in.read(buf, count, capacity - count);
            if (delta <= 0) {
                throw new EOFException();
            }
            count += delta;
            size -= delta;
        }
    }

    @Override
    public int read() throws IOException {
        if (remaining > 0L) {
            if (fill()) {
                remaining--;
                return 0xff & buf[pos++];
            }
        }
        return -1;
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (remaining > 0L) {
            if (fill()) {
                length = (int) Math.min(remaining, Math.min(count - pos, length));
                System.arraycopy(buf, pos, array, offset, length);
                pos += length;
                remaining -= length;
                return length;
            } else {
                return -1;
            }
        }
        return -1;
    }

    @Override
    public long skip(long n) throws IOException {
        n = Math.min(n, remaining);
        if (n <= 0L) {
            return 0L;
        }
        if (fill()) {
            int m = (int) Math.min(n, count - pos);
            pos += m;
            remaining -= m;
            return m;
        } else {
            return 0L;
        }
    }

    @Override
    public int available() throws IOException {
        final long value = Math.min(remaining, (long) (count - pos) + in.available());
        return (int) Math.min(value, Integer.MAX_VALUE);
    }

    @Override
    public void mark(int readLimit) {
        // pass
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override
    public boolean markSupported() {
        return false;
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
            throw new EOFException(); // fail fast
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
        if (n > 0 && remaining > 0L) {
            if (fill()) {
                n = (int) Math.min(Math.min(n, count - pos), remaining);
                pos += n;
                remaining -= n;
                return n;
            }
        }
        return 0;
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        readFill(1);
        return buf[pos++];
    }

    @Override
    public int readUnsignedByte() throws IOException {
        readFill(1);
        return 0xff & buf[pos++];
    }

    @Override
    public short readShort() throws IOException {
        readFill(2);
        int value = buf[pos++] << 8;
        value |= 0xff & (buf[pos++]);
        return (short) value;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        readFill(2);
        int value = (0xff & buf[pos++]) << 8;
        value |= 0xff & (buf[pos++]);
        return value;
    }

    @Override
    public char readChar() throws IOException {
        readFill(2);
        int value = (0xff & buf[pos++]) << 8;
        value |= 0xff & (buf[pos++]);
        return (char) value;
    }

    @Override
    public int readInt() throws IOException {
        readFill(4);
        int value = buf[pos++] << 24;
        value |= (0xff & buf[pos++]) << 16;
        value |= (0xff & buf[pos++]) << 8;
        value |= 0xff & buf[pos++];
        return value;
    }

    @Override
    public long readLong() throws IOException {
        readFill(8);
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
        readFill(length);
        final String string = new String(buf, pos, length);
        pos += length;
        return string;
    }

    @Override
    public String toString() {
        return "BufferedLimitedDataInputStream[pos = " + pos + ", count = " + count + ", remaining = " + remaining + "]";
    }
}
