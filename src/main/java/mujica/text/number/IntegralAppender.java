package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;

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

    public void acceptChar(char value, @NotNull StringBuilder out) {
        out.append((int) value);
    }

    public void acceptInt(int value, @NotNull StringBuilder out) {
        out.append(value);
    }

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
        public void acceptInt(int value, @NotNull StringBuilder out) {
            out.append(Integer.toUnsignedString(value));
        }

        @Override
        public void acceptLong(long value, @NotNull StringBuilder out) {
            out.append(Long.toUnsignedString(value));
        }
    }
}
