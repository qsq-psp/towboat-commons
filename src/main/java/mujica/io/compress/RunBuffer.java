package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@CodeHistory(date = "2025/10/31", name = "LookBackMemory")
@CodeHistory(date = "2025/11/26")
public abstract class RunBuffer {

    protected int maxDistance;

    protected RunBuffer(int maxDistance) {
        super();
        if (maxDistance <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxDistance = maxDistance;
    }

    protected RunBuffer() {
        super();
        maxDistance = Integer.MAX_VALUE;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        if (maxDistance <= 0) {
            throw new IllegalArgumentException();
        }
        this.maxDistance = maxDistance;
    }

    protected void checkDistance(int distance) {
        if (distance <= 0 || distance > maxDistance) {
            throw new IllegalArgumentException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
    }

    public abstract void put(byte data);

    public int put(@NotNull byte[] array, @Index(of = "array") int offset, int maxLength) {
        if (maxLength > 0) {
            put(array[offset]);
            return 1;
        } else {
            return 0;
        }
    }

    public void putFully(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        while (length > 0) {
            int count = put(array, offset, length);
            assert count > 0;
            offset += count;
            length -= count;
        }
        assert length == 0;
    }

    public int put(@NotNull InputStream is, int maxLength) throws IOException {
        if (maxLength > 0) {
            int data = is.read();
            if (data == -1) {
                throw new EOFException();
            }
            put((byte) data);
            return 1;
        } else {
            return 0;
        }
    }

    public void putFully(@NotNull InputStream is, int length) throws IOException {
        while (length > 0) {
            int count = put(is, length);
            assert count > 0;
            length -= count;
        }
        assert length == 0;
    }

    public abstract byte copy(int distance);

    public int copy(int distance, @NotNull byte[] array, @Index(of = "array") int offset, int maxLength) {
        if (maxLength > 0) {
            array[offset] = copy(distance);
            return 1;
        } else {
            return 0;
        }
    }

    public void copyFully(int distance, @NotNull byte[] array, int offset, int length) {
        while (length > 0) {
            int count = copy(distance, array, offset, length);
            assert count > 0;
            offset += count;
            length -= count;
        }
        assert length == 0;
    }

    public int copy(int distance, @NotNull OutputStream os, int maxLength) throws IOException {
        if (maxLength > 0) {
            os.write(copy(distance));
            return 1;
        } else {
            return 0;
        }
    }

    public void copyFully(int distance, @NotNull OutputStream os, int length) throws IOException {
        while (length > 0) {
            int count = copy(distance, os, length);
            assert count > 0;
            length -= count;
        }
        assert length == 0;
    }

    public int available() {
        return maxDistance;
    }

    public void clear() {
        // pass
    }

    /**
     * if this buffer is allocated from a pool, it is returned to that pool and can be allocated again
     * if this buffer is not allocated from a pool, it is destroyed and can not be used again
     */
    public boolean release() {
        return true;
    }
}
