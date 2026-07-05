package mujica.ds.f32;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "PublicFloatSlot")
@CodeHistory(date = "2026/7/4")
public class F32 implements F32Slot, Comparable<F32>, Cloneable, Serializable {

    private static final long serialVersionUID = 0x5a0fec3769996a7bL;

    float value;

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public void setFloat(float newValue) {
        value = newValue;
    }

    @Override
    public int compareTo(@NotNull F32 that) {
        return Float.compare(this.value, that.value);
    }
}
