package mujica.ds.of_int.set;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

@CodeHistory(date = "2018/6/7", project = "existence", name = "SimpleBoundI")
@CodeHistory(date = "2025/3/9", name = "PublicIntInterval")
@CodeHistory(date = "2026/1/18")
public class SimpleInterval implements Interval, IntPredicate, IntConsumer, Cloneable, Serializable {

    private static final long serialVersionUID = 0xf2499a7fb880f2adL;

    public int left, right;

    public SimpleInterval() {
        super();
    }

    public SimpleInterval(int left, int right) {
        super();
        this.left = left;
        this.right = right;
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public boolean test(int value) {
        return left <= value && value <= right;
    }

    @Override
    public void accept(int value) {
        left = Math.min(left, value);
        right = Math.max(right, value);
    }

    @Override
    public int hashCode() {
        return left * 31 + right;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleInterval)) {
            return false;
        }
        final SimpleInterval that = (SimpleInterval) obj;
        return this.left == that.left && this.right == that.right;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public SimpleInterval clone() {
        return new SimpleInterval(left, right);
    }

    @Override
    public String toString() {
        return "[" + left + ", " + right + "]";
    }
}
