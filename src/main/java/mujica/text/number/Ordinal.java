package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/2/26")
public class Ordinal implements IntegralToStringFunction {

    public static final Ordinal INSTANCE = new Ordinal();

    @NotNull
    public static String ordinalSuffix(long value) {
        switch (Math.abs((int) (value % 100))) {
            case 1: case 21: case 31: case 41:
            case 51: case 61: case 71: case 81: case 91:
                return "st";
            case 2: case 22: case 32: case 42:
            case 52: case 62: case 72: case 82: case 92:
                return "nd";
            case 3: case 23: case 33: case 43:
            case 53: case 63: case 73: case 83: case 93:
                return "rd";
            default: // including 11, 12, 13, Long.MIN_VALUE is also considered
                return "th";
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @NotNull
    @Override
    public String stringify(int value) {
        return value + ordinalSuffix(value);
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @NotNull
    @Override
    public String stringify(long value) {
        return value + ordinalSuffix(value);
    }

    private static final BigInteger HUNDRED = BigInteger.valueOf(100L);

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value.mod(HUNDRED).intValue()));
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        return value + ordinalSuffix(value.mod(HUNDRED).intValue());
    }
}
