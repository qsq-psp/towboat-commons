package mujica.math.geometry.g2d;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2022/10/5.
 * Planar jordan geometry associated with a center point
 */
public abstract class OriginJordan2<P extends Point> extends Jordan2 {

    private static final long serialVersionUID = 0x87775456ada14ecfL;

    @NotNull
    public final P o;

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
