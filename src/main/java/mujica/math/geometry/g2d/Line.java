package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2018/3/17", project = "aquarium", name = "LineV4")
@CodeHistory(date = "2018/7/9", project = "existence", name = "MtLineD4")
@CodeHistory(date = "2020/3/10", project = "va", name = "PlaneLineSegment")
@CodeHistory(date = "2021/9/10", project = "va", name = "LineSegment")
@CodeHistory(date = "2022/6/6", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class Line<G extends Point> extends Jordan2 implements Curve2 {

    private static final long serialVersionUID = 0xbffc0adddc7943a3L;

    public final G p1;

    public final G p2;

    public Line(G p1, G p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }

    public static Line<Point> points() {
        return new Line<>(new Point(), new Point());
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public Line<G> duplicate() {
        return new Line<>(
                (G) p1.duplicate(),
                (G) p2.duplicate()
        );
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        p1.checkHealth(consumer);
        p2.checkHealth(consumer);
    }

    @Override
    public boolean isHealthy() {
        return p1.isHealthy() && p2.isHealthy();
    }

    public double x2y(double x) {
        final double t = (x - p1.x) / (p2.x - p1.x);
        if (0.0 <= t && t <= 1.0) {
            return p1.y + (p2.y - p1.y) * t;
        } else {
            return Double.NaN;
        }
    }

    public double y2x(double y) {
        final double t = (y - p1.y) / (p2.y - p1.y);
        if (0.0 <= t && t <= 1.0) {
            return p1.x + (p2.x - p1.x) * t;
        } else {
            return Double.NaN;
        }
    }

    /**
     * @param point both src and dst
     */
    public double x2y(Point point) {
        final double t = (point.x - p1.x) / (p2.x - p1.x);
        point.y = p1.y + (p2.y - p1.y) * t;
        return t;
    }

    /**
     * @param point both src and dst
     */
    public double y2x(Point point) {
        final double t = (point.y - p1.y) / (p2.y - p1.y);
        point.x = p1.x + (p2.x - p1.x) * t;
        return t;
    }

    @Override
    public double measure1() {
        return Point.euclidDistance(p1, p2);
    }

    @Override
    @NotNull
    public GeometryOperationResult interpolate(double position, @NotNull Point point) {
        point.setToLerp(p1, p2, position);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult derivative(double position, @NotNull Point point) {
        point.setToDifference(p2, p1);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        bound.includePoint(p1);
        bound.includePoint(p2);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestBound(@NotNull Bound bound) {
        bound.invalidate();
        includedInto(bound);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestCircle(@NotNull Circle<?> circle) {
        circle.o.setToMedian(p1, p2);
        circle.r = 0.5 * Point.euclidDistance(p1, p2);
        return GeometryOperationResult.UNKNOWN;
    }

    private static boolean xIntervalSeparated(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4) {
        return Math.max(p1.x, p2.x) < Math.min(p3.x, p4.x);
    }

    private static boolean yIntervalSeparated(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4) {
        return Math.max(p1.y, p2.y) < Math.min(p3.y, p4.y);
    }

    private static boolean between(double x, double y) {
        return y <= x && x <= 0.0 || y >= x && x >= 0.0;
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean intersectsSparse(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4) {
        if (xIntervalSeparated(p1, p2, p3, p4)
                || xIntervalSeparated(p4, p3, p2, p1)
                || yIntervalSeparated(p1, p2, p3, p4)
                || yIntervalSeparated(p4, p3, p2, p1)) {
            return false;
        }
        final double det = (p2.x - p1.x) * (p3.y - p4.y) - (p2.y - p1.y) * (p3.x - p4.x);
        if (Math.abs(det) > Geometry.EPSILON) {
            double arg = (p3.x - p1.x) * (p3.y - p4.y) - (p3.y - p1.y) * (p3.x - p4.x);
            if (between(arg, det)) {
                arg = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
                if (between(arg, det)) {
                    return true;
                }
            }
        } else if (det == det) {
            return Math.abs(p1.relativeCrossProduct(p2, p3)) + Math.abs(p3.relativeCrossProduct(p4, p1)) < Geometry.EPSILON;
        }
        return false;
    }

    public static boolean intersectsSparse(@NotNull Line<?> a, @NotNull Line<?> b) {
        return intersectsSparse(a.p1, a.p2, b.p1, b.p2);
    }

    public boolean intersectsSparse(@NotNull Line<?> that) {
        return intersectsSparse(this.p1, this.p2, that.p1, that.p2);
    }

    @SuppressWarnings("RedundantIfStatement")
    public static boolean intersectsDense(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4) {
        final double det = (p2.x - p1.x) * (p3.y - p4.y) - (p2.y - p1.y) * (p3.x - p4.x);
        if (Math.abs(det) > Geometry.EPSILON) {
            double arg = ((p3.x - p1.x) * (p3.y - p4.y) - (p3.y - p1.y) * (p3.x - p4.x));
            if (between(arg, det)) {
                arg = ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x));
                if (between(arg, det)) {
                    return true;
                }
            }
        } else if (det == det) {
            double d1 = Point.manhattanDistance(p1, p2);
            double d2 = Point.manhattanDistance(p3, p4);
            if (d1 > d2) {
                if (d1 > Geometry.EPSILON && Math.abs(p1.relativeCrossProduct(p2, p3)) < Geometry.EPSILON) {
                    return p3.relativeInnerProduct(p1, p2) <= 0.0 || p4.relativeInnerProduct(p1, p2) <= 0.0;
                }
            } else {
                if (d2 > Geometry.EPSILON && Math.abs(p3.relativeCrossProduct(p4, p1)) < Geometry.EPSILON) {
                    return p1.relativeInnerProduct(p3, p4) <= 0.0 || p2.relativeInnerProduct(p3, p4) <= 0.0;
                }
            }
        }
        return false;
    }

    public static boolean intersectsDense(@NotNull Line<?> a, @NotNull Line<?> b) {
        return intersectsDense(a.p1, a.p2, b.p1, b.p2);
    }

    public boolean intersectsDense(@NotNull Line<?> that) {
        return intersectsDense(this.p1, this.p2, that.p1, that.p2);
    }

    /**
     * out.x = t1
     * out.y = t2
     */
    public static boolean intersectionParameter(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4, @NotNull Point out) {
        final double det = (p2.x - p1.x) * (p3.y - p4.y) - (p2.y - p1.y) * (p3.x - p4.x);
        if (Math.abs(det) > Geometry.EPSILON) {
            out.setPoint(
                    ((p3.x - p1.x) * (p3.y - p4.y) - (p3.y - p1.y) * (p3.x - p4.x)) / det,
                    ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) / det
            );
            return true;
        } else {
            return false;
        }
    }

    public static boolean intersectionParameter(@NotNull Line<?> a, @NotNull Line<?> b, @NotNull Point out) {
        return intersectionParameter(a.p1, a.p2, b.p1, b.p2, out);
    }

    public static boolean intersectionPoint(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4, @NotNull Point out) {
        final double det = (p2.x - p1.x) * (p3.y - p4.y) - (p2.y - p1.y) * (p3.x - p4.x);
        if (Math.abs(det) > Geometry.EPSILON) {
            out.setToLerp(p1, p2, ((p3.x - p1.x) * (p3.y - p4.y) - (p3.y - p1.y) * (p3.x - p4.x)) / det);
            return true;
        } else {
            return false;
        }
    }

    public static boolean intersectionPoint(@NotNull Line<?> a, @NotNull Line<?> b, @NotNull Point out) {
        return intersectionPoint(a.p1, a.p2, b.p1, b.p2, out);
    }

    public double minimumDistance(@NotNull Point point) {
        final double dx = p2.x - p1.x;
        final double dy = p2.y - p1.y;
        final double d = dx * dx + dy * dy;
        if (d > Geometry.EPSILON) {
            double w = (dx * (point.x - p1.x) + dy * (point.y - p1.y)) / d;
            if (w <= 0.0) {
                return Point.euclidDistance(p1, point);
            }
            if (w >= 1.0) {
                return Point.euclidDistance(p2, point);
            }
            return Math.abs(dx * (point.y - p1.y) - dy * (point.x - p1.x)) / Math.sqrt(d);
        } else if (d == d) {
            return Point.euclidDistance(p1, point);
        } else {
            return Double.NaN;
        }
    }

    public static double minimumDistance(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3, @NotNull Point p4) {
        double dx, dy, d, w;
        double min;
        int code = 0, between = 0;
        dx = p2.x - p1.x;
        dy = p2.y - p1.y;
        d = dx * dx + dy * dy;
        w = (dx * (p3.x - p1.x) + dy * (p3.y - p1.y)) / d;
        if (w <= 0.0) {
            min = Point.euclidDistance(p1, p3);
            code |= 0x1;
        } else if (w >= 1.0) {
            min = Point.euclidDistance(p2, p3);
            code |= 0x2;
        } else {
            min = Math.abs(dx * (p3.y - p1.y) - dy * (p3.x - p1.x)) / Math.sqrt(d);
            between++;
        }
        w = (dx * (p4.x - p1.x) + dy * (p4.y - p1.y)) / d;
        if (w <= 0.0) {
            min = Math.min(min, Point.euclidDistance(p1, p4));
            code |= 0x4;
        } else if (w >= 1.0) {
            min = Math.min(min, Point.euclidDistance(p2, p4));
            code |= 0x8;
        } else {
            min = Math.min(min, Math.abs(dx * (p4.y - p1.y) - dy * (p4.x - p1.x)) / Math.sqrt(d));
            between++;
        }
        dx = p4.x - p3.x;
        dy = p4.y - p3.y;
        d = dx * dx + dy * dy;
        w = (dx * (p1.x - p3.x) + dy * (p1.y - p3.y)) / d;
        if (w <= 0.0) {
            if ((code & 0x1) == 0) {
                min = Math.min(min, Point.euclidDistance(p3, p1));
            }
        } else if (w >= 1.0) {
            if ((code & 0x4) == 0) {
                min = Math.min(min, Point.euclidDistance(p4, p1));
            }
        } else {
            min = Math.min(min, Math.abs(dx * (p1.y - p3.y) - dy * (p1.x - p3.x)) / Math.sqrt(d));
            between++;
        }
        w = (dx * (p2.x - p3.x) + dy * (p2.y - p3.y)) / d;
        if (w <= 0.0) {
            if ((code & 0x2) == 0) {
                min = Math.min(min, Point.euclidDistance(p3, p2));
            }
        } else if (w >= 1.0) {
            if ((code & 0x8) == 0) {
                min = Math.min(min, Point.euclidDistance(p4, p2));
            }
        } else {
            min = Math.min(min, Math.abs(dx * (p2.y - p3.y) - dy * (p2.x - p3.x)) / Math.sqrt(d));
            between++;
        }
        if (between >= 2) {
            d = (p2.x - p1.x) * (p3.y - p4.y) - (p2.y - p1.y) * (p3.x - p4.x);
            if (Math.abs(d) > Geometry.EPSILON
                    && between((p3.x - p1.x) * (p3.y - p4.y) - (p3.y - p1.y) * (p3.x - p4.x), d)
                    && between((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x), d)
            ) {
                min = 0.0;
            }
        }
        return min;
    }

    public static double minimumDistance(@NotNull Line<?> a, @NotNull Line<?> b) {
        return minimumDistance(a.p1, a.p2, b.p1, b.p2);
    }

    public double minimumDistance(@NotNull Line<?> that) {
        return minimumDistance(this.p1, this.p2, that.p1, that.p2);
    }

    @Override
    @NotNull
    public GeometryOperationResult enterArea(@NotNull Point point) {
        final double dx = p2.x - p1.x;
        final double dy = p2.y - p1.y;
        double w = dx * dx + dy * dy;
        if (w > Geometry.EPSILON) {
            w = (dx * (point.x - p1.x) + dy * (point.y - p1.y)) / w;
            w = Math.max(0.0, Math.min(w, 1.0));
            point.setPoint(
                    p1.x + w * dx,
                    p1.y + w * dy
            );
        } else {
            point.setPoint(p1);
        }
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        point.setToLerp(p1, p2, rc.nextDouble());
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        return p1.reset().or(p2.reset());
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        return p1.invalidate().or(p2.invalidate());
    }

    @Override
    public int hashCode() {
        return p1.hashCode() * 0x2d + p2.hashCode();
    }

    public boolean equalLine(@NotNull Line<?> that) {
        return this.p1.equalPoint(that.p1) && this.p2.equalPoint(that.p2);
    }

    public boolean equalLineExchange(@NotNull Line<?> that) {
        return this.p1.equalPoint(that.p1) && this.p2.equalPoint(that.p2) || this.p1.equalPoint(that.p2) && this.p2.equalPoint(that.p1);
    }

    public boolean equalLineEpsilon(@NotNull Line<?> that) {
        return this.p1.equalPointEpsilon(that.p1) && this.p2.equalPointEpsilon(that.p2);
    }

    public boolean sameLine(@NotNull Line<?> that) {
        return this.p1 == that.p1 && this.p2 == that.p2;
    }

    public boolean sameLineExchange(@NotNull Line<?> that) {
        return this.p1 == that.p1 && this.p2 == that.p2 || this.p1 == that.p2 && this.p2 == that.p1;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj || obj instanceof Line<?> && equalLine((Line<?>) obj);
    }

    @Override
    public String toString() {
        return String.format("Line[%s, %s]", p1, p2);
    }
}
