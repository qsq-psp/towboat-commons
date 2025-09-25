package mujica.math.geometry.g2d;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/8", project = "Ultramarine")
@CodeHistory(date = "2024/4/1")
@FunctionalInterface
public interface FunctionalTransform2 {

    void transform(@NotNull Point src, @NotNull Point dst);
}
