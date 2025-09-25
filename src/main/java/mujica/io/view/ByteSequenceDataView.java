package mujica.io.view;

import mujica.io.function.Base16Case;
import mujica.math.algebra.discrete.ClampedMath;
import mujica.math.algebra.discrete.IntegralMath;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * Created on 2025/4/16.
 */
public class ByteSequenceDataView implements DataView {

    @NotNull
    private final ByteSequence byteSequence;

    @NotNull
    private final ByteOrder byteOrder;

    public ByteSequenceDataView(@NotNull ByteSequence byteSequence, @NotNull ByteOrder byteOrder) {
        super();
        this.byteSequence = byteSequence;
        this.byteOrder = byteOrder;
    }

    @Override
    public int bitLength() {
        return ClampedMath.INSTANCE.multiply(byteSequence.byteLength(), Byte.SIZE);
    }

    @Override
    public boolean getBit(int index) {
        return false;
    }

    @Override
    public boolean getBitExact() {
        return false;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        final int byteLength = byteSequence.byteLength();
        final char[] charArray = new char[IntegralMath.INSTANCE.multiply(byteLength, Byte.SIZE)];
        int shiftStart, shiftDelta;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            shiftStart = Byte.SIZE - 1;
            shiftDelta = -1;
        } else {
            shiftStart = 0;
            shiftDelta = 1;
        }
        int charIndex = 0;
        for (int index = 0; index < byteLength; index++) {
            int value = byteSequence.getByte(index);
            int shift = shiftStart;
            for (int step = 0; step < Byte.SIZE; step++) {
                charArray[charIndex++] = (value & (1 << shift)) != 0 ? '1' : '0';
                shift += shiftDelta;
            }
        }
        return new String(charArray);
    }

    @NotNull
    @Override
    public String toHexString(boolean upperCase) {
        final int byteLength = byteSequence.byteLength();
        final char[] charArray = new char[byteLength << 1];
        int charIndex = 0;
        for (int index = 0; index < byteLength; index++) {
            int value = byteSequence.getByte(index);
            for (int shift = 4; shift >= 0; shift -= 4) {
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
