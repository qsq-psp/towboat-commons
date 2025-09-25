package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2020/4/10", project = "coo", name = "PlanarTransform.Conformal")
@CodeHistory(date = "2021/10/4", project = "va", name = "FreeAffine2")
@CodeHistory(date = "2022/6/26", project = "Ultramarine")
@CodeHistory(date = "2024/4/1", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class Affine2 extends Axes {

    private static final long serialVersionUID = 0x5d1b263e5448d684L;

    public double myx;

    public double myy;

    public Affine2(Void empty) {
        super(empty);
    }

    public Affine2() {
        super();
        myy = 1.0;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Affine2(Affine2 that) {
        super((Void) null);
        setTranslation(that);
        setCore(that);
    }

    @Override
    @NotNull
    public Affine2 duplicate() {
        return new Affine2(this);
    }

    @Override
    public boolean preserveDirection() {
        return Math.max(Math.abs(mxx - myy), Math.max(Math.abs(myx), Math.abs(mxy))) < EPSILON;
    }

    @Override
    public boolean preserveAngle() {
        return Math.max(Math.abs(mxx - myy), Math.abs(myx + mxy)) < EPSILON;
    }

    @Override
    public boolean componentIndependent() {
        return Math.abs(myx) + Math.abs(mxy) < EPSILON;
    }

    @Override
    public double calculateDeterminant() {
        return mxx * myy - myx * mxy;
    }

    @Override
    public double coreMaxAbsolute() {
        return Math.max(
                Math.max(
                        Math.abs(mxx),
                        Math.abs(myx)
                ),
                Math.max(
                        Math.abs(mxy),
                        Math.abs(myy)
                )
        );
    }

    @NotNull
    @Override
    public GeometryOperationResult stationaryPoint(Point dst) {
        final double det = (mxx - 1.0) * (myy - 1.0) - mxy * myx;
        if (Math.abs(det) > EPSILON) {
            dst.setPoint(
                    (myx * ty - (myy - 1.0) * tx) / det,
                    (mxy * tx - (mxx - 1.0) * ty) / det
            );
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    public void setCore(double mxx, double myx, double mxy, double myy) {
        this.mxx = mxx;
        this.myx = myx;
        this.mxy = mxy;
        this.myy = myy;
    }

    public void setCore(Affine2 that) {
        setCore(that.mxx, that.myx, that.mxy, that.myy);
    }

    @Override
    public void setAxesCore(double mxx, double mxy) {
        setCore(mxx, -mxy, mxy, mxx);
    }

    /**
     * Uniform scale before and uniform scale after are same
     */
    @Override
    public void scaleBefore(double s) {
        setCore(
                s * mxx, s * myx,
                s * mxy, s * myy
        );
    }

    public void scaleBefore(double sx, double sy) {
        setCore(
                sx * mxx, sy * myx,
                sx * mxy, sy * myy
        );
    }

    public void scaleAfter(double sx, double sy) {
        setCore(
                sx * mxx, sx * myx,
                sy * mxy, sy * myy
        );
    }

    /**
     * s scale will affect input vector first
     * this Axes will affect input vector last
     */
    @Override
    public void scaleBefore(@NotNull Point center, double s) {
        final double ms = 1.0 - s;
        setTranslation(
                (mxx * center.x + myx * center.y) * ms + tx,
                (mxy * center.x + myy * center.y) * ms + ty
        );
        setCore(
                s * mxx, s * myx,
                s * mxy, s * myy
        );
    }

    /**
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
        setCore(
                s * mxx, s * myx,
                s * mxy, s * myy
        );
    }

    @Override
    public void setCoreRotation(double radian) {
        final double cos = Math.cos(radian);
        final double sin = Math.sin(radian);
        setCore(cos, -sin, sin, cos);
    }

    @Override
    public void rotateBefore(double cos, double sin) {
        setCore(
                cos * mxx + sin * myx,
                cos * myx - sin * mxx,
                cos * mxy + sin * myy,
                cos * myy - sin * mxy
        );
    }

    @Override
    public void rotateBefore(@NotNull Point pivot, double cos, double sin) {
        final double sx = pivot.x - cos * pivot.x + sin * pivot.y;
        final double sy = pivot.y - sin * pivot.x - cos * pivot.y;
        setTranslation(
                sx * mxx + sy * myx + tx,
                sx * mxy + sy * myy + ty
        );
        setCore(
                cos * mxx + sin * myx,
                cos * myx - sin * mxx,
                cos * mxy + sin * myy,
                cos * myy - sin * mxy
        );
    }

    @Override
    public void rotateAfter(double cos, double sin) {
        setTranslation(
                cos * tx - sin * ty,
                cos * ty + sin * tx
        );
        setCore(
                cos * mxx - sin * mxy,
                cos * myx - sin * myy,
                cos * mxy + sin * mxx,
                cos * myy + sin * myx
        );
    }

    @Override
    public void rotateAfter(@NotNull Point pivot, double cos, double sin) {
        setTranslation(
                cos * (tx - pivot.x) - sin * (ty - pivot.y) + pivot.x,
                sin * (tx - pivot.x) + cos * (ty - pivot.y) + pivot.y
        );
        setCore(
                cos * mxx - sin * mxy,
                cos * myx - sin * myy,
                cos * mxy + sin * mxx,
                cos * myy + sin * myx
        );
    }

    @Override
    public GeometryOperationResult orthogonalizeCoreX() {
        final double u = (mxx * myx + mxy * myy) / (mxx * mxx + mxy * mxy);
        if (Math.abs(u) < EPSILON) {
            return GeometryOperationResult.REMAIN;
        }
        setCore(
                mxx,
                myx - u * mxx,
                mxy,
                myy - u * mxy
        );
        return GeometryOperationResult.MODIFIED;
    }

    @Override
    public GeometryOperationResult orthogonalizeCoreY() {
        final double u = (mxx * myx + mxy * myy) / (myx * myx + myy * myy);
        if (Math.abs(u) < EPSILON) {
            return GeometryOperationResult.REMAIN;
        }
        setCore(
                mxx - u * myx,
                myx,
                mxy - u * myy,
                myy
        );
        return GeometryOperationResult.MODIFIED;
    }

    /**
     * transform0: before orthogonalize
     * transform1: after orthogonalize
     * transform0.transform(fixed, p0)
     * transform1.transform(fixed, p1)
     * then p0 and p1 are equal
     */
    @Override
    @NotNull
    public GeometryOperationResult orthogonalizeCore(@NotNull Point fixed) {
        final double ux = fixed.x;
        final double uy = fixed.y;
        final double det = ux * ux + uy * uy;
        if (Math.abs(det) > EPSILON) {
            double vx = mxx * ux + myx * uy;
            double vy = mxy * ux + myy * uy;
            double mxx = (ux * vx + uy * vy) / det;
            double mxy = (ux * vy - uy * vx) / det;
            setCore(mxx, -mxy, mxy, mxx);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    /**
     * right Axes will affect input vector first
     * left Axes will affect input vector first
     */
    public void setToProduct(@NotNull Affine2 left, @NotNull Affine2 right) {
        setTranslation(
                left.mxx * right.tx + left.myx * right.ty + left.tx,
                left.mxy * right.tx + left.myy * right.ty + left.ty
        );
        setCore(
                left.mxx * right.mxx + left.myx * right.mxy,
                left.mxx * right.myx + left.myx * right.myy,
                left.mxy * right.mxx + left.myy * right.mxy,
                left.mxy * right.myx + left.myy * right.myy
        );
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setCore(1.0, 0.0, 0.0, 1.0);
        return super.reset();
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setCore(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        return super.invalidate();
    }

    @Override
    public void transform0(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                tx + mxx * src.x + myx * src.y,
                ty + mxy * src.x + myy * src.y
        );
    }

    @Override
    public void transform(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(
                Math.fma(mxx, src.x, Math.fma(myx, src.y, tx)),
                Math.fma(mxy, src.x, Math.fma(myy, src.y, ty))
        );
    }

    @Override
    public void transform(@NotNull HalfPlane src, @NotNull HalfPlane dst) {
        final double a = myy * src.a - mxy * src.b;
        final double b = mxx * src.b - myx * src.a;
        final double c = src.c * calculateDeterminant() - tx * a - ty * b;
        dst.setHalfPlane(a, b, c);
    }

    private void setInverse(double iDet, @NotNull Affine2 that) {
        that.setTranslation(
                (myx * ty - myy * tx) * iDet,
                (mxy * tx - mxx * ty) * iDet
        );
        that.setCore(myy * iDet, -myx * iDet, -mxy * iDet, mxx * iDet);
    }

    @Override
    @NotNull
    public GeometryOperationResult setToInverse() {
        final double det = calculateDeterminant();
        if (Math.abs(det) > EPSILON) {
            setInverse(1.0 / det, this);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @Override
    @Nullable
    public Affine2 invert() {
        double det = calculateDeterminant();
        if (Math.abs(det) > EPSILON) {
            Affine2 inverse = new Affine2((Void) null);
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
                    (myy * x - myx * y) / det,
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
                return mxx;
            case 1:
                return myx;
            case 2:
                return tx;
            case 3:
                return mxy;
            case 4:
                return myy;
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
        return String.format("Affine2(%.3f, %.3f, %.3f, %.3f; %.3f, %.3f)", mxx, myx, mxy, myy, tx, ty);
    }
}
