package mujica.ds.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.*;

@CodeHistory(date = "2026/4/4", name = "DefaultIntegralAppender")
@CodeHistory(date = "2026/4/16")
public class DefaultNumberAppender implements IntegralAppender, DecimalAppender {

    private static final long serialVersionUID = 0xC95C30F85F93964BL;

    public static final DefaultNumberAppender INSTANCE = new DefaultNumberAppender();

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        out.put(value);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        out.writeByte(value);
    }

    public void append(byte value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void append(byte value, @NotNull StringBuffer out) {
        out.append(value);
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        return String.valueOf(value);
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        out.putShort(value);
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        out.writeShort(value);
    }

    public void append(short value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void append(short value, @NotNull StringBuffer out) {
        out.append(value);
    }

    @NotNull
    @Override
    public String stringify(short value) {
        return String.valueOf(value);
    }

    @Override
    public void write(char value, @NotNull ByteBuffer out) {
        out.putChar(value);
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        out.writeChar(value);
    }

    public void append(char value, @NotNull StringBuilder out) {
        out.append((int) value);
    }

    public void append(char value, @NotNull StringBuffer out) {
        out.append((int) value);
    }

    @NotNull
    @Override
    public String stringify(char value) {
        return String.valueOf(value);
    }

    public static final List<Class<? extends Number>> NUMBER_INT_CLASSES = List.of(
            Integer.class,
            AtomicInteger.class
    );

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        out.putInt(value);
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        out.writeInt(value);
    }

    public void append(int value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void append(int value, @NotNull StringBuffer out) {
        out.append(value);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        return String.valueOf(value);
    }

    public static final List<Class<? extends Number>> NUMBER_LONG_CLASSES = List.of(
            Long.class,
            AtomicLong.class,
            LongAdder.class,
            LongAccumulator.class
    );

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        out.putLong(value);
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        out.writeLong(value);
    }

    public void append(long value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public void append(long value, @NotNull StringBuffer out) {
        out.append(value);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        return String.valueOf(value);
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuffer out) {
        out.put(value.toByteArray());
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        out.writeBytes(value.toByteArray());
    }

    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString());
    }

    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value.toString());
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        return value.toString();
    }

    public static final List<Class<? extends Number>> NUMBER_FLOAT_CLASSES = List.of(
            Float.class
    );

    @Override
    public void append(float value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public static final List<Class<? extends Number>> NUMBER_DOUBLE_CLASSES = List.of(
            Double.class,
            DoubleAdder.class,
            DoubleAccumulator.class
    );

    @Override
    public void append(double value, @NotNull StringBuilder out) {
        out.append(value);
    }
}
