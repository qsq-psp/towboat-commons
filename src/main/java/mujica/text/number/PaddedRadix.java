package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Special case: when minLength == 0, 0 is converted to empty string ""
 */
@CodeHistory(date = "2025/2/25")
@SuppressWarnings("StringRepeatCanBeUsed")
public class PaddedRadix implements IntegralToStringFunction, Serializable {

    private static final long serialVersionUID = 0x24eac7505fd7a969L;

    public final int radix, minLength, maxLength;

    public PaddedRadix(int radix, int minLength, int maxLength) {
        super();
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            throw new IllegalArgumentException("Radix out of range");
        }
        if (!(0 <= minLength && minLength <= maxLength)) {
            throw new IllegalArgumentException("Invalid length range");
        }
        this.radix = radix;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public PaddedRadix(int radix, int length) {
        this(radix, length, length);
    }

    public PaddedRadix(int length) {
        this(10, length, length);
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        if (value == 0 && minLength == 0) {
            return;
        }
        final String string = Integer.toString(value, radix);
        final int length = string.length();
        if (value < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                out.append('-');
                while (numLength++ < minLength) {
                    out.append('0');
                }
                out.append(string, 1, length);
                return;
            }
            if (numLength > maxLength) {
                out.append('-').append(string, numLength - maxLength, numLength);
                return;
            }
        } else {
            if (length < minLength) {
                for (int numLength = length; numLength < minLength; numLength++) {
                    out.append('0');
                }
                out.append(string);
                return;
            }
            if (length > maxLength) {
                out.append(string, length - maxLength, length);
                return;
            }
        }
        out.append(string);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        if (value == 0 && minLength == 0) {
            return "";
        }
        final String string = Integer.toString(value, radix);
        final int length = string.length();
        if (value < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                return '-' + "0".repeat(minLength - numLength) + string.substring(1);
            }
            if (numLength > maxLength) {
                return '-' + string.substring(length - maxLength);
            }
        } else {
            if (length < minLength) {
                return "0".repeat(minLength - length) + string;
            }
            if (length > maxLength) {
                return string.substring(length - maxLength);
            }
        }
        return string;
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        if (value == 0L && minLength == 0) {
            return;
        }
        final String string = Long.toString(value, radix);
        final int length = string.length();
        if (value < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                out.append('-');
                while (numLength++ < minLength) {
                    out.append('0');
                }
                out.append(string, 1, length);
                return;
            }
            if (numLength > maxLength) {
                out.append('-').append(string, numLength - maxLength, numLength);
                return;
            }
        } else {
            if (length < minLength) {
                for (int numLength = length; numLength < minLength; numLength++) {
                    out.append('0');
                }
                out.append(string);
                return;
            }
            if (length > maxLength) {
                out.append(string, length - maxLength, length);
                return;
            }
        }
        out.append(string);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        if (value == 0L && minLength == 0) {
            return "";
        }
        final String string = Long.toString(value, radix);
        final int length = string.length();
        if (value < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                return '-' + "0".repeat(minLength - numLength) + string.substring(1);
            }
            if (numLength > maxLength) {
                return '-' + string.substring(length - maxLength);
            }
        } else {
            if (length < minLength) {
                return "0".repeat(minLength - length) + string;
            }
            if (length > maxLength) {
                return string.substring(length - maxLength);
            }
        }
        return string;
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        if (value.signum() == 0 && minLength == 0) {
            return;
        }
        final String string = value.toString(radix);
        final int length = string.length();
        if (value.signum() < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                out.append('-');
                while (numLength++ < minLength) {
                    out.append('0');
                }
                out.append(string, 1, length);
                return;
            }
            if (numLength > maxLength) {
                out.append('-').append(string, numLength - maxLength, numLength);
                return;
            }
        } else {
            if (length < minLength) {
                for (int numLength = length; numLength < minLength; numLength++) {
                    out.append('0');
                }
                out.append(string);
                return;
            }
            if (length > maxLength) {
                out.append(string, length - maxLength, length);
                return;
            }
        }
        out.append(string);
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        if (value.signum() == 0 && minLength == 0) {
            return "";
        }
        final String string = value.toString(radix);
        final int length = string.length();
        if (value.signum() < 0) {
            int numLength = length - 1;
            if (numLength < minLength) {
                return '-' + "0".repeat(minLength - numLength) + string.substring(1);
            }
            if (numLength > maxLength) {
                return '-' + string.substring(length - maxLength);
            }
        } else {
            if (length < minLength) {
                return "0".repeat(minLength - length) + string;
            }
            if (length > maxLength) {
                return string.substring(length - maxLength);
            }
        }
        return string;
    }
}
