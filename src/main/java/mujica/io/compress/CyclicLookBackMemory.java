package mujica.io.compress;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created on 2025/11/3.
 */
@CodeHistory(date = "2025/11/3")
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
            }
        }
        assert tail < capacity;
        array[tail] = (byte) data;
        tail++;
        if (tail >= capacity) {
            assert tail == capacity;
            if (capacity < maxDistance) {
                capacity = policy.nextLargerCapacity(capacity);
                array = Arrays.copyOf(array, capacity);
            } else {
                tail = 0;
            }
        }
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        if (length <= 0) {
            return 0;
        }
        int capacity;
        if (this.array == null) {
            if (policy == null) {
                capacity = maxDistance;
            } else {
                capacity = policy.initialCapacity();
            }
            this.array = new byte[capacity];
        } else {
            capacity = this.array.length;
            if (head == tail) {
                assert tail < capacity;
                length = Math.min(length, capacity - tail);
                System.arraycopy(array, offset, this.array, tail, length);
                tail += length;
                if (tail == capacity) {
                    tail = 0;
                }
                head = tail; // keep equal
                return length;
            }
        }
        assert tail < capacity;
        length = Math.min(length, capacity - tail);
        System.arraycopy(array, offset, this.array, tail, length);
        tail += length;
        if (tail >= capacity) {
            assert tail == capacity;
            if (capacity < maxDistance) {
                capacity = policy.nextLargerCapacity(capacity);
                this.array = Arrays.copyOf(array, capacity);
            } else {
                tail = 0;
            }
        }
        return length;
    }

    @Override
    public byte copyAndWrite(int distance) {
        if (distance <= 0 || distance > maxDistance) {
            throw new IllegalArgumentException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        int index = tail - distance;
        if (index < 0) {
            if (head >= tail) {
                index += array.length;
                if (index < head) {
                    throw new IllegalArgumentException("distance = " + distance + ", head = " + head + ", tail = " + tail);
                }
            } else {
                throw new IllegalArgumentException("distance = " + distance + ", head = " + head + ", tail = " + tail);
            }
        }
        final byte data = array[index];
        write(data);
        return data;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        if (distance <= 0 || distance > maxDistance) {
            throw new IllegalArgumentException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        int index = tail - distance;
        if (index < 0) {
            if (head >= tail) {
                int capacity = this.array.length;
                index += capacity;
                if (index < head) {
                    throw new IllegalArgumentException("distance = " + distance + ", head = " + head + ", tail = " + tail);
                }
                length = Math.min(length, capacity - index);
            } else {
                throw new IllegalArgumentException("distance = " + distance + ", head = " + head + ", tail = " + tail);
            }
        } else {
            length = Math.min(length, distance);
        }
        System.arraycopy(this.array, index, array, offset, length);
        writeFully(array, offset, length);
        return length;
    }

    @Override
    public boolean release() {
        array = null;
        head = 0;
        tail = 0;
        return true;
    }
}
