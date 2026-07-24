package mujica.ds.i32;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/6/15")
public class I32 implements I32Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0x7A8D4E595E0C9E2EL;

    int value;

    public I32() {
        super();
    }

    public I32(int value) {
        super();
        this.value = value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public I32 clone() {
        return new I32(value);
    }

    @Override
    public int getI32() {
        return value;
    }

    @Override
    public void setI32(int newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("0x%08x", value);
    }

    @CodeHistory(date = "2026/7/19")
    public static class Node1 extends I32 {

        private static final long serialVersionUID = 0xCAE10142A563E541L;

        Node1 first;
    }

    @CodeHistory(date = "2026/7/18")
    public static class Node2 extends I32 {

        private static final long serialVersionUID = 0x70E331E9B515D4CCL;

        Node2 first, second;
    }

    @CodeHistory(date = "2026/7/19")
    public static class Node3 extends I32 {

        private static final long serialVersionUID = 0xD430272C312D9681L;

        Node3 first, second, third;
    }
}
