package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@CodeHistory(date = "2025/5/5")
public abstract class ByteBlockBitHashCore extends ByteBlockByteHashCore {

    protected static int ch(int x, int y, int z) {
        return (x & y) ^ (~x & z);
    }

    protected static long ch(long x, long y, long z) {
        return (x & y) ^ (~x & z);
    }

    protected static int maj(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    protected static long maj(long x, long y, long z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    protected static int parity(int x, int y, int z) {
        return x ^ y ^ z;
    }

    protected static int bigSigma0(int x) {
        return ((x >>> 2) | (x << 30))
                ^ ((x >>> 13) | (x << 19))
                ^ ((x >>> 22) | (x << 10));
    }

    protected static long bigSigma0(long x) {
        return ((x >>> 28) | (x << 36))
                ^ ((x >>> 34) | (x << 30))
                ^ ((x >>> 39) | (x << 25));
    }

    protected static int bigSigma1(int x) {
        return ((x >>> 6) | (x << 26))
                ^ ((x >>> 11) | (x << 21))
                ^ ((x >>> 25) | (x << 7));
    }

    protected static long bigSigma1(long x) {
        return ((x >>> 14) | (x << 50))
                ^ ((x >>> 18) | (x << 46))
                ^ ((x >>> 41) | (x << 23));
    }

    protected static int sigma0(int x) {
        return ((x >>> 7) | (x << 25))
                ^ ((x >>> 18) | (x << 14))
                ^ (x >>> 3);
    }

    protected static long sigma0(long x) {
        return ((x >>> 1) | (x << 63))
                ^ ((x >>> 8) | (x << 56))
                ^ (x >>> 7);
    }

    protected static int sigma1(int x) {
        return ((x >>> 17) | (x << 15))
                ^ ((x >>> 19) | (x << 13))
                ^ (x >>> 10);
    }

    protected static long sigma1(long x) {
        return ((x >>> 19) | (x << 45))
                ^ ((x >>> 61) | (x << 3))
                ^ (x >>> 6);
    }

    public ByteBlockBitHashCore() {
        super();
    }

    @NotNull
    public abstract ByteOrder bitOrder();

    @Override
    public abstract int blockBytes();

    @Override
    public abstract int resultBytes();

    @NotNull
    @Override
    public abstract ByteBlockBitHashCore clone();

    public abstract void finish(@NotNull ByteBuffer buffer, int paddingBits);

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        finish(buffer, 0);
    }

    protected void pad10(@NotNull ByteBuffer buffer, int paddingBits, int lengthFieldBytes) {
        assert 0 <= paddingBits;
        assert paddingBits < Byte.SIZE;
        final int blockSize = blockBytes() << 3;
        final int leftSize = (buffer.position() << 3) - paddingBits;
        assert leftSize < blockSize;
        int steps = (leftSize + (lengthFieldBytes << 3) + blockSize + 1) / blockSize;
        assert steps == 1 || steps == 2;
        if (paddingBits > 0) {
            int position = buffer.position() - 1;
            int value = buffer.get(position);
            int mask = 1 << (paddingBits - 1);
            value |= mask;
            value &= mask - 1;
            buffer.put(position, (byte) value);
        } else {
            buffer.put((byte) 0x80);
        }
        for (int count = ((steps * blockSize + (Byte.SIZE - 1)) >> 3) - buffer.position(); count > 0; count--) {
            buffer.put((byte) 0x00);
        }
        {
            long blockCount = blockCount();
            assert blockCount >= 0;
            long totalBits = blockCount * blockSize + leftSize;
            int position = buffer.position();
            if (buffer.order() == ByteOrder.BIG_ENDIAN) {
                for (int i = 0; i < lengthFieldBytes; i++) {
                    buffer.put(--position, (byte) totalBits);
                    totalBits >>= Byte.SIZE;
                }
            } else {
                position -= lengthFieldBytes;
                for (int i = 0; i < lengthFieldBytes; i++) {
                    buffer.put(position++, (byte) totalBits);
                    totalBits >>= Byte.SIZE;
                }
            }
        }
        buffer.flip();
        for (; steps > 0; steps--) {
            step(buffer);
        }
    }
}
