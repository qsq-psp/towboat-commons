package mujica.ds.bit;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/4/22", name = "BooleanSlot")
public interface BitSlot {

    boolean getBit();

    void setBit(boolean newValue);
}
