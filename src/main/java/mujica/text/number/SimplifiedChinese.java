package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2021/10/26", project = "va")
@CodeHistory(date = "2022/10/19", project = "Ultramarine")
@CodeHistory(date = "2025/2/26")
public class SimplifiedChinese extends TraditionalChinese {

    public static final SimplifiedChinese SIMPLIFIED = new SimplifiedChinese(
            new char[] {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'},
            new char[] {'一', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百', '千', '万', '十', '百', '千', '兆', '十', '百', '千'}
    );

    private static final char TWO_PRIME = '两';

    protected SimplifiedChinese(@NotNull char[] radix, @NotNull char[] exponent) {
        super(radix, exponent);
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
                    if (index % 4 == 1) {
                        int last = sb.length() - 1;
                        if (last >= 0 && sb.charAt(last) == TWO_PRIME) {
                            sb.setCharAt(last, radix[2]);
                        }
                    }
                    sb.append(exponent[index]);
                }
                if (digit == 2 && index != 0 && index % 4 != 1) {
                    sb.append(TWO_PRIME);
                } else {
                    sb.append(radix[digit]);
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
                    if (index % 4 == 1) {
                        int last = sb.length() - 1;
                        if (last >= 0 && sb.charAt(last) == TWO_PRIME) {
                            sb.setCharAt(last, radix[2]);
                        }
                    }
                    sb.append(exponent[index]);
                }
                if (digit == 2 && index != 0 && index % 4 != 1) {
                    sb.append(TWO_PRIME);
                } else {
                    sb.append(radix[digit]);
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
        if (value.signum() < 0) {
            sb.append(NEGATIVE_SIGN);
        }
        return sb.reverse().toString();
    }

    private void appendDecimal(@NotNull CharSequence num, int length, @NotNull StringBuilder out) {
        for (int index = 0; index < length; index++) {
            int digit = num.charAt(index) - '0';
            int position = length - index - 1;
            if (digit == 2 && position != 0 && position % 4 != 1) {
                out.append(TWO_PRIME);
            } else {
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
                if (num.charAt(index) != '.') {
                    index++;
                    continue;
                }
                appendDecimal(num, index, out);
                out.append(DOT_SIGN);
                index++;
                while (index < length) {
                    out.append(radix[num.charAt(index++) - '0']);
                }
                return;
            }
            appendDecimal(num, length, out);
        } catch (ArrayIndexOutOfBoundsException e) {
            out.delete(start, out.length());
            throw new NumberFormatException(num.toString());
        }
    }
}
