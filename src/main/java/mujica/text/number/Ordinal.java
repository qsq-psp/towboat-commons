package mujica.text.number;

import io.netty.buffer.ByteBuf;
import mujica.algebra.discrete.BigConstants;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/2/26")
public class Ordinal extends IntegralAdapter {

    private static final long serialVersionUID = 0x7F194AFD4E00D24DL;

    public static final Ordinal INSTANCE = new Ordinal();

    static final byte[] ST = {'s', 't'};

    static final byte[] ND = {'n', 'd'};

    static final byte[] RD = {'r', 'd'};

    static final byte[] TH = {'t', 'h'};

    @NotNull
    static byte[] ordinalSuffixBytes(int value) {
        switch (Math.abs(value % 100)) {
            case 1: case 21: case 31: case 41:
            case 51: case 61: case 71: case 81: case 91:
                return ST;
            case 2: case 22: case 32: case 42:
            case 52: case 62: case 72: case 82: case 92:
                return ND;
            case 3: case 23: case 33: case 43:
            case 53: case 63: case 73: case 83: case 93:
                return RD;
            default: // including 11, 12, 13
                return TH;
        }
    }

    @NotNull
    static String ordinalSuffix(int value) {
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
            default: // including 11, 12, 13
                return "th";
        }
    }

    protected Ordinal() {
        super();
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.put(ordinalSuffixBytes(value));
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.writeBytes(ordinalSuffixBytes(value));
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix(value));
    }

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.put(ordinalSuffixBytes((int) (value % 100L)));
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.writeBytes(ordinalSuffixBytes((int) (value % 100L)));
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix((int) (value % 100L)));
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix((int) (value % 100L)));
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuffer out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.put(ordinalSuffixBytes(value.mod(BigConstants.HUNDRED).intValue()));
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        SignedBase10Appender.INSTANCE.write(value, out);
        out.writeBytes(ordinalSuffixBytes(value.mod(BigConstants.HUNDRED).intValue()));
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append(value).append(ordinalSuffix(value.mod(BigConstants.HUNDRED).intValue()));
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        out.append(value).append(ordinalSuffix(value.mod(BigConstants.HUNDRED).intValue()));
    }
}
