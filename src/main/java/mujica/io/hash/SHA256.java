package mujica.io.hash;

import mujica.ds.of_int.list.IntSequence;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/11/2", project = "UltraIO")
@CodeHistory(date = "2024/12/20", project = "OSHI", name = "SHA256Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "FIPS PUB 180-4", href = "https://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf")
public class SHA256 extends ByteBlockBitHashCore implements IntSequence {

    private static final long serialVersionUID = 0xa0a7542191ecf503L;

    protected static final int INITIAL_LENGTH = 8;

    private static final int[] INITIAL = new int[INITIAL_LENGTH];

    static {
        int writeIndex = 0;
        int value = 1;
        LOOP:
        while (writeIndex < INITIAL_LENGTH) {
            value++;
            for (int readIndex = 0; readIndex < writeIndex; readIndex++) {
                if (value % INITIAL[readIndex] == 0) {
                    continue LOOP;
                }
            }
            INITIAL[writeIndex++] = value;
        }
        for (int index = 0; index < INITIAL_LENGTH; index++) {
            double r = Math.sqrt(INITIAL[index]);
            INITIAL[index] = (int) (long) Math.floor((1L << Integer.SIZE) * (r - Math.floor(r)));
        }
    }

    @NotNull
    protected final int[] out = new int[INITIAL_LENGTH];

    static final int ADDEND_LENGTH = 64;

    static final int[] ADDEND = new int[ADDEND_LENGTH];

    static {
        int writeIndex = 0;
        int value = 1;
        LOOP:
        while (writeIndex < ADDEND_LENGTH) {
            value++;
            for (int j = 0; j < writeIndex; j++) {
                if (value % ADDEND[j] == 0) {
                    continue LOOP;
                }
            }
            ADDEND[writeIndex++] = value;
        }
        for (int index = 0; index < ADDEND_LENGTH; index++) {
            double r = Math.cbrt(ADDEND[index]);
            ADDEND[index] = (int) (long) Math.floor((1L << Integer.SIZE) * (r - Math.floor(r)));
        }
    }

    @NotNull
    protected final transient int[] in = new int[ADDEND_LENGTH];

    protected int blockCount;

    @NotNull
    @Override
    public ByteOrder bitOrder() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override
    public int blockBytes() {
        return 64;
    }

    @Override
    public int resultBytes() {
        return 32;
    }

    @NotNull
    @Override
    public SHA256 clone() {
        final SHA256 that = new SHA256();
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
        cloneArray(INITIAL, out);
        blockCount = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getInt();
        } // read one block (512 bits) from input
        for (int i = 16; i < 64; i++) {
            in[i] = sigma1(in[i - 2]) + in[i - 7] + sigma0(in[i - 15]) + in[i - 16];
        }
        int a = out[0];
        int b = out[1];
        int c = out[2];
        int d = out[3];
        int e = out[4];
        int f = out[5];
        int g = out[6];
        int h = out[7];
        for (int i = 0; i < ADDEND_LENGTH; i++) {
            int t1 = h + bigSigma1(e) + ch(e, f, g) + ADDEND[i] + in[i];
            int t2 = bigSigma0(a) + maj(a, b, c);
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
    public DataView getDataView(@NotNull Runnable guard) {
        return new IntSequenceDataView(this, ByteOrder.BIG_ENDIAN, guard);
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer, int paddingBits) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        pad10(buffer, paddingBits, 8);
    }

    @Override
    public int blockCount() {
        return blockCount;
    }

    @Override
    public int intLength() {
        return INITIAL_LENGTH;
    }

    @Override
    public int getInt(int index) {
        return out[index];
    }

    @Override
    public String toString() {
        return "SHA256";
    }
}
