package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/6")
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

    @CodeHistory(date = "2025/7/1")
    public static class Order2 extends ResizePolicy {

        private static final long serialVersionUID = 0x7e5c74e47d6fc6dfL;

        private final int p0, p1, p2;

        public Order2(int p0, int p1, int p2) {
            super();
            if (p0 < 0 || p1 < 0 || p2 <= 0) {
                throw new IllegalArgumentException();
            }
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public int intLength() {
            return 0;
        }

        @Override
        public int getInt(int index) {
            return Math.addExact(p0, Math.multiplyExact(Math.addExact(p1, Math.multiplyExact(p2, index)), index));
        }
    }

    @CodeHistory(date = "2025/7/1")
    public static class Order3 extends ResizePolicy {

        private static final long serialVersionUID = 0x5b6d827aad3d0339L;

        private final int p0, p3;

        public Order3(int p0, int p3) {
            super();
            if (p0 < 0 || p3 <= 0) {
                throw new IllegalArgumentException();
            }
            this.p0 = p0;
            this.p3 = p3;
        }

        @Override
        public int intLength() {
            return 0;
        }

        @Override
        public int getInt(int index) {
            return Math.addExact(p0, Math.multiplyExact(Math.multiplyExact(p3, index), Math.multiplyExact(index, index)));
        }
    }

}
