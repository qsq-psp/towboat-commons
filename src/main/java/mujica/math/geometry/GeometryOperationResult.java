package mujica.math.geometry;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
@Stable(date = "2025/7/25")
public enum GeometryOperationResult {

    FAIL, MODIFIED, UNKNOWN, REMAIN;

    private static final long serialVersionUID = 0x1e1e91b29f7168a1L;

    @NotNull
    public static GeometryOperationResult of(int index) {
        switch (index) {
            default:
            case 0:
                return FAIL;
            case 1:
                return MODIFIED;
            case 2:
                return UNKNOWN;
            case 3:
                return REMAIN;
        }
    }

    @NotNull
    public GeometryOperationResult or(@NotNull GeometryOperationResult that) {
        return of(Math.min(this.ordinal(), that.ordinal()));
    }
}
