package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/15")
public class ShiftResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0xaf21ac31f1adc7b8L;

    public static final ShiftResizePolicy INSTANCE = new ShiftResizePolicy();

    private static final int MIN_SHIFT = 4;
    private static final int MAX_SHIFT = 30;

    @Override
    public int intLength() {
        return MAX_SHIFT - MIN_SHIFT + 1;
    }

    @Override
    public int getInt(int index) {
        if (index < 0 || index > MAX_SHIFT - MIN_SHIFT) {
            throw new IndexOutOfBoundsException();
        }
        return 1 << (MIN_SHIFT + index);
    }

    @Override
    public int notSmallerCapacity(int minCapacity) {
        final int shift = Integer.SIZE - Integer.numberOfLeadingZeros(Math.max(0, minCapacity - 1));
        return 1 << Math.max(MIN_SHIFT, Math.min(shift, MAX_SHIFT));
    }

    @Override
    public int initialCapacity() {
        return 1 << MIN_SHIFT;
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        assert currentCapacity != 0;
        assert (currentCapacity & (currentCapacity - 1)) == 0;
        if ((currentCapacity & (1 << MAX_SHIFT)) != 0) {
            return currentCapacity;
        } else {
            return currentCapacity << 1;
        }
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<>";
    }
}
