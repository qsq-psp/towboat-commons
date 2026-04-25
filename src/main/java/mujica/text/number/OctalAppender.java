package mujica.text.number;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2026/4/17.
 */
public abstract class OctalAppender implements IntegralAppender { // Base8Appender

    abstract OctalAppender append3Bits(int value, @NotNull StringBuilder out);

    abstract OctalAppender append3Bits(int value, @NotNull StringBuffer out);

    public static final OctalAppender TRIMMED = new OctalAppender() {

        @Override
        OctalAppender append3Bits(int value, @NotNull StringBuilder out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.append((char) ('0' + value));
            return PADDED;
        }

        @Override
        OctalAppender append3Bits(int value, @NotNull StringBuffer out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.append((char) ('0' + value));
            return PADDED;
        }
    };

    public static final OctalAppender PADDED = new OctalAppender() {

        @Override
        OctalAppender append3Bits(int value, @NotNull StringBuilder out) {
            value &= 0x7;
            out.append((char) ('0' + value));
            return this;
        }

        @Override
        OctalAppender append3Bits(int value, @NotNull StringBuffer out) {
            value &= 0x7;
            out.append((char) ('0' + value));
            return this;
        }
    };

    private OctalAppender() {
        super();
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        append3Bits(value >> 6, out.append('0')).append3Bits(value >> 3, out).append3Bits(value, out);
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuffer out) {
        append3Bits(value >> 6, out.append('0')).append3Bits(value >> 3, out).append3Bits(value, out);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuffer out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptChar(char value, @NotNull StringBuilder out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptChar(char value, @NotNull StringBuffer out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuilder out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuffer out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuilder out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuffer out) {
        out.append('0');
        OctalAppender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString(8));
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value.toString(8));
    }
}
