package mujica.io.hash;

import mujica.ds.of_int.list.IntSequence;
import mujica.io.codec.Base16Case;
import mujica.math.algebra.discrete.ClampedMath;
import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

@CodeHistory(date = "2025/5/9")
public class IntSequenceDataView implements DataView {

    @NotNull
    private final IntSequence intSequence;

    @NotNull
    private final ByteOrder byteOrder;

    @NotNull
    private final Runnable guard;

    public IntSequenceDataView(@NotNull IntSequence intSequence, @NotNull ByteOrder byteOrder, @NotNull Runnable guard) {
        super();
        this.intSequence = intSequence;
        this.byteOrder = byteOrder;
        this.guard = guard;
    }

    public IntSequenceDataView(@NotNull IntSequence intSequence, @NotNull ByteOrder byteOrder) {
        this(intSequence, byteOrder, NOP_GUARD);
    }

    @Override
    public int bitLength() {
        guard.run();
        return ClampedMath.INSTANCE.multiply(intSequence.intLength(), Integer.SIZE);
    }

    @Override
    public boolean getBit(int index) {
        guard.run();
        final int value = intSequence.getInt(index >>> 5);
        int shift = index & 0x1f;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Integer.SIZE - 1) - shift;
        }
        return (value & (1 << shift)) != 0;
    }

    @Override
    public boolean getBitExact() {
        guard.run();
        throw new DataSizeMismatchException(bitLength() + " != 1");
    }

    @Override
    public int byteLength() {
        guard.run();
        return intSequence.intLength() << 2;
    }

    @Override
    public byte getByte(int index) {
        guard.run();
        final int value = intSequence.getInt(index >>> 2);
        int shift = (index & 0x3) << 3;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Integer.SIZE - Byte.SIZE) - shift;
        }
        return (byte) (value >> shift);
    }

    @Override
    public byte getByteAll() {
        guard.run();
        if (intSequence.intLength() != 0) {
            throw new RuntimeException();
        }
        return 0;
    }

    @Override
    public byte getByteExact() {
        guard.run();
        throw new DataSizeMismatchException(bitLength() + " != " + Byte.SIZE);
    }

    @Override
    public short getUnsignedByte(int index) {
        guard.run();
        final int value = intSequence.getInt(index >>> 2);
        int shift = (index & 0x3) << 3;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Integer.SIZE - Byte.SIZE) - shift;
        }
        return (short) (0xff & (value >> shift));
    }

    @Override
    public short getUnsignedByteAll() {
        guard.run();
        if (intSequence.intLength() != 0) {
            throw new DataSizeMismatchException();
        }
        return 0;
    }

    @Override
    public short getUnsignedByteExact() {
        guard.run();
        throw new DataSizeMismatchException(bitLength() + " != " + Byte.SIZE);
    }

    @Override
    public int shortLength() {
        guard.run();
        return intSequence.intLength() << 1;
    }

    @Override
    public short getShort(int index) {
        guard.run();
        final int value = intSequence.getInt(index >>> 1);
        int shift = (index & 0x1) << 4;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Integer.SIZE - Short.SIZE) - shift;
        }
        return (short) (value >> shift);
    }

    @Override
    public short shortAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public short getShortAll() {
        guard.run();
        if (intSequence.intLength() != 0) {
            throw new DataSizeMismatchException();
        }
        return 0;
    }

    @Override
    public short getShortExact() {
        guard.run();
        throw new DataSizeMismatchException(bitLength() + " != " + Short.SIZE);
    }

    @Override
    public int getUnsignedShort(int index) {
        guard.run();
        final int value = intSequence.getInt(index >>> 1);
        int shift = (index & 0x1) << 4;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Integer.SIZE - Short.SIZE) - shift;
        }
        return 0xffff & (value >> shift);
    }

    @Override
    public int unsignedShortAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public int getUnsignedShortAll() {
        guard.run();
        if (intSequence.intLength() != 0) {
            throw new DataSizeMismatchException();
        }
        return 0;
    }

    @Override
    public int getUnsignedShortExact() {
        guard.run();
        throw new DataSizeMismatchException(bitLength() + " != " + Short.SIZE);
    }

    @Override
    public int intLength() {
        guard.run();
        return intSequence.intLength();
    }

    @Override
    public int getInt(int index) {
        guard.run();
        return intSequence.getInt(index);
    }

    @Override
    public int intAt(int byteOffset) {
        guard.run();
        final int index = byteOffset >> 2;
        switch (byteOffset & 0x3) {
            default:
            case 0:
                return intSequence.getInt(index);
            case 1:
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (intSequence.getInt(index) << 8) | (intSequence.getInt(index + 1) >>> 24);
                } else {
                    return (intSequence.getInt(index) >>> 8) | (intSequence.getInt(index + 1) << 24);
                }
            case 2:
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (intSequence.getInt(index) << 16) | (intSequence.getInt(index + 1) >>> 16);
                } else {
                    return (intSequence.getInt(index) >>> 16) | (intSequence.getInt(index + 1) << 16);
                }
            case 3:
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (intSequence.getInt(index) << 24) | (intSequence.getInt(index + 1) >>> 8);
                } else {
                    return (intSequence.getInt(index) >>> 24) | (intSequence.getInt(index + 1) << 8);
                }
        }
    }

    @Override
    public int getIntAll() {
        guard.run();
        final int intLength = intSequence.intLength();
        if (intLength == 1) {
            return intSequence.getInt(0);
        } else if (intLength == 0) {
            return 0;
        } else {
            throw new DataSizeMismatchException(bitLength() + " > " + Integer.SIZE);
        }
    }

    @Override
    public int getIntExact() {
        guard.run();
        if (intSequence.intLength() == 1) {
            return intSequence.getInt(0);
        } else {
            throw new DataSizeMismatchException(bitLength() + " != " + Integer.SIZE);
        }
    }

    @Override
    public long getUnsignedInt(int index) {
        guard.run();
        return 0xffffffffL & getInt(index);
    }

    @Override
    public long unsignedIntAt(int byteOffset) {
        guard.run();
        return 0xffffffffL & intAt(byteOffset);
    }

    @Override
    public long getUnsignedIntAll() {
        guard.run();
        final int size = intSequence.intLength();
        if (size == 1) {
            return 0xffffffffL & intSequence.getInt(0);
        } else if (size == 0) {
            return 0L;
        } else {
            throw new DataSizeMismatchException(bitLength() + " > " + Integer.SIZE);
        }
    }

    @Override
    public long getUnsignedIntExact() {
        guard.run();
        if (intSequence.intLength() == 1) {
            return 0xffffffffL & intSequence.getInt(0);
        } else {
            throw new DataSizeMismatchException(bitLength() + " != " + Integer.SIZE);
        }
    }

    @Override
    public int longLength() {
        guard.run();
        return (intSequence.intLength() + 0x1) >>> 1;
    }

    @Override
    public long longAt(int index) {
        guard.run();
        index <<= 1;
        final int intLength = intSequence.intLength();
        if (index < intLength) {
            long value0 = intSequence.getInt(index);
            long value1 = 0L;
            if (index + 1 < intLength) {
                value1 = intSequence.getInt(index + 1);
            }
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                return (value0 << Integer.SIZE) | value1;
            } else {
                return (value1 << Integer.SIZE) | value0;
            }
        } else {
            throw new IndexOutOfBoundsException(index + " >= " + intLength);
        }
    }

    @Override
    public long getLong(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public long getLongAll() {
        guard.run();
        final int intLength = intSequence.intLength();
        long value0;
        long value1 = 0L;
        switch (intLength) {
            case 2:
                value1 = intSequence.getInt(1);
                // no break here
            case 1:
                value0 = intSequence.getInt(0);
                break;
            case 0:
                return 0L;
            default:
                throw new DataSizeMismatchException(bitLength() + " > " + Long.SIZE);
        }
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return (value0 << Integer.SIZE) | value1;
        } else {
            return (value1 << Integer.SIZE) | value0;
        }
    }

    @Override
    public long getLongExact() {
        guard.run();
        if (intSequence.intLength() != 2) {
            throw new DataSizeMismatchException(bitLength() + " != " + Long.SIZE);
        }
        final long value0 = intSequence.getInt(0);
        final long value1 = intSequence.getInt(1);
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return (value0 << Integer.SIZE) | value1;
        } else {
            return (value1 << Integer.SIZE) | value0;
        }
    }

    @NotNull
    @Override
    public String toBinaryString() {
        guard.run();
        final int intLength = intSequence.intLength();
        final char[] charArray = new char[IntegralMath.INSTANCE.multiply(intLength, Integer.SIZE)];
        int shiftStart, shiftDelta;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            shiftStart = Integer.SIZE - 1;
            shiftDelta = -1;
        } else {
            shiftStart = 0;
            shiftDelta = 1;
        }
        int charIndex = 0;
        for (int index = 0; index < intLength; index++) {
            int value = intSequence.getInt(index);
            int shift = shiftStart;
            for (int step = 0; step < Integer.SIZE; step++) {
                charArray[charIndex++] = (value & (1 << shift)) != 0 ? '1' : '0';
                shift += shiftDelta;
            }
        }
        return new String(charArray);
    }

    private static final int[] SHIFT_BIG = {28, 24, 20, 16, 12, 8, 4, 0};

    private static final int[] SHIFT_LITTLE = {4, 0, 12, 8, 20, 16, 28, 24};

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        guard.run();
        final int intLength = intSequence.intLength();
        final char[] charArray = new char[IntegralMath.INSTANCE.multiply(intLength, 8)];
        int[] shiftArray;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            shiftArray = SHIFT_BIG;
        } else {
            shiftArray = SHIFT_LITTLE;
        }
        int charIndex = 0;
        for (int intIndex = 0; intIndex < intLength; intIndex++) {
            int value = intSequence.getInt(intIndex);
            for (int shift : shiftArray) {
                int digit = 0xf & (value >> shift);
                if (digit < 0xa) {
                    digit += '0';
                } else if (upperCase) {
                    digit += Base16Case.UPPER;
                } else {
                    digit += Base16Case.LOWER;
                }
                charArray[charIndex++] = (char) digit;
            }
        }
        return new String(charArray);
    }
}
