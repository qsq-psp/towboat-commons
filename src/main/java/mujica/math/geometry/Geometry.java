package mujica.math.geometry;

import mujica.ds.DataStructure;
import mujica.reflect.basic.ClassUtil;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.function.Consumer;

@CodeHistory(date = "2019/4/23", name = "Geometric")
@CodeHistory(date = "2020/3/8", project = "coo", name = "Geometric")
@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public abstract class Geometry implements DataStructure, Serializable {

    private static final long serialVersionUID = 0xfbb56e095bfb38c6L;

    public static final double EPSILON = 0x1.0p-12;

    public Geometry() {
        super();
    }

    @NotNull
    @Override
    public abstract Geometry duplicate();

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int length = vectorLength();
        for (int index = 0; index < length; index++) {
            if (Double.isNaN(vectorComponent(index))) {
                consumer.accept(new RuntimeException("vector[" + index + "] is NaN"));
            }
        }
    }

    public static void checkNotNaN(@NotNull String name, double value, @NotNull Consumer<RuntimeException> consumer) {
        if (Double.isNaN(value)) {
            consumer.accept(new RuntimeException(name + " is NaN"));
        }
    }

    public static void checkFinite(@NotNull String name, double value, @NotNull Consumer<RuntimeException> consumer) {
        if (Double.isFinite(value)) {
            return;
        }
        consumer.accept(new RuntimeException(name + " is not finite"));
    }

    public static void checkNotNegative(@NotNull String name, double value, @NotNull Consumer<RuntimeException> consumer) {
        if (value >= 0.0) {
            return;
        }
        consumer.accept(new RuntimeException(name + " = " + value + " is negative (or NaN)"));
    }

    public static void checkPositive(@NotNull String name, double value, @NotNull Consumer<RuntimeException> consumer) {
        if (value > 0.0) {
            return;
        }
        consumer.accept(new RuntimeException(name + " = " + value + " is not positive"));
    }

    public static void checkNotNaN(@NotNull String name, @NotNull double[] array, @NotNull Consumer<RuntimeException> consumer) {
        final int length = array.length;
        for (int index = 0; index < length; index++) {
            double value = array[index];
            if (Double.isNaN(value)) {
                consumer.accept(new RuntimeException(name + "[" + index + "] is NaN"));
            }
        }
    }

    public static void checkFinite(@NotNull String name, @NotNull double[] array, @NotNull Consumer<RuntimeException> consumer) {
        final int length = array.length;
        for (int index = 0; index < length; index++) {
            double value = array[index];
            if (Double.isFinite(value)) {
                continue;
            }
            consumer.accept(new RuntimeException(name + "[" + index + "] is not finite"));
        }
    }

    @NotNull
    public GeometryOperationResult reset() {
        return GeometryOperationResult.FAIL;
    }

    @NotNull
    public GeometryOperationResult invalidate() {
        return GeometryOperationResult.FAIL;
    }

    @NotNull
    public GeometryOperationResult repair() {
        return GeometryOperationResult.REMAIN; // nothing to repair
    }

    @NotNull
    public GeometryOperationResult normalize() {
        return GeometryOperationResult.REMAIN; // nothing to normalize
    }

    public int vectorLength() {
        return -1; // not a vector
    }

    public double vectorComponent(int index) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }

    public void dataToBuffer(@NotNull DoubleBuffer dst, boolean relative) {
        final int length = vectorLength();
        for (int index = 0; index < length; index++) {
            if (relative) {
                dst.put(vectorComponent(index));
            } else {
                dst.put(index, vectorComponent(index));
            }
        }
    }

    public void dataToBuffer(@NotNull FloatBuffer dst, boolean relative) {
        final int length = vectorLength();
        for (int index = 0; index < length; index++) {
            if (relative) {
                dst.put((float) vectorComponent(index));
            } else {
                dst.put(index, (float) vectorComponent(index));
            }
        }
    }

    @NotNull
    @Override
    public String summaryToString() {
        return ClassUtil.removeCommonPrefix(getClass(), Geometry.class);
    }

    @NotNull
    @Override
    public String detailToString() {
        final int length = vectorLength();
        if (length <= 0) {
            return "()";
        }
        final StringBuilder sb = new StringBuilder("(");
        boolean subsequent = false;
        for (int index = 0; index < length; index++) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(vectorComponent(index));
            subsequent = true;
        }
        return sb.append(")").toString();
    }
}
