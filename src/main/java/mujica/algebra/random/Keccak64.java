package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

@CodeHistory(date = "2025/1/27", project = "Ultramarine")
@CodeHistory(date = "2026/2/24")
public class Keccak64 implements RandomSource, LongSupplier {

    private static void theta(@NotNull long[] srcState, @NotNull long[] dstState, @NotNull long[] srcPlane, @NotNull long[] dstPlane) {
        for (int x = 0; x < 5; x++) {
            long xor = 0;
            for (int y = 0; y < 5; y++) {
                xor ^= srcState[y * 5 + x];
            }
            srcPlane[x] = xor;
        }
        for (int x = 0; x < 5; x++) {
            long lane = srcPlane[(x + 1) % 5];
            lane = (lane >>> 63) | (lane << 1);
            dstPlane[x] = srcPlane[(x + 4) % 5] ^ lane;
        }
        for (int yx = 0; yx < 25; yx++) {
            dstState[yx] = srcState[yx] ^ dstPlane[yx % 5];
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static void rho(@NotNull long[] srcState, @NotNull long[] dstState) {
        dstState[0] = srcState[0];
        int x = 1;
        int y = 0;
        for (int t = 0; t < 24; t++) {
            int yx = y * 5 + x;
            int shift = (((t + 1) * (t + 2)) >> 1) & 0x3f;
            long lane = srcState[yx];
            lane = (lane >>> (64 - shift)) | (lane << shift);
            dstState[yx] = lane;
            int g = x;
            x = y;
            y = (2 * g + 3 * y) % 5;
        }
    }

    private static void pi(@NotNull long[] srcState, @NotNull long[] dstState) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                dstState[y * 5 + x] = srcState[x * 5 + (y * 3 + x) % 5];
            }
        }
    }

    private static void chi(@NotNull long[] srcState, @NotNull long[] dstState) {
        for (int y = 0; y < 5; y++) {
            int by = 5 * y;
            for (int x = 0; x < 5; x++) {
                dstState[by + x] = srcState[by + x] ^ (~srcState[by + (x + 1) % 5] & srcState[by + (x + 2) % 5]);
            }
        }
    }

    private static final byte[] RC = new byte[] {
            1, 26, 94, 112, 31, 33, 121, 85, 14, 12, 53, 38, 63, 79, 93, 83, 82, 72, 22, 102, 121, 88, 33, 116
    };

    private static void iota(@NotNull long[] state, int roundIndex) {
        final int roundConstant = RC[roundIndex];
        for (int shift = 0; shift <= 6; shift++) {
            int bit = 1 << shift;
            if ((roundConstant & bit) != 0) {
                state[0] ^= 1L << (bit - 1);
            }
        }
    }

    @Override
    public long getAsLong() {
        return 0;
    }

    @Override
    public long applyAsLong(int bitCount) {
        return getAsLong() >>> (Long.SIZE - bitCount);
    }

    @NotNull
    @Override
    public IntSupplier intBind(int bitCount) {
        if (bitCount == Integer.SIZE) {
            return () -> (int) getAsLong();
        }
        if (!(0 < bitCount && bitCount < Integer.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }

    @NotNull
    @Override
    public LongSupplier longBind(int bitCount) {
        if (bitCount == Long.SIZE) {
            return this;
        }
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        final int shift = Long.SIZE - bitCount;
        return () -> (int) (getAsLong() >>> shift);
    }
}
