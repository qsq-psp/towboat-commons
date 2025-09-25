package mujica.ds.of_int.list;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/30")
public class TwiceResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0x1872490f58fd75e0L;

    public static final TwiceResizePolicy INSTANCE = new TwiceResizePolicy(15);

    private final int initial;

    public TwiceResizePolicy(int initial) {
        super();
        if (initial <= 0) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
    }

    public TwiceResizePolicy(@NotNull RandomContext rc) {
        this(rc.nextInt(0x100) + 1);
    }

    @Override
    public int intLength() {
        return Integer.numberOfLeadingZeros(initial);
    }

    @Override
    public int getInt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int value = initial << index;
        if (value >> index != initial) {
            throw new IndexOutOfBoundsException();
        }
        return value;
    }

    @Override
    public int initialCapacity() {
        return initial;
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        assert initial << (Integer.numberOfLeadingZeros(initial) - Integer.numberOfLeadingZeros(currentCapacity)) == currentCapacity;
        return Math.max(currentCapacity, currentCapacity << 1);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<initial = " + initial + ">";
    }
}
