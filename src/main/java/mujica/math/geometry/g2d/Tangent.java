package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created in va on 2021/9/22.
 * Created on 2022/6/26.
 */
public class Tangent extends Direction2 {

    private static final long serialVersionUID = 0x429e504cc7aaddeaL;

    public static final int AXIS_PX         = 0x0;
    public static final int AXIS_PY         = 0x1;
    public static final int AXIS_NX         = 0x2;
    public static final int AXIS_NY         = 0x3;
    
    public int axis;
    
    public double ratio;

    public Tangent() {
        super();
    }

    public Tangent(int axis, double ratio) {
        super();
        setTangent(axis, ratio);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Tangent(Tangent that) {
        super();
        setTangent(that);
    }

    @Override
    @NotNull
    public Tangent duplicate() {
        return new Tangent(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkNotNaN("ratio", ratio, consumer); // any axis value is healthy; no check
    }

    @Override
    public boolean isHealthy() {
        return ratio == ratio; // any axis value is healthy; no check; using == operator is faster
    }

    @Override
    public double getRadian() {
        final double atan = Math.atan(ratio);
        switch (AXIS_NY & axis) {
            case AXIS_PX:
                return atan;
            case AXIS_PY:
                return atan + HPI;
            case AXIS_NX:
                if (ratio > 0.0) {
                    return atan - PI;
                } else {
                    return atan + PI;
                }
            case AXIS_NY:
                return atan - HPI;
            default: // impossible
                throw new IllegalStateException();
        }
    }

    @Override
    public void setRadian(double value) {
        if (value < 0.0) {
            axis = (int) ((value - 0.25 * PI) * (1.0 / HPI));
        } else {
            axis = (int) ((value + 0.25 * PI) * (1.0 / HPI));
        }
        if ((AXIS_PY & axis) == 0) {
            ratio = Math.tan(value);
        } else {
            ratio = Math.tan(value + HPI);
        }
    }

    @Override
    public double getClampedRadian() {
        return getRadian();
    }

    @Override
    public double cos() {
        final double a = this.ratio;
        final double r = Math.sqrt(1.0 + a * a);
        switch (AXIS_NY & axis) {
            case AXIS_PX:
                return 1.0 / r;
            case AXIS_PY:
                return -a / r;
            case AXIS_NX:
                return -1.0 / r;
            case AXIS_NY:
                return a / r;
            default: // impossible
                throw new IllegalStateException();
        }
    }

    @Override
    public double sin() {
        final double a = this.ratio;
        final double r = Math.sqrt(1.0 + a * a);
        switch (AXIS_NY & axis) {
            case AXIS_PX:
                return a / r;
            case AXIS_PY:
                return 1.0 / r;
            case AXIS_NX:
                return -a / r;
            case AXIS_NY:
                return -1.0 / r;
            default: // impossible
                throw new IllegalStateException();
        }
    }

    public void toPoint(double radius, Point point) {
        final double a = this.ratio;
        radius /= Math.sqrt(1.0 + a * a);
        switch (AXIS_NY & axis) {
            case AXIS_PX:
                point.setPoint(
                        radius,
                        a * radius
                );
                break;
            case AXIS_PY:
                point.setPoint(
                        -a * radius,
                        radius
                );
                break;
            case AXIS_NX:
                point.setPoint(
                        -radius,
                        -a * radius
                );
                break;
            case AXIS_NY:
                point.setPoint(
                        a * radius,
                        -radius
                );
                break;
            default: // impossible
                throw new IllegalStateException();
        }
    }

    @Override
    public double tan() {
        if ((AXIS_PY & axis) == 0) {
            return ratio;
        } else {
            return -1.0 / ratio;
        }
    }

    @Override
    public double cot() {
        if ((AXIS_PY & axis) == 0) {
            return 1.0 / ratio;
        } else {
            return -ratio;
        }
    }

    @Override
    public boolean test(Quadrant q) {
        switch (q) {
            case Q1:
                return axis == AXIS_PX && 0.0 < ratio && ratio < Double.POSITIVE_INFINITY
                        || axis == AXIS_PY && ratio < 0.0 && Double.NEGATIVE_INFINITY < ratio;
            case Q2:
                return axis == AXIS_PY && 0.0 < ratio && ratio < Double.POSITIVE_INFINITY
                        || axis == AXIS_NX && ratio < 0.0 && Double.NEGATIVE_INFINITY < ratio;
            case Q3:
                return axis == AXIS_NX && 0.0 < ratio && ratio < Double.POSITIVE_INFINITY
                        || axis == AXIS_NY && ratio < 0.0 && Double.NEGATIVE_INFINITY < ratio;
            case Q4:
                return axis == AXIS_NY && 0.0 < ratio && ratio < Double.POSITIVE_INFINITY
                        || axis == AXIS_PX && ratio < 0.0 && Double.NEGATIVE_INFINITY < ratio;
            case XPN:
                return (axis == AXIS_PX || axis == AXIS_NX) && ratio == 0.0
                        || (axis == AXIS_PY || axis == AXIS_NY) && (ratio == Double.POSITIVE_INFINITY || ratio == Double.NEGATIVE_INFINITY);
            case YPN:
                return (axis == AXIS_PY || axis == AXIS_NY) && ratio == 0.0
                        || (axis == AXIS_PX || axis == AXIS_NX) && (ratio == Double.POSITIVE_INFINITY || ratio == Double.NEGATIVE_INFINITY);
            case XYPN:
                return ratio == 0.0 || ratio == Double.POSITIVE_INFINITY || ratio == Double.NEGATIVE_INFINITY;
            default:
                return false;
        }
    }

    private static final Quadrant[] QUADRANTS_A = {
            Quadrant.XP, Quadrant.YP, Quadrant.XN, Quadrant.YN, Quadrant.XP, Quadrant.YP, Quadrant.XN
    };

    private static final Quadrant[] QUADRANTS_B = {
            Quadrant.Q1, Quadrant.Q2, Quadrant.Q3, Quadrant.Q4, Quadrant.Q1, Quadrant.Q2, Quadrant.Q3
    };

    @Override
    public Quadrant quadrant() {
        final double ratio = this.ratio;
        if (ratio >= 0.0) {
            if (ratio == 0.0) {
                return QUADRANTS_A[axis];
            } else if (ratio == Double.POSITIVE_INFINITY) {
                return QUADRANTS_A[axis + 1];
            } else {
                return QUADRANTS_B[axis];
            }
        } else {
            if (ratio == Double.NEGATIVE_INFINITY) {
                return QUADRANTS_A[axis + 3];
            } else if (ratio == ratio) {
                return QUADRANTS_B[axis + 3];
            } else {
                return null;
            }
        }
    }

    public void setTangent(int axis, double ratio) {
        this.axis = axis;
        this.ratio = ratio;
    }

    public void setTangent(Tangent that) {
        setTangent(that.axis, that.ratio);
    }

    @Override
    public void add(Tangent that) {
        setToSum(this, that);
    }

    @Override
    public void subtract(Tangent that) {
        setToDifference(this, that);
    }

    public void setToSum(Tangent a, Tangent b) {
        a.normalize();
        b.normalize();
        setTangent(
                a.axis + b.axis,
                (a.ratio + b.ratio) / (1.0 - a.ratio * b.ratio)
        );
    }

    public void setToDifference(Tangent a, Tangent b) {
        a.normalize();
        b.normalize();
        setTangent(
                a.axis - b.axis,
                (a.ratio - b.ratio) / (1.0 + a.ratio * b.ratio)
        );
    }

    public void setToVector(double vx, double vy) {
        if (Math.abs(vx) < Math.abs(vy)) {
            axis = vy < 0.0 ? AXIS_NY : AXIS_PY;
            ratio = -vx / vy;
        } else {
            axis = vx < 0.0 ? AXIS_NX : AXIS_PX;
            ratio = vy / vx;
        }
    }

    @Override
    public void setToVector(@NotNull Point a) {
        setToVector(a.x, a.y);
    }

    @Override
    public void setToVector(@NotNull Point a, @NotNull Point b) {
        setToVector(b.x - a.x, b.y - a.y);
    }

    /**
     * The random is not uniform in radian
     */
    @Override
    public void setRandom(@NotNull RandomContext rc) {
        axis = AXIS_NY & rc.nextInt();
        ratio = 2.0 * rc.nextDouble() - 1.0;
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        axis = AXIS_PX;
        ratio = 0.0;
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult invalidate() {
        axis = AXIS_PX;
        ratio = Double.NaN;
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult normalize() {
        if (ratio >= 1.0) {
            axis++;
            ratio = -1.0 / ratio;
        }
        if (ratio < -1.0) {
            axis--;
            ratio = -1.0 / ratio;
        }
        axis &= AXIS_NY;
        return GeometryOperationResult.UNKNOWN;
    }

    private static final String[] AXES_NAMES = {"+X", "+Y", "-X", "-Y"};

    public String axisToString() {
        return AXES_NAMES[AXIS_NY & axis];
    }

    @Override
    public String toString() {
        return String.format("(%s, %.4f)", axisToString(), ratio);
    }
}
