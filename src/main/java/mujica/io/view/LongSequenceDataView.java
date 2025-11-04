package mujica.io.view;

import mujica.ds.of_long.LongSequence;
import mujica.io.codec.Base16Case;
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

    @NotNull
    private final Runnable guard;

    public LongSequenceDataView(@NotNull LongSequence longSequence, @NotNull ByteOrder byteOrder, @NotNull Runnable guard) {
        super();
        this.longSequence = longSequence;
        this.byteOrder = byteOrder;
        this.guard = guard;
    }

    public LongSequenceDataView(@NotNull LongSequence longSequence, @NotNull ByteOrder byteOrder) {
        this(longSequence, byteOrder, NOP_GUARD);
    }

    @Override
    public int bitLength() {
        guard.run();
        return ClampedMath.INSTANCE.multiply(longSequence.longLength(), Long.SIZE);
    }

    @Override
    public boolean getBit(int index) {
        guard.run();
        final long value = longSequence.getLong(index >>> 6);
        int shift = index & 0x3f;
        if (byteOrder != ByteOrder.LITTLE_ENDIAN) {
            shift = (Long.SIZE - 1) - shift;
        }
        return (value & (1L << shift)) != 0;
    }

    @Override
    public boolean getBitExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public int byteLength() {
        guard.run();
        return 0;
    }

    @Override
    public byte getByte(int index) {
        guard.run();
        return 0;
    }

    @Override
    public byte getByteAll() {
        guard.run();
        return 0;
    }

    @Override
    public byte getByteExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public short getUnsignedByte(int index) {
        guard.run();
        return 0;
    }

    @Override
    public short getUnsignedByteAll() {
        guard.run();
        return 0;
    }

    @Override
    public short getUnsignedByteExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public int shortLength() {
        guard.run();
        return 0;
    }

    @Override
    public short getShort(int index) {
        guard.run();
        return 0;
    }

    @Override
    public short shortAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public short getShortAll() {
        guard.run();
        return 0;
    }

    @Override
    public short getShortExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public int getUnsignedShort(int index) {
        guard.run();
        return 0;
    }

    @Override
    public int unsignedShortAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public int getUnsignedShortAll() {
        guard.run();
        return 0;
    }

    @Override
    public int getUnsignedShortExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public int intLength() {
        guard.run();
        return 0;
    }

    @Override
    public int getInt(int index) {
        guard.run();
        return 0;
    }

    @Override
    public int intAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public int getIntAll() {
        guard.run();
        return 0;
    }

    @Override
    public int getIntExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public long getUnsignedInt(int index) {
        guard.run();
        return 0;
    }

    @Override
    public long unsignedIntAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public long getUnsignedIntAll() {
        guard.run();
        return 0;
    }

    @Override
    public long getUnsignedIntExact() {
        guard.run();
        throw new DataSizeMismatchException();
    }

    @Override
    public int longLength() {
        guard.run();
        return 0;
    }

    @Override
    public long getLong(int index) {
        guard.run();
        return 0;
    }

    @Override
    public long longAt(int byteOffset) {
        guard.run();
        return 0;
    }

    @Override
    public long getLongAll() {
        guard.run();
        return 0;
    }

    @Override
    public long getLongExact() {
        guard.run();
        return 0;
    }

    @NotNull
    @Override
    public String toBinaryString() {
        guard.run();
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
        guard.run();
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
