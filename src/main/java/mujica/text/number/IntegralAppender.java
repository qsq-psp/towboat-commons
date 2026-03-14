package mujica.text.number;

import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_long.PublicLongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

@CodeHistory(date = "2025/2/25", name = "IntegralToStringFunction")
@CodeHistory(date = "2026/3/7")
public class IntegralAppender implements Serializable {

    private static final long serialVersionUID = 0x2F0E41CC5A5B2066L;

    public IntegralAppender() {
        super();
    }

    public void acceptByte(byte value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptShort(short value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void acceptCharacter(char value, @NotNull StringBuilder out) {
        out.append((int) value);
    }

    public static final List<Class<? extends Number>> NUMBER_INT_CLASSES = List.of(
            Integer.class,
            AtomicInteger.class,
            PublicIntSlot.class
    );

    public void acceptInteger(int value, @NotNull StringBuilder out) {
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

    @CodeHistory(date = "2026/3/8")
    public static class Unsigned extends IntegralAppender {

        private static final long serialVersionUID = 0xAA1A0277D25B2120L;

        @Override
        public void acceptByte(byte value, @NotNull StringBuilder out) {
            out.append(0xff & value);
        }

        @Override
        public void acceptShort(short value, @NotNull StringBuilder out) {
            out.append(0xffff & value);
        }

        @Override
        public void acceptInteger(int value, @NotNull StringBuilder out) {
            out.append(Integer.toUnsignedString(value));
        }

        @Override
        public void acceptLong(long value, @NotNull StringBuilder out) {
            out.append(Long.toUnsignedString(value));
        }
    }
}
