package mujica.io.view;

import mujica.math.algebra.discrete.ClampedMath;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.HexEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

@CodeHistory(date = "2025/5/14")
public class IntDataView implements DataView {

    @NotNull
    private final IntSupplier intSupplier;

    @NotNull
    private final ByteFillPolicy byteFillPolicy;

    private final int bitLength;

    public IntDataView(@NotNull IntSupplier intSupplier, @NotNull ByteFillPolicy byteFillPolicy, int bitLength) {
        super();
        if (bitLength <= 0 || bitLength > Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        this.intSupplier = intSupplier;
        this.byteFillPolicy = byteFillPolicy;
        this.bitLength = bitLength;
    }

    public IntDataView(int value, @NotNull ByteFillPolicy byteFillPolicy, int bitLength) {
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
                index = Integer.SIZE - 1 - index;
                break;
            case MIDDLE_TO_LEFT:
                index += Integer.SIZE - bitLength;
                break;
            case RIGHT_TO_MIDDLE:
                break;
            case MIDDLE_TO_RIGHT:
                index = bitLength - 1 - index;
                break;
            default:
                throw new RuntimeException();
        }
        return (intSupplier.getAsInt() & (1 << index)) != 0;
    }

    @Override
    public boolean getBitExact() {
        if (bitLength != 1) {
            throw new DataSizeMismatchException();
        }
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (value & Integer.MIN_VALUE) != 0;
        } else {
            return (value & 1) != 0;
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
                index = Integer.SIZE - Byte.SIZE - index;
                break;
            case MIDDLE_TO_LEFT:
                index += Integer.SIZE - bitLength;
                break;
            case RIGHT_TO_MIDDLE:
                break;
            case MIDDLE_TO_RIGHT:
                index = bitLength - Byte.SIZE - index;
                break;
            default:
                throw new RuntimeException();
        }
        return (byte) (intSupplier.getAsInt() >>> index);
    }

    @Override
    public byte getByteAll() {
        if (bitLength > Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (byte) (value >>> (Integer.SIZE - Byte.SIZE));
        } else {
            return (byte) value;
        }
    }

    @Override
    public byte getByteExact() {
        if (bitLength != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (byte) (value >>> (Integer.SIZE - Byte.SIZE));
        } else {
            return (byte) value;
        }
    }

    @Override
    public short getUnsignedByte(int index) {
        index = ClampedMath.INSTANCE.multiply(index, Byte.SIZE);
        if (index < 0 || index >= bitLength) {
            throw new IndexOutOfBoundsException();
        }
        switch (byteFillPolicy) {
            case LEFT_TO_MIDDLE:
                index = Integer.SIZE - Byte.SIZE - index;
                break;
            case MIDDLE_TO_LEFT:
                index += Integer.SIZE - bitLength;
                break;
            case RIGHT_TO_MIDDLE:
                break;
            case MIDDLE_TO_RIGHT:
                index = bitLength - Byte.SIZE - index;
                break;
            default:
                throw new RuntimeException();
        }
        return (short) (0xff & (intSupplier.getAsInt() >>> index));
    }

    @Override
    public short getUnsignedByteAll() {
        if (bitLength > Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (short) (value >>> (Integer.SIZE - Byte.SIZE));
        } else {
            return (short) (0xff & value);
        }
    }

    @Override
    public short getUnsignedByteExact() {
        if (bitLength != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (short) (value >>> (Integer.SIZE - Byte.SIZE));
        } else {
            return (short) (0xff & value);
        }
    }

    @Override
    public int shortLength() {
        return (bitLength + (Short.SIZE - 1)) >>> 4;
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
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return (short) (value >>> (Short.SIZE - Byte.SIZE));
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
        final int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            return value >>> (Short.SIZE - Byte.SIZE);
        } else {
            return 0xffff & value;
        }
    }

    @Override
    public int intLength() {
        return 1;
    }

    @Override
    public int getInt(int index) {
        if (index == 0) {
            int value = intSupplier.getAsInt();
            if (byteFillPolicy.align == ByteAlign.RIGHT) {
                return ((1 << bitLength) - 1) & value;
            } else {
                return value >>> (Integer.SIZE - bitLength);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
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
        if (bitLength == Integer.SIZE) {
            return intSupplier.getAsInt();
        } else {
            throw new DataSizeMismatchException();
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
        if (bitLength == Integer.SIZE) {
            return 0xffffffffL & intSupplier.getAsInt();
        } else {
            throw new DataSizeMismatchException();
        }
    }

    @Override
    public int longLength() {
        return 1;
    }

    @Override
    public long getLong(int index) {
        if (index == 0) {
            return ((1L << bitLength) - 1L) & intSupplier.getAsInt();
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
        return ((1L << bitLength) - 1L) & intSupplier.getAsInt();
    }

    @Override
    public long getLongExact() {
        throw new DataSizeMismatchException();
    }

    @NotNull
    @Override
    public String toBinaryString() {
        int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            value >>>= Integer.SIZE - bitLength;
        }
        return Integer.toBinaryString(value);
    }

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        int value = intSupplier.getAsInt();
        if (byteFillPolicy.align == ByteAlign.LEFT) {
            value >>>= Integer.SIZE - bitLength;
        }
        if (upperCase) {
            return HexEncoder.UPPER_ENCODER.hex32(value);
        } else {
            return HexEncoder.LOWER_ENCODER.hex32(value);
        }
    }
}
