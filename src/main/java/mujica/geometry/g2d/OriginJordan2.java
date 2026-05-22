package mujica.geometry.g2d;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2022/10/5", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
@DirectSubclass({Circle.class, Arc.class, ConicSection.class})
public abstract class OriginJordan2<P extends Point> extends Jordan2 {

    private static final long serialVersionUID = 0x87775456ada14ecfL;

    @NotNull
    public final P o; // planar jordan geometry associated with a center point

    public OriginJordan2(@NotNull P o) {
        super();
        this.o = o;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        o.checkHealth(consumer);
    }

    @Override
    public boolean isHealthy() {
        return o.isHealthy();
    }
}
