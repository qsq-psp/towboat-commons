package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2020/4/10", project = "coo", name = "PlanarTransform")
@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2024/4/1")
public class Transform2 extends Geometry implements FunctionalTransform2, Comparable<Transform2> {

    private static final long serialVersionUID = 0x62657cbd9cfbec19L;

    /**
     * The base transform is not abstract and represents the identity transform
     */
    public Transform2() {
        super();
    }

    @NotNull
    @Override
    public Transform2 duplicate() {
        return new Transform2();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // always healthy; pass
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    public boolean preserveLength() {
        return true;
    }

    public boolean preserveDirection() {
        return true;
    }

    public boolean preserveAngle() {
        return true;
    }

    public boolean componentIndependent() {
        return true;
    }

    public double calculateDeterminant() {
        return 1.0;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        return GeometryOperationResult.REMAIN;
    }

    public void transform0(@NotNull Point src, @NotNull Point dst) {
        if (src != dst) {
            dst.setPoint(src);
        }
    }

    @Override
    public void transform(@NotNull Point src, @NotNull Point dst) {
        transform0(src, dst);
    }

    public void transform(@NotNull Point[] array) {
        for (Point point : array) {
            transform(point, point);
        }
    }

    public void transform(@NotNull Iterable<Point> points) {
        for (Point point : points) {
            transform(point, point);
        }
    }

    @NotNull
    public GeometryOperationResult setToInverse() {
        return GeometryOperationResult.REMAIN;
    }

    /**
     * @return null if invertible
     */
    @Nullable
    public Transform2 invert() {
        return this;
    }

    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Point src, @NotNull Point dst) {
        if (src != dst) {
            dst.setPoint(src);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.REMAIN;
        }
    }

    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Point[] array) {
        int result = GeometryOperationResult.REMAIN.ordinal();
        for (Point point : array) {
            result = Math.min(result, inverseTransform(point, point).ordinal());
        }
        return GeometryOperationResult.of(result);
    }

    @NotNull
    public GeometryOperationResult inverseTransform(@NotNull Iterable<Point> points) {
        int result = GeometryOperationResult.REMAIN.ordinal();
        for (Point point : points) {
            result = Math.min(result, inverseTransform(point, point).ordinal());
        }
        return GeometryOperationResult.of(result);
    }

    @Override
    public int vectorLength() {
        return 9;
    }

    @Override
    public double vectorComponent(int index) {
        switch (index) {
            case 0: // scale x
            case 4: // scale y
            case 8: // homogeneous coordinates
                return 1.0;
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                return 0.0;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int compareTo(@NotNull Transform2 that) {
        for (int index = 0; index < 6; index++) {
            int result = Double.compare(this.vectorComponent(index), that.vectorComponent(index));
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 0xff;
        for (int index = 0; index < 6; index++) {
            hash = hash * 0x2d + Double.hashCode(vectorComponent(index));
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Transform2 && compareTo((Transform2) obj) == 0;
    }

    @Override
    public String toString() {
        return "Transform2";
    }
}
