package mujica.ds.of_boolean;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/4/22")
public interface BooleanSlot {

    boolean getBoolean();

    boolean setBoolean(boolean newValue);
}
