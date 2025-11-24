package mujica.io.view;

import mujica.ds.of_boolean.BitSequence;
import mujica.math.algebra.discrete.ClampedMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

@CodeHistory(date = "2025/4/15")
public class BitSequenceDataView implements DataView {

    @NotNull
    private final BitSequence bitSequence;

    @NotNull
    private final ByteOrder byteOrder;

    @NotNull
    private final Runnable guard;

    public BitSequenceDataView(@NotNull BitSequence bitSequence, @NotNull ByteOrder byteOrder, @NotNull Runnable guard) {
        super();
        this.bitSequence = bitSequence;
        this.byteOrder = byteOrder;
        this.guard = guard;
    }

    public BitSequenceDataView(@NotNull BitSequence bitSequence, @NotNull ByteOrder byteOrder) {
        this(bitSequence, byteOrder, NOP_GUARD);
    }

    @Override
    public int bitLength() {
        guard.run();
        return bitSequence.bitLength();
    }

    @Override
    public boolean getBit(int index) {
        guard.run();
        return bitSequence.getBit(index);
    }

    @Override
    public boolean getBitExact() {
        guard.run();
        if (bitSequence.bitLength() == 1) {
            return bitSequence.getBit(0);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int byteLength() {
        guard.run();
        return (bitSequence.bitLength() + (Byte.SIZE - 1)) >>> 3;
    }

    @Override
    public byte getByte(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return (byte) value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public byte getByteAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (byte) value;
    }

    @Override
    public byte getByteExact() {
        guard.run();
        if (bitSequence.bitLength() != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (byte) value;
    }

    @Override
    public short getUnsignedByte(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return (short) value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public short getUnsignedByteAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public short getUnsignedByteExact() {
        guard.run();
        if (bitSequence.bitLength() != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public int shortLength() {
        guard.run();
        return (bitSequence.bitLength() + (Short.SIZE - 1)) >>> 4;
    }

    @Override
    public short getShort(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Short.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return (short) value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public short shortAt(int byteOffset) {
        guard.run();
        int index = ClampedMath.INSTANCE.multiply(byteOffset, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return (short) value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public short getShortAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public short getShortExact() {
        guard.run();
        if (bitSequence.bitLength() != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public int getUnsignedShort(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Short.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return 0xffff & value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int unsignedShortAt(int byteOffset) {
        guard.run();
        int index = ClampedMath.INSTANCE.multiply(byteOffset, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return 0xffff & value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getUnsignedShortAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return 0xffff & value;
    }

    @Override
    public int getUnsignedShortExact() {
        guard.run();
        if (bitSequence.bitLength() != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return 0xffff & value;
    }

    @Override
    public int intLength() {
        guard.run();
        return (bitSequence.bitLength() + (Integer.SIZE - 1)) >>> 5;
    }

    @Override
    public int getInt(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Integer.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int intAt(int byteOffset) {
        guard.run();
        int index = ClampedMath.INSTANCE.multiply(byteOffset, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1 << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getIntAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return value;
    }

    @Override
    public int getIntExact() {
        guard.run();
        if (bitSequence.bitLength() != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return value;
    }

    @Override
    public long getUnsignedInt(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Integer.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1L << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long unsignedIntAt(int byteOffset) {
        guard.run();
        int index = ClampedMath.INSTANCE.multiply(byteOffset, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1L << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long getUnsignedIntAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (bitSequence.getBit(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public long getUnsignedIntExact() {
        guard.run();
        if (bitSequence.bitLength() != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public int longLength() {
        guard.run();
        return (bitSequence.bitLength() + (Long.SIZE - 1)) >>> 6;
    }

    @Override
    public long getLong(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Long.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1L << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long longAt(int byteOffset) {
        guard.run();
        int index = ClampedMath.INSTANCE.multiply(byteOffset, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = bitSequence.bitLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    value <<= 1;
                    if (bitSequence.getBit(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    if (bitSequence.getBit(index++)) {
                        value |= 1L << shift;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long getLongAll() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength > Long.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < bitLength; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < bitLength; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public long getLongExact() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        if (bitLength != Long.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < bitLength; shift++) {
                value <<= 1;
                if (bitSequence.getBit(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < bitLength; shift++) {
                if (bitSequence.getBit(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @NotNull
    @Override
    public String toBinaryString() {
        guard.run();
        final int bitLength = bitSequence.bitLength();
        final char[] charArray = new char[bitLength];
        for (int index = 0; index < bitLength; index++) {
            charArray[index] = bitSequence.getBit(index) ? '1' : '0';
        }
        return new String(charArray);
    }

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        guard.run();
        return "";
    }
}
