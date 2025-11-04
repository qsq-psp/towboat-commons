package mujica.io.hash;

import mujica.io.view.DataView;
import mujica.ds.of_long.LongSequence;
import mujica.io.view.LongSequenceDataView;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created on 2025/5/17.
 */
public class MurmurHash128 extends ByteBlockByteHashCore implements LongSequence {

    private static final long serialVersionUID = 0xe5c9c053858bf205L;

    private long mix(long hash) {
        hash ^= hash >>> 33;
        hash *= 0xff51afd7ed558ccdL;
        hash ^= hash >>> 33;
        hash *= 0xc4ceb9fe1a85ec53L;
        hash ^= hash >>> 33;
        return hash;
    }

    private final int seed;

    private long h1, h2;

    private long length;

    public MurmurHash128(int seed) {
        super();
        this.seed = seed;
    }

    public MurmurHash128() {
        this(0);
    }

    @Override
    public int blockBytes() {
        return 16;
    }

    @Override
    public int resultBytes() {
        return 16;
    }

    @NotNull
    @Override
    public MurmurHash128 clone() {
        final MurmurHash128 that = new MurmurHash128(seed);
        that.h1 = this.h1;
        that.h2 = this.h2;
        that.length = this.length;
        return that;
    }

    @Override
    public void clear() {
        h1 = 0;
        h2 = 0;
        length = 0;
    }

    @Override
    public void start() {
        h1 = 0xffffffffL & seed;
        h2 = h1;
        length = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long k1 = buffer.getLong();
        long k2 = buffer.getLong();
        k1 *= 0x87c37b91114253d5L;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= 0x4cf5ad432745937fL;
        h1 ^= k1;
        h1 = Long.rotateLeft(h1, 27);
        h1 += h2;
        h1 = h1 * 5 + 0x52dce729;
        k2 *= 0x4cf5ad432745937fL;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= 0x87c37b91114253d5L;
        h2 ^= k2;
        h2 = Long.rotateLeft(h2, 31);
        h2 += h1;
        h2 = h2 * 5 + 0x38495ab5;
        length += 16;
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int remaining = buffer.remaining();
        length += remaining;
        if (remaining > 0) {
            long k1 = 0;
            long k2 = 0;
            if (remaining > 8) {
                k1 = buffer.getLong();
                remaining -= 8;
                for (int shift = 0; shift < Long.SIZE; shift += Byte.SIZE) {
                    k2 |= (0xffL & buffer.get()) << shift;
                    if (--remaining == 0) {
                        break;
                    }
                }
                k2 *= 0x4cf5ad432745937fL;
                k2 = Long.rotateLeft(k2, 33);
                k2 *= 0x87c37b91114253d5L;
                h2 ^= k2;
            } else {
                for (int shift = 0; shift < Long.SIZE; shift += Byte.SIZE) {
                    k1 |= (0xffL & buffer.get()) << shift;
                    if (--remaining == 0) {
                        break;
                    }
                }
            }
            k1 *= 0x87c37b91114253d5L;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= 0x4cf5ad432745937fL;
            h1 ^= k1;
        }
        h1 ^= length;
        h2 ^= length;
        h1 += h2;
        h2 += h1;
        h1 = mix(h1);
        h2 = mix(h2);
        h1 += h2;
        h2 += h1;
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongSequenceDataView(this, ByteOrder.BIG_ENDIAN, guard);
    }

    @Override
    public int longLength() {
        return 2;
    }

    @Override
    public long getLong(int index) {
        if (index == 0) {
            return h1;
        } else if (index == 1) {
            return h2;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
