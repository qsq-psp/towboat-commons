package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/8", project = "Ultramarine")
@CodeHistory(date = "2025/10/10")
public enum JsonConstant {

    UNDEFINED, NULL, PRESENT;

    private static final long serialVersionUID = 0x38fcc7ebdc1f7a01L;

    @Override
    @NotNull
    public String toString() {
        return name().toLowerCase();
    }
}
