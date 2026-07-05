package mujica.ds.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2026/3/8", name = "Unsigned")
@CodeHistory(date = "2026/4/16", name = "UnsignedIntegralAppender")
@CodeHistory(date = "2026/5/7")
public class UnsignedBase10Appender extends SignedBase10Appender {

    private static final long serialVersionUID = 0xAA1A0277D25B2120L;

    public static final UnsignedBase10Appender INSTANCE = new UnsignedBase10Appender();

    protected UnsignedBase10Appender() {
        super();
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        int intValue = 0xff & value;
        if (intValue == 0) {
            out.put((byte) '0');
            return;
        }
        intValue = -intValue;
        write(intValue, BYTE_HIGH, out);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        int intValue = 0xff & value;
        if (intValue == 0) {
            out.writeByte('0');
            return;
        }
        intValue = -intValue;
        write(intValue, BYTE_HIGH, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        int intValue = 0xff & value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, BYTE_HIGH, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        int intValue = 0xff & value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, BYTE_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        return super.stringify(0xff & value);
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        int intValue = 0xffff & value;
        if (intValue == 0) {
            out.put((byte) '0');
            return;
        }
        intValue = -intValue;
        write(intValue, SHORT_HIGH, out);
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        int intValue = 0xffff & value;
        if (intValue == 0) {
            out.writeByte('0');
            return;
        }
        intValue = -intValue;
        write(intValue, SHORT_HIGH, out);
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        int intValue = 0xffff & value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, SHORT_HIGH, out);
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        int intValue = 0xffff & value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, SHORT_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(short value) {
        return super.stringify(0xffff & value);
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        if (value > 0) {
            value = -value;
            write(value, INT_HIGH, out);
        } else if (value < 0) {
            write(0xffffffffL & value, INT_HIGH, out);
        } else {
            out.put((byte) '0');
        }
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        if (value > 0) {
            value = -value;
            write(value, INT_HIGH, out);
        } else if (value < 0) {
            write(0xffffffffL & value, INT_HIGH, out);
        } else {
            out.writeByte('0');
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        if (value > 0) {
            value = -value;
            append(value, INT_HIGH, out);
        } else if (value < 0) {
            append(-(0xffffffffL & value), INT_HIGH, out);
        } else {
            out.append('0');
        }
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        if (value > 0) {
            value = -value;
            append(value, INT_HIGH, out);
        } else if (value < 0) {
            append(-(0xffffffffL & value), INT_HIGH, out);
        } else {
            out.append('0');
        }
    }

    @NotNull
    @Override
    public String stringify(int value) {
        if (value < 0) {
            return Long.toString(0xffffffffL & value);
        } else {
            return Integer.toString(value);
        }
    }

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        if (value > 0) {
            value = -value;
            write(value, LONG_HIGH, out);
        } else if (value < 0) {
            long positiveValue = (value >>> 1) / 5L;
            long lastDigit = value - positiveValue * 10L;
            write(-positiveValue, LONG_HIGH, out);
            out.put((byte) ('0' + lastDigit));
        } else {
            out.put((byte) '0');
        }
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        if (value > 0) {
            value = -value;
            write(value, LONG_HIGH, out);
        } else if (value < 0) {
            long positiveValue = (value >>> 1) / 5L;
            long lastDigit = value - positiveValue * 10L;
            write(-positiveValue, LONG_HIGH, out);
            out.writeByte((int) ('0' + lastDigit));
        } else {
            out.writeByte('0');
        }
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        if (value > 0) {
            value = -value;
            append(value, LONG_HIGH, out);
        } else if (value < 0) {
            long positiveValue = (value >>> 1) / 5L;
            long lastDigit = value - positiveValue * 10L;
            append(-positiveValue, LONG_HIGH, out);
            out.append((char) ('0' + lastDigit));
        } else {
            out.append('0');
        }
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        if (value > 0) {
            value = -value;
            append(value, LONG_HIGH, out);
        } else if (value < 0) {
            long positiveValue = (value >>> 1) / 5L;
            long lastDigit = value - positiveValue * 10L;
            append(-positiveValue, LONG_HIGH, out);
            out.append((char) ('0' + lastDigit));
        } else {
            out.append('0');
        }
    }

    @NotNull
    @Override
    public String stringify(long value) {
        if (value < 0) {
            StringBuilder sb = new StringBuilder(LONG_HIGH);
            long positiveValue = (value >>> 1) / 5L;
            long lastDigit = value - positiveValue * 10L;
            append(-positiveValue, LONG_HIGH, sb);
            sb.append((char) ('0' + lastDigit));
            return sb.toString();
        } else {
            return Long.toString(value);
        }
    }
}
