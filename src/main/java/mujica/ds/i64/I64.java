package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2023/11/29", project = "OSHI")
@CodeHistory(date = "2023/12/1", project = "Ultramarine", name = "LongQuantity")
@CodeHistory(date = "2023/12/6", project = "Ultramarine", name = "LongBox")
@CodeHistory(date = "2025/3/9", name = "PublicI64Slot")
@CodeHistory(date = "2026/7/1")
@FieldOrder("value") // only "value", not "long"
public class I64 implements I64Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xb0ca00edf2e5e344L;

    long value;

    public I64() {
        super();
    }

    public I64(long value) {
        super();
        this.value = value;
    }

    @Override
    @NotNull
    public I64 clone() {
        return new I64();
    }

    @Override
    public long getI64() {
        return value;
    }

    @Override
    public void setI64(long newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }
}
