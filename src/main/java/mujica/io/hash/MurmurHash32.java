package mujica.io.hash;

import mujica.ds.of_int.list.IntSequence;
import mujica.io.view.*;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created on 2025/5/17.
 */
@ReferencePage(title = "MurmurHash3", href = "https://github.com/aappleby/smhasher/blob/master/src/MurmurHash3.cpp")
public class MurmurHash32 extends ByteBlockByteHashCore implements IntSequence {

    private static final long serialVersionUID = 0x6bb9b29b4ca2e0dcL;

    private final int seed;

    private int hash;

    private int length;

    public MurmurHash32(int seed) {
        super();
        this.seed = seed;
    }

    public MurmurHash32() {
        this(0);
    }

    @Override
    public int blockBytes() {
        return Integer.BYTES; // 4
    }

    @Override
    public int resultBytes() {
        return Integer.BYTES; // 4
    }

    @NotNull
    @Override
    public MurmurHash32 clone() {
        final MurmurHash32 that = new MurmurHash32(seed);
        that.hash = this.hash;
        that.length = this.length;
        return that;
    }

    @Override
    public void clear() {
        hash = 0;
        length = 0;
    }

    @Override
    public void start() {
        hash = seed;
        length = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int value = buffer.getInt();
        value *= 0xcc9e2d51;
        value = Integer.rotateLeft(value, 15);
        value *= 0x1b873593;
        hash ^= value;
        hash = Integer.rotateLeft(hash, 13) * 5 + 0xe6546b64;
        length += Integer.BYTES; // 4
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            int value = 0;
            for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
                value |= (0xff & buffer.get()) << shift;
                length++;
                if (!buffer.hasRemaining()) {
                    break;
                }
            }
            value *= 0xcc9e2d51;
            value = Integer.rotateLeft(value, 15);
            value *= 0x1b873593;
            hash ^= value;
        }
        hash ^= length;
        hash ^= hash >>> 16;
        hash *= 0x85ebca6b;
        hash ^= hash >>> 13;
        hash *= 0xc2b2ae35;
        hash ^= hash >>> 16;
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new IntSequenceDataView(this, ByteOrder.BIG_ENDIAN, guard);
    }

    @Override
    public int intLength() {
        return 1;
    }

    @Override
    public int getInt(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return hash;
    }
}
