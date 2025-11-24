package mujica.io.compress;

import mujica.ds.of_int.list.HalfResizePolicy;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created on 2025/11/4.
 */
@CodeHistory(date = "2025/11/4")
public class ArrayCopyLookBackMemory extends ArrayLookBackMemory {

    @NotNull
    private static ResizePolicy createPolicy(int maxDistance) {
        maxDistance += Math.max(40, maxDistance / 3);
        return new HalfResizePolicy(maxDistance);
    }

    private int position;

    public ArrayCopyLookBackMemory(int maxDistance) {
        super(maxDistance, createPolicy(maxDistance));
        array = new byte[policy.initialCapacity()];
    }

    private int ensureRoom() {
        int capacity = array.length;
        if (position >= capacity) {
            assert position == capacity;
            int newCapacity = policy.nextCapacity(capacity);
            if (capacity < newCapacity) {
                array = Arrays.copyOf(array, newCapacity);
                capacity = newCapacity;
            } else {
                assert capacity == newCapacity;
                assert maxDistance < capacity;
                System.arraycopy(array, capacity - maxDistance, array, 0, maxDistance);
                position = maxDistance;
            }
        }
        return capacity - position;
    }

    @Override
    public void write(int data) {
        ensureRoom();
        array[position++] = (byte) data;
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        if (length <= 0) {
            return 0;
        }
        length = Math.min(length, ensureRoom());
        System.arraycopy(array, offset, this.array, position, length);
        position += length;
        return length;
    }

    @Override
    public byte copyAndWrite(int distance) {
        if (distance <= 0 || distance > maxDistance) {
            throw new IllegalArgumentException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        final int index = position - distance;
        if (index < 0) {
            throw new IllegalArgumentException("dictionary");
        }
        final byte data = array[index];
        write(data);
        return data;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        if (length <= 0) {
            return 0;
        }
        if (distance <= 0 || distance > maxDistance) {
            throw new IllegalArgumentException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        final int index = position - distance;
        if (index < 0) {
            throw new IllegalArgumentException("dictionary");
        }
        length = Math.min(length, distance);
        System.arraycopy(this.array, index, array, offset, length);
        writeFully(array, offset, length);
        return length;
    }

    @Override
    public boolean release() {
        position = 0;
        return true;
    }
}
