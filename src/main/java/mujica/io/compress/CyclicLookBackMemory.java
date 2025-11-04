package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created on 2025/11/3.
 */
public class CyclicLookBackMemory extends ArrayLookBackMemory {

    private static final long serialVersionUID = 0xE41B8FD8A206C4FFL;

    private int head, tail; // when array == null, the buffer is empty; when head == tail, the buffer is full

    public CyclicLookBackMemory(int maxDistance, @Nullable ResizePolicy policy) {
        super(maxDistance, policy);
    }

    public CyclicLookBackMemory(int maxDistance) {
        super(maxDistance, TwiceResizePolicy.INSTANCE);
    }

    @Override
    public void write(int data) {
        int capacity;
        if (array == null) {
            if (policy == null) {
                capacity = maxDistance;
            } else {
                capacity = policy.initialCapacity();
            }
            array = new byte[capacity];
            array[tail++] = (byte) data;
        } else {
            capacity = array.length;
            if (head == tail) {
                array[tail] = (byte) data;
                tail++;
                if (tail == capacity) {
                    tail = 0;
                }
                head = tail; // keep equal
                return;
            } else {
                array[tail++] = (byte) data;
            }
        }
        if (tail == capacity) {
            if (capacity < maxDistance) {
                capacity = policy.nextCapacity(capacity);
                array = Arrays.copyOf(array, capacity);
            } else {
                tail = 0;
            }
        }
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        return length;
    }

    @Override
    public int copyAndWrite(int distance) {
        if (distance <= 0 || distance > maxDistance) {
            throw new CodecException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        int index = tail - distance;
        if (index < 0) {
            if (head >= tail) {
                index += array.length;
                if (index < head) {
                    throw new CodecException("distance = " + distance + ", head = " + head + ", tail = " + tail);
                }
            } else {
                throw new CodecException("distance = " + distance + ", head = " + head + ", tail = " + tail);
            }
        }
        final int data = array[index];
        write(data);
        return data;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        return length;
    }

    @Override
    public void release() {
        super.release();
        head = 0;
        tail = 0;
    }
}
