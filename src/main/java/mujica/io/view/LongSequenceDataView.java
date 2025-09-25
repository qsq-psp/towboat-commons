package mujica.io.view;

import mujica.ds.of_long.LongSequence;
import mujica.io.function.Base16Case;
import mujica.math.algebra.discrete.ClampedMath;
import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * Created on 2025/5/10.
 */
@CodeHistory(date = "2025/5/10")
public class LongSequenceDataView implements DataView {

    @NotNull
    private final LongSequence longSequence;

    @NotNull
    private final ByteOrder byteOrder;

    public LongSequenceDataView(@NotNull LongSequence longSequence, @NotNull ByteOrder byteOrder) {
        super();
        this.longSequence = longSequence;
        this.byteOrder = byteOrder;
    }

    @Override
    public int bitLength() {
        return ClampedMath.INSTANCE.multiply(longSequence.longLength(), Long.SIZE);
    }

    @Override
    public boolean getBit(int index) {
        final long value = longSequence.getLong(index >>> 6);
        int shift = index & 0x3f;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Long.SIZE - 1) - shift;
        }
        return (value & (1L << shift)) != 0;
    }

    @Override
    public boolean getBitExact() {
        throw new DataSizeMismatchException();
    }

    @Override
    public int byteLength() {
        return 0;
    }

    @Override
    public byte getByte(int index) {
        return 0;
    }

    @Override
    public byte getByteAll() {
        return 0;
    }

    @Override
    public byte getByteExact() {
        throw new DataSizeMismatchException();
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
        throw new DataSizeMismatchException();
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
        throw new DataSizeMismatchException();
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
        throw new DataSizeMismatchException();
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
        throw new DataSizeMismatchException();
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
        throw new DataSizeMismatchException();
    }

    @Override
    public int longLength() {
        return 0;
    }

    @Override
    public long getLong(int index) {
        return 0;
    }

    @Override
    public long longAt(int byteOffset) {
        return 0;
    }

    @Override
    public long getLongAll() {
        return 0;
    }

    @Override
    public long getLongExact() {
        return 0;
    }

    @NotNull
    @Override
    public String toBinaryString() {
        final int longLength = longSequence.longLength();
        final char[] charArray = new char[IntegralMath.INSTANCE.multiply(longLength, Long.SIZE)];
        int shiftStart, shiftDelta;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            shiftStart = Long.SIZE - 1;
            shiftDelta = -1;
        } else {
            shiftStart = 0;
            shiftDelta = 1;
        }
        int charIndex = 0;
        for (int index = 0; index < longLength; index++) {
            long value = longSequence.getLong(index);
            int shift = shiftStart;
            for (int step = 0; step < Integer.SIZE; step++) {
                charArray[charIndex++] = (value & (1L << shift)) != 0 ? '1' : '0';
                shift += shiftDelta;
            }
        }
        return new String(charArray);
    }

    private static final int[] SHIFT_BIG = {60, 56, 52, 48, 44, 40, 36, 32, 28, 24, 20, 16, 12, 8, 4, 0};

    private static final int[] SHIFT_LITTLE = {4, 0, 12, 8, 20, 16, 28, 24, 36, 32, 44, 40, 52, 48, 60, 56};

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        final int intLength = longSequence.longLength();
        final char[] charArray = new char[IntegralMath.INSTANCE.multiply(intLength, 16)];
        int[] shiftArray;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            shiftArray = SHIFT_BIG;
        } else {
            shiftArray = SHIFT_LITTLE;
        }
        int charIndex = 0;
        for (int index = 0; index < intLength; index++) {
            long value = longSequence.getLong(index);
            for (int shift : shiftArray) {
                int digit = 0xf & (int) (value >> shift);
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
