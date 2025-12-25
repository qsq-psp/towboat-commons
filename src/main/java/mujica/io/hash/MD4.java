package mujica.io.hash;

import mujica.ds.of_int.list.IntSequence;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/11/22", project = "Ultramarine")
@CodeHistory(date = "2025/1/17", project = "OSHI", name = "MD4Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "The MD4 Message-Digest Algorithm", href = "https://datatracker.ietf.org/doc/html/rfc1320")
public class MD4 extends ByteBlockBitHashCore implements IntSequence {

    private static final long serialVersionUID = 0xb1dfffa2cb0e5738L;

    private static final int[] REMAP = new int[] {0, 3, 2, 1};

    private static final int[] INPUT_G = {
            0, 4, 8, 12,
            1, 5, 9, 13,
            2, 6, 10, 14,
            3, 7, 11, 15
    };

    private static final int[] INPUT_H = {
            0, 8, 4, 12,
            2, 10, 6, 14,
            1, 9, 5, 13,
            3, 11, 7, 15
    };

    private static final int[] ROTATE_F = new int[] {3, 7, 11, 19};

    private static final int[] ROTATE_G = new int[] {3, 5, 9, 13};

    private static final int[] ROTATE_H = new int[] {3, 9, 11, 15};

    @NotNull
    private final transient int[] in = new int[16];

    @NotNull
    private final int[] out = new int[8];

    private int blockCount;

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
        return 16;
    }

    @NotNull
    @Override
    public MD4 clone() {
        final MD4 that = new MD4();
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
        out[0] = 0x67452301;
        out[1] = 0xefcdab89;
        out[2] = 0x98badcfe;
        out[3] = 0x10325476;
        blockCount = 0;
    }

    private void stepF(int remap, int inputIndex, int rotate) {
        final int value = out[remap] + ch(out[(remap + 1) & 0x3], out[(remap + 2) & 0x3], out[(remap + 3) & 0x3]) + in[inputIndex];
        out[remap] = ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    private void stepG(int remap, int inputIndex, int rotate) {
        final int value = out[remap] + maj(out[(remap + 1) & 0x3], out[(remap + 2) & 0x3], out[(remap + 3) & 0x3]) + in[inputIndex] + 0x5a827999;
        out[remap] = ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    private void stepH(int remap, int inputIndex, int rotate) {
        final int value = out[remap] + parity(out[(remap + 1) & 0x3], out[(remap + 2) & 0x3], out[(remap + 3) & 0x3]) + in[inputIndex] + 0x6ed9eba1;
        out[remap] = ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getInt();
        }
        System.arraycopy(out, 0, out, 4, 4);
        for (int i = 0; i < 16; i++) {
            stepF(REMAP[i & 0x3], i, ROTATE_F[i & 0x3]);
        }
        for (int i = 0; i < 16; i++) {
            stepG(REMAP[i & 0x3], INPUT_G[i], ROTATE_G[i & 0x3]);
        }
        for (int i = 0; i < 16; i++) {
            stepH(REMAP[i & 0x3], INPUT_H[i], ROTATE_H[i & 0x3]);
        }
        for (int i = 0; i < 4; i++) {
            out[i] += out[i + 4];
        }
        blockCount++;
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new IntSequenceDataView(this, ByteOrder.LITTLE_ENDIAN, guard);
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer, int paddingBits) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        pad10(buffer, paddingBits, 8);
    }

    @Override
    public int blockCount() {
        return blockCount;
    }

    @Override
    public int intLength() {
        return 4;
    }

    @Override
    public int getInt(int index) {
        if (index >= 4) {
            throw new IndexOutOfBoundsException();
        }
        return out[index];
    }

    @Override
    public String toString() {
        return "MD4";
    }
}
