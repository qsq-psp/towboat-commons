package mujica.ds.of_int;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;
import java.util.function.IntPredicate;

@CodeHistory(date = "2025/3/23")
public class OffsetAndLength implements IntInterval, IntPredicate, Cloneable, Serializable {

    private static final long serialVersionUID = 0x102e29335b840c06L;

    public int offset;

    public int length;

    public OffsetAndLength() {
        super();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public OffsetAndLength(int offset, int length) {
        super();
        Math.addExact(offset, length);
        this.offset = offset;
        this.length = length;
    }

    @Override
    public int getMin() {
        return offset;
    }

    @Override
    public int getMax() {
        return offset + length - 1;
    }

    @Override
    public boolean test(int value) {
        return offset <= value && value < offset + length;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void increase() throws ArithmeticException {
        offset = Math.addExact(offset, length);
        Math.addExact(offset, length);
    }

    public void decrease() throws ArithmeticException {
        offset = Math.subtractExact(offset, length);
    }

    @Override
    public int hashCode() {
        return offset * 31 + length;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OffsetAndLength)) {
            return false;
        }
        final OffsetAndLength that = (OffsetAndLength) obj;
        return this.offset == that.offset && this.length == that.length;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public OffsetAndLength clone() {
        return new OffsetAndLength(offset, length);
    }

    @Override
    public String toString() {
        return "[offset = " + offset + ", length = " + length + "]";
    }
}
