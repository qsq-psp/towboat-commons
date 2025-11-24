package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@CodeHistory(date = "2025/10/7")
public class JdkInflateInputStream extends AbstractInflateInputStream {

    @NotNull
    private final Inflater inflater = new Inflater(true);

    @NotNull
    private final byte[] buffer;

    private static final int STATE_NORMAL               = 0;
    private static final int STATE_END                  = 1;
    private static final int STATE_TRAILING_BYTES       = 2;
    private static final int STATE_CLOSED               = 3;
    private static final int STATE_ERROR                = 4;

    private int state;

    private int pointer;

    public JdkInflateInputStream(@NotNull InputStream in, int bufferSize) {
        super(in);
        if (bufferSize < 2) {
            throw new IllegalArgumentException();
        }
        buffer = new byte[bufferSize];
    }

    public JdkInflateInputStream(@NotNull InputStream in) {
        this(in, 512);
    }

    @Override
    public void trailingBytesMode() {
        if (state == STATE_END) {
            state = STATE_TRAILING_BYTES;
            int remaining = inflater.getRemaining();
            int newPointer = buffer.length - remaining;
            System.arraycopy(buffer, 1 + pointer - remaining, buffer, newPointer, remaining);
            pointer = newPointer;
        } else {
            throw new IllegalStateException();
        }
    }

    private int readNormal(@NotNull byte[] array, int offset, int length) throws IOException {
        try {
            int count;
            while ((count = inflater.inflate(array, offset, length)) == 0) {
                if (inflater.finished() || inflater.needsDictionary()) {
                    state = STATE_END;
                    return -1;
                }
                if (inflater.needsInput()) {
                    pointer = in.read(buffer, 1, buffer.length - 1);
                    if (pointer <= 0) {
                        throw new EOFException();
                    }
                    inflater.setInput(buffer, 1, pointer);
                }
            }
            return count;
        } catch (DataFormatException e) {
            state = STATE_ERROR;
            throw new IOException(e);
        }
    }

    @Override
    public int read() throws IOException {
        switch (state) {
            case STATE_NORMAL: {
                int count = readNormal(buffer, 0, 1);
                if (count == 1) {
                    return 0xff & buffer[0];
                } else if (count == -1) {
                    return -1;
                } else {
                    throw new IOException();
                }
            }
            case STATE_END:
                return -1;
            case STATE_TRAILING_BYTES:
                if (pointer < buffer.length) {
                    return 0xff & buffer[pointer++];
                } else {
                    return in.read();
                }
            case STATE_CLOSED:
                throw new IOException("closed");
            case STATE_ERROR:
                throw new IOException("error");
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || Math.addExact(offset, length) > array.length) {
            throw new IndexOutOfBoundsException();
        }
        switch (state) {
            case STATE_NORMAL:
                if (length == 0) {
                    return 0;
                } else {
                    return readNormal(array, offset, length);
                }
            case STATE_END:
                return -1;
            case STATE_TRAILING_BYTES: {
                    int remaining = buffer.length - pointer;
                    if (remaining > 0) {
                        length = Math.min(length, remaining);
                        System.arraycopy(buffer, pointer, array, offset, length);
                        pointer += length;
                        return length;
                    } else {
                        return in.read(array, offset, length);
                    }
                }
            case STATE_CLOSED:
                throw new IOException("closed");
            case STATE_ERROR:
                throw new IOException("error");
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public void close() throws IOException {
        state = STATE_CLOSED;
        inflater.end();
        in.close();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
