package mujica.io.hash;

import mujica.io.view.DataView;
import mujica.ds.of_long.LongSequence;
import mujica.io.view.LongSequenceDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2025/1/17", project = "Ultramarine", name = "SHA3CoreYXZ")
@CodeHistory(date = "2025/5/17")
@ReferencePage(title = "FIPS PUB 202", href = "https://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf")
public class SHA3 extends ByteBlockBitHashCore implements LongSequence {

    private static final long serialVersionUID = 0x3c72f8005f09e481L;

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

    @NotNull
    private final long[] state0 = new long[25];

    @NotNull
    private final transient long[] state1 = new long[25];

    @NotNull
    private final transient long[] plane0 = new long[5];

    @NotNull
    private final transient long[] plane1 = new long[5];

    private void keccak() {
        for (int roundIndex = 0; roundIndex < 24; roundIndex++) {
            theta(state0, state1, plane0, plane1);
            rho(state1, state0);
            pi(state0, state1);
            chi(state1, state0);
            iota(state0, roundIndex);
        }
    }

    private final int digestBits;

    private final int rateBits;

    public SHA3(int digestBits) {
        super();
        this.digestBits = digestBits;
        this.rateBits = 1600 - 2 * digestBits;
        if (((digestBits | rateBits) & 0x1f) != 0) {
            throw new IllegalArgumentException();
        }
        if (!(0 < digestBits && digestBits <= rateBits)) {
            throw new IllegalArgumentException();
        }
    }

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    @Override
    public int blockBytes() {
        return rateBits >>> 3;
    }

    @Override
    public int resultBytes() {
        return digestBits >>> 3;
    }

    @NotNull
    @Override
    public SHA3 clone() {
        final SHA3 that = new SHA3(digestBits);
        cloneArray(this.state0, that.state0);
        return that;
    }

    @Override
    public void clear() {
        Arrays.fill(state0, 0L);
        Arrays.fill(state1, 0L);
        Arrays.fill(plane0, 0L);
        Arrays.fill(plane1, 0L);
    }

    @Override
    public void start() {
        Arrays.fill(state0, 0L);
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        final int longCount = this.rateBits >>> 6;
        for (int index = 0; index < longCount; index++) {
            state0[index] ^= buffer.getLong();
        }
        keccak();
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongSequenceDataView(this, ByteOrder.LITTLE_ENDIAN, guard);
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer, int paddingBits) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // todo: pad
    }

    @Override
    public int longLength() {
        return digestBits >>> 6;
    }

    @Override
    public long getLong(int index) {
        return state0[index];
    }
}
