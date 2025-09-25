package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

@CodeHistory(date = "2021/10/26", project = "va")
@CodeHistory(date = "2022/10/19", project = "Ultramarine")
@CodeHistory(date = "2025/2/26")
public class TraditionalChinese implements IntegralToStringFunction, DecimalToStringFunction {

    public static final TraditionalChinese TRADITIONAL = new TraditionalChinese(
            new char[] {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'},
            new char[] {'壹', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万', '拾', '佰', '仟', '兆', '拾', '佰', '仟'}
    );

    protected static final char NEGATIVE_SIGN = '负';

    protected static final char DOT_SIGN = '点';

    protected static final String PERCENT_SIGN = "百分之";

    @NotNull
    protected final char[] radix;

    @NotNull
    protected final char[] exponent;

    protected TraditionalChinese(@NotNull char[] radix, @NotNull char[] exponent) {
        super();
        this.radix = radix;
        this.exponent = exponent;
    }

    protected void fillZero(@NotNull StringBuilder sb, int e1, int e2) {
        // System.out.println("sb = " + sb + ", e1 = " + e1 + ", e2 = " + e2);
        int e0 = 4;
        if (e1 <= 16 && 16 <= e2) {
            e0 = 16;
        } else if (e1 <= 8 && 8 <= e2) {
            e0 = 8;
        }
        for (int e = e0; e < e2; e += 4) {
            if (e1 <= e) {
                sb.append(exponent[e]);
            }
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append(stringify(value));
    }

    @NotNull
    @Override
    public String stringify(int value) {
        return stringify((long) value);
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append(stringify(value));
    }

    @NotNull
    @Override
    public String stringify(long value) {
        boolean sign;
        if (value < 0) {
            if (value == Long.MIN_VALUE) {
                throw new IllegalArgumentException();
            }
            value = -value;
            sign = true;
        } else {
            sign = false;
        }
        final StringBuilder sb = new StringBuilder();
        int zero = -1;
        int index = 0;
        while (value != 0) {
            long next = value / 10;
            int digit = (int) (value - 10 * next);
            if (digit == 0) {
                if (zero == -1) {
                    zero = index;
                }
            } else {
                if (zero != -1) {
                    if (zero != 0) {
                        sb.append(radix[0]);
                    }
                    fillZero(sb, zero, index);
                    zero = -1;
                }
                if (index != 0) {
                    sb.append(exponent[index]);
                }
            }
            value = next;
            index++;
        }
        if (index == 0) {
            sb.append(radix[0]);
        } else if (index % 4 == 2) {
            int last = sb.length() - 1;
            if (last >= 0 && sb.charAt(last) == exponent[0]) {
                sb.delete(last, last + 1);
            }
        }
        if (sign) {
            sb.append(NEGATIVE_SIGN);
        }
        return sb.reverse().toString();
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(stringify(value));
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        BigInteger remainder = value.abs();
        final StringBuilder sb = new StringBuilder();
        int zero = -1;
        int index = 0;
        while (remainder.signum() != 0) {
            BigInteger next = remainder.divide(BigInteger.TEN);
            int digit = remainder.subtract(next.multiply(BigInteger.TEN)).intValue();
            remainder = next;
            if (digit == 0) {
                if (zero == -1) {
                    zero = index;
                }
            } else {
                if (zero != -1) {
                    if (zero != 0) {
                        sb.append(radix[0]);
                    }
                    fillZero(sb, zero, index);
                    zero = -1;
                }
                if (index != 0) {
                    sb.append(exponent[index]);
                }
            }
            index++;
        }
        if (index == 0) {
            sb.append(radix[0]);
        } else if (index % 4 == 2) {
            int last = sb.length() - 1;
            if (last >= 0 && sb.charAt(last) == exponent[0]) {
                sb.delete(last, last + 1);
            }
        }
        if (value.signum() < 0) {
            sb.append(NEGATIVE_SIGN);
        }
        return sb.reverse().toString();
    }

    private void appendDecimal(@NotNull CharSequence num, int length, @NotNull StringBuilder out) {
        int index = 0;
        if (num.charAt(index) == '-') {
            out.append(NEGATIVE_SIGN);
            index++;
        }
        int zero = -1;
        while (index < length) {
            int digit = num.charAt(index++) - '0';
            if (digit == 0) {
                if (zero == -1) {
                    zero = index;
                }
            } else {
                if (zero != -1) {
                    out.append(radix[0]);
                }
                zero = -1;
                out.append(radix[digit]);
            }
        }
    }

    @Override
    public void append(@NotNull CharSequence num, int numRadix, @NotNull StringBuilder out) {
        final int start = out.length();
        try {
            int length = num.length();
            if (num.charAt(length - 1) == '%') {
                length--;
                out.append(PERCENT_SIGN);
            }
            if (numRadix != 10) { // do not support dot in non-decimals
                if (length <= 12) { // floor(log(Long.MAX_VALUE, Character.MAX_RADIX)) = 12
                    stringify(Long.parseLong(num, 0, length, numRadix));
                } else {
                    stringify(new BigInteger(num.subSequence(0, length).toString(), numRadix));
                }
                return;
            }
            int index = 0;
            while (index < length) {
                if (num.charAt(index++) == '.') {
                    appendDecimal(num, index, out);
                    out.append(DOT_SIGN);
                    while (index < length) {
                        out.append(radix[num.charAt(index++) - '0']);
                    }
                    return;
                }
            }
            appendDecimal(num, length, out);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NumberFormatException(num.toString());
        } finally {
            out.delete(start, out.length());
        }
    }

    @NotNull
    @Override
    public String stringify(@NotNull CharSequence num, int numRadix) {
        final StringBuilder sb = new StringBuilder();
        append(num, numRadix, sb);
        return sb.toString();
    }

    @Override
    public void append(@NotNull BigDecimal decimal, @NotNull StringBuilder out) {

    }

    @NotNull
    @Override
    public String stringify(@NotNull BigDecimal decimal) {
        return "";
    }
}
