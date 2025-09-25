package mujica.io.hash;

import mujica.io.view.DataView;
import mujica.ds.of_int.list.IntSequence;
import mujica.io.view.IntSequenceDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/11/14", project = "Ultramarine")
@CodeHistory(date = "2025/1/17", project = "OSHI", name = "MD5Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "RFC1321", href = "https://datatracker.ietf.org/doc/html/rfc1321")
public class MD5 extends ByteBlockBitHashCore implements IntSequence {

    private static final long serialVersionUID = 0x72ac2e225c2045c6L;

    private static final int TABLE_LENGTH = 64;

    private static final int[] TABLE = new int[TABLE_LENGTH];

    static {
        for (int index = 0; index < TABLE_LENGTH; index++) {
            TABLE[index] = (int) (long) (0x1.0p32 * Math.abs(Math.sin(index + 1)));
        }
    }

    private static final int[] REMAP = new int[] {0, 3, 2, 1};

    private static final int[] ROTATE_F = new int[] {7, 12, 17, 22};

    private static final int[] ROTATE_G = new int[] {5, 9, 14, 20};

    private static final int[] ROTATE_H = new int[] {4, 11, 16, 23};

    private static final int[] ROTATE_I = new int[] {6, 10, 15, 21};

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
    public MD5 clone() {
        final MD5 that = new MD5();
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

    private void stepF(int remap, int inputIndex, int rotate, int tableIndex) {
        final int value = out[remap] + ch(out[(remap + 1) & 0x3], out[(remap + 2) & 0x3], out[(remap + 3) & 0x3]) + in[inputIndex] + TABLE[tableIndex];
        out[remap] = out[(remap + 1) & 0x3] + ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    private void stepG(int remap, int inputIndex, int rotate, int tableIndex) {
        final int value = out[remap] + ch(out[(remap + 3) & 0x3], out[(remap + 1) & 0x3], out[(remap + 2) & 0x3]) + in[inputIndex] + TABLE[tableIndex];
        out[remap] = out[(remap + 1) & 0x3] + ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    private void stepH(int remap, int inputIndex, int rotate, int tableIndex) {
        final int value = out[remap] + parity(out[(remap + 1) & 0x3], out[(remap + 2) & 0x3], out[(remap + 3) & 0x3]) + in[inputIndex] + TABLE[tableIndex];
        out[remap] = out[(remap + 1) & 0x3] + ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    private void stepI(int remap, int inputIndex, int rotate, int tableIndex) {
        int value = out[(remap + 2) & 0x3] ^ (out[(remap + 1) & 0x3] | ~out[(remap + 3) & 0x3]);
        value = out[remap] + value + in[inputIndex] + TABLE[tableIndex];
        out[remap] = out[(remap + 1) & 0x3] + ((value << rotate) | (value >>> (Integer.SIZE - rotate)));
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getInt();
        }
        System.arraycopy(out, 0, out, 4, 4);
        for (int i = 0; i < 16; i++) {
            stepF(REMAP[i & 0x3], i, ROTATE_F[i & 0x3], i);
        }
        for (int i = 0; i < 16; i++) {
            stepG(REMAP[i & 0x3], (i * 5 + 1) & 0xf, ROTATE_G[i & 0x3], 16 + i);
        }
        for (int i = 0; i < 16; i++) {
            stepH(REMAP[i & 0x3], (i * 3 + 5) & 0xf, ROTATE_H[i & 0x3], 32 + i);
        }
        for (int i = 0; i < 16; i++) {
            stepI(REMAP[i & 0x3], (i * 7) & 0xf, ROTATE_I[i & 0x3], 48 + i);
        }
        for (int i = 0; i < 4; i++) {
            out[i] += out[i + 4];
        }
        blockCount++;
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new IntSequenceDataView(this, ByteOrder.LITTLE_ENDIAN);
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
        return "MD5";
    }
}
