package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/11/19", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public interface Curve2 {

    @NotNull
    GeometryOperationResult interpolate(double position, @NotNull Point point);

    @NotNull
    GeometryOperationResult derivative(double position, @NotNull Point point);

    @NotNull
    GeometryOperationResult includedInto(@NotNull Bound bound);

    @NotNull
    GeometryOperationResult smallBound(@NotNull Bound bound);

    @NotNull
    GeometryOperationResult smallestBound(@NotNull Bound bound);
}
