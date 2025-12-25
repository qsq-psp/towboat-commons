package mujica.io.hash;

import mujica.ds.of_int.list.IntSequence;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/12/7", project = "Ultramarine")
@CodeHistory(date = "2025/1/21", project = "OSHI", name = "SM3Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "SM3", href = "https://www.oscca.gov.cn/sca/xxgk/2010-12/17/content_1002389.shtml")
public class SM3 extends ByteBlockBitHashCore implements IntSequence {

    private static final long serialVersionUID = 0xa8df5a9ab740af24L;

    private static final int[] INITIAL_VECTOR = {
            0x7380166f,
            0x4914b2b9,
            0x172442d7,
            0xda8a0600,
            0xa96f30bc,
            0x163138aa,
            0xe38dee4d,
            0xb0fb0e4e
    }; // length = 8

    @NotNull
    private final transient int[] in = new int[68];

    @NotNull
    private final int[] out = new int[INITIAL_VECTOR.length];

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
        return 32;
    }

    @NotNull
    @Override
    public SM3 clone() {
        final SM3 that = new SM3();
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
        cloneArray(INITIAL_VECTOR, out);
        blockCount = 0;
    }

    private void prepare(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getInt();
        }
        for (int i = 16; i < 68; i++) {
            int v3 = in[i - 3];
            v3 = (v3 << 15) | (v3 >>> 17);
            v3 = in[i - 16] ^ in[i - 9] ^ v3;
            v3 = v3 ^ ((v3 << 15) | (v3 >>> 17)) ^ ((v3 << 23) | (v3 >>> 9));
            int v13 = in[i - 13];
            v13 = (v13 << 7) | (v13 >>> 25);
            in[i] = v3 ^ v13 ^ in[i - 6];
        }
    }

    private void loop() {
        int a = out[0];
        int b = out[1];
        int c = out[2];
        int d = out[3];
        int e = out[4];
        int f = out[5];
        int g = out[6];
        int h = out[7];
        for (int i = 0; i < 16; i++) {
            int a12 = (a << 12) | (a >>> 20);
            int ss1 = a12 + e + Integer.rotateLeft(0x79cc4519, i);
            ss1 = (ss1 << 7) | (ss1 >>> 25);
            int ss2 = ss1 ^ a12;
            int tt1 = (a ^ b ^ c) + d + ss2 + (in[i] ^ in[i + 4]);
            int tt2 = (e ^ f ^ g) + h + ss1 + in[i];
            d = c;
            c = (b << 9) | (b >>> 23);
            b = a;
            a = tt1;
            h = g;
            g = (f << 19) | (f >>> 13);
            f = e;
            e = tt2 ^ (tt2 << 9) ^ (tt2 >>> 23) ^ (tt2 << 17) ^ (tt2 >>> 15);
        }
        for (int i = 16; i < 64; i++) {
            int a12 = (a << 12) | (a >>> 20);
            int ss1 = a12 + e + Integer.rotateLeft(0x7a879d8a, i);
            ss1 = (ss1 << 7) | (ss1 >>> 25);
            int ss2 = ss1 ^ a12;
            int tt1 = maj(a, b, c) + d + ss2 + (in[i] ^ in[i + 4]);
            int tt2 = ch(e, f, g) + h + ss1 + in[i];
            d = c;
            c = (b << 9) | (b >>> 23);
            b = a;
            a = tt1;
            h = g;
            g = (f << 19) | (f >>> 13);
            f = e;
            e = tt2 ^ (tt2 << 9) ^ (tt2 >>> 23) ^ (tt2 << 17) ^ (tt2 >>> 15);
        }
        out[0] ^= a;
        out[1] ^= b;
        out[2] ^= c;
        out[3] ^= d;
        out[4] ^= e;
        out[5] ^= f;
        out[6] ^= g;
        out[7] ^= h;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        prepare(buffer);
        loop();
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
        return INITIAL_VECTOR.length;
    }

    @Override
    public int getInt(int index) {
        return out[index];
    }

    @Override
    public String toString() {
        return "SM3";
    }
}
