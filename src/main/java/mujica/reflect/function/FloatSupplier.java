package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2021/10/9", project = "va")
@CodeHistory(date = "2023/9/24", project = "Ultramarine")
@CodeHistory(date = "2025/3/3")
@Stable(date = "2025/8/7")
@FunctionalInterface
public interface FloatSupplier {

    float getAsFloat();
}
