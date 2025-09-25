package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2022/7/4.
 */
public class ViewportTransform extends Translation2 {

    private static final long serialVersionUID = 0xd8dfeb8bd098d122L;

    public double mxx;

    @SuppressWarnings("unused")
    public ViewportTransform(Void empty) {
        super();
    }

    public ViewportTransform() {
        super();
        mxx = 1.0;
    }

    public ViewportTransform(ViewportTransform that) {
        super();
        setTranslation(that);
        setCoreScale(that.mxx);
    }

    @NotNull
    @Override
    public ViewportTransform duplicate() {
        return new ViewportTransform(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        super.checkHealth(consumer);
        final double det = calculateDeterminant();
        if (det > EPSILON) {
            return;
        }
        consumer.accept(new RuntimeException("sick transform determinant = " + det));
    }

    @Override
    public boolean isHealthy() {
        return super.isHealthy() && isCoreHealthy();
    }

    public boolean isCoreHealthy() {
        return calculateDeterminant() > EPSILON;
    }

    @Override
    public boolean preserveLength() {
        return Math.abs(mxx - 1.0) < EPSILON;
    }

    @Override
    public double calculateDeterminant() {
        return mxx * mxx;
    }

    public double coreMaxAbsolute() {
        return Math.abs(mxx);
    }

    public int coreRank() {
        if (isCoreHealthy()) {
            return 2;
        } else if (coreMaxAbsolute() > EPSILON) {
            return 1;
        } else {
            return 0;
        }
    }

    public GeometryOperationResult stationaryPoint(Point dst) {
        final double ms = 1.0 - mxx;
        if (Math.abs(ms) > EPSILON) {
            dst.setPoint(tx / ms, ty / ms);
            return GeometryOperationResult.UNKNOWN;
        } else if (Math.max(Math.abs(tx), Math.abs(ty)) > EPSILON) {
            return GeometryOperationResult.FAIL;
        } else {
            return GeometryOperationResult.REMAIN;
        }
    }

    public void setCoreScale(double s) {
        mxx = s;
    }

    /**
     * s scale will affect input vector first
     * this Axes will affect input vector last
     */
    public void scaleBefore(double s) {
        setCoreScale(s * mxx);
    }

    /**
     * s scale will affect input vector first
     * this Axes will affect input vector last
     */
    public void scaleAfter(double s) {
        setTranslation(s * tx, s * ty);
        setCoreScale(s * mxx);
    }

    /**
     * the scale center point is in input space
     */
    public void scaleBefore(@NotNull Point center, double s) {
        final double ms = 1.0 - s;
        setTranslation(
                mxx * center.x * ms + tx,
                mxx * center.y * ms + ty
        );
        setCoreScale(s * mxx);
    }

    /**
     * the scale center point is in output space
     */
    public void scaleAfter(@NotNull Point center, double s) {
        final double ms = 1.0 - s;
        setTranslation(
                s * tx + ms * center.x,
                s * ty + ms * center.y
        );
        setCoreScale(s * mxx);
    }

    @Override
    public void transform0(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                tx + mxx * src.x,
                ty + mxx * src.y
        );
    }

    @Override
    public void transform(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                Math.fma(mxx, src.x, tx),
                Math.fma(mxx, src.y, ty)
        );
    }

    @Override
    public void transform(@NotNull HalfPlane src, @NotNull HalfPlane dst) {
        dst.setHalfPlane(
                src.a, src.b, mxx * src.c - tx * src.a - ty * src.b
        );
    }

    @Override
    @NotNull
    public GeometryOperationResult setToInverse() {
        double s = mxx;
        if (Math.abs(s) > EPSILON) {
            s = 1.0 / s;
            setTranslation(
                    -s * tx, -s * ty
            );
            setCoreScale(s);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    public ViewportTransform invert() {
        double s = mxx;
        if (Math.abs(s) > EPSILON) {
            s = 1.0 / s;
            ViewportTransform inverse = new ViewportTransform((Void) null);
            inverse.setTranslation(
                    -s * tx, -s * ty
            );
            inverse.setCoreScale(s);
            return inverse;
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Point src, @NotNull Point dst) {
        final double s = mxx;
        if (Math.abs(s) > EPSILON) {
            dst.setPoint(
                    (src.x - tx) / s,
                    (src.y - ty) / s
            );
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0:
            case 4:
                return mxx;
            case 2:
                return tx;
            case 5:
                return ty;
            case 1:
            case 3:
            case 6:
            case 7:
                return 0.0;
            case 8: // homogeneous coordinates
                return 1.0;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        return String.format("ViewportTransform(%.3f; %.3f, %.3f)", mxx, tx, ty);
    }
}
