package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2026/4/16.
 */
@CodeHistory(date = "2026/3/8", name = "Unsigned")
@CodeHistory(date = "2026/4/16")
public class UnsignedIntegralAppender implements IntegralAppender {

    private static final long serialVersionUID = 0xAA1A0277D25B2120L;

    public static final UnsignedIntegralAppender INSTANCE = new UnsignedIntegralAppender();

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        out.append(0xff & value);
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuffer out) {
        out.append(0xff & value);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        out.append(0xffff & value);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuffer out) {
        out.append(0xffff & value);
    }

    public void acceptChar(char value, @NotNull StringBuilder out) {
        out.append((int) value); // not affected
    }

    public void acceptChar(char value, @NotNull StringBuffer out) {
        out.append((int) value); // not affected
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuilder out) {
        out.append(Integer.toUnsignedString(value));
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuffer out) {
        out.append(Integer.toUnsignedString(value));
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuilder out) {
        out.append(Long.toUnsignedString(value));
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuffer out) {
        out.append(Long.toUnsignedString(value));
    }

    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString(10)); // not affected
    }

    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value.toString(10)); // not affected
    }
}
