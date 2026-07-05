package mujica.ds.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@CodeHistory(date = "2026/4/17", name = "OctalAppender")
@CodeHistory(date = "2026/5/7")
public abstract class Base8Appender extends DefaultNumberAppender {

    abstract Base8Appender write3Bits(int value, @NotNull ByteBuffer out);

    abstract Base8Appender write3Bits(int value, @NotNull ByteBuf out);

    abstract Base8Appender append3Bits(int value, @NotNull StringBuilder out);

    abstract Base8Appender append3Bits(int value, @NotNull StringBuffer out);

    public static final Base8Appender TRIMMED = new Base8Appender() {

        @Override
        Base8Appender write3Bits(int value, @NotNull ByteBuffer out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.put((byte) ('0' + value));
            return PADDED;
        }

        @Override
        Base8Appender write3Bits(int value, @NotNull ByteBuf out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.writeByte('0' + value);
            return PADDED;
        }

        @Override
        Base8Appender append3Bits(int value, @NotNull StringBuilder out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.append((char) ('0' + value));
            return PADDED;
        }

        @Override
        Base8Appender append3Bits(int value, @NotNull StringBuffer out) {
            value &= 0x7;
            if (value == 0) {
                return this;
            }
            out.append((char) ('0' + value));
            return PADDED;
        }

        @Override
        public String toString() {
            return "Base8Appender(trimmed)";
        }
    };

    public static final Base8Appender PADDED = new Base8Appender() {

        @Override
        Base8Appender write3Bits(int value, @NotNull ByteBuffer out) {
            value &= 0x7;
            out.put((byte) ('0' + value));
            return this;
        }

        @Override
        Base8Appender write3Bits(int value, @NotNull ByteBuf out) {
            value &= 0x7;
            out.writeByte('0' + value);
            return this;
        }

        @Override
        Base8Appender append3Bits(int value, @NotNull StringBuilder out) {
            value &= 0x7;
            out.append((char) ('0' + value));
            return this;
        }

        @Override
        Base8Appender append3Bits(int value, @NotNull StringBuffer out) {
            value &= 0x7;
            out.append((char) ('0' + value));
            return this;
        }

        @Override
        public String toString() {
            return "Base8Appender(padded)";
        }
    };

    private Base8Appender() {
        super();
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        write3Bits(value >> 6, out.put((byte) '0')).write3Bits(value >> 3, out).write3Bits(value, out);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        write3Bits(value >> 6, out.writeByte('0')).write3Bits(value >> 3, out).write3Bits(value, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        append3Bits(value >> 6, out.append('0')).append3Bits(value >> 3, out).append3Bits(value, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        append3Bits(value >> 6, out.append('0')).append3Bits(value >> 3, out).append3Bits(value, out);
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        if (value == 0) {
            return "0";
        }
        return '0' + Integer.toString(0xff & value, 8);
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        out.put((byte) '0');
        Base8Appender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        out.writeByte('0');
        Base8Appender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Short.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @NotNull
    @Override
    public String stringify(short value) {
        if (value == 0) {
            return "0";
        }
        return '0' + Integer.toString(0xffff & value, 8);
    }

    @Override
    public void write(char value, @NotNull ByteBuffer out) {
        out.put((byte) '0');
        Base8Appender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        out.writeByte('0');
        Base8Appender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(char value, @NotNull StringBuffer out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Character.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @NotNull
    @Override
    public String stringify(char value) {
        if (value == 0) {
            return "0";
        }
        return '0' + Integer.toString(value, 8);
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        out.put((byte) '0');
        Base8Appender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        out.writeByte('0');
        Base8Appender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Integer.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits(value >>> shift, out);
            shift -= 3;
        }
    }

    @NotNull
    @Override
    public String stringify(int value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        out.put((byte) '0');
        Base8Appender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        out.writeByte('0');
        Base8Appender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.write3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        out.append('0');
        Base8Appender appender = this;
        int shift = (Long.SIZE - 1) / 3 * 3;
        while (shift >= 0) {
            appender = appender.append3Bits((int) (value >>> shift), out);
            shift -= 3;
        }
    }

    @NotNull
    @Override
    public String stringify(long value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        if (value.signum() == 0) {
            out.append('0');
            return;
        }
        final int index = out.length();
        out.append('0').append(value.toString(8));
        if (out.charAt(index + 1) == '-') {
            // replace "0-" with "-0"
            out.setCharAt(index, '-');
            out.setCharAt(index + 1, '0');
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        if (value.signum() == 0) {
            out.append('0');
            return;
        }
        final int index = out.length();
        out.append('0').append(value.toString(8));
        if (out.charAt(index + 1) == '-') {
            // replace "0-" with "-0"
            out.setCharAt(index, '-');
            out.setCharAt(index + 1, '0');
        }
    }
}
