package mujica.math.geometry.g2d;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2024/2/17.
 */
public class Parabola<P extends Point, Q extends Point> extends ConicSection<P, Q> {

    private static final long serialVersionUID = 0xe8c6ebcb02685cf6L;

    public Parabola(@NotNull P o, @NotNull Q v) {
        super(o, v);
    }

    @Override
    public double eccentricity() {
        return 1.0;
    }

    @Override
    public double focalParameter() {
        return 2 * v.euclidNorm();
    }

    @Override
    public double semiLatusRectum() {
        return 2 * v.euclidNorm();
    }

    @Override
    public int focusCount() {
        return 1;
    }

    @Override
    public void getFocus(int index, @NotNull Point point) {
        if (index == 0) {
            point.setToSum(o, v);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int directrixCount() {
        return 1;
    }

    @Override
    public void getDirectrix(int index, @NotNull HalfPlane halfPlane) {
        if (index == 0) {
            halfPlane.setHalfPlane(
                    v.x,
                    v.y,
                    v.squareNorm() - Point.innerProduct(o, v)
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int symmetryAxisCount() {
        return 1;
    }

    @Override
    public void getSymmetryAxis(int index, @NotNull HalfPlane halfPlane) {
        if (index == 0) {
            halfPlane.setHalfPlane(
                    -v.y,
                    v.x,
                    Point.crossProduct(o, v)
            );
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int symmetryPointCount() {
        return 1;
    }

    @Override
    public void getSymmetryPoint(int index, @NotNull Point point) {
        if (index == 0) {
            point.setPoint(o);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
