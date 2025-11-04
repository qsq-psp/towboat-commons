package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Optional class for angle representation
 * All directions are mutable
 */
@CodeHistory(date = "2018/2/19", project = "aquarium", name = "AngleD")
@CodeHistory(date = "2018/7/9", project = "existence", name = "Angle")
@CodeHistory(date = "2020/2/27", project = "coo", name = "Angle")
@CodeHistory(date = "2022/6/26", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public abstract class Direction2 extends Geometry {

    private static final long serialVersionUID = 0x2e052c1d0ecbb94bL;

    public static final double PI = Math.PI;

    public static final double HPI = 0.5 * PI;

    public static final double TAU = 2.0 * PI; // Named DPI, double pi

    public static final double INV_PI = 1.0 / PI;

    public static final double INV_HPI = 2.0 / PI;

    public static final double INV_TAU = 0.5 / PI;

    public Direction2() {
        super();
    }

    @NotNull
    @Override
    public Direction2 duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkNotNaN("radian", getRadian(), consumer);
    }

    public abstract double getRadian();

    public abstract void setRadian(double value);

    /**
     * The range is [-PI, PI), same as ClampedRadian
     */
    public double getClampedRadian() {
        double rad = getRadian();
        if (rad < 0.0) {
            rad = (rad - PI) % TAU + PI;
        } else {
            rad = (rad + PI) % TAU - PI;
        }
        return rad;
    }

    public double getDegree() {
        return Math.toDegrees(getRadian());
    }

    public void setDegree(double value) {
        setRadian(Math.toRadians(value));
    }

    public double cos() {
        return Math.cos(getRadian());
    }

    public double sin() {
        return Math.sin(getRadian());
    }

    public double tan() {
        return Math.tan(getRadian());
    }

    public double cot() {
        return 1.0 / Math.tan(getRadian());
    }

    public enum Quadrant {

        Q1, Q2, Q3, Q4, XP, YP, XN, YN, XPN, YPN, XYPN;

        private static final long serialVersionUID = 0xe632c9c8493ea6f8L;
    }

    public boolean test(Quadrant q) {
        double rad = getRadian();
        switch (q) {
            case Q1:
                rad %= TAU;
                return 0.0 < rad && rad < HPI || rad < HPI - TAU;
            case Q2:
                rad %= TAU;
                return HPI < rad && rad < PI || HPI - TAU < rad && rad < -PI;
            case Q3:
                rad %= TAU;
                return PI < rad && rad < TAU - HPI || -PI < rad && rad < -HPI;
            case Q4:
                rad %= TAU;
                return TAU - HPI < rad || -HPI < rad && rad < 0.0;
            case XP:
                rad %= TAU;
                return rad == 0.0;
            case YP:
                rad %= TAU;
                return rad == HPI || rad == HPI - TAU;
            case XN:
                rad %= TAU;
                return rad == PI || rad == -PI;
            case YN:
                rad %= TAU;
                return rad == TAU - HPI || rad == -HPI;
            case XPN:
                rad %= PI;
                return rad == 0.0;
            case YPN:
                rad %= PI;
                return rad == HPI || rad == -HPI;
            case XYPN:
                rad %= HPI;
                return rad == 0.0;
            default:
                return false;
        }
    }

    public Quadrant quadrant() {
        double rad = getRadian() % TAU;
        if (rad < 0.0) {
            rad += TAU;
        }
        if (rad < PI) {
            if (rad < HPI) {
                if (rad == 0.0) {
                    return Quadrant.XP;
                } else {
                    return Quadrant.Q1;
                }
            } else {
                if (rad == HPI) {
                    return Quadrant.YP;
                } else {
                    return Quadrant.Q2;
                }
            }
        } else {
            if (rad < TAU - HPI) {
                if (rad == PI) {
                    return Quadrant.XN;
                } else {
                    return Quadrant.Q3;
                }
            } else {
                if (rad == TAU - HPI) {
                    return Quadrant.YN;
                } else {
                    return Quadrant.Q4;
                }
            }
        }
    }

    public void negate() {
        setRadian(-getRadian());
    }

    public void add(Direction2 that) {
        setRadian(this.getRadian() + that.getRadian());
    }

    public void add(ClampedRadian that) {
        add((Direction2) that);
    }

    public void add(Tangent that) {
        add((Direction2) that);
    }

    public void subtract(Direction2 that) {
        setRadian(this.getRadian() - that.getRadian());
    }

    public void subtract(ClampedRadian that) {
        subtract((Direction2) that);
    }

    public void subtract(Tangent that) {
        subtract((Direction2) that);
    }

    public int loopCompare(Direction2 that) {
        double rad = this.getRadian() - that.getRadian();
        double compare = Math.sin(rad);
        if (compare > 0.0) {
            return 1;
        }
        if (compare < 0.0) {
            return -1;
        }
        compare = Math.cos(rad);
        if (compare > 0.0) {
            return 0;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    public void setToVector(@NotNull Point a) {
        setRadian(a.direction());
    }

    /**
     * a is the origin
     */
    public void setToVector(@NotNull Point a, @NotNull Point b) {
        setRadian(a.relativeDirection(b));
    }

    public void setVector(double length, @NotNull Point dst) {
        dst.setPoint(cos() * length, sin() * length);
    }

    public void addVector(double length, @NotNull Point dst) {
        dst.addVector(cos() * length, sin() * length);
    }

    public void setRandom(@NotNull RandomContext rc) {
        setRadian(TAU * rc.nextDouble());
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        setRadian(0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult invalidate() {
        setRadian(Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    public boolean equalsDirectionEpsilon(Direction2 that) {
        final double dx = this.cos() - that.cos();
        final double dy = this.sin() - that.sin();
        return Math.hypot(dx, dy) < EPSILON;
    }

    public boolean equalsDirection(Direction2 that) {
        return this.cos() == that.cos() && this.sin() == that.sin();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Direction2 && equalsDirection((Direction2) obj));
    }

    public static double btan(double increment) {
        increment *= 0.5;
        return (4.0 / 3.0) * Math.sin(increment) / (1.0 + Math.cos(increment));
    }
}
