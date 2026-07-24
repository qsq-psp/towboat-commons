package mujica.ds.f64;

import mujica.ds.slot.Base2Float;
import mujica.ds.slot.Base2Integer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2018/7/3", project = "existence", name = "MtDouble")
@CodeHistory(date = "2020/2/22", project = "coo", name = "DoubleModel")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/21")
@FieldOrder("value") // only "value", not "double"
public class F64 implements F64Slot, Comparable<F64>, Base2Float, Cloneable, Serializable {

    private static final long serialVersionUID = 0x6a6bc6ef5338a773L;

    double value;

    public F64() {
        super();
    }

    public F64(double value) {
        super();
        this.value = value;
    }

    @Override
    public double getF64() {
        return value;
    }

    @Override
    public void setF64(double newValue) {
        value = newValue;
    }

    @Override
    public int compareTo(@NotNull F64 that) {
        return Double.compare(this.value, that.value);
    }

    @NotNull
    @Override
    public Base2Integer exponent() {
        return null;
    }

    @NotNull
    @Override
    public Base2Integer mantissa() {
        return null;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((F64) obj).value;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    public F64 clone() {
        return new F64(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
