package mujica.io.hash;

import mujica.io.view.DataView;
import mujica.ds.of_long.LongSequence;
import mujica.io.view.LongSequenceDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/11/7", project = "Ultramarine")
@CodeHistory(date = "2024/12/21", project = "OSHI", name = "SHA512Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "FIPS PUB 180-4", href = "https://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf")
public class SHA512 extends ByteBlockBitHashCore implements LongSequence {

    private static final long serialVersionUID = 0xc7731d765418b0d7L;

    protected static final long[] INITIAL = new long[16];

    static {
        int writeIndex = 0;
        int value = 1;
        INITIAL_INDEX:
        while (writeIndex < 16) {
            value++;
            for (int readIndex = 0; readIndex < writeIndex; readIndex++) {
                if (value % ((int) INITIAL[readIndex]) == 0) {
                    continue INITIAL_INDEX;
                }
            }
            INITIAL[writeIndex++] = value;
        }
        for (int index = 0; index < 16; index++) {
            INITIAL[index] = BigInteger.valueOf(INITIAL[index])
                    .shiftLeft(2 * 64).sqrt().longValue();
        }
    }

    @NotNull
    protected final long[] out = new long[8];

    static final int ADDEND_LENGTH = 80;

    static final long[] ADDEND = new long[ADDEND_LENGTH];

    static {
        int writeIndex = 0;
        int value = 1;
        ADDEND_INDEX:
        while (writeIndex < ADDEND_LENGTH) {
            value++;
            for (int readIndex = 0; readIndex < writeIndex; readIndex++) {
                if (value % ((int) ADDEND[readIndex]) == 0) {
                    continue ADDEND_INDEX;
                }
            }
            ADDEND[writeIndex++] = value;
        }
        final BigInteger lowStart = BigInteger.TEN.pow(19);
        final BigInteger highStart = BigInteger.TEN.pow(21);
        for (int index = 0; index < ADDEND_LENGTH; index++) {
            BigInteger target = BigInteger.valueOf(ADDEND[index])
                    .shiftLeft(3 * 64);
            BigInteger low = lowStart;
            BigInteger high = highStart;
            while (high.subtract(low).compareTo(BigInteger.ONE) > 0) {
                BigInteger mid = low.add(high).shiftRight(1);
                BigInteger cube = mid.multiply(mid).multiply(mid); // cube
                if (cube.compareTo(target) > 0) {
                    high = mid;
                } else {
                    low = mid;
                }
            }
            ADDEND[index] = low.longValue();
        }
    }

    @NotNull
    protected final transient long[] in = new long[ADDEND_LENGTH];

    protected int blockCount;

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public int blockBytes() {
        return 128;
    }

    @Override
    public int resultBytes() {
        return 64;
    }

    @NotNull
    @Override
    public SHA512 clone() {
        final SHA512 that = new SHA512();
        cloneArray(this.out, that.out);
        that.blockCount = this.blockCount;
        return that;
    }

    @Override
    public void clear() {
        Arrays.fill(in, 0);
        Arrays.fill(out, 0);
        blockCount = 0;
    }

    @Override
    public void start() {
        System.arraycopy(INITIAL, 0, out, 0, 8);
        blockCount = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getLong();
        }
        for (int i = 16; i < 80; i++) {
            in[i] = sigma1(in[i - 2]) + in[i - 7] + sigma0(in[i - 15]) + in[i - 16];
        }
        long a = out[0];
        long b = out[1];
        long c = out[2];
        long d = out[3];
        long e = out[4];
        long f = out[5];
        long g = out[6];
        long h = out[7];
        for (int i = 0; i < ADDEND_LENGTH; i++) {
            long t1 = h + bigSigma1(e) + ch(e, f, g) + ADDEND[i] + in[i];
            long t2 = bigSigma0(a) + maj(a, b, c);
            h = g;
            g = f;
            f = e;
            e = d + t1;
            d = c;
            c = b;
            b = a;
            a = t1 + t2;
        }
        out[0] += a;
        out[1] += b;
        out[2] += c;
        out[3] += d;
        out[4] += e;
        out[5] += f;
        out[6] += g;
        out[7] += h;
        blockCount++;
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new LongSequenceDataView(this, ByteOrder.BIG_ENDIAN);
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer, int paddingBits) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        pad10(buffer, paddingBits, 16);
    }

    @Override
    public int blockCount() {
        return blockCount;
    }

    @Override
    public int longLength() {
        return 8;
    }

    @Override
    public long getLong(int index) {
        return out[index];
    }

    @Override
    public String toString() {
        return "SHA512";
    }
}
