package mujica.ds.f32;

import mujica.ds.slot.Base2Float;
import mujica.ds.slot.Base2Integer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "PublicFloatSlot")
@CodeHistory(date = "2026/7/4")
public class F32 implements F32Slot, Comparable<F32>, Base2Float, Cloneable, Serializable {

    private static final long serialVersionUID = 0x5a0fec3769996a7bL;

    float value;

    public F32() {
        super();
    }

    public F32(float value) {
        super();
        this.value = value;
    }

    @Override
    public float getF32() {
        return value;
    }

    @Override
    public void setF32(float newValue) {
        value = newValue;
    }

    @Override
    public int compareTo(@NotNull F32 that) {
        return Float.compare(this.value, that.value);
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
        return Float.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((F32) obj).value;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    public F32 clone() {
        return new F32(value);
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @CodeHistory(date = "2026/7/22")
    public static class Node1 extends F32 {

        private static final long serialVersionUID = 0xE211473CC5F17AD6L;

        Node1 first;
    }

    @CodeHistory(date = "2026/7/22")
    public static class Node2 extends F32 {

        private static final long serialVersionUID = 0x43E5E2B6415B19B5L;

        Node2 first, second;
    }

    @CodeHistory(date = "2026/7/22")
    public static class Node3 extends F32 {

        private static final long serialVersionUID = 0x26D2187A3A10C20AL;

        Node3 first, second, third;
    }
}
