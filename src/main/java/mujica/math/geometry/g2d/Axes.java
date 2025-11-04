package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Created in existence on 2018/7/9, named MtCoordSysD3.
 * Recreated in coo on 2020/4/10, named PlanarTransform.Orthogonal.
 * Recreated on 2022/6/26.
 */
@CodeHistory(date = "2018/7/9", project = "existence", name = "MtCoordSysD3")
@CodeHistory(date = "2020/4/10", project = "coo", name = "PlanarTransform.Orthogonal")
@CodeHistory(date = "2022/6/26")
@ReferencePage(title = "Rotation and Translation (2D)", href = "https://www.cs.usfca.edu/~galles/visualization/RotateTranslate2D.html")
public class Axes extends ViewportTransform {

    private static final long serialVersionUID = 0x5d227d99fa9b185cL;

    public double mxy;

    public Axes(Void empty) {
        super(empty);
    }

    public Axes() {
        super();
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Axes(Axes that) {
        super((Void) null);
        setTranslation(that);
        setAxesCore(that);
    }

    @Override
    @NotNull
    public Axes duplicate() {
        return new Axes(this);
    }

    @Override
    public boolean isCoreHealthy() {
        return Math.abs(calculateDeterminant()) > EPSILON;
    }

    @Override
    public boolean preserveLength() {
        return Math.abs(calculateDeterminant() - 1.0) < EPSILON;
    }

    @Override
    public boolean preserveDirection() {
        return Math.abs(mxy) < EPSILON;
    }

    @Override
    public boolean componentIndependent() {
        return Math.abs(mxy) < EPSILON;
    }

    @Override
    public double calculateDeterminant() {
        return mxx * mxx + mxy * mxy;
    }

    @Override
    public double coreMaxAbsolute() {
        return Math.max(
                Math.abs(mxx),
                Math.abs(mxy)
        );
    }

    @Override
    @NotNull
    public GeometryOperationResult stationaryPoint(Point dst) {
        final double ms = mxx - 1.0;
        final double det = ms * ms + mxy * mxy;
        if (Math.abs(det) > EPSILON) {
            dst.setPoint(
                    -(mxy * ty + ms * tx) / det,
                    (mxy * tx - ms * ty) / det
            );
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    public void setAxesCore(double mxx, double mxy) {
        this.mxx = mxx;
        this.mxy = mxy;
    }

    public void setAxesCore(@NotNull Axes that) {
        setAxesCore(that.mxx, that.mxy);
    }

    @Override
    public void setCoreScale(double s) {
        setAxesCore(s, s);
    }

    @Override
    public void scaleBefore(double s) {
        setAxesCore(s * mxx, s * mxy);
    }

    @Override
    public void scaleAfter(double s) {
        setTranslation(s * tx, s * ty);
        setAxesCore(s * mxx, s * mxy);
    }

    /**
     * the scale center point is in input space
     * s scale will affect input vector first
     * this Axes will affect input vector last
     */
    @Override
    public void scaleBefore(@NotNull Point center, double s) {
        final double ms = 1.0 - s;
        setTranslation(
                (mxx * center.x - mxy * center.y) * ms + tx,
                (mxy * center.x + mxx * center.y) * ms + ty
        );
        setAxesCore(s * mxx, s * mxy);
    }

    /**
     * the scale center point is in output space
     * s scale will affect input vector first
     * this Axes will affect input vector last
     */
    @Override
    public void scaleAfter(@NotNull Point center, double s) {
        final double ms = 1.0 - s;
        setTranslation(
                s * tx + ms * center.x,
                s * ty + ms * center.y
        );
        setAxesCore(s * mxx, s * mxy);
    }

    public void setCoreRotation(double cos, double sin) {
        setAxesCore(cos, sin);
    }

    public void setCoreRotation(double radian) {
        setCoreRotation(Math.cos(radian), Math.sin(radian));
    }

    public void setCoreRotation(@NotNull Direction2 angle) {
        setCoreRotation(angle.cos(), angle.sin());
    }

    public void setRotation(@NotNull Point pivot, double cos, double sin) {
        setTranslation(
                pivot.x - cos * pivot.x + sin * pivot.y,
                pivot.y - sin * pivot.x - cos * pivot.y
        );
        setAxesCore(cos, sin);
    }

    public void setRotation(@NotNull Point pivot, double radian) {
        setRotation(pivot, Math.cos(radian), Math.sin(radian));
    }

    public void setRotation(@NotNull Point pivot, @NotNull Direction2 angle) {
        setRotation(pivot, angle.cos(), angle.sin());
    }

    /**
     * (cos, sin) rotation will affect input vector first
     * this Axes will affect input vector last
     */
    public void rotateBefore(double cos, double sin) {
        setAxesCore(
                cos * mxx - sin * mxy,
                cos * mxy + sin * mxx
        );
    }

    public void rotateBefore(double radian) {
        rotateBefore(Math.cos(radian), Math.sin(radian));
    }

    public void rotateBefore(@NotNull Direction2 angle) {
        rotateBefore(angle.cos(), angle.sin());
    }

    /**
     * the rotate pivot point is in input space
     * (cos, sin) rotation will affect input vector first
     * this Axes will affect input vector last
     */
    public void rotateBefore(@NotNull Point pivot, double cos, double sin) {
        final double sx = pivot.x - cos * pivot.x + sin * pivot.y;
        final double sy = pivot.y - sin * pivot.x - cos * pivot.y;
        setTranslation(
                sx * mxx - sy * mxy + tx,
                sx * mxy + sy * mxx + ty
        );
        setAxesCore(
                cos * mxx - sin * mxy,
                cos * mxy + sin * mxx
        );
    }

    public void rotateBefore(@NotNull Point pivot, double radian) {
        rotateBefore(pivot, Math.cos(radian), Math.sin(radian));
    }

    public void rotateBefore(@NotNull Point pivot, @NotNull Direction2 angle) {
        rotateBefore(pivot, angle.cos(), angle.sin());
    }

    /**
     * this Axes will affect input vector first
     * (cos, sin) rotation will affect input vector last
     */
    public void rotateAfter(double cos, double sin) {
        setTranslation(
                cos * tx - sin * ty,
                cos * ty + sin * tx
        );
        setAxesCore(
                cos * mxx - sin * mxy,
                cos * mxy + sin * mxx
        );
    }

    public void rotateAfter(double radian) {
        rotateAfter(Math.cos(radian), Math.sin(radian));
    }

    public void rotateAfter(@NotNull Direction2 angle) {
        rotateAfter(angle.cos(), angle.sin());
    }

    /**
     * the rotate pivot point is in output space
     * this Axes will affect input vector first
     * (cos, sin) rotation will affect input vector last
     */
    public void rotateAfter(@NotNull Point pivot, double cos, double sin) {
        setTranslation(
                cos * (tx - pivot.x) - sin * (ty - pivot.y) + pivot.x,
                sin * (tx - pivot.x) + cos * (ty - pivot.y) + pivot.y
        );
        setAxesCore(
                cos * mxx - sin * mxy,
                cos * mxy + sin * mxx
        );
    }

    public void rotateAfter(@NotNull Point pivot, double radian) {
        rotateAfter(pivot, Math.cos(radian), Math.sin(radian));
    }

    public void rotateAfter(@NotNull Point pivot, Direction2 angle) {
        rotateAfter(pivot, angle.cos(), angle.sin());
    }

    public GeometryOperationResult orthogonalizeCoreX() {
        return GeometryOperationResult.REMAIN; // axes is already orthogonal
    }

    public GeometryOperationResult orthogonalizeCoreY() {
        return GeometryOperationResult.REMAIN; // axes is already orthogonal
    }

    /**
     * transform0: before orthogonalize
     * transform1: after orthogonalize
     * transform0.transform(fixed, p0)
     * transform1.transform(fixed, p1)
     * then p0 and p1 are same
     */
    @NotNull
    public GeometryOperationResult orthogonalizeCore(@NotNull Point fixed) {
        return GeometryOperationResult.REMAIN; // axes is already orthogonal
    }

    /**
     * right Axes will affect input vector first
     * left Axes will affect input vector first
     */
    public void setToProduct(Axes left, Axes right) {
        setTranslation(
                left.mxx * right.tx - left.mxy * right.ty + left.tx,
                left.mxy * right.tx + left.mxx * right.ty + left.ty
        );
        setAxesCore(
                left.mxx * right.mxx - left.mxy * right.mxy,
                left.mxy * right.mxx + left.mxx * right.mxy
        );
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setAxesCore(1.0, 0.0);
        return super.reset();
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setAxesCore(Double.NaN, Double.NaN);
        return super.invalidate();
    }

    @Override
    public void transform0(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                tx + mxx * src.x - mxy * src.y,
                ty + mxy * src.x + mxx * src.y
        );
    }

    @Override
    public void transform(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                Math.fma(mxx, src.x, Math.fma(-mxy, src.y, tx)),
                Math.fma(mxy, src.x, Math.fma(mxx, src.y, ty))
        );
    }

    @Override
    public void transform(@NotNull HalfPlane src, @NotNull HalfPlane dst) {
        final double a = mxx * src.a - mxy * src.b;
        final double b = mxx * src.b + mxy * src.a;
        final double c = src.c * calculateDeterminant() - tx * a - ty * b;
        dst.setHalfPlane(a, b, c);
    }

    private void setInverse(double iDet, @NotNull Axes that) {
        that.setTranslation(
                -(mxx * tx + mxy * ty) * iDet,
                (mxy * tx - mxx * ty) * iDet
        );
        that.setAxesCore(mxx * iDet, -mxy * iDet);
    }

    @NotNull
    @Override
    public GeometryOperationResult setToInverse() {
        double det = calculateDeterminant();
        if (Math.abs(det) > EPSILON) {
            setInverse(1.0 / det, this);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    public Axes invert() {
        double det = calculateDeterminant();
        if (Math.abs(det) > EPSILON) {
            Axes inverse = new Axes((Void) null);
            setInverse(1.0 / det, inverse);
            return inverse;
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public GeometryOperationResult inverseTransform(@NotNull Point src, @NotNull Point dst) {
        final double det = calculateDeterminant();
        if (Math.abs(det) > EPSILON) {
            double x = src.x - tx;
            double y = src.y - ty;
            dst.setPoint(
                    (mxx * x + mxy * y) / det,
                    (mxx * y - mxy * x) / det
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
            case 1:
                return -mxy;
            case 2:
                return tx;
            case 3:
                return mxy;
            case 5:
                return ty;
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
        return String.format("Axes(%.3f, %.3f; %.3f, %.3f)", mxx, mxy, tx, ty);
    }
}
