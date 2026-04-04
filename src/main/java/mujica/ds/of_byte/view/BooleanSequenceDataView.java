package mujica.ds.of_byte.view;

import mujica.ds.of_boolean.list.BooleanSequence;
import mujica.io.codec.Base16Case;
import mujica.algebra.discrete.ClampedMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

@CodeHistory(date = "2025/4/15")
public class BooleanSequenceDataView implements DataView {

    @NotNull
    private final BooleanSequence booleanSequence;

    @NotNull
    private final ByteOrder byteOrder;

    @NotNull
    private final Runnable guard;

    public BooleanSequenceDataView(@NotNull BooleanSequence booleanSequence, @NotNull ByteOrder byteOrder, @NotNull Runnable guard) {
        super();
        this.booleanSequence = booleanSequence;
        this.byteOrder = byteOrder;
        this.guard = guard;
    }

    public BooleanSequenceDataView(@NotNull BooleanSequence booleanSequence, @NotNull ByteOrder byteOrder) {
        this(booleanSequence, byteOrder, NOP_GUARD);
    }

    @Override
    public int booleanLength() {
        guard.run();
        return booleanSequence.booleanLength();
    }

    @Override
    public boolean getBoolean(int index) {
        guard.run();
        return booleanSequence.getBoolean(index);
    }

    @Override
    public boolean getBitExact() {
        guard.run();
        if (booleanSequence.booleanLength() == 1) {
            return booleanSequence.getBoolean(0);
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int byteLength() {
        guard.run();
        return (booleanSequence.booleanLength() + (Byte.SIZE - 1)) >>> 3;
    }

    @Override
    public byte getByte(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Byte.SIZE); // integer overflow exception reserved
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (byte) value;
    }

    @Override
    public byte getByteExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Byte.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public short getUnsignedByteExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Byte.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public int shortLength() {
        guard.run();
        return (booleanSequence.booleanLength() + (Short.SIZE - 1)) >>> 4;
    }

    @Override
    public short getShort(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Short.SIZE); // integer overflow exception reserved
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return (short) value;
    }

    @Override
    public short getShortExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Short.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return 0xffff & value;
    }

    @Override
    public int getUnsignedShortExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Short.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Short.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return 0xffff & value;
    }

    @Override
    public int intLength() {
        guard.run();
        return (booleanSequence.booleanLength() + (Integer.SIZE - 1)) >>> 5;
    }

    @Override
    public int getInt(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Integer.SIZE); // integer overflow exception reserved
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            int value = 0;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1 << shift;
                }
            }
        }
        return value;
    }

    @Override
    public int getIntExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        int value = 0;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Integer.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
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
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (shift == bitLength) {
                    break;
                }
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public long getUnsignedIntExact() {
        guard.run();
        if (booleanSequence.booleanLength() != Integer.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public int longLength() {
        guard.run();
        return (booleanSequence.booleanLength() + (Long.SIZE - 1)) >>> 6;
    }

    @Override
    public long getLong(int index) {
        guard.run();
        index = ClampedMath.INSTANCE.multiply(index, Long.SIZE); // integer overflow exception reserved
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (index < bitLength) {
            long value = 0L;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    value <<= 1;
                    if (booleanSequence.getBoolean(index++)) {
                        value |= 1L;
                    }
                    if (index == bitLength) {
                        break;
                    }
                }
            } else {
                for (int shift = 0; shift < Long.SIZE; shift++) {
                    if (booleanSequence.getBoolean(index++)) {
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
        final int bitLength = booleanSequence.booleanLength();
        if (bitLength > Long.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < bitLength; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < bitLength; shift++) {
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L << shift;
                }
            }
        }
        return value;
    }

    @Override
    public long getLongExact() {
        guard.run();
        final int bitLength = booleanSequence.booleanLength();
        if (bitLength != Long.SIZE) {
            throw new DataSizeMismatchException();
        }
        long value = 0L;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int shift = 0; shift < bitLength; shift++) {
                value <<= 1;
                if (booleanSequence.getBoolean(shift)) {
                    value |= 1L;
                }
            }
        } else {
            for (int shift = 0; shift < bitLength; shift++) {
                if (booleanSequence.getBoolean(shift)) {
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
        final int bitLength = booleanSequence.booleanLength();
        final char[] charArray = new char[bitLength];
        for (int index = 0; index < bitLength; index++) {
            charArray[index] = booleanSequence.getBoolean(index) ? '1' : '0';
        }
        return new String(charArray);
    }

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        guard.run();
        final int bitLength = booleanSequence.booleanLength();
        int bitIndex;
        final char[] charArray = new char[(bitLength + 0x3) >>> 2];
        int charIndex = 0;
        for (bitIndex = 0; bitIndex < bitLength; bitIndex += 0x4) {
            int digit = 0;
            if (booleanSequence.getBoolean(bitIndex)) {
                digit |= 0x1;
            }
            if (booleanSequence.getBoolean(bitIndex + 1)) {
                digit |= 0x2;
            }
            if (booleanSequence.getBoolean(bitIndex + 2)) {
                digit |= 0x4;
            }
            if (booleanSequence.getBoolean(bitIndex + 3)) {
                digit |= 0x8;
            }
            if (digit < 0xa) {
                digit += '0';
            } else if (upperCase) {
                digit += Base16Case.UPPER_CONSTANT;
            } else {
                digit += Base16Case.LOWER_CONSTANT;
            }
            charArray[charIndex++] = (char) digit;
        }
        if (charIndex < charArray.length) {
            int digit = 0;
            if (bitIndex < bitLength && booleanSequence.getBoolean(bitIndex)) {
                digit |= 0x1;
            }
            if (bitIndex < bitLength && booleanSequence.getBoolean(bitIndex + 1)) {
                digit |= 0x2;
            }
            if (bitIndex < bitLength && booleanSequence.getBoolean(bitIndex + 2)) {
                digit |= 0x4;
            }
            if (bitIndex < bitLength && booleanSequence.getBoolean(bitIndex + 3)) {
                digit |= 0x8;
            }
            if (digit < 0xa) {
                digit += '0';
            } else if (upperCase) {
                digit += Base16Case.UPPER_CONSTANT;
            } else {
                digit += Base16Case.LOWER_CONSTANT;
            }
            charArray[charIndex++] = (char) digit;
        }
        assert charIndex == charArray.length;
        return new String(charArray);
    }
}
