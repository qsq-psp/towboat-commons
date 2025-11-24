package mujica.text.number;

import mujica.io.codec.Base16Case;
import mujica.math.algebra.discrete.CastToFloor;
import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;

@CodeHistory(date = "2025/3/3")
public class DataSizeStyle extends HexEncoder implements IntegralToStringFunction {

    public static final int STYLE_DEC = 0x01;

    public static final int STYLE_HEX = 0x02;

    public static final int HEX_PADDING = 0x04;

    public static final int STYLE_1000 = 0x10;

    public static final int STYLE_1024 = 0x20;

    public final int style;

    protected final int multiplier;

    @NotNull
    protected final PaddedRadix mantissa;

    /**
     * separator: visible characters
     * delimiter: both visible and invisible characters
     */
    @NotNull
    protected final String itemDelimiter;

    protected DataSizeStyle(@MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset, int style, int mantissa, @NotNull String itemDelimiter) {
        super(alphabetOffset);
        if (mantissa < 0 || 3 < mantissa) {
            throw new IllegalArgumentException();
        }
        this.style = style;
        this.multiplier = IntegralMath.INSTANCE.power(10, mantissa);
        this.mantissa = new PaddedRadix(10, mantissa);
        this.itemDelimiter = itemDelimiter;
    }

    @NotNull
    public static DataSizeStyle create(boolean upperCase, int style, int mantissa, @Nullable String itemDelimiter) {
        return new DataSizeStyle(
                upperCase ? UPPER : LOWER,
                style, mantissa,
                Objects.requireNonNullElse(itemDelimiter, ", ")
        );
    }

    @NotNull
    public static DataSizeStyle createDec() {
        return new DataSizeStyle(LOWER, STYLE_DEC, 0, "");
    }

    @NotNull
    public static DataSizeStyle createHex(boolean upperCase, boolean padding) {
        int style = STYLE_HEX;
        if (padding) {
            style |= HEX_PADDING;
        }
        return new DataSizeStyle(upperCase ? UPPER : LOWER, style, 0, "");
    }

    @NotNull
    public static DataSizeStyle create1000(int mantissa) {
        return new DataSizeStyle(LOWER, STYLE_1000, mantissa, "");
    }

    @NotNull
    public static DataSizeStyle create1024(int mantissa) {
        return new DataSizeStyle(LOWER, STYLE_1024, mantissa, "");
    }

    public boolean isEmpty() {
        return (style & (STYLE_DEC | STYLE_HEX | STYLE_1000 | STYLE_1024)) == 0;
    }

    private void appendMantissa(long numerator, long denominator, @NotNull StringBuilder out) {
        if (mantissa.minLength == 0) { // minLength and maxLength are same
            return;
        }
        try {
            numerator = IntegralMath.INSTANCE.multiply(numerator, multiplier); // throws ArithmeticException if overflow
            numerator = CastToFloor.INSTANCE.divide(numerator, denominator);
            out.append('.');
            mantissa.append(numerator, out);
        } catch (ArithmeticException e) {
            appendMantissa(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator), out);
        }
    }

    private void appendMantissa(BigInteger numerator, BigInteger denominator, @NotNull StringBuilder out) {
        if (mantissa.minLength == 0) { // minLength and maxLength are same
            return;
        }
        numerator = numerator.multiply(BigInteger.valueOf(multiplier)).divide(denominator);
        out.append('.');
        mantissa.append(numerator, out);
    }

    private static final String[] UNITS_1000 = {
            " byte(s)", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"
    };

    private static final String[] UNITS_1024 = {
            " byte(s)", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"
    };

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        if (value < 0) {
            throw new IllegalArgumentException("negative data size");
        }
        boolean subsequent = false;
        if ((style & STYLE_DEC) != 0 && !(value < 1000 && (style & STYLE_1000) != 0 || value < 1024 && (style & STYLE_1024) != 0)) {
            out.append(value);
            subsequent = true;
        }
        if ((style & STYLE_HEX) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            out.append("0x");
            if ((style & HEX_PADDING) != 0) {
                hex32(out, value);
            } else {
                hexMax32(out, value);
            }
            subsequent = true;
        }
        if ((style & STYLE_1000) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index = 0;
            int unit0 = 1;
            while (true) {
                long unit1 = 1000L * unit0;
                if (value < unit1) {
                    break;
                }
                index++;
                unit0 = (int) unit1;
            }
            int integral = value / unit0;
            out.append(integral);
            if (index != 0) {
                appendMantissa(value - integral * unit0, unit0, out);
            }
            out.append(UNITS_1000[index]);
            subsequent = true;
        }
        if ((style & STYLE_1024) != 0 && !(value < 1000 && (style & STYLE_1000) != 0)) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index = 0;
            int unit0 = 1;
            while (true) {
                long unit1 = ((long) unit0) << 10;
                if (value < unit1) {
                    break;
                }
                index++;
                unit0 = (int) unit1;
            }
            int integral = value / unit0;
            out.append(integral);
            if (index != 0) {
                appendMantissa(value - integral * unit0, unit0, out);
            }
            out.append(UNITS_1024[index]);
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
    public void append(long value, @NotNull StringBuilder out) {
        if (value < 0L) {
            throw new IllegalArgumentException("negative data size");
        }
        boolean subsequent = false;
        if ((style & STYLE_DEC) != 0 && !(value < 1000L && (style & STYLE_1000) != 0 || value < 1024L && (style & STYLE_1024) != 0)) {
            out.append(value);
            subsequent = true;
        }
        if ((style & STYLE_HEX) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            out.append("0x");
            if ((style & HEX_PADDING) != 0) {
                hex64(out, value);
            } else {
                hexMax64(out, value);
            }
            subsequent = true;
        }
        if ((style & STYLE_1000) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index = 0;
            long unit0 = 1L;
            while (true) {
                long unit1 = 1000L * unit0;
                if (value < unit1 || unit1 / 1000L != unit0) {
                    break;
                }
                index++;
                unit0 = unit1;
            }
            long integral = value / unit0;
            out.append(integral);
            appendMantissa(value - integral * unit0, unit0, out);
            out.append(UNITS_1000[index]);
            subsequent = true;
        }
        if ((style & STYLE_1024) != 0 && !(value < 1000L && (style & STYLE_1000) != 0)) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index = 0;
            long unit0 = 1L;
            while (true) {
                long unit1 = unit0 << 10;
                if (value < unit1 || (unit1 >>> 10) != unit0) {
                    break;
                }
                index++;
                unit0 = unit1;
            }
            long integral = value / unit0;
            out.append(integral);
            appendMantissa(value - integral * unit0, unit0, out);
            out.append(UNITS_1024[index]);
        }
    }

    @NotNull
    @Override
    public String stringify(long value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    private static final BigInteger BIG_1000 = BigInteger.valueOf(1000L);

    private static final BigInteger BIG_1024 = BigInteger.valueOf(1024L);

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        if (value.signum() < 0) {
            throw new IllegalArgumentException("negative data size");
        }
        boolean subsequent = false;
        if ((style & STYLE_DEC) != 0 && !(value.compareTo(BIG_1000) < 0 && (style & STYLE_1000) != 0 || value.compareTo(BIG_1024) < 0 && (style & STYLE_1024) != 0)) {
            out.append(value);
            subsequent = true;
        }
        if ((style & STYLE_HEX) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            String string = value.toString(16);
            if (alphabetOffset == UPPER) {
                string = string.toUpperCase();
            }
            out.append("0x").append(string);
            subsequent = true;
        }
        if ((style & STYLE_1000) != 0) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index0 = 0;
            BigInteger unit0 = BigInteger.ONE;
            while (true) {
                int index1 = index0 + 1;
                if (UNITS_1000.length <= index1) {
                    break;
                }
                BigInteger unit1 = unit0.multiply(BIG_1000);
                if (value.compareTo(unit1) < 0) {
                    break;
                }
                index0 = index1;
                unit0 = unit1;
            }
            BigInteger[] result = value.divideAndRemainder(unit0); // [quotient, remainder], length == 2
            out.append(result[0]);
            appendMantissa(result[1], unit0, out);
            out.append(UNITS_1000[index0]);
            subsequent = true;
        }
        if ((style & STYLE_1024) != 0 && !(value.compareTo(BIG_1000) < 0 && (style & STYLE_1000) != 0)) {
            if (subsequent) {
                out.append(itemDelimiter);
            }
            int index0 = 0;
            BigInteger unit0 = BigInteger.ONE;
            while (true) {
                int index1 = index0 + 1;
                if (UNITS_1024.length <= index1) {
                    break;
                }
                BigInteger unit1 = unit0.shiftLeft(10);
                if (value.compareTo(unit1) < 0) {
                    break;
                }
                index0 = index1;
                unit0 = unit1;
            }
            BigInteger[] result = value.divideAndRemainder(unit0); // [quotient, remainder], length == 2
            out.append(result[0]);
            appendMantissa(result[1], unit0, out);
            out.append(UNITS_1000[index0]);
        }
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }
}
