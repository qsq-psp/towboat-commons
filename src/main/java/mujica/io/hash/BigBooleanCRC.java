package mujica.io.hash;

import mujica.ds.of_boolean.BitSequence;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2025/4/8", project = "Ultramarine", name = "BigCRC")
@CodeHistory(date = "2025/4/11")
public class BigBooleanCRC extends EachBitStreamHash implements BitSequence, Serializable {

    private static final long serialVersionUID = 0x6f93d78e9e6d95a5L;

    @NotNull
    public static BigBooleanCRC build32(int polynomial) {
        final int[] indexes = new int[Integer.bitCount(polynomial)];
        {
            int position = 0;
            for (int shift = 0; shift < Integer.SIZE; shift++) {
                if ((polynomial & (1 << shift)) != 0) {
                    indexes[position++] = shift;
                }
            }
            assert position == indexes.length;
        }
        return new BigBooleanCRC(Integer.SIZE, indexes, new boolean[] {true});
    }

    @NotNull
    public static BigBooleanCRC crc32() {
        return build32(0xedb88320);
    }

    @NotNull
    public static BigBooleanCRC crc32C() {
        return build32(0x82f63b78);
    }

    @NotNull
    final boolean[] current;

    @NotNull
    final int[] indexes;

    @Nullable
    final boolean[] initial;

    int offset;

    public BigBooleanCRC(int length, @NotNull int[] indexes, @Nullable boolean[] initial) {
        super();
        if (length < 2) {
            throw new IllegalArgumentException("Too short");
        }
        this.current = new boolean[length];
        for (int index : indexes) {
            if (current[index]) { // index checked inside
                throw new IllegalArgumentException("Duplicate index");
            }
            current[index] = true;
        }
        this.indexes = indexes; // ascending order not checked
        this.initial = initial;
    }

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    @Override
    @NotNull
    public BigBooleanCRC clone() {
        return new BigBooleanCRC(current.length, indexes, initial);
    }

    @Override
    public void clear() {
        Arrays.fill(current, false);
        offset = 0;
    }

    @Override
    public void start() {
        int initialLength;
        if (initial == null) {
            initialLength = 0;
        } else {
            initialLength = initial.length;
        }
        if (initialLength <= 1) { // use array fill
            boolean fillValue = false;
            if (initialLength == 1) {
                fillValue = initial[0];
            }
            Arrays.fill(current, fillValue);
        } else { // use array copy
            int currentLength = current.length;
            for (int position = 0; position < currentLength; position += initialLength) {
                System.arraycopy(initial, 0, current, position, Math.min(initialLength, currentLength - position));
            }
        }
        offset = 0;
    }

    @Override
    public void update(boolean input) {
        final int modulo = current.length;
        if (current[offset]) {
            input = !input;
            current[offset] = false;
        }
        if (++offset == modulo) {
            offset = 0;
        }
        if (input) {
            for (int index : indexes) {
                index = (offset + index) % modulo;
                current[index] = !current[index];
            }
        }
    }

    @NotNull
    @Override
    public DataView finish() {
        return new BitSequenceDataView(this, ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public int bitLength() {
        return current.length;
    }

    @Override
    public boolean getBit(int index) {
        return !current[(offset + index) % current.length];
    }
}
