package mujica.ds.i32.list;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/30")
public class HalfCapacityPolicy extends CapacityPolicy {

    private static final long serialVersionUID = 0xb2d22614ce6cde8eL;

    private final int last;

    public HalfCapacityPolicy(int last) {
        super();
        if (last <= 0) {
            throw new IllegalArgumentException();
        }
        this.last = last;
    }

    public HalfCapacityPolicy(@NotNull RandomContext rc) {
        this(rc.nextInt(1 << 30) | (1 << 30));
    }

    @Override
    public int intLength() {
        return Integer.SIZE - Integer.numberOfLeadingZeros(last);
    }

    @Override
    public int getInt(int index) {
        final int length = Integer.SIZE - Integer.numberOfLeadingZeros(last);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }
        return last >> (length - 1 - index);
    }

    @Override
    public int notSmallerCapacity(int minCapacity) {
        if (minCapacity <= 1) {
            return 1;
        }
        return super.notSmallerCapacity(minCapacity);
    }

    @Override
    public int initialCapacity() {
        return 1;
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        return super.nextCapacity(currentCapacity);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<last = " + last + ">";
    }
}
