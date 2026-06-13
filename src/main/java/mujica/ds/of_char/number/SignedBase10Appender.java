package mujica.ds.of_char.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/4/26", name = "Base10Appender")
@CodeHistory(date = "2026/5/8")
@DirectSubclass(UnsignedBase10Appender.class)
public class SignedBase10Appender implements IntegralAppender {

    private static final long serialVersionUID = 0x182EB541A46919F2L;

    public static final SignedBase10Appender INSTANCE = new SignedBase10Appender();

    protected SignedBase10Appender() {
        super();
    }

    static final int[] INT_POWER_10 = {-1, -10, -100, -1000, -10000, -100000, -1000000, -10000000, -100000000,
            -1000000000}; // length = 10

    static final int BYTE_HIGH = 3;

    static final int SHORT_HIGH = 5;

    static final int INT_HIGH = 10;

    void write(int value, int high, @NotNull ByteBuffer out) {
        assert value < 0;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > INT_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            int magnitude = INT_POWER_10[low];
            int digit = value / magnitude;
            value -= magnitude * digit;
            out.put((byte) ('0' + digit));
            low--;
        }
    }

    void write(int value, int high, @NotNull ByteBuf out) {
        assert value < 0;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > INT_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            int magnitude = INT_POWER_10[low];
            int digit = value / magnitude;
            value -= magnitude * digit;
            out.writeByte('0' + digit);
            low--;
        }
    }

    void append(int value, int high, @NotNull StringBuilder out) {
        assert value < 0;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > INT_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            int magnitude = INT_POWER_10[low];
            int digit = value / magnitude;
            value -= magnitude * digit;
            out.append((char) ('0' + digit));
            low--;
        }
    }

    void append(int value, int high, @NotNull StringBuffer out) {
        assert value < 0;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > INT_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            int magnitude = INT_POWER_10[low];
            int digit = value / magnitude;
            value -= magnitude * digit;
            out.append((char) ('0' + digit));
            low--;
        }
    }

    static final long[] LONG_POWER_10 = {-1L, -10L, -100L, -1000L, -10000L, -100000L, -1000000L, -10000000L, -100000000L,
            -1000000000L, -10000000000L, -100000000000L, -1000000000000L, -10000000000000L, -100000000000000L,
            -1000000000000000L, -10000000000000000L, -100000000000000000L, -1000000000000000000L}; // length = 19

    static final int LONG_HIGH = 19;

    void write(long value, int high, @NotNull ByteBuffer out) {
        assert value < 0L;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > LONG_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            long magnitude = LONG_POWER_10[low];
            long digit = value / magnitude;
            value -= magnitude * digit;
            out.put((byte) ('0' + digit));
            low--;
        }
    }

    void write(long value, int high, @NotNull ByteBuf out) {
        assert value < 0L;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > LONG_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            long magnitude = LONG_POWER_10[low];
            long digit = value / magnitude;
            value -= magnitude * digit;
            out.writeByte((int) ('0' + digit));
            low--;
        }
    }

    void append(long value, int high, @NotNull StringBuilder out) {
        assert value < 0L;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > LONG_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            long magnitude = LONG_POWER_10[low];
            long digit = value / magnitude;
            value -= magnitude * digit;
            out.append((char) ('0' + digit));
            low--;
        }
    }

    void append(long value, int high, @NotNull StringBuffer out) {
        assert value < 0L;
        int low = 0;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (value > LONG_POWER_10[mid]) {
                high = mid;
            } else {
                low = mid;
            }
        }
        while (low >= 0) {
            long magnitude = LONG_POWER_10[low];
            long digit = value / magnitude;
            value -= magnitude * digit;
            out.append((char) ('0' + digit));
            low--;
        }
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.put((byte) '-');
        } else {
            out.put((byte) '0');
            return;
        }
        write(intValue, BYTE_HIGH, out);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.writeByte('-');
        } else {
            out.writeByte('0');
            return;
        }
        write(intValue, BYTE_HIGH, out);
    }

    public void append(byte value, @NotNull StringBuilder out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(intValue, BYTE_HIGH, out);
    }

    public void append(byte value, @NotNull StringBuffer out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(intValue, BYTE_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        return Integer.toString(value); // Byte.toString() also call Integer.toString()
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.put((byte) '-');
        } else {
            out.put((byte) '0');
            return;
        }
        write(intValue, SHORT_HIGH, out);
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.writeByte('-');
        } else {
            out.writeByte('0');
            return;
        }
        write(intValue, SHORT_HIGH, out);
    }

    public void append(short value, @NotNull StringBuilder out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(intValue, SHORT_HIGH, out);
    }

    public void append(short value, @NotNull StringBuffer out) {
        int intValue = value;
        if (intValue > 0) {
            intValue = -intValue;
        } else if (intValue < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(intValue, SHORT_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(short value) {
        return Integer.toString(value); // Short.toString() also call Integer.toString()
    }

    @Override
    public void write(char value, @NotNull ByteBuffer out) {
        int intValue = value;
        if (intValue == 0) {
            out.put((byte) '0');
            return;
        }
        intValue = -intValue;
        write(intValue, SHORT_HIGH, out);
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        int intValue = value;
        if (intValue == 0) {
            out.writeByte('0');
            return;
        }
        intValue = -intValue;
        write(intValue, SHORT_HIGH, out);
    }

    public void append(char value, @NotNull StringBuilder out) {
        int intValue = value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, SHORT_HIGH, out);
    }

    public void append(char value, @NotNull StringBuffer out) {
        int intValue = value;
        if (intValue == 0) {
            out.append('0');
            return;
        }
        intValue = -intValue;
        append(intValue, SHORT_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(char value) {
        return Integer.toString(value);
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.put((byte) '-');
        } else {
            out.put((byte) '0');
            return;
        }
        write(value, INT_HIGH, out);
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.writeByte('-');
        } else {
            out.writeByte('0');
            return;
        }
        write(value, INT_HIGH, out);
    }

    public void append(int value, @NotNull StringBuilder out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(value, INT_HIGH, out);
    }

    public void append(int value, @NotNull StringBuffer out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(value, INT_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        return Integer.toString(value);
    }

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.put((byte) '-');
        } else {
            out.put((byte) '0');
            return;
        }
        write(value, LONG_HIGH, out);
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.writeByte('-');
        } else {
            out.writeByte('0');
            return;
        }
        write(value, LONG_HIGH, out);
    }

    public void append(long value, @NotNull StringBuilder out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(value, LONG_HIGH, out);
    }

    public void append(long value, @NotNull StringBuffer out) {
        if (value > 0) {
            value = -value;
        } else if (value < 0) {
            out.append('-');
        } else {
            out.append('0');
            return;
        }
        append(value, LONG_HIGH, out);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        return Long.toString(value);
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuffer out) {
        out.put(value.toString().getBytes(StandardCharsets.US_ASCII));
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        out.writeCharSequence(value.toString(), StandardCharsets.US_ASCII);
    }

    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value.toString(10));
    }

    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value.toString(10));
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        return value.toString(10);
    }
}
