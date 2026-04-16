package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/6")
@DirectSubclass({ShiftResizePolicy.class, TwiceResizePolicy.class, HalfResizePolicy.class, Order1ResizePolicy.class, Order2ResizePolicy.class, Order3ResizePolicy.class, ModuloLookUpResizePolicy.class})
public abstract class ResizePolicy extends AbstractIntList {

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
    public static ResizePolicy checkPrime(@Nullable ResizePolicy policy) {
        if (policy == null) {
            return LookUpResizePolicy.PRIME_PAPER;
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
    public static ResizePolicy checkQuadraticFull(@Nullable ResizePolicy policy) {
        if (policy == null) {
            return ModuloLookUpResizePolicy.INSTANCE_59;
        } else if (policy.notQuadraticFull()) {
            throw new IllegalArgumentException();
        } else {
            return policy;
        }
    }

}
