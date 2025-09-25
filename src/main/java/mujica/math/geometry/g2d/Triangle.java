package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Created in va on 2020/7/10, named TriangleModel.
 * Recreated on 2022/9/16.
 */
public class Triangle<G extends Point> extends Jordan2 {

    private static final long serialVersionUID = 0x0e3a49955bf60e00L;

    public final G p1;

    public final G p2;

    public final G p3;

    public Triangle(G p1, G p2, G p3) {
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @NotNull
    public static Triangle<Point> points() {
        return new Triangle<>(new Point(), new Point(), new Point());
    }

    @Override
    @SuppressWarnings("unchecked")
    @NotNull
    public Triangle<G> duplicate() {
        return new Triangle<>(
                (G) p1.duplicate(),
                (G) p2.duplicate(),
                (G) p3.duplicate()
        );
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!(Point.squareNorm(p1, p2) > EPSILON)) {
            consumer.accept(new RuntimeException("triangle point p1 " + p1 + " and p2 " + p2 + " are too close"));
        }
        if (!(Point.squareNorm(p2, p3) > EPSILON)) {
            consumer.accept(new RuntimeException("triangle point p2 " + p2 + " and p3 " + p3 + " are too close"));
        }
        if (!(Point.squareNorm(p3, p1) > EPSILON)) {
            consumer.accept(new RuntimeException("triangle point p3 " + p3 + " and p1 " + p1 + " are too close"));
        }
        double area = Math.abs(p1.relativeCrossProduct(p2, p3));
        if (!(area > EPSILON)) {
            consumer.accept(new RuntimeException("triangle area = " + area + " is too small"));
        }
    }

    @Override
    public boolean isHealthy() {
        return Point.squareNorm(p1, p2) > EPSILON &&
                Point.squareNorm(p2, p3) > EPSILON &&
                Point.squareNorm(p3, p1) > EPSILON &&
                Math.abs(p1.relativeCrossProduct(p2, p3)) > EPSILON;
    }

    @NotNull
    public G pointAt(int index) {
        switch (index) {
            case 0:
                return p1;
            case 1:
                return p2;
            case 2:
                return p3;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    public G pointOf(int index) {
        switch (index % 3) {
            case 0:
                return p1;
            case -2:
            case 1:
                return p2;
            case -1:
            case 2:
                return p3;
            default:
                throw new IndexOutOfBoundsException(); // Impossible
        }
    }

    public int pointIndex(@Nullable Point p) {
        if (p1 == p) {
            return 0;
        } else if (p2 == p) {
            return 1;
        } else if (p3 == p) {
            return 2;
        } else {
            return -1;
        }
    }

    @NotNull
    public Line<G> edgeAt(int index) {
        switch (index) {
            case 0:
                return new Line<>(p2, p3); // opposite of p1
            case 1:
                return new Line<>(p3, p1); // opposite of p2
            case 2:
                return new Line<>(p1, p2); // opposite of p3
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    public Line<G> edgeOf(int index) {
        switch (index % 3) {
            case 0:
                return new Line<>(p2, p3); // opposite of p1
            case -2:
            case 1:
                return new Line<>(p3, p1); // opposite of p2
            case -1:
            case 2:
                return new Line<>(p1, p2); // opposite of p3
            default:
                throw new IndexOutOfBoundsException(); // Impossible
        }
    }

    @NotNull
    public GeometryOperationResult edgeAt(HalfPlane out, int index) {
        switch (index) {
            case 0:
                return out.setHalfPlane(p2, p3); // opposite of p1
            case 1:
                return out.setHalfPlane(p3, p1); // opposite of p2
            case 2:
                return out.setHalfPlane(p1, p2); // opposite of p3
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    public GeometryOperationResult edgeOf(HalfPlane out, int index) {
        switch (index % 3) {
            case 0:
                return out.setHalfPlane(p2, p3); // opposite of p1
            case -2:
            case 1:
                return out.setHalfPlane(p3, p1); // opposite of p2
            case -1:
            case 2:
                return out.setHalfPlane(p1, p2); // opposite of p3
            default:
                throw new IndexOutOfBoundsException(); // Impossible
        }
    }

    @Override
    public double measure1() {
        return Point.euclidDistance(p1, p2) + Point.euclidDistance(p2, p3) + Point.euclidDistance(p3, p1);
    }

    @Override
    public double measure2() {
        return 0.5 * Math.abs(p1.relativeCrossProduct(p2, p3));
    }

    @NotNull
    public Point center(@Nullable Point point) {
        if (point == null) {
            point = new Point();
        }
        point.setPoint(
                (p1.x + p2.x + p3.x) / 3,
                (p1.y + p2.y + p3.y) / 3
        );
        return point;
    }

    public GeometryOperationResult orthogonalCenter(Point point) {
        // TODO
        return GeometryOperationResult.FAIL;
    }

    /**
     * In geometry, the incircle or inscribed circle of a triangle is the largest circle that can be contained in the triangle;
     * it touches (is tangent to) the three sides. The center of the incircle is a triangle center called the triangle's incenter.
     *
     * The Cartesian coordinates of the incenter are a weighted average of the coordinates of the three vertices
     * using the side lengths of the triangle relative to the perimeter as weights.
     */
    @ReferencePage(title = "incircle", href = "https://en.wikipedia.org/wiki/Incircle_and_excircles_of_a_triangle")
    @Name(value = "内心", language = "zh")
    @NotNull
    public GeometryOperationResult innerCenter(@NotNull Point point) {
        double w1 = Point.euclidDistance(p2, p3);
        double w2 = Point.euclidDistance(p3, p1);
        double w3 = Point.euclidDistance(p1, p2);
        double w = w1 + w2 + w3;
        point.setPoint(
                (w1 * p1.x + w2 * p2.x + w3 * p3.x) / w,
                (w1 * p1.y + w2 * p2.y + w3 * p3.y) / w
        );
        return GeometryOperationResult.UNKNOWN;
    }

    public double innerRadius() {
        return Math.abs(p1.relativeCrossProduct(p2, p3)) / measure1();
    }

    @Name(value = "外心", language = "zh")
    @NotNull
    public GeometryOperationResult outerCenter(@NotNull Point point) {
        double a11 = p2.x - p1.x;
        double a12 = p2.y - p1.y;
        double a21 = p3.x - p1.x;
        double a22 = p3.y - p1.y;
        double det = a11 * a22 - a12 * a21;
        if (Math.abs(det) > EPSILON) {
            double s = p1.squareNorm();
            double c1 = 0.5 * (p2.squareNorm() - s);
            double c2 = 0.5 * (p3.squareNorm() - s);
            point.setPoint(
                    (c1 * a22 - a12 * c2) / det,
                    (a11 * c2 - c1 * a21) / det
            );
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    public double outerRadius() {
        return 0.0; // todo
    }

    @Override
    public boolean test(@NotNull Point point) {
        double cp1 = point.relativeCrossProduct(p1, p2);
        double cp2 = point.relativeCrossProduct(p2, p3);
        double cp3 = point.relativeCrossProduct(p3, p1);
        return cp1 >= 0.0 && cp2 >= 0.0 && cp3 >= 0.0 || cp1 <= 0.0 && cp2 <= 0.0 && cp3 <= 0.0;
    }

    @Override
    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        bound.includePoint(p1);
        bound.includePoint(p2);
        bound.includePoint(p3);
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
    public GeometryOperationResult enterArea(@NotNull Point point) {
        final double[] products = new double[3];
        Point p1 = this.p1;
        Point p2 = this.p2;
        Point p3 = this.p3;
        for (int i = 0; i < 3; i++) {
            products[i] = p1.relativeCrossProduct(p2, point);
            Point p4 = p1;
            p1 = p2;
            p2 = p3;
            p3 = p4;
        }
        if (products[0] >= 0.0 && products[1] >= 0.0 && products[2] >= 0.0 || products[0] <= 0.0 && products[1] <= 0.0 && products[2] <= 0.0) {
            return GeometryOperationResult.REMAIN;
        }
        int minIndex = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        double minWeight = 0.0;
        for (int i = 0; i < 3; i++) {
            double length = Point.squareNorm(p1, p2);
            double weight = p1.relativeInnerProduct(p2, point) / length;
            if (weight < 0.0) {
                double distance = Point.euclidDistance(p1, point);
                if (distance < minDistance) {
                    minIndex = 3 + i;
                    minDistance = distance;
                }
            } else if (weight <= 1.0) {
                double distance = Math.abs(products[i]) / StrictMath.sqrt(length);
                if (distance < minDistance) {
                    minIndex = i;
                    minDistance = distance;
                    minWeight = weight;
                }
            }
            Point p4 = p1;
            p1 = p2;
            p2 = p3;
            p3 = p4;
        }
        if (minIndex < 3) {
            if (minIndex == -1) {
                return GeometryOperationResult.FAIL;
            }
            point.setToLerp(pointAt(minIndex), pointOf(minIndex + 1), minWeight);
        } else {
            point.setPoint(pointAt(minIndex - 3));
        }
        return GeometryOperationResult.MODIFIED;
    }

    @Override
    @NotNull
    public GeometryOperationResult leaveArea(@NotNull Point point) {
        final double[] products = new double[3];
        Point p1 = this.p1;
        Point p2 = this.p2;
        Point p3 = this.p3;
        for (int i = 0; i < 3; i++) {
            products[i] = p1.relativeCrossProduct(p2, point);
            Point p4 = p1;
            p1 = p2;
            p2 = p3;
            p3 = p4;
        }
        if (products[0] >= 0.0 && products[1] >= 0.0 && products[2] >= 0.0 || products[0] <= 0.0 && products[1] <= 0.0 && products[2] <= 0.0) {
            int minIndex = -1;
            double minDistance = Double.POSITIVE_INFINITY;
            double minWeight = 0.0;
            for (int i = 0; i < 3; i++) {
                double length = Point.squareNorm(p1, p2);
                double weight = p1.relativeInnerProduct(p2, point) / length;
                if (0.0 <= weight && weight <= 1.0) {
                    double distance = Math.abs(products[i]) / StrictMath.sqrt(length);
                    if (distance < minDistance) {
                        minIndex = i;
                        minDistance = distance;
                        minWeight = weight;
                    }
                }
                Point p4 = p1;
                p1 = p2;
                p2 = p3;
                p3 = p4;
            }
            if (minIndex == -1) {
                return GeometryOperationResult.FAIL;
            }
            point.setToLerp(pointAt(minIndex), pointOf(minIndex + 1), minWeight);
            return GeometryOperationResult.MODIFIED;
        }
        return GeometryOperationResult.REMAIN;
    }

    @Override
    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        double u = rc.nextDouble();
        double v = rc.nextDouble();
        if (u + v > 1.0) {
            u = 1.0 - u;
            v = 1.0 - v;
        }
        point.setToLerp(p1, p2, p3, u, v);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        return p1.reset().or(p2.reset()).or(p3.reset());
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        return p1.invalidate().or(p2.invalidate()).or(p3.invalidate());
    }

    @Override
    public int hashCode() {
        return (p1.hashCode() * 0x2d + p2.hashCode()) * 0x2d + p3.hashCode();
    }

    public boolean equalTriangle(@NotNull Triangle<?> that) {
        return this.p1.equalPoint(that.p1) && this.p2.equalPoint(that.p2) && this.p3.equalPoint(that.p3);
    }

    public boolean equalTriangleRotate(@NotNull Triangle<?> that) {
        return this.p1.equalPoint(that.p1) && this.p2.equalPoint(that.p2) && this.p3.equalPoint(that.p3)
                || this.p1.equalPoint(that.p2) && this.p2.equalPoint(that.p3) && this.p3.equalPoint(that.p1)
                || this.p1.equalPoint(that.p3) && this.p2.equalPoint(that.p1) && this.p3.equalPoint(that.p2);
    }

    public boolean equalTrianglePermute(@NotNull Triangle<?> that) {
        int code = 0;
        int bit = 1;
        for (int thisIndex = 0; thisIndex < 3; thisIndex++) {
            for (int thatIndex = 0; thatIndex < 3; thatIndex++) {
                if (this.pointAt(thisIndex).equalPoint(that.pointAt(thatIndex))) {
                    code |= bit;
                }
                bit <<= 1;
            }
        }
        return (code & 0b000000111) != 0 && (code & 0b000111000) != 0 && (code & 0b111000000) != 0
                && (code & 0b001001001) != 0 && (code & 0b010010010) != 0 && (code & 0b100100100) != 0;
    }

    public boolean sameTriangle(@NotNull Triangle<?> that) {
        return this.p1 == that.p1 && this.p2 == that.p2 && this.p3 == that.p3;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj || obj instanceof Triangle<?> && equalTriangle((Triangle<?>) obj);
    }

    @Override
    public String toString() {
        return String.format("Triangle[%s, %s, %s]", p1, p2, p3);
    }
}
