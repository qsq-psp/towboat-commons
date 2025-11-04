package mujica.io.codec;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created on 2025/4/23.
 */
public class ByteBufferOutputStream extends OutputStream {

    @NotNull
    protected final ByteBuffer buffer;

    public ByteBufferOutputStream(@NotNull ByteBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    @Override
    public void write(int value) {
        buffer.put((byte) value);
    }

    @Override
    public void write(@NotNull byte[] array, int offset, int length) {
        buffer.put(array, offset, length);
    }

    @Override
    public void flush() {
        // no effect; no exception
    }

    @Override
    public void close() {
        // no effect; no exception
    }
}
