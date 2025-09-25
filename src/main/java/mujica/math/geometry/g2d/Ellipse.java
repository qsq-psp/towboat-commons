package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2018/7/9", project = "existence", name = "MtEllipseD5")
@CodeHistory(date = "2022/10/5", project = "Ultramarine")
public class Ellipse<P extends Point, Q extends Point> extends ConicSection<P, Q> {

    private static final long serialVersionUID = 0x4f018ab5f7cb5ee2L;

    public double k; // axes ratio; k = b / a

    public Ellipse(@NotNull P o, @NotNull Q v, double k) {
        super(o, v);
        this.k = k;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        super.checkHealth(consumer);
        if (!(0.0 < k && k < 1.0)) {
            consumer.accept(new RuntimeException("axes ratio k = " + k + " is not in range"));
        }
    }

    @Override
    public boolean isHealthy() {
        return super.isHealthy() && 0.0 < k && k < 1.0;
    }

    @Override
    public double eccentricity() {
        return Math.sqrt(1.0 - k * k);
    }

    @Override
    public double focalParameter() {
        final double kSquare = k * k;
        return v.euclidNorm() * kSquare / Math.sqrt(1.0 - kSquare);
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
        final double eccentricity = Math.sqrt(1.0 - k * k);
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
        final double h = v.squareNorm() / Math.sqrt(1.0 - k * k);
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
        return 4;
    }

    @Override
    public void getSymmetryPoint(int index, @NotNull Point point) {
        switch (index) {
            case 0:
                point.setToSum(o, v);
                break;
            case 1:
                point.setToDifference(o, v);
                break;
            case 2:
                point.setPoint(
                        o.x - k * v.y,
                        o.y + k * v.x
                );
                break;
            case 3:
                point.setPoint(
                        o.x + k * v.y,
                        o.y - k * v.x
                );
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    public GeometryOperationResult trueAnomalyToEccentricAnomaly(@NotNull Direction2 trueAnomaly, @NotNull Direction2 eccentricAnomaly) {
        trueAnomaly.setToVector(new Point(
                eccentricAnomaly.cos(),
                eccentricAnomaly.sin() / k
        ));
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    public GeometryOperationResult eccentricAnomalyToTrueAnomaly(@NotNull Direction2 eccentricAnomaly, @NotNull Direction2 trueAnomaly) {
        trueAnomaly.setToVector(new Point(
                eccentricAnomaly.cos(),
                eccentricAnomaly.sin() * k
        ));
        return GeometryOperationResult.UNKNOWN;
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        k = Math.sqrt(0.5);
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
            if (k < -1.0) {
                v.setPoint(
                        -k * v.y,
                        k * v.x
                );
                k = -1.0 / k;
                return GeometryOperationResult.MODIFIED;
            } else if (k > -1.0) {
                k = -k;
                return GeometryOperationResult.MODIFIED;
            }
        } else if (k > 0.0) {
            if (k < 1.0) {
                return GeometryOperationResult.REMAIN;
            } else if (k > 1.0) {
                v.setPoint(
                        -k * v.y,
                        k * v.x
                );
                k = 1.0 / k;
                return GeometryOperationResult.MODIFIED;
            }
        }
        return GeometryOperationResult.FAIL;
    }
}
