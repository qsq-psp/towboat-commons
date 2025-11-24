package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created on 2025/4/23.
 * Found in org.eclipse.jgit.util.io.ByteBufferInputStream, since 6.8.
 */
@CodeHistory(date = "2025/4/23")
public class ByteBufferInputStream extends InputStream {

    @NotNull
    protected final ByteBuffer buffer;

    public ByteBufferInputStream(@NotNull ByteBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if (buffer.hasRemaining()) {
            return buffer.get();
        } else {
            return -1;
        }
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) {
        if (!buffer.hasRemaining()) {
            return -1;
        }
        length = Math.min(length, buffer.remaining());
        buffer.get(array, offset, length);
        return length;
    }

    @Override
    @NotNull
    public byte[] readNBytes(int length) {
        length = Math.min(length, buffer.remaining());
        final byte[] array = new byte[length];
        buffer.get(array);
        return array;
    }

    @Override
    public int readNBytes(@NotNull byte[] array, int offset, int length) {
        length = Math.min(length, buffer.remaining());
        buffer.get(array, offset, length);
        return length;
    }

    @Override
    public long skip(long n) {
        final int oldPosition = buffer.position();
        final int newPosition = (int) Math.min(oldPosition + n, buffer.limit());
        if (oldPosition < newPosition) {
            buffer.position(newPosition);
            return newPosition - oldPosition;
        } else {
            return 0;
        }
    }

    @Override
    public int available() {
        return buffer.remaining();
    }

    @Override
    public void close() {
        // no effect; no exception
    }

    @Override
    public synchronized void mark(int readAheadLimit) {
        buffer.mark(); // the readAheadLimit is meaningless, there is no limit
    }

    @Override
    public synchronized void reset() {
        buffer.reset();
    }
}
