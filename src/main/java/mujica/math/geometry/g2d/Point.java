package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2018/3/16", name = "PointV2")
@CodeHistory(date = "2018/7/9", name = "MtPointD2")
@CodeHistory(date = "2022/6/5")
@CodeHistory(date = "2025/3/2")
@Stable(date = "2025/8/4")
public class Point extends Geometry implements Comparable<Point> {

    private static final long serialVersionUID = 0x2ee216dfd4ff322aL;

    public double x;

    public double y;

    public Point() {
        super();
    }

    public Point(double x, double y) {
        super();
        setPoint(x, y);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Point(Point that) {
        super();
        setPoint(that);
    }

    @NotNull
    public static Point[] newArray(int length) {
        final Point[] array = new Point[length];
        for (int index = 0; index < length; index++) {
            array[index] = new Point();
        }
        return array;
    }

    @Override
    @NotNull
    public Point duplicate() {
        return new Point(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkNotNaN("x", x, consumer);
        checkNotNaN("y", y, consumer);
    }

    @Override
    public boolean isHealthy() {
        return x == x && y == y; // faster
    }

    public double squareNorm() {
        return x * x + y * y;
    }

    public static double squareNorm(Point a, Point b) {
        final double x = a.x - b.x;
        final double y = a.y - b.y;
        return x * x + y * y;
    }

    public double euclidNorm() {
        return StrictMath.hypot(x, y);
    }

    public static double euclidDistance(Point a, Point b) {
        return StrictMath.hypot(a.x - b.x, a.y - b.y);
    }

    public double manhattanNorm() {
        return Math.abs(x) + Math.abs(y);
    }

    public static double manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public double chebyshevNorm() {
        return Math.max(Math.abs(x), Math.abs(y));
    }

    public static double chebyshevDistance(Point a, Point b) {
        return Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
    }

    public static double innerProduct(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public double relativeInnerProduct(Point a, Point b) {
        return (a.x - x) * (b.x - x) + (a.y - y) * (b.y - y);
    }

    public static double crossProduct(Point a, Point b) {
        return a.x * b.y - b.x * a.y;
    }

    public double relativeCrossProduct(Point a, Point b) {
        return (a.x - x) * (b.y - y) - (a.y - y) * (b.x - x);
    }

    public static double cos(Point a, Point b) {
        return innerProduct(a, b) / StrictMath.sqrt(a.squareNorm() * b.squareNorm());
    }

    public double relativeCos(Point a, Point b) {
        double xa = a.x - x;
        double ya = a.y - y;
        double xb = b.x - x;
        double yb = b.y - y;
        return (xa * xb + ya * yb) / StrictMath.sqrt((xa * xa + ya * ya) * (xb * xb + yb * yb));
    }

    public static double sin(Point a, Point b) {
        return crossProduct(a, b) / StrictMath.sqrt(a.squareNorm() * b.squareNorm());
    }

    public double relativeSin(Point a, Point b) {
        final double xa = a.x - x;
        final double ya = a.y - y;
        final double xb = b.x - x;
        final double yb = b.y - y;
        return (xa * yb + ya * xb) / StrictMath.sqrt((xa * xa + ya * ya) * (xb * xb + yb * yb));
    }

    public double direction() {
        return Math.atan2(y, x);
    }

    /**
     * this is the origin
     */
    public double relativeDirection(Point that) {
        return Math.atan2(that.y - this.y, that.x - this.x);
    }

    public double includedAngle(Point that) {
        return Math.atan2(
                this.x * that.y - this.y * that.x,
                this.x * that.x + this.y * that.y
        );
    }

    public double relativeIncludedAngle(Point a, Point b) {
        final double xa = a.x - x;
        final double ya = a.y - y;
        final double xb = b.x - x;
        final double yb = b.y - y;
        return Math.atan2(
                xa * yb - ya * xb,
                xa * xb + ya * yb
        );
    }

    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPoint(@NotNull Point that) {
        setPoint(that.x, that.y);
    }

    @NotNull
    public Point createPoint(double x, double y) {
        return new Point(x, y);
    }

    @NotNull
    public Point createPoint(@NotNull Consumer<Point> setMethod) {
        final Point point = new Point();
        setMethod.accept(point);
        return point;
    }

    public void addVector(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void addVector(@NotNull Point that) {
        addVector(that.x, that.y);
    }

    public void setToSum(@NotNull Point a, @NotNull Point b) {
        setPoint(
                a.x + b.x,
                a.y + b.y
        ); // maybe this == a || this == b, so use setPoint
    }

    @NotNull
    public Point createSum(@NotNull Point a, @NotNull Point b) {
        return createPoint(
                a.x + b.x,
                a.y + b.y
        );
    }

    public void subtract(@NotNull Point that) {
        setPoint(
                this.x - that.x,
                this.y - that.y
        ); // maybe this == that, so use setPoint
    }

    public void setToDifference(@NotNull Point a, @NotNull Point b) {
        setPoint(
                a.x - b.x,
                a.y - b.y
        ); // maybe this == a || this == b, so use setPoint
    }

    @NotNull
    public static Point createDifference(@NotNull Point a, @NotNull Point b) {
        return new Point(
                a.x - b.x,
                a.y - b.y
        );
    }

    public void multiply(double v) {
        setPoint(
                v * x,
                v * y
        );
    }

    public void setToProduct(double v, @NotNull Point that) {
        setPoint(
                v * that.x,
                v * that.y
        );
    }

    @NotNull
    public Point createProduct(double v, @NotNull Point that) {
        return createPoint(
                v * that.x,
                v * that.y
        );
    }

    public void complexConjugate() {
        setToComplexConjugate(this);
    }

    public void setToComplexConjugate(@NotNull Point that) {
        setPoint(that.x, -that.y);
    }

    @NotNull
    public Point createComplexConjugate(@NotNull Point that) {
        return createPoint(that.x, -that.y);
    }

    public void complexMultiply(@NotNull Point that) {
        setToComplexProduct(this, that);
    }

    public void setToComplexProduct(@NotNull Point a, @NotNull Point b) {
        setPoint(
                a.x * b.x - a.y * b.y,
                a.x * b.y + a.y * b.x
        ); // maybe this == a || this == b, so use setPoint
    }

    @NotNull
    public Point createComplexProduct(@NotNull Point a, @NotNull Point b) {
        return createPoint(
                a.x * b.x - a.y * b.y,
                a.x * b.y + a.y * b.x
        );
    }

    public void complexInvert() {
        setToComplexReciprocal(this);
    }

    public void setToComplexReciprocal(@NotNull Point that) {
        final double s = that.squareNorm();
        setPoint(
                that.x / s,
                -that.y / s
        );
    }

    @NotNull
    public Point createComplexReciprocal(@NotNull Point that) {
        final double s = that.squareNorm();
        return createPoint(
                that.x / s,
                -that.y / s
        );
    }

    public void complexDivide(@NotNull Point that) {
        setToComplexQuotient(this, that);
    }

    public void setToComplexQuotient(@NotNull Point a, @NotNull Point b) {
        final double s = b.squareNorm();
        setPoint(
                (a.x * b.x + a.y * b.y) / s,
                (a.y * b.x - a.x * b.y) / s
        );
    }

    @NotNull
    public Point createComplexQuotient(@NotNull Point a, @NotNull Point b) {
        final double s = b.squareNorm();
        return createPoint(
                (a.x * b.x + a.y * b.y) / s,
                (a.y * b.x - a.x * b.y) / s
        );
    }

    public void complexPower(int exp) {
        if (exp == 1) {
            return;
        }
        setToComplexPower(this, exp);
    }

    public void setToComplexPower(@NotNull Point that, int exp) {
        if (exp == 0) {
            setPoint(1.0, 0.0);
            return; // fixed on 2022/10/23
        }
        double sx = that.x;
        double sy = that.y;
        if (exp < 0) {
            if (exp == Integer.MIN_VALUE) {
                throw new IllegalArgumentException();
            }
            double s = sx * sx + sy * sy;
            sx = sx / s;
            sy = -sy / s;
            exp = -exp;
        }
        double tx = sx;
        double ty = sy;
        for (int shift = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exp); shift >= 0; shift--) {
            double t = tx * tx - ty * ty;
            ty = 2 * tx * ty;
            tx = t;
            if ((exp & (1 << shift)) != 0) {
                t = tx * sx - ty * sy;
                ty = tx * sy + ty * sx;
                tx = t;
            }
        }
        setPoint(tx, ty);
    }

    @NotNull
    public Point createComplexPower(int exp) {
        return createPoint(that -> that.setToComplexPower(this, exp));
    }

    public void complexPower(double exp) {
        if (exp == 1.0) {
            return;
        }
        setToComplexPower(this, exp);
    }

    public void setToComplexPower(@NotNull Point that, double exp) {
        final double mod = StrictMath.pow(that.squareNorm(), 0.5 * exp);
        final double arg = that.direction() * exp;
        setPoint(
                mod * Math.cos(arg),
                mod * Math.sin(arg)
        );
    }

    @NotNull
    public Point createComplexPower(double exp) {
        return createPoint(that -> that.setToComplexPower(this, exp));
    }

    /**
     * exp(z) = exp(x + i * y) = exp(x) * (cos(y) + i * sin(y))
     */
    public void setToComplexExponent(@NotNull Point that) {
        final double mod = Math.exp(that.x);
        setPoint(
                mod * Math.cos(that.y),
                mod * Math.sin(that.y)
        );
    }

    @NotNull
    public Point createComplexExponent() {
        final double mod = Math.exp(x);
        return createPoint(
                mod * Math.cos(y),
                mod * Math.sin(y)
        );
    }

    public void setToComplexNaturalLogarithm(@NotNull Point that) {
        setPoint(
                0.5 * Math.log(that.squareNorm()),
                that.direction()
        );
    }

    @NotNull
    public Point createComplexNaturalLogarithm() {
        return createPoint(
                0.5 * Math.log(squareNorm()),
                direction()
        );
    }

    public void complexCosine() {
        setToComplexCosine(this);
    }

    public void setToComplexCosine(@NotNull Point that) {
        setPoint(
                Math.cos(that.x) * Math.cosh(that.y),
                -Math.sin(that.x) * Math.sinh(that.y)
        );
    }

    @NotNull
    public Point createComplexCosine() {
        return createPoint(
                Math.cos(x) * Math.cosh(y),
                -Math.sin(x) * Math.sinh(y)
        );
    }

    public void complexHyperbolicCosine() {
        setToComplexHyperbolicCosine(this);
    }

    public void setToComplexHyperbolicCosine(@NotNull Point that) {
        setPoint(
                Math.cosh(that.x) * Math.cos(that.y),
                Math.sinh(that.x) * Math.sin(that.y)
        );
    }

    @NotNull
    public Point createComplexHyperbolicCosine() {
        return createPoint(
                Math.cosh(x) * Math.cos(y),
                Math.sinh(x) * Math.sin(y)
        );
    }

    public void complexSine() {
        setToComplexSine(this);
    }

    public void setToComplexSine(@NotNull Point that) {
        setPoint(
                Math.sin(that.x) * Math.cosh(that.y),
                Math.cos(that.x) * Math.sinh(that.y)
        );
    }

    @NotNull
    public Point createComplexSine() {
        return createPoint(
                Math.sin(x) * Math.cosh(y),
                Math.cos(x) * Math.sinh(y)
        );
    }

    public void complexHyperbolicSine() {
        setToComplexHyperbolicSine(this);
    }

    public void setToComplexHyperbolicSine(@NotNull Point that) {
        setPoint(
                Math.sinh(that.x) * Math.cos(that.y),
                Math.cosh(that.x) * Math.sin(that.y)
        );
    }

    @NotNull
    public Point createComplexHyperbolicSine() {
        return createPoint(
                Math.sinh(x) * Math.cos(y),
                Math.cosh(x) * Math.sin(y)
        );
    }

    public void setToMedian(@NotNull Point p1, @NotNull Point p2) {
        setPoint(
                0.5 * (p1.x + p2.x),
                0.5 * (p1.y + p2.y)
        ); // maybe this == p1 || this == p2, so use setPoint
    }

    @NotNull
    public Point createMedian(@NotNull Point p1, @NotNull Point p2) {
        return createPoint(
                0.5 * (p1.x + p2.x),
                0.5 * (p1.y + p2.y)
        );
    }

    /**
     * LERP = Linear Interpolation
     */
    public void setToLerp(@NotNull Point p1, @NotNull Point p2, double w2) {
        setPoint(
                p1.x + w2 * (p2.x - p1.x),
                p1.y + w2 * (p2.y - p1.y)
        ); // maybe this == p1 || this == p2, so use setPoint
    }

    /**
     * LERP = Linear Interpolation
     */
    @NotNull
    public static Point lerp(@NotNull Point p1, @NotNull Point p2, double w2) {
        return new Point(
                p1.x + w2 * (p2.x - p1.x),
                p1.y + w2 * (p2.y - p1.y)
        );
    }

    /**
     * LERP = Linear Interpolation
     */
    public void setToLerp(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3,
                          double w2, double w3) {
        setPoint(
                p1.x + w2 * (p2.x - p1.x) + w3 * (p3.x - p1.x),
                p1.y + w2 * (p2.y - p1.y) + w3 * (p3.y - p1.y)
        );
    }

    /**
     * LERP = Linear Interpolation
     */
    @NotNull
    public static Point lerp(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3,
                             double w2, double w3) {
        return new Point(
                p1.x + w2 * (p2.x - p1.x) + w3 * (p3.x - p1.x),
                p1.y + w2 * (p2.y - p1.y) + w3 * (p3.y - p1.y)
        );
    }

    public static void exchange(@NotNull Point a, @NotNull Point b) {
        double x = a.x;
        double y = a.y;
        a.x = b.x;
        a.y = b.y;
        b.x = x;
        b.y = y;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setPoint(0.0, 0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setPoint(Double.NaN, Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult normalize() {
        final double r = euclidNorm();
        if (Math.abs(r - 1.0) < EPSILON) {
            return GeometryOperationResult.REMAIN;
        } else if (r == r) {
            setPoint(
                    x / r,
                    y / r
            );
            return GeometryOperationResult.MODIFIED;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    public int vectorLength() {
        return 2;
    }

    @Override
    public double vectorComponent(int index) {
        if (index == 0) {
            return x;
        } else if (index == 1) {
            return y;
        } else {
            return 1.0; // homogeneous coordinates
        }
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 0x2d + Double.hashCode(y);
    }

    @Override
    public int compareTo(@NotNull Point that) {
        final int compare = Double.compare(this.x, that.x);
        if (compare != 0) {
            return compare;
        }
        return Double.compare(this.y, that.y);
    }

    public boolean equalPointEpsilon(@NotNull Point that) {
        return euclidDistance(this, that) < EPSILON;
    }

    public boolean equalPoint(@NotNull Point that) {
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Point && equalPoint((Point) obj);
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
