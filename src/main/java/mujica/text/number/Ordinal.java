package mujica.text.number;

import mujica.algebra.discrete.BigConstants;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/2/26")
public class Ordinal implements IntegralAppender {

    private static final long serialVersionUID = 0x7F194AFD4E00D24DL;

    public static final Ordinal INSTANCE = new Ordinal();

    @NotNull
    public static String ordinalSuffix(int value) {
        switch (Math.abs(value % 100)) {
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

    public Ordinal() {
        super();
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuffer out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuffer out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptChar(char value, @NotNull StringBuilder out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptChar(char value, @NotNull StringBuffer out) {
        acceptInt(value, out);
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @Override
    public void acceptInt(int value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix((int) (value % 100L)));
    }

    @Override
    public void acceptLong(long value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix((int) (value % 100L)));
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value.mod(BigConstants.HUNDRED).intValue()));
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix(value.mod(BigConstants.HUNDRED).intValue()));
    }
}
