package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

@CodeHistory(date = "2018/3/18", project = "aquarium", name = "MtRectV4")
@CodeHistory(date = "2020/5/25", project = "coo", name = "PlaneBound")
@CodeHistory(date = "2021/9/10", project = "va")
@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2024/4/1", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class Bound extends Jordan2 {

    private static final long serialVersionUID = 0x50568329a71d52abL;

    public double x1, x2;

    public double y1, y2;

    @SuppressWarnings("unused")
    public Bound(Void origin) {
        super();
    }

    public Bound() {
        super();
        invalidate();
    }

    public Bound(double x, double y) {
        super();
        setBound(x, y);
    }

    public Bound(double x1, double x2, double y1, double y2) {
        super();
        setBound(x1, x2, y1, y2);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Bound(@NotNull Bound that) {
        super();
        setBound(that);
    }

    @Override
    @NotNull
    public Bound duplicate() {
        return new Bound(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!(x1 <= x2)) {
            throw new RuntimeException("bound order x (" + x1 + " <= " + x2 + ") violated");
        }
        if (!(y1 <= y2)) {
            throw new RuntimeException("bound order y (" + y1 + " <= " + y2 + ") violated");
        }
    }

    @Override
    public boolean isHealthy() {
        return x1 <= x2 && y1 <= y2;
    }

    public double width() {
        return x2 - x1;
    }

    public double height() {
        return y2 - y1;
    }

    @Override
    public double measure1() {
        return 2.0 * ((x2 - x1) + (y2 - y1));
    }

    @Override
    public double measure2() {
        return (x2 - x1) * (y2 - y1);
    }

    public boolean testX(double x) {
        return x1 <= x && x <= x2;
    }

    public boolean testY(double y) {
        return y1 <= y && y <= y2;
    }

    @Override
    public boolean test(@NotNull Point point) {
        return x1 <= point.x && point.x <= x2 && y1 <= point.y && point.y <= y2;
    }

    public boolean testCircle(double cx, double cy, double r) {
        return testX(cx + r) && testY(cy + r) && testX(cx - r) && testY(cy - r);
    }

    @Override
    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        bound.includeX(this.x1);
        bound.includeX(this.x2);
        bound.includeY(this.y1);
        bound.includeY(this.y2);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestBound(@NotNull Bound bound) {
        bound.setBound(this);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestCircle(@NotNull Circle<?> circle) {
        circle.setCircle(
                0.5 * (x1 + x2),
                0.5 * (y1 + y2),
                0.5 * StrictMath.hypot(x1 - x2, y1 - y2)
        );
        return GeometryOperationResult.UNKNOWN;
    }

    public static boolean intersects(@NotNull Bound a, @NotNull Bound b) {
        return a.x1 <= b.x2 && a.x2 >= b.x1 && a.y1 <= b.y2 && a.y2 >= b.y1;
    }

    public boolean intersects(@NotNull Bound that) {
        return intersects(this, that);
    }

    public static boolean intersection(@NotNull Bound a, @NotNull Bound b, @NotNull Bound dst) {
        dst.setBound(
                Math.max(a.x1, b.x1),
                Math.min(a.x2, b.x2),
                Math.max(a.y1, b.y1),
                Math.min(a.y2, b.y2)
        );
        return dst.isHealthy();
    }

    public boolean setToIntersection(@NotNull Bound a, @NotNull Bound b) {
        return intersection(a, b, this);
    }

    @SuppressWarnings("ManualMinMaxCalculation")
    public double minimumDistance(@NotNull Bound a, @NotNull Bound b) {
        final double dx = Math.max(a.x1 - b.x2, b.x1 - a.x2);
        final double dy = Math.max(a.y1 - b.y2, b.y1 - a.y2);
        if (dx == dx && dy == dy) {
            if (dx > 0.0) {
                if (dy > 0.0) {
                    return Math.hypot(dx, dy);
                } else {
                    return dx;
                }
            } else {
                if (dy > 0.0) {
                    return dy;
                } else {
                    return 0.0;
                }
            }
        } else {
            return Double.NaN;
        }
    }

    public double minimumDistance(@NotNull Bound that) {
        return minimumDistance(this, that);
    }

    public double manhattanDistance(@NotNull Bound a, @NotNull Bound b) {
        return Math.abs(a.x1 - b.x1)
                + Math.abs(a.y1 - b.y1)
                + Math.abs(a.x2 - b.x2)
                + Math.abs(a.y2 - b.y2);
    }

    public double manhattanDistance(@NotNull Bound that) {
        return manhattanDistance(this, that);
    }

    public boolean cutLine(@NotNull Line<?> src, @NotNull Line<?> dst) {
        double t1 = Double.NaN;
        double t2 = Double.NaN;
        if (src.p1.x < src.p2.x) {
            dst.p1.x = x1;
            t1 = src.x2y(dst.p1);
            if (!testY(dst.p1.y)) {
                t1 = Double.NaN;
            }
            dst.p2.x = x2;
            t2 = src.x2y(dst.p2);
            if (!testY(dst.p2.y)) {
                t2 = Double.NaN;
            }
        } else if (src.p2.x < src.p1.x) {
            dst.p1.x = x2;
            t1 = src.x2y(dst.p1);
            if (!testY(dst.p1.y)) {
                t1 = Double.NaN;
            }
            dst.p2.x = x1;
            t2 = src.x2y(dst.p2);
            if (!testY(dst.p2.y)) {
                t2 = Double.NaN;
            }
        }
        if (t1 != t1 || t2 != t2) {
            if (src.p1.y < src.p2.y) {
                if (t1 != t1) {
                    dst.p1.y = y1;
                    t1 = src.y2x(dst.p1);
                    if (!testX(dst.p1.x)) {
                        return false;
                    }
                }
                if (t2 != t2) {
                    dst.p2.y = y2;
                    t2 = src.y2x(dst.p2);
                    if (!testX(dst.p2.x)) {
                        return false;
                    }
                }
            } else if (src.p2.y < src.p1.y) {
                if (t1 != t1) {
                    dst.p1.y = y2;
                    t1 = src.y2x(dst.p1);
                    if (!testX(dst.p1.x)) {
                        return false;
                    }
                }
                if (t2 != t2) {
                    dst.p2.y = y1;
                    t2 = src.y2x(dst.p2);
                    if (!testX(dst.p2.x)) {
                        return false;
                    }
                }
            }
        }
        if (t1 < 1.0 && 0.0 < t2) {
            if (t1 < 0.0) {
                dst.p1.setPoint(src.p1);
            }
            if (t2 > 1.0) {
                dst.p2.setPoint(src.p2);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean cutHalfPlane(@NotNull HalfPlane src, @NotNull Line<?> dst) {
        boolean c1 = false;
        boolean c2 = false;
        if (src.b < 0.0) {
            dst.p1.x = x1;
            src.x2y(dst.p1);
            if (testY(dst.p1.y)) {
                c1 = true;
            }
            dst.p2.x = x2;
            src.x2y(dst.p2);
            if (testY(dst.p2.y)) {
                c2 = true;
            }
        } else if (src.b > 0.0) {
            dst.p1.x = x2;
            src.x2y(dst.p1);
            if (testY(dst.p1.y)) {
                c1 = true;
            }
            dst.p2.x = x1;
            src.x2y(dst.p2);
            if (testY(dst.p2.y)) {
                c2 = true;
            }
        }
        if (c1 && c2) {
            return true;
        }
        if (src.a > 0.0) {
            if (!c1) {
                dst.p1.y = y1;
                src.y2x(dst.p1);
                if (testX(dst.p1.x)) {
                    c1 = true;
                }
            }
            if (!c2) {
                dst.p2.y = y2;
                src.y2x(dst.p2);
                if (testX(dst.p2.x)) {
                    c2 = true;
                }
            }
        } else if (src.a < 0.0) {
            if (!c1) {
                dst.p1.y = y2;
                src.y2x(dst.p1);
                if (testX(dst.p1.x)) {
                    c1 = true;
                }
            }
            if (!c2) {
                dst.p2.y = y1;
                src.y2x(dst.p2);
                if (testX(dst.p2.x)) {
                    c2 = true;
                }
            }
        }
        return c1 && c2;
    }

    @Override
    @NotNull
    public GeometryOperationResult enterArea(@NotNull Point point) {
        if (!isHealthy()) {
            return GeometryOperationResult.FAIL;
        }
        boolean modified = false;
        if (point.x < x1) {
            point.x = x1;
            modified = true;
        } else if (point.x > x2) {
            point.x = x2;
            modified = true;
        }
        if (point.y < y1) {
            point.y = y1;
            modified = true;
        } else if (point.y > y2) {
            point.y = y2;
            modified = true;
        }
        return modified ? GeometryOperationResult.MODIFIED : GeometryOperationResult.REMAIN;
    }

    @Override
    @NotNull
    public GeometryOperationResult leaveArea(@NotNull Point point) {
        if (!isHealthy()) {
            return GeometryOperationResult.FAIL;
        }
        double mx, my; // moved x, moved y
        if (x1 < point.x && point.x < x2) {
            if (point.x - x1 <= x2 - point.x) {
                mx = x1;
            } else {
                mx = x2;
            }
        } else {
            mx = Double.NaN;
        }
        if (y1 < point.y && point.y < y2) {
            if (point.y - y1 <= y2 - point.y) {
                my = x1;
            } else {
                my = x2;
            }
        } else {
            my = Double.NaN;
        }
        if (mx == mx && my == my) {
            if (Math.abs(point.x - mx) < Math.abs(point.y - my)) {
                point.x = mx;
            } else {
                point.y = my;
            }
            return GeometryOperationResult.MODIFIED;
        } else {
            return GeometryOperationResult.REMAIN;
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        point.setPoint(
                rc.nextDouble(x1, x2),
                rc.nextDouble(y1, y2)
        );
        return GeometryOperationResult.UNKNOWN;
    }

    /**
     * Created in va on 2021/9/15, named Grid.
     * Recreated on 2022/12/10.
     */
    private static abstract class GeometryIterator<G extends Geometry> implements Iterable<G>, Iterator<G>, Serializable {

        private static final long serialVersionUID = 0xdd7e2c00e4b7a683L;

        final int xSize, ySize;

        int xIndex, yIndex;

        GeometryIterator(int xSize, int ySize) {
            super();
            if (xSize <= 0 || ySize <= 0) {
                throw new IllegalArgumentException();
            }
            this.xSize = xSize;
            this.ySize = ySize;
        }

        @Override
        @NotNull
        public Iterator<G> iterator() {
            xIndex = 0;
            yIndex = 0;
            return this;
        }
    }

    @NotNull
    public Iterable<Point> rowMajorLattice(int xSize, int ySize, @Nullable Point point) {
        return new RowMajorPointIterator(xSize, ySize, point);
    }

    private class RowMajorPointIterator extends GeometryIterator<Point> {

        private static final long serialVersionUID = 0x02d7fb1df9f9e168L;

        @NotNull
        final Point point;

        RowMajorPointIterator(int xSize, int ySize, @Nullable Point point) {
            super(xSize, ySize);
            if (point == null) {
                point = new Point();
            }
            this.point = point;
        }

        @Override
        public boolean hasNext() {
            return yIndex <= ySize;
        }

        @NotNull
        @Override
        public Point next() {
            point.setPoint(
                    x1 + (x2 - x1) * xIndex / xSize,
                    y1 + (y2 - y1) * yIndex / ySize
            );
            if (++xIndex > xSize) {
                xIndex = 0;
                ++yIndex;
            }
            return point;
        }
    }

    @NotNull
    public Iterable<Point> columnMajorLattice(int xSize, int ySize, @Nullable Point point) {
        return new ColumnMajorPointIterator(xSize, ySize, point);
    }

    private class ColumnMajorPointIterator extends GeometryIterator<Point> {

        private static final long serialVersionUID = 0xb46ed67be6cede9cL;

        @NotNull
        final Point point;

        ColumnMajorPointIterator(int xSize, int ySize, @Nullable Point point) {
            super(xSize, ySize);
            if (point == null) {
                point = new Point();
            }
            this.point = point;
        }

        @Override
        public boolean hasNext() {
            return xIndex <= xSize;
        }

        @Override
        @NotNull
        public Point next() {
            point.setPoint(
                    x1 + (x2 - x1) * xIndex / xSize,
                    y1 + (y2 - y1) * yIndex / ySize
            );
            if (++yIndex > ySize) {
                yIndex = 0;
                ++xIndex;
            }
            return point;
        }
    }

    @NotNull
    public Iterable<Bound> rowMajorGrid(int xSize, int ySize, @Nullable Bound bound) {
        return new RowMajorBoundIterator(xSize, ySize, bound);
    }

    private class RowMajorBoundIterator extends GeometryIterator<Bound> {

        private static final long serialVersionUID = 0x069a32ee2309869eL;

        @NotNull
        final Bound bound;

        RowMajorBoundIterator(int xSize, int ySize, @Nullable Bound bound) {
            super(xSize, ySize);
            if (bound == null) {
                bound = new Bound();
            } else if (bound == Bound.this) {
                throw new IllegalArgumentException();
            }
            this.bound = bound;
        }

        @Override
        public boolean hasNext() {
            return yIndex < ySize;
        }

        @Override
        public Bound next() {
            final double xStep = (x2 - x1) / xSize;
            final double yStep = (y2 - y1) / ySize;
            bound.setBound(
                    x1 + xStep * xIndex,
                    x1 + xStep * (xIndex + 1),
                    y1 + yStep * yIndex,
                    y1 + yStep * (yIndex + 1)
            );
            if (++xIndex >= xSize) {
                xIndex = 0;
                ++yIndex;
            }
            return bound;
        }
    }

    @NotNull
    public Iterable<Bound> columnMajorGrid(int xSize, int ySize, @Nullable Bound bound) {
        return new ColumnMajorIterator(xSize, ySize, bound);
    }

    private class ColumnMajorIterator extends GeometryIterator<Bound> {

        private static final long serialVersionUID = 0x4c83f32e99f91cf6L;

        @NotNull
        final Bound bound;

        ColumnMajorIterator(int xSize, int ySize, @Nullable Bound bound) {
            super(xSize, ySize);
            if (bound == null) {
                bound = new Bound();
            } else if (bound == Bound.this) {
                throw new IllegalArgumentException();
            }
            this.bound = bound;
        }

        @Override
        public boolean hasNext() {
            return xIndex < xSize;
        }

        @Override
        public Bound next() {
            double xStep = (x2 - x1) / xSize;
            double yStep = (y2 - y1) / ySize;
            bound.setBound(
                    x1 + xStep * xIndex,
                    x1 + xStep * (xIndex + 1),
                    y1 + yStep * yIndex,
                    y1 + yStep * (yIndex + 1)
            );
            if (++yIndex >= ySize) {
                yIndex = 0;
                ++xIndex;
            }
            return bound;
        }
    }

    public void setBound(double x1, double x2, double y1, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void setBound(double x, double y) {
        setBound(x, x, y, y);
    }

    public void setBound(@NotNull Bound that) {
        setBound(that.x1, that.x2, that.y1, that.y2);
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setBound(0.0, 0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setBound(Double.NaN, Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult repair() {
        if (x1 <= x2) {
            if (y1 <= y2) {
                return GeometryOperationResult.REMAIN;
            } else if (y1 > y2) {
                setBound(x1, x2, y2, y1);
                return GeometryOperationResult.MODIFIED;
            } else {
                return GeometryOperationResult.FAIL;
            }
        } else if (x1 > x2) {
            if (y1 <= y2) {
                setBound(x2, x1, y1, y2);
                return GeometryOperationResult.MODIFIED;
            } else if (y1 > y2) {
                setBound(x2, x1, y2, y1);
                return GeometryOperationResult.MODIFIED;
            } else {
                return GeometryOperationResult.FAIL;
            }
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    public void includeX(double x) {
        if (x == x) {
            if (!(x1 < x)) {
                x1 = x;
            }
            if (!(x < x2)) {
                x2 = x;
            }
        }
    }

    public void includeY(double y) {
        if (y == y) {
            if (!(y1 < y)) {
                y1 = y;
            }
            if (!(y < y2)) {
                y2 = y;
            }
        }
    }

    public void includePoint(Point point) {
        includeX(point.x);
        includeY(point.y);
    }

    public void includePoint(double x, double y) {
        includeX(x);
        includeY(y);
    }

    public void includeCircle(Circle<?> circle) {
        includeCircle(circle.o.x, circle.o.y, circle.r);
    }

    public void includeCircle(double cx, double cy, double r) {
        includeX(cx - r);
        includeX(cx + r);
        includeY(cy - r);
        includeY(cy + r);
    }

    private static int quadrant(double dx, double dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 0x2;
            } else {
                return 0x1;
            }
        } else {
            if (dy < 0) {
                return 0x3;
            } else {
                return 0x0;
            }
        }
    }

    /**
     * @param clockwise if rotate from positive Y to positive X
     */
    @SuppressWarnings("DuplicateExpressions")
    public void includeArc(double x1, double y1, double x2, double y2, double x3, double y3, boolean clockwise) {
        double dx1 = x1 - x2;
        double dy1 = y1 - y2;
        double dx2 = x3 - x2;
        double dy2 = y3 - y2;
        int q1 = quadrant(dx1, dy1);
        int q2 = quadrant(dx2, dy2);
        if (q1 == q2) {
            double vp = dx1 * dy2 - dx2 * dy1;
            if (!clockwise && vp >= 0 || clockwise && vp < 0) {
                includePoint(x3, y3);
            } else {
                includeCircle(x2, y2, Math.sqrt(dx1 * dx1 + dy1 * dy1));
            }
            return;
        }
        final double r = Math.sqrt(dx1 * dx1 + dy1 * dy1);
        final int code;
        if (clockwise) {
            code = (q2 << 4) | q1;
        } else {
            code = (q1 << 4) | q2;
        }
        switch (code) {
            case 0x03:
                includeY(y2 - r);
            case 0x02:
                includeX(x2 - r);
            case 0x01:
                includeY(y2 + r);
                break;
            case 0x10:
                includeX(x2 + r);
            case 0x13:
                includeY(y2 - r);
            case 0x12:
                includeX(x2 - r);
                break;
            case 0x21:
                includeY(y2 + r);
            case 0x20:
                includeX(x2 + r);
            case 0x23:
                includeY(y2 - r);
                break;
            case 0x32:
                includeX(x2 - r);
            case 0x31:
                includeY(y2 + r);
            case 0x30:
                includeX(x2 + r);
                break;
        }
    }

    public void includeQuad(double x1, double y1, double x2, double y2, double x3, double y3) {
        includeX(x1);
        includeY(y1);
        includeX(x3);
        includeY(y3);
        double t = (x1 - x2) / (x1 + x3 - 2 * x2);
        if (0.0 < t && t < 1.0) {
            includeX(x1 - (x1 - x2) * t);
        }
        t = (y1 - y2) / (y1 + y3 - 2 * y2);
        if (0.0 < t && t < 1.0) {
            includeY(y1 - (y1 - y2) * t);
        }
    }

    private void includeCubic(double p1, double p2, double p3, double p4, boolean isY) {
        double a2 = 6 * (p2 - p3) + 2 * (p4 - p1);
        double b = 2 * (p1 + p3) - 4 * p2;
        double c2 = 2 * (p2 - p1);
        double delta = b * b - a2 * c2;
        double t;
        if (delta < 0) {
            return;
        }
        p4 += 3 * (p2 - p3) - p1;
        p3 = 3 * (p1 + p3 - 2 * p2);
        p2 = 3 * (p2 - p1);
        if (delta == 0) {
            t = -b / a2;
            if (0.0 < t && t < 1.0) {
                double p = ((p4 * t + p3) * t + p2) * t + p1;
                if (isY) {
                    includeY(p);
                } else {
                    includeX(p);
                }
            }
        }
        delta = Math.sqrt(delta);
        t = -(b + delta) / a2;
        if (0.0 < t && t < 1.0) {
            double p = ((p4 * t + p3) * t + p2) * t + p1;
            if (isY) {
                includeY(p);
            } else {
                includeX(p);
            }
        }
        t = (delta - b) / a2;
        if (0.0 < t && t < 1.0) {
            double p = ((p4 * t + p3) * t + p2) * t + p1;
            if (isY) {
                includeY(p);
            } else {
                includeX(p);
            }
        }
    }

    public void includeCubic(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        includeX(x1);
        includeY(y1);
        includeX(x4);
        includeY(y4);
        includeCubic(x1, x2, x3, x4, false);
        includeCubic(y1, y2, y3, y4, true);
    }

    /**
     * h > 0.0 : outside
     * h < 0.0 : inside
     */
    public void absoluteOffset(double h) {
        x1 -= h;
        y1 -= h;
        x2 += h;
        y2 += h;
        if (x1 > x2) {
            x1 = Double.NaN;
            x2 = Double.NaN;
        }
        if (y1 > y2) {
            y1 = Double.NaN;
            y2 = Double.NaN;
        }
    }

    /**
     * h > 1.0 : outside
     * 0.0 < h < 1.0 : inside
     */
    public void relativeOffset(double h) {
        h = 0.5 * (1.0 - h);
        final double rx = h * width();
        final double ry = h * height();
        x1 -= rx;
        y1 -= ry;
        x2 += rx;
        y2 += ry;
        if (x1 > x2) {
            x1 = Double.NaN;
            x2 = Double.NaN;
        }
        if (y1 > y2) {
            y1 = Double.NaN;
            y2 = Double.NaN;
        }
    }

    @Override
    public int vectorLength() {
        return 4;
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0:
                return x1;
            case 1:
                return x2;
            case 2:
                return y1;
            case 3:
                return y2;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private static int hashCode(double v1, double v2) {
        return Double.hashCode(v1) + Double.hashCode(v2);
    }

    @Override
    public int hashCode() {
        return hashCode(x1, x2) * 0xb2d + hashCode(y1, y2) + 1;
    }

    public boolean equalBoundEpsilon(@NotNull Bound that) {
        return Math.max(
                Math.max(
                        Math.abs(this.x1 - that.x1),
                        Math.abs(this.x2 - that.x2)
                ),
                Math.max(
                        Math.abs(this.y1 - that.y1),
                        Math.abs(this.y2 - that.y2)
                )
        ) < EPSILON;
    }

    public boolean equalBound(@NotNull Bound that) {
        return this.x1 == that.x1 && this.x2 == that.x2 && this.y1 == that.y1 && this.y2 == that.y2;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj || obj instanceof Bound && equalBound((Bound) obj);
    }

    @Override
    public String toString() {
        return String.format("(%.3f~%.3f, %.3f~%.3f)", x1, x2, y1, y2);
    }
}
