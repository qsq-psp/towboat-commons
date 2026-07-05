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
}
