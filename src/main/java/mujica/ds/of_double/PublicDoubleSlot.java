package mujica.ds.of_double;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/7/3", project = "existence", name = "MtDouble")
@CodeHistory(date = "2020/2/22", project = "coo", name = "DoubleModel")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/21")
public class PublicDoubleSlot extends Number implements DoubleSlot, Comparable<PublicDoubleSlot>, Cloneable {

    private static final long serialVersionUID = 0x6a6bc6ef5338a773L;

    public double value;

    public PublicDoubleSlot() {
        super();
    }

    public PublicDoubleSlot(double value) {
        super();
        this.value = value;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public double setDouble(double newValue) {
        final double oldValue = value;
        value = newValue;
        return oldValue;
    }

    @Override
    public int compareTo(@NotNull PublicDoubleSlot that) {
        return Double.compare(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((PublicDoubleSlot) obj).value;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    public PublicDoubleSlot clone() {
        return new PublicDoubleSlot(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
