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

@CodeHistory(date = "2024/11/4", project = "UltraIO", name = "SHA1")
@CodeHistory(date = "2024/12/20", project = "OSHI", name = "SHA1Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "FIPS PUB 180-4", href = "https://csrc.nist.gov/publications/fips/fips180-4/fips-180-4.pdf")
public class SHA1 extends ByteBlockBitHashCore implements IntSequence {

    private static final long serialVersionUID = 0xffa29959ab8bcc4dL;

    @NotNull
    private final transient int[] in = new int[80];

    @NotNull
    private final int[] out = new int[5];

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
        return 20;
    }

    @NotNull
    @Override
    public SHA1 clone() {
        final SHA1 that = new SHA1();
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
        out[4] = 0xc3d2e1f0;
        blockCount = 0;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 16; i++) {
            in[i] = buffer.getInt();
        }
        for (int i = 16; i < 80; i++) {
            int xor = in[i - 3] ^ in[i - 8] ^ in[i - 14] ^ in[i - 16];
            in[i] = (xor << 1) | (xor >>> 31);
        }
        int a = out[0];
        int b = out[1];
        int c = out[2];
        int d = out[3];
        int e = out[4];
        for (int i = 0; i < 20; i++) {
            int t = in[i] + ((a << 5) | (a >>> 27)) + e + ch(b, c, d) + 0x5a827999;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = t;
        }
        for (int i = 20; i < 40; i++) {
            int t = in[i] + ((a << 5) | (a >>> 27)) + e + parity(b, c, d) + 0x6ed9eba1;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = t;
        }
        for (int i = 40; i < 60; i++) {
            int t = in[i] + ((a << 5) | (a >>> 27)) + e + maj(b, c, d) + 0x8f1bbcdc;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = t;
        }
        for (int i = 60; i < 80; i++) {
            int t = in[i] + ((a << 5) | (a >>> 27)) + e + parity(b, c, d) + 0xca62c1d6;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = t;
        }
        out[0] += a;
        out[1] += b;
        out[2] += c;
        out[3] += d;
        out[4] += e;
        blockCount++;
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new IntSequenceDataView(this, ByteOrder.BIG_ENDIAN);
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
        return 5;
    }

    @Override
    public int getInt(int index) {
        return out[index];
    }

    @Override
    public String toString() {
        return "SHA1";
    }
}
