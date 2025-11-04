package mujica.io.view;

import mujica.io.codec.Base16Case;
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

    @NotNull
    private final Runnable guard;

    public ByteSequenceDataView(@NotNull ByteSequence byteSequence, @NotNull ByteOrder byteOrder, @NotNull Runnable guard) {
        super();
        this.byteSequence = byteSequence;
        this.byteOrder = byteOrder;
        this.guard = guard;
    }

    public ByteSequenceDataView(@NotNull ByteSequence byteSequence, @NotNull ByteOrder byteOrder) {
        this(byteSequence, byteOrder, NOP_GUARD);
    }

    @Override
    public int bitLength() {
        return ClampedMath.INSTANCE.multiply(byteSequence.byteLength(), Byte.SIZE);
    }

    @Override
    public boolean getBit(int index) {
        guard.run();
        return false;
    }

    @Override
    public boolean getBitExact() {
        guard.run();
        return false;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        return 0;
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
        guard.run();
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
