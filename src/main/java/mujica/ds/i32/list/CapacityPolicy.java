package mujica.ds.i32.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/6", name = "ResizePolicy")
@CodeHistory(date = "2026/7/7")
@DirectSubclass({ShiftCapacityPolicy.class, TwiceCapacityPolicy.class, HalfCapacityPolicy.class, Order1CapacityPolicy.class, Order2CapacityPolicy.class, Order3CapacityPolicy.class, ModuloLookUpCapacityPolicy.class})
public abstract class CapacityPolicy extends AbstractIntList {

    private static final long serialVersionUID = 0xe7b4b4a342f3c493L;

    @Override
    public abstract int intLength();

    @Override
    public abstract int getInt(int index);

    @Override
    public int min() {
        return getFirst();
    }

    @Override
    public int max() {
        return getLast();
    }

    public int notSmallerCapacity(int minCapacity) {
        for (int capacity : this) {
            if (capacity >= minCapacity) {
                return capacity;
            }
        }
        throw new IllegalArgumentException();
    }

    public int initialCapacity() {
        return notSmallerCapacity(0);
    }

    public int nextCapacity(int currentCapacity) {
        int index = firstIndexOf(currentCapacity);
        if (++index == 0) {
            throw new IllegalArgumentException();
        }
        if (index == intLength()) {
            index--;
        }
        return getInt(index);
    }

    public int nextLargerCapacity(int currentCapacity) {
        final int newCapacity = nextCapacity(currentCapacity);
        if (newCapacity <= currentCapacity) {
            throw new IllegalArgumentException();
        }
        return newCapacity;
    }

    /**
     * for rehash policy
     * @return true if the loaded size is too big for the current capacity
     */
    public boolean testLoadedSize(int size, int capacity) {
        return size >= (capacity >> 1); // half
    }

    /**
     * for rehash policy
     * @return true if the link is too long for the current capacity
     */
    public boolean testLinkLength(int length, int capacity) {
        return length >= (capacity >> 2); // quarter
    }

    public boolean notPrime() {
        return true;
    }

    @NotNull
    public static CapacityPolicy checkPrime(@Nullable CapacityPolicy policy) {
        if (policy == null) {
            return LookUpCapacityPolicy.PRIME_PAPER;
        } else if (policy.notPrime()) {
            throw new IllegalArgumentException();
        } else {
            return policy;
        }
    }

    public boolean notQuadraticFull() {
        return true;
    }

    @NotNull
    public static CapacityPolicy checkQuadraticFull(@Nullable CapacityPolicy policy) {
        if (policy == null) {
            return ModuloLookUpCapacityPolicy.INSTANCE_59;
        } else if (policy.notQuadraticFull()) {
            throw new IllegalArgumentException();
        } else {
            return policy;
        }
    }
}
