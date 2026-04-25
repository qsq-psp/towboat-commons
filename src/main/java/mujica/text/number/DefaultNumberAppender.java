package mujica.text.number;

import mujica.ds.of_double.PublicDoubleSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_long.PublicLongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.*;

/**
 * Created on 2026/4/4.
 */
@CodeHistory(date = "2026/4/4", name = "DefaultIntegralAppender")
@CodeHistory(date = "2026/4/16")
public class DefaultNumberAppender implements IntegralAppender, DecimalAppender {

    private static final long serialVersionUID = 0xC95C30F85F93964BL;

    public static final DefaultNumberAppender INSTANCE = new DefaultNumberAppender();

    public void acceptByte(byte value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptByte(byte value, @NotNull StringBuffer out) {
        out.append(value);
    }

    public void acceptShort(short value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptShort(short value, @NotNull StringBuffer out) {
        out.append(value);
    }

    public void acceptChar(char value, @NotNull StringBuilder out) {
        out.append((int) value);
    }

    public void acceptChar(char value, @NotNull StringBuffer out) {
        out.append((int) value);
    }

    public static final List<Class<? extends Number>> NUMBER_INT_CLASSES = List.of(
            Integer.class,
            AtomicInteger.class,
            PublicIntSlot.class
    );

    public void acceptInt(int value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptInt(int value, @NotNull StringBuffer out) {
        out.append(value);
    }

    public static final List<Class<? extends Number>> NUMBER_LONG_CLASSES = List.of(
            Long.class,
            AtomicLong.class,
            LongAdder.class,
            LongAccumulator.class,
            PublicLongSlot.class
    );

    public void acceptLong(long value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptLong(long value, @NotNull StringBuffer out) {
        out.append(value);
    }

    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString(10));
    }

    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value.toString(10));
    }

    public static final List<Class<? extends Number>> NUMBER_FLOAT_CLASSES = List.of(
            Float.class
    );

    @Override
    public void acceptFloat(float value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public static final List<Class<? extends Number>> NUMBER_DOUBLE_CLASSES = List.of(
            Double.class,
            DoubleAdder.class,
            DoubleAccumulator.class,
            PublicDoubleSlot.class
    );

    @Override
    public void acceptDouble(double value, @NotNull StringBuilder out) {
        out.append(value);
    }
}
