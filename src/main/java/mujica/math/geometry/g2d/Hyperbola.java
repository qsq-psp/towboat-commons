package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2024/2/17.
 */
public class Hyperbola<P extends Point, Q extends Point> extends ConicSection<P, Q> {

    private static final long serialVersionUID = 0x8c090ed58b8565d9L;

    public double k;

    public Hyperbola(@NotNull P o, @NotNull Q v, double k) {
        super(o, v);
        this.k = k;
    }

    public Hyperbola(@NotNull P o, @NotNull Q v) {
        this(o, v, 1.0);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        super.checkHealth(consumer);
        checkPositive("k", k, consumer);
    }

    @Override
    public boolean isHealthy() {
        return super.isHealthy() && k > 0.0;
    }

    @Override
    public double eccentricity() {
        return Math.sqrt(1.0 + k * k);
    }

    @Override
    public double focalParameter() {
        final double kSquare = k * k;
        return v.euclidNorm() * kSquare / Math.sqrt(1.0 + kSquare);
    }

    @Override
    public double semiLatusRectum() {
        return v.euclidNorm() * k * k;
    }

    @Override
    public int focusCount() {
        return 2;
    }

    @Override
    public void getFocus(int index, @NotNull Point point) {
        final double eccentricity = Math.sqrt(1.0 + k * k);
        if (index == 0) {
            point.setPoint(
                    o.x + eccentricity * v.x,
                    o.y + eccentricity * v.y
            );
        } else if (index == 1) {
            point.setPoint(
                    o.x - eccentricity * v.x,
                    o.y - eccentricity * v.y
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int directrixCount() {
        return 2;
    }

    @Override
    public void getDirectrix(int index, @NotNull HalfPlane halfPlane) {
        final double h = v.squareNorm() / Math.sqrt(1.0 + k * k);
        if (index == 0) {
            halfPlane.setHalfPlane(
                    v.x,
                    v.y,
                    - (h + Point.innerProduct(o, v))
            );
        } else if (index == 1) {
            halfPlane.setHalfPlane(
                    v.x,
                    v.y,
                    h - Point.innerProduct(o, v)
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int asymptoteCount() {
        return 2;
    }

    @Override
    public void getAsymptote(int index, @NotNull HalfPlane halfPlane) {
        final Point point = new Point(
                o.x + v.x,
                o.y + v.y
        );
        if (index == 0) {
            point.addVector(
                    -k * v.y,
                    k * v.x
            );
        } else if (index == 1) {
            point.addVector(
                    k * v.y,
                    -k * v.x
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
        halfPlane.setHalfPlane(o, point);
    }

    @Override
    public int symmetryAxisCount() {
        return 2;
    }

    @Override
    public void getSymmetryAxis(int index, @NotNull HalfPlane halfPlane) {
        if (index == 0) {
            halfPlane.setHalfPlane(
                    -v.y,
                    v.x,
                    Point.crossProduct(o, v)
            );
        } else if (index == 1) {
            halfPlane.setHalfPlane(
                    v.x,
                    v.y,
                    -Point.innerProduct(o, v)
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int symmetryPointCount() {
        return 2;
    }

    @Override
    public void getSymmetryPoint(int index, @NotNull Point point) {
        if (index == 0) {
            point.setToSum(o, v);
        } else if (index == 1) {
            point.setToDifference(o, v);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void symmetry() {
        v.setPoint(
                -v.x,
                -v.y
        );
    }

    public void conjugate() {
        v.setPoint(
                -k * v.y,
                k * v.x
        );
        k = 1.0 / k;
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        k = 1.0;
        return super.reset();
    }

    @NotNull
    @Override
    public GeometryOperationResult invalidate() {
        k = Double.NaN;
        return super.invalidate();
    }

    @NotNull
    @Override
    public GeometryOperationResult repair() {
        if (k < 0.0) {
            k = -k;
            return GeometryOperationResult.MODIFIED;
        } else if (k > 0.0) {
            return GeometryOperationResult.REMAIN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }
}
