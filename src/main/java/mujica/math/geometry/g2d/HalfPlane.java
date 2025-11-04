package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * a * x + b * y + c > 0
 */
@CodeHistory(date = "2018/3/21", project = "aquarium", name = "GeneralLineV2")
@CodeHistory(date = "2020/3/10", project = "coo", name = "GeneralLine")
@CodeHistory(date = "2021/9/10", project = "va", name = "Line")
@CodeHistory(date = "2022/6/6")
public class HalfPlane extends Jordan2 {

    private static final long serialVersionUID = 0x6c4eece83f57e51bL;

    public double a;

    public double b;

    public double c;

    public HalfPlane() {
        super();
    }

    public HalfPlane(double a, double b, double c) {
        super();
        setHalfPlane(a, b, c);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public HalfPlane(HalfPlane that) {
        super();
        setHalfPlane(that);
    }

    @Override
    @NotNull
    public HalfPlane duplicate() {
        return new HalfPlane(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkNotNaN("c", c, consumer);
        final double squareAB = squareAB();
        if (!(squareAB > EPSILON)) {
            consumer.accept(new RuntimeException("a * a + b * b = " + squareAB + " too small"));
        }
    }

    @Override
    public boolean isHealthy() {
        return c == c && squareAB() > EPSILON;
    }

    public double x2y(double x) {
        return -(a * x + c) / b;
    }

    public double y2x(double y) {
        return -(b * y + c) / a;
    }

    /**
     * @param point both src and dst
     */
    public void x2y(@NotNull Point point) {
        point.y = x2y(point.x);
    }

    /**
     * @param point both src and dst
     */
    public void y2x(@NotNull Point point) {
        point.x = y2x(point.y);
    }

    public double squareAB() {
        return a * a + b * b;
    }

    public double squareNorm() {
        return a * a + b * b + c * c;
    }

    @NotNull
    public GeometryOperationResult toLine(@NotNull Line<?> line) {
        return toLine(line, 1.0);
    }

    @NotNull
    public GeometryOperationResult toLine(@NotNull Line<?> line, double hintLength) {
        if (!(hintLength > Geometry.EPSILON)) {
            return GeometryOperationResult.FAIL;
        }
        double s = squareAB();
        if (s < Geometry.EPSILON) {
            return GeometryOperationResult.FAIL;
        }
        line.p1.setPoint(
                -a * c / s,
                -b * c / s
        );
        s = hintLength / StrictMath.sqrt(s);
        line.p2.setPoint(
                line.p1.x - b * s,
                line.p1.y + a * s
        );
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    public GeometryOperationResult toLine(@NotNull Line<?> line, @NotNull Point hintPoint) {
        return toLine(line, hintPoint, 1.0);
    }

    @NotNull
    public GeometryOperationResult toLine(@NotNull Line<?> line, @NotNull Point hintPoint, double hintLength) {
        if (!hintPoint.isHealthy()) {
            return GeometryOperationResult.FAIL;
        }
        if (!(hintLength > Geometry.EPSILON)) {
            return GeometryOperationResult.FAIL;
        }
        double s = squareAB();
        if (s < Geometry.EPSILON) {
            return GeometryOperationResult.FAIL;
        }
        double f = b * hintPoint.x - a * hintPoint.y;
        line.p1.setPoint(
                (b * f - a * c) / s,
                -(a * f + b * c) / s
        );
        s = hintLength / StrictMath.sqrt(s);
        line.p2.setPoint(
                line.p1.x - b * s,
                line.p1.y + a * s
        );
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public boolean test(@NotNull Point point) {
        return a * point.x + b * point.y + c > 0.0;
    }

    @Override
    @NotNull
    public GeometryOperationResult enterArea(@NotNull Point point) {
        if (a * point.x + b * point.y + c >= 0.0) {
            return GeometryOperationResult.REMAIN;
        } else {
            return project(point, point).or(GeometryOperationResult.MODIFIED);
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult leaveArea(@NotNull Point point) {
        if (a * point.x + b * point.y + c <= 0.0) {
            return GeometryOperationResult.REMAIN;
        } else {
            return project(point, point).or(GeometryOperationResult.MODIFIED);
        }
    }

    @NotNull
    public GeometryOperationResult project(@NotNull Point src, @NotNull Point dst) {
        final double s = squareAB();
        if (s < Geometry.EPSILON) {
            return GeometryOperationResult.FAIL;
        }
        dst.setPoint(
                (b * b * src.x - a * (c + b * src.y)) / s,
                (a * a * src.y - b * (c + a * src.x)) / s
        );
        return GeometryOperationResult.UNKNOWN;
    }

    public double height(@NotNull Point point) {
        return a * point.x + b * point.y + c;
    }

    public void setHalfPlane(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void setHalfPlane(HalfPlane that) {
        setHalfPlane(that.a, that.b, that.c);
    }

    @NotNull
    public GeometryOperationResult setHalfPlane(@NotNull Point p1, @NotNull Point p2) {
        setHalfPlane(p1.y - p2.y, p2.x - p1.x, Point.crossProduct(p1, p2));
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    public GeometryOperationResult setHalfPlane(@NotNull Line<?> line) {
        return setHalfPlane(line.p1, line.p2);
    }

    public void setParallel(@NotNull HalfPlane that, @NotNull Point point) {
        setHalfPlane(
                that.a,
                that.b,
                -(that.a * point.x + that.b * point.y)
        );
    }

    public void setPerpendicular(@NotNull HalfPlane that, @NotNull Point point) {
        setHalfPlane(
                that.b,
                -that.a,
                that.a * point.y - that.b * point.x
        );
    }

    public void flip() {
        setHalfPlane(-a, -b, -c);
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setHalfPlane(1.0, 0.0, 0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setHalfPlane(Double.NaN, Double.NaN, Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult normalize() {
        final double r = StrictMath.hypot(a, b);
        if (Math.abs(r - 1.0) < EPSILON) {
            return GeometryOperationResult.REMAIN;
        } else if (r == r) {
            setHalfPlane(
                    a / r,
                    b / r,
                    c / r
            );
            return GeometryOperationResult.MODIFIED;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    public int vectorLength() {
        return 3;
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int hashCode() {
        return (Double.hashCode(a) * 0x2d + Double.hashCode(b)) * 0x2d + Double.hashCode(c);
    }

    public boolean equalHalfPlaneEpsilon(@NotNull HalfPlane that) {
        double ab = this.a * that.b - that.a * this.b;
        double bc = this.b * that.c - that.b * this.c;
        double ca = this.c * that.a - that.c * this.a;
        return (ab * ab + bc * bc + ca * ca) < EPSILON * this.squareNorm() * that.squareNorm();
    }

    public boolean equalHalfPlane(@NotNull HalfPlane that) {
        return this.a == that.a && this.b == that.b && this.c == that.c;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj || obj instanceof HalfPlane && equalHalfPlane((HalfPlane) obj);
    }

    @Override
    public String toString() {
        return String.format("HalfPlane(%.3f, %.3f, %.3f)", a, b, c);
    }
}
