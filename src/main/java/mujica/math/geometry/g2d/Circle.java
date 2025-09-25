package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Created in existence on 2018/7/9, named MtCircleD3.
 * Recreated in coo on 2020/7/20, named PlaneCircle.
 * Recreated in va on 2021/9/10.
 * Recreated on 2022/10/3.
 */
@CodeHistory(date = "2018/7/9", project = "existence", name = "MtCircleD3")
@CodeHistory(date = "2020/7/20", project = "coo", name = "PlaneCircle")
@CodeHistory(date = "2021/9/10", project = "va")
@CodeHistory(date = "2022/10/3", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class Circle<P extends Point> extends OriginJordan2<P> {

    private static final long serialVersionUID = 0x34cbbd3bb6b299c1L;

    public double r;

    public Circle(@NotNull P o) {
        super(o);
    }

    public Circle(@NotNull P o, double r) {
        super(o);
        this.r = r;
    }

    @NotNull
    public static Circle<Point> origin() {
        return new Circle<>(new Point());
    }

    @NotNull
    public static Circle<Point> origin(double r) {
        return new Circle<>(new Point(), r);
    }

    @NotNull
    public static Circle<Point> unit() {
        return new Circle<>(new Point(), 1.0);
    }

    @NotNull
    public static Circle<Point> unit(@NotNull Point o) {
        return new Circle<>(o, 1.0);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public Circle<P> duplicate() {
        return new Circle<>((P) o.duplicate(), r);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        o.checkHealth(consumer);
        checkPositive("r", r, consumer);
    }

    @Override
    public boolean isHealthy() {
        return o.isHealthy() && r > 0.0;
    }

    @Override
    public double measure1() {
        return Direction2.TAU * r;
    }

    @Override
    public double measure2() {
        return Direction2.PI * r * r;
    }

    @Override
    public boolean test(@NotNull Point point) {
        return Point.squareNorm(this.o, point) <= r * r;
    }

    @Override
    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        bound.includeCircle(o.x, o.y, r);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestBound(@NotNull Bound bound) {
        bound.setBound(o.x - r, o.x + r, o.y - r, o.y + r);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestCircle(@NotNull Circle<?> circle) {
        circle.setCircle(this);
        return GeometryOperationResult.UNKNOWN;
    }

    public boolean intersects(Circle<?> that) {
        final double x = that.o.x - this.o.x;
        final double y = that.o.y - this.o.y;
        final double r = that.r + this.r;
        return x * x + y * y < r * r;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean intersects(Line<?> line) {
        double rr = r * r;
        double lx = line.p2.x - line.p1.x;
        double ly = line.p2.y - line.p1.y;
        double ll = lx * lx + ly * ly;
        double px = o.x - line.p1.x;
        double py = o.y - line.p1.y;
        double n = lx * py - ly * px;
        if (n * n < ll * rr) {
            n = lx * px + ly * py;
            if (0 < n && n < ll) {
                return true;
            }
            if (px * px + py * py < rr) {
                return true;
            }
            px -= lx;
            py -= ly;
            if (px * px + py * py < rr) {
                return true;
            }
        }
        return false;
    }

    public boolean cutLine(Line<?> src, Line<?> dst) {
        double lx = src.p1.x - o.x;
        double ly = src.p1.y - o.y;
        double tx = src.p2.x - src.p1.x;
        double ty = src.p2.y - src.p1.y;
        double a = tx * tx + ty * ty;
        double b = lx * tx + ly * ty;
        double c = lx * lx + ly * ly - r * r;
        c = b * b - a * c;
        if (!(c > 0.0)) {
            return false;
        }
        c = Math.sqrt(c);
        double t1 = -(b + c) / a;
        double t2 = (c - b) / a;
        if (t1 < 1.0 && 0.0 < t2) {
            if (t1 <= 0.0) {
                dst.p1.setPoint(src.p1);
            } else {
                dst.p1.setToLerp(src.p1, src.p2, t1);
            }
            if (1.0 <= t2) {
                dst.p2.setPoint(src.p2);
            } else {
                dst.p2.setToLerp(src.p1, src.p2, t2);
            }
            return true;
        }
        return false;
    }

    public boolean cutHalfPlane(HalfPlane src, Line<?> dst) {
        final Point shadow = new Point();
        if (src.project(o, shadow) == GeometryOperationResult.FAIL) {
            return false;
        }
        double d = Point.euclidDistance(o, shadow);
        if (d < r) {
            d = Math.sqrt((r * r - d * d) / src.squareAB());
            dst.p1.setPoint(
                    shadow.x - d * src.b,
                    shadow.y + d * src.a
            );
            dst.p2.setPoint(
                    shadow.x + d * src.b,
                    shadow.y - d * src.a
            );
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public GeometryOperationResult enterArea(@NotNull Point point) {
        double d = Point.euclidDistance(this.o, point);
        if (d <= r) {
            return GeometryOperationResult.REMAIN;
        }
        d = r / d;
        point.setPoint(
                this.o.x + d * (point.x - this.o.x),
                this.o.y + d * (point.y - this.o.y)
        );
        return GeometryOperationResult.MODIFIED;
    }

    @Override
    @NotNull
    public GeometryOperationResult leaveArea(@NotNull Point point) {
        double d = Point.euclidDistance(this.o, point);
        if (d >= r) {
            return GeometryOperationResult.REMAIN;
        }
        if (d < EPSILON) {
            point.setPoint(
                    this.o.x + this.r,
                    this.o.y
            );
        } else {
            d = r / d;
            point.setPoint(
                    this.o.x + d * (point.x - this.o.x),
                    this.o.y + d * (point.y - this.o.y)
            );
        }
        return GeometryOperationResult.MODIFIED;
    }

    @Override
    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        double u, v;
        do {
            u = 2.0 * rc.nextDouble() - 1.0;
            v = 2.0 * rc.nextDouble() - 1.0;
        } while (u * u + v * v > 1.0);
        point.setPoint(
                this.o.x + u * r,
                this.o.y + v * r
        );
        return GeometryOperationResult.UNKNOWN;
    }

    public void setCircle(double cx, double cy, double r) {
        this.o.setPoint(cx, cy);
        this.r = r;
    }

    public void setCircle(@NotNull Point o, double r) {
        this.o.setPoint(o);
        this.r = r;
    }

    public void setCircle(@NotNull Circle<? extends Point> that) {
        this.o.setPoint(that.o);
        this.r = that.r;
    }

    public void setRadius(double r) {
        this.r = r;
    }

    public void setRadius(@NotNull Point point) {
        this.r = Point.euclidDistance(this.o, point);
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        o.reset();
        r = 0.0;
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        o.invalidate();
        r = Double.NaN;
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult repair() {
        if (r < 0.0) {
            r = -r;
            return GeometryOperationResult.MODIFIED;
        } else {
            return GeometryOperationResult.REMAIN;
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult normalize() {
        r = 1.0;
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public int hashCode() {
        return o.hashCode() * 0x2d + Double.hashCode(r);
    }

    public boolean equalCircle(@NotNull Circle<?> that) {
        return this.o.equalPoint(that.o) && this.r == that.r;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj || obj instanceof Circle<?> && equalCircle((Circle<?>) obj);
    }

    @Override
    public String toString() {
        return String.format("Circle[%s, %.3f]", o, r);
    }
}
