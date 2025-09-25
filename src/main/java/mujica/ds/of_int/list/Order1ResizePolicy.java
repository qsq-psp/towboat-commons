package mujica.ds.of_int.list;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/7/1")
public class Order1ResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0xa8d943cc284807c3L;

    private final int p0, p1;

    public Order1ResizePolicy(int p0, int p1) {
        super();
        if (p0 < 0 || p1 <= 0) {
            throw new IllegalArgumentException();
        }
        this.p0 = p0;
        this.p1 = p1;
    }

    public Order1ResizePolicy(@NotNull RandomContext rc) {
        this(rc.nextInt(0x80), rc.nextInt(0x10000) + 1);
    }

    @Override
    public int intLength() {
        return (Integer.MAX_VALUE - p0) / p1;
    }

    @Override
    public int getInt(int index) {
        try {
            return Math.addExact(p0, Math.multiplyExact(p1, index));
        } catch (ArithmeticException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int notSmallerCapacity(int minCapacity) {
        int capacity = minCapacity - p0;
        if (capacity <= 0) {
            return p0;
        }
        capacity = p0 + capacity / p1 * p1;
        if (capacity != minCapacity) {
            assert capacity < minCapacity;
            capacity += p1;
            if (capacity < 0) { // integer overflow
                throw new IllegalArgumentException();
            }
            assert capacity > minCapacity;
        }
        return capacity;
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        int capacity = currentCapacity + p1;
        if (capacity < 0) { // integer overflow
            capacity = currentCapacity;
        }
        return capacity;
    }
}
