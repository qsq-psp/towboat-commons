package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@CodeHistory(date = "2025/10/14")
public class BufferedLimitedInputStream extends BufferedInputStream implements LimitedInput {

    protected Behavior behavior = Behavior.END;

    protected long remaining = Long.MAX_VALUE;

    public BufferedLimitedInputStream(@NotNull InputStream in, int initialCapacity) {
        super(in, initialCapacity);
    }

    public BufferedLimitedInputStream(@NotNull InputStream in) {
        this(in, 4096);
    }

    @NotNull
    public static BufferedLimitedInputStream createWithRemaining(@NotNull InputStream in, long remaining) {
        final BufferedLimitedInputStream lis = new BufferedLimitedInputStream(in);
        lis.setRemaining(remaining);
        return lis;
    }

    /**
     * supports all behaviors
     * decrease remaining value after use
     */
    protected boolean fill() throws IOException {
        if (pos < count) {
            return true;
        }
        pos = 0;
        count = 0;
        final int size = (int) Math.min(remaining, buf.length);
        if (size <= 0) {
            return false;
        }
        int delta = in.read(buf, 0, size);
        if (delta <= 0) {
            if (behavior == Behavior.THROW) {
                throw new EOFException("in");
            }
            if (behavior == Behavior.END) {
                return false;
            }
            Arrays.fill(buf, 0, size, (byte) 0);
            delta = size;
        }
        count = delta;
        return true;
    }

    /**
     * do not support Behavior.END, replaced by Behavior.THROW
     * remaining value decreased inside
     */
    protected void fill(int size) throws IOException {
        final int originalSize = size;
        size = size - count + pos;
        if (size > 0) {
            if (remaining < originalSize && behavior != Behavior.PAD) {
                throw new EOFException("remaining");
            }
            int capacity = buf.length;
            if (pos + originalSize > capacity) {
                if (originalSize > capacity) {
                    capacity = Math.min(capacity << 1, originalSize);
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
            while (size > 0) {
                int delta = (int) Math.min(remaining, capacity - count);
                if (delta <= 0) {
                    throw new EOFException("remaining");
                }
                delta = in.read(buf, count, delta);
                if (delta <= 0) {
                    if (behavior == Behavior.PAD) {
                        delta = size;
                        Arrays.fill(buf, count, count + delta, (byte) 0);
                    } else {
                        throw new EOFException("in");
                    }
                }
                count += delta;
                size -= delta;
            }
        }
        remaining -= originalSize;
    }

    @Override
    public int read() throws IOException {
        if (fill()) {
            remaining--;
            return 0xff & buf[pos++];
        }
        return -1;
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (fill()) {
            length = (int) Math.min(remaining, Math.min(count - pos, length));
            System.arraycopy(buf, pos, array, offset, length);
            pos += length;
            remaining -= length;
            return length;
        }
        return -1;
    }

    @Override
    public long skip(long n) throws IOException {
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
        long value;
        if (behavior == Behavior.PAD) {
            value = remaining;
        } else {
            value = Math.min(remaining, (long) (count - pos) + in.available());
        }
        return (int) Math.min(value, Integer.MAX_VALUE);
    }

    public void drain() throws IOException {
        skipNBytes(remaining);
    }

    @Override
    public void close() {
        // pass
    }

    @Override
    public void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
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
}
