package mujica.ds.of_int;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Created in existence on 2018/6/7, named SimpleBoundI.
 * Created on 2025/3/9.
 */
@CodeHistory(date = "2018/6/7", project = "existence", name = "SimpleBoundI")
@CodeHistory(date = "2025/3/9")
public class PublicIntInterval implements IntInterval, IntPredicate, IntConsumer, Cloneable, Serializable {

    private static final long serialVersionUID = 0xf2499a7fb880f2adL;

    public int min, max;

    public PublicIntInterval() {
        super();
    }

    public PublicIntInterval(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public boolean test(int value) {
        return min <= value && value <= max;
    }

    @Override
    public void accept(int value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PublicIntInterval)) {
            return false;
        }
        final PublicIntInterval that = (PublicIntInterval) obj;
        return this.min == that.min && this.max == that.max;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public PublicIntInterval clone() {
        return new PublicIntInterval(min, max);
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }
}
