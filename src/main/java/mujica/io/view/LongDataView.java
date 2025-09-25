package mujica.io.view;

import mujica.math.algebra.discrete.ClampedMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

/**
 * Created on 2025/5/15.
 */
@CodeHistory(date = "2025/5/15")
public class LongDataView implements DataView {

    @NotNull
    private final LongSupplier longSupplier;

    @NotNull
    private final ByteFillPolicy byteFillPolicy;

    private final int bitLength;

    public LongDataView(@NotNull LongSupplier longSupplier, @NotNull ByteFillPolicy byteFillPolicy, int bitLength) {
        super();
        if (bitLength <= 0 || bitLength > Long.SIZE) {
            throw new DataSizeMismatchException();
        }
        this.longSupplier = longSupplier;
        this.byteFillPolicy = byteFillPolicy;
        this.bitLength = bitLength;
    }

    public LongDataView(long value, @NotNull ByteFillPolicy byteFillPolicy, int bitLength) {
        this(() -> value, byteFillPolicy, bitLength);
    }

    @Override
    public int bitLength() {
        return bitLength;
    }

    @Override
    public boolean getBit(int index) {
        if (index < 0 || index >= bitLength) {
            throw new IndexOutOfBoundsException();
        }
        switch (byteFillPolicy) {
            case LEFT_TO_MIDDLE:
                index = Long.SIZE - 1 - index;
                break;
            case MIDDLE_TO_LEFT:
                index += Long.SIZE - bitLength;
                break;
            case RIGHT_TO_MIDDLE:
                break;
            case MIDDLE_TO_RIGHT:
                index = bitLength - 1 - index;
                break;
            default:
                throw new RuntimeException();
        }
        return (longSupplier.getAsLong() & (1L << index)) != 0L;
    }

    @Override
    public boolean getBitExact() {
        if (bitLength != 1) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (value & Long.MIN_VALUE) != 0L;
        } else {
            return (value & 1L) != 0L;
        }
    }

    @Override
    public int byteLength() {
        return (bitLength + (Byte.SIZE - 1)) >>> 3;
    }

    @Override
    public byte getByte(int index) {
        index = ClampedMath.INSTANCE.multiply(index, Byte.SIZE);
        if (index < 0 || index >= bitLength) {
            throw new IndexOutOfBoundsException();
        }
        switch (byteFillPolicy) {
            case LEFT_TO_MIDDLE:
                index = Long.SIZE - Byte.SIZE - index;
                break;
            case MIDDLE_TO_LEFT:
                index += Long.SIZE - bitLength;
                break;
            case RIGHT_TO_MIDDLE:
                break;
            case MIDDLE_TO_RIGHT:
                index = bitLength - Byte.SIZE - index;
                break;
            default:
                throw new RuntimeException();
        }
        return (byte) (longSupplier.getAsLong() >>> index);
    }

    @Override
    public byte getByteAll() {
        return 0;
    }

    @Override
    public byte getByteExact() {
        if (bitLength != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (byte) (value >>> (Long.SIZE - Byte.SIZE));
        } else {
            return (byte) value;
        }
    }

    @Override
    public short getUnsignedByte(int index) {
        return 0;
    }

    @Override
    public short getUnsignedByteAll() {
        return 0;
    }

    @Override
    public short getUnsignedByteExact() {
        if (bitLength != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (short) (value >>> (Long.SIZE - Byte.SIZE));
        } else {
            return (short) (0xff & value);
        }
    }

    @Override
    public int shortLength() {
        return 0;
    }

    @Override
    public short getShort(int index) {
        return 0;
    }

    @Override
    public short shortAt(int byteOffset) {
        return 0;
    }

    @Override
    public short getShortAll() {
        return 0;
    }

    @Override
    public short getShortExact() {
        if (bitLength != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (short) (value >>> (Long.SIZE - Short.SIZE));
        } else {
            return (short) value;
        }
    }

    @Override
    public int getUnsignedShort(int index) {
        return 0;
    }

    @Override
    public int unsignedShortAt(int byteOffset) {
        return 0;
    }

    @Override
    public int getUnsignedShortAll() {
        return 0;
    }

    @Override
    public int getUnsignedShortExact() {
        if (bitLength != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (int) (value >>> (Long.SIZE - Short.SIZE));
        } else {
            return 0xffff & (int) value;
        }
    }

    @Override
    public int intLength() {
        return 0;
    }

    @Override
    public int getInt(int index) {
        return 0;
    }

    @Override
    public int intAt(int byteOffset) {
        return 0;
    }

    @Override
    public int getIntAll() {
        return 0;
    }

    @Override
    public int getIntExact() {
        if (bitLength != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (int) (value >>> (Long.SIZE - Integer.SIZE));
        } else {
            return (int) value;
        }
    }

    @Override
    public long getUnsignedInt(int index) {
        return 0;
    }

    @Override
    public long unsignedIntAt(int byteOffset) {
        return 0;
    }

    @Override
    public long getUnsignedIntAll() {
        return 0;
    }

    @Override
    public long getUnsignedIntExact() {
        if (bitLength != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        final long value = longSupplier.getAsLong();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (value >>> (Long.SIZE - Integer.SIZE));
        } else {
            return 0xffffffffL & value;
        }
    }

    @Override
    public int longLength() {
        return 1;
    }

    @Override
    public long getLong(int index) {
        if (index == 0) {
            long value = longSupplier.getAsLong();
            if (byteFillPolicy.align == ByteAlign.RIGHT) {
                return ((1L << bitLength) - 1L) & value;
            } else {
                return value >>> (Long.SIZE - bitLength);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long longAt(int byteOffset) {
        return 0;
    }

    @Override
    public long getLongAll() {
        return ((1L << bitLength) - 1L) & longSupplier.getAsLong();
    }

    @Override
    public long getLongExact() {
        if (bitLength == Long.SIZE) {
            return longSupplier.getAsLong();
        } else {
            throw new DataSizeMismatchException();
        }
    }

    @NotNull
    @Override
    public String toBinaryString() {
        return null;
    }

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        return null;
    }
}
