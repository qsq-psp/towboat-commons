package mujica.ds.i8;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/6/25")
public class I8 implements I8Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xEC429A7C188F0D47L;

    byte value;

    public I8() {
        super();
    }

    public I8(byte value) {
        super();
        this.value = value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public I8 clone() {
        return new I8(value);
    }

    @Override
    public byte getI8() {
        return value;
    }

    @Override
    public void setI8(byte newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
