package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created in coo on 2020/4/10, named PlanarTransform.Translation.
 * Created in va on 2021/9/16.
 * Created on 2022/6/5.
 */
public class Translation2 extends Transform2 {

    private static final long serialVersionUID = 0x39def2f4f9147a49L;

    public double tx;

    public double ty;

    public Translation2() {
        super();
    }

    public Translation2(double tx, double ty) {
        super();
        setTranslation(tx, ty);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Translation2(Translation2 that) {
        super();
        setTranslation(that);
    }

    @Override
    @NotNull
    public Translation2 duplicate() {
        return new Translation2(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        checkNotNaN("tx", tx, consumer);
        checkNotNaN("ty", ty, consumer);
    }

    @Override
    public boolean isHealthy() {
        return tx == tx && ty == ty; // is faster
    }

    public void setTranslation(double tx, double ty) {
        this.tx = tx;
        this.ty = ty;
    }

    public void setTranslation(@NotNull Translation2 that) {
        setTranslation(that.tx, that.ty);
    }

    public void resetTranslation() {
        setTranslation(0.0, 0.0);
    }

    @NotNull
    @Override
    public GeometryOperationResult reset() {
        resetTranslation();
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setTranslation(Double.NaN, Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public void transform0(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(src.x + tx, src.y + ty);
    }

    public void transform(@NotNull HalfPlane src, @NotNull HalfPlane dst) {
        dst.setHalfPlane(src.a, src.b, src.c - tx * src.a - ty * src.b);
    }

    @Override
    @NotNull
    public GeometryOperationResult setToInverse() {
        setTranslation(-tx, -ty);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public Translation2 invert() {
        return new Translation2(-tx, -ty);
    }

    @Override
    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Point src, @NotNull Point dst) {
        dst.setPoint(src.x - tx, src.y - ty);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0: // scale x
            case 4: // scale y
            case 8: // homogeneous coordinates
                return 1.0;
            case 1:
            case 3:
            case 6:
            case 7:
                return 0.0;
            case 2:
                return tx;
            case 5:
                return ty;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        return String.format("Translation2(%.3f, %.3f)", tx, ty);
    }
}
