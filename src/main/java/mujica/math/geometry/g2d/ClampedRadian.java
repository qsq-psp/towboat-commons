package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Math.atan2(0.0, -1.0) == PI
 * Math.atan2(-0.0, -1.0) == -PI
 * The clamped range is the value range of Math.atan2 except PI, which is [-PI, PI)
 */
@CodeHistory(date = "2022/6/26", project = "Ultramarine")
public class ClampedRadian extends Radian {

    private static final long serialVersionUID = 0x2962c49b390606bcL;

    public ClampedRadian() {
        super();
    }

    @Override
    public void setRadian(double value) {
        if (value < 0.0) {
            rad = (value - PI) % TAU + PI;
        } else {
            rad = (value + PI) % TAU - PI;
        }
    }

    @Override
    public double getClampedRadian() {
        return rad;
    }

    @Override
    public void negate() {
        rad = -rad;
    }

    @Override
    public void add(ClampedRadian that) {
        double sum = this.rad + that.rad;
        if (sum >= PI) {
            sum -= TAU;
        } else if (sum < -PI) {
            sum += TAU;
        }
        rad = sum;
    }

    @Override
    public void subtract(ClampedRadian that) {
        double sum = this.rad - that.rad;
        if (sum >= PI) {
            sum -= TAU;
        } else if (sum < -PI) {
            sum += TAU;
        }
        rad = sum;
    }

    @Override
    public void setToVector(@NotNull Point a) {
        rad = a.direction();
    }

    @Override
    public void setToVector(@NotNull Point a, @NotNull Point b) {
        rad = a.relativeDirection(b);
    }

    @Override
    public void setRandom(@NotNull RandomContext rc) {
        rad = TAU * rc.nextDouble() - PI;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        rad = 0.0;
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        rad = Double.NaN;
        return GeometryOperationResult.UNKNOWN;
    }
}
