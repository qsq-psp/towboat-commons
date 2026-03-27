package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;


@CodeHistory(date = "2021/10/9", project = "va")
@CodeHistory(date = "2023/9/24", project = "Ultramarine")
@CodeHistory(date = "2025/3/3")
@FunctionalInterface
public interface FloatSupplier {

    float getAsFloat();
}
