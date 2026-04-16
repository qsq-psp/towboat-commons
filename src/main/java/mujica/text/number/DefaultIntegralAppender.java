package mujica.text.number;

import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_long.PublicLongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created on 2026/4/4.
 */
@CodeHistory(date = "2026/4/4")
public class DefaultIntegralAppender extends IntegralAppender {

    public static final DefaultIntegralAppender INSTANCE = new DefaultIntegralAppender();

    public void acceptByte(byte value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptShort(short value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptChar(char value, @NotNull StringBuilder out) {
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

    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString(10));
    }
}
