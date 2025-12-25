package mujica.io.compress;

import mujica.ds.of_int.list.HalfResizePolicy;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2025/11/4", name = "ArrayCopyLookBackMemory")
@CodeHistory(date = "2025/11/26")
public class ArrayCopyRunBuffer extends ArrayRunBuffer {

    @NotNull
    private static ResizePolicy createPolicy(int maxDistance) {
        maxDistance += Math.max(40, maxDistance / 3);
        return new HalfResizePolicy(maxDistance);
    }

    private int position;

    public ArrayCopyRunBuffer(int maxDistance) {
        super(maxDistance, createPolicy(maxDistance));
        array = new byte[policy.initialCapacity()];
    }

    @Override
    public void setMaxDistance(int maxDistance) {
        if (maxDistance <= 0 || this.maxDistance < maxDistance) {
            throw new IllegalArgumentException();
        }
        this.maxDistance = maxDistance;
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
    public void put(byte data) {
        ensureRoom();
        array[position++] = data;
    }

    @Override
    public int put(@NotNull byte[] array, int offset, int length) {
        if (length <= 0) {
            return 0;
        }
        length = Math.min(length, ensureRoom());
        System.arraycopy(array, offset, this.array, position, length);
        position += length;
        return length;
    }

    @Override
    public byte copy(int distance) {
        checkDistance(distance);
        final int index = position - distance;
        if (index < 0) {
            throw new IllegalArgumentException("dictionary");
        }
        final byte data = array[index];
        put(data);
        return data;
    }

    /*
    @Override
    public int copy(int distance, @NotNull byte[] array, int offset, int length) {
        if (length <= 0) {
            return 0;
        }
        checkDistance(distance);
        final int index = position - distance;
        if (index < 0) {
            throw new IllegalArgumentException("dictionary");
        }
        length = Math.min(length, distance);
        System.arraycopy(this.array, index, array, offset, length);
        putFully(array, offset, length);
        return length;
    }
    //*/

    @Override
    public void clear() {
        position = 0;
    }

    @Override
    public boolean release() {
        position = 0;
        return true;
    }
}
