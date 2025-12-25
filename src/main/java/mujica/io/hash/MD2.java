package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@CodeHistory(date = "2024/11/17", project = "Ultramarine")
@CodeHistory(date = "2025/1/17", project = "OSHI", name = "MD2Core")
@CodeHistory(date = "2025/5/16")
@ReferencePage(title = "The MD2 Message-Digest Algorithm", href = "https://datatracker.ietf.org/doc/html/rfc1319")
public class MD2 extends ByteBlockByteHashCore implements ByteSequence {

    private static final long serialVersionUID = 0x2e2ac6b13b29d6bfL;

    private static final byte[] PERMUTATION = {
            41, 46, 67, -55, -94, -40, 124, 1, 61, 54, 84, -95, -20, -16, 6, 19, 98, -89, 5, -13, -64, -57, 115, -116,
            -104, -109, 43, -39, -68, 76, -126, -54, 30, -101, 87, 60, -3, -44, -32, 22, 103, 66, 111, 24, -118, 23,
            -27, 18, -66, 78, -60, -42, -38, -98, -34, 73, -96, -5, -11, -114, -69, 47, -18, 122, -87, 104, 121, -111,
            21, -78, 7, 63, -108, -62, 16, -119, 11, 34, 95, 33, -128, 127, 93, -102, 90, -112, 50, 39, 53, 62, -52,
            -25, -65, -9, -105, 3, -1, 25, 48, -77, 72, -91, -75, -47, -41, 94, -110, 42, -84, 86, -86, -58, 79, -72,
            56, -46, -106, -92, 125, -74, 118, -4, 107, -30, -100, 116, 4, -15, 69, -99, 112, 89, 100, 113, -121, 32,
            -122, 91, -49, 101, -26, 45, -88, 2, 27, 96, 37, -83, -82, -80, -71, -10, 28, 70, 97, 105, 52, 64, 126, 15,
            85, 71, -93, 35, -35, 81, -81, 58, -61, 92, -7, -50, -70, -59, -22, 38, 44, 83, 13, 110, -123, 40, -124, 9,
            -45, -33, -51, -12, 65, -127, 77, 82, 106, -36, 55, -56, 108, -63, -85, -6, 36, -31, 123, 8, 12, -67, -79,
            74, 120, -120, -107, -117, -29, 99, -24, 109, -23, -53, -43, -2, 59, 0, 29, 57, -14, -17, -73, 14, 102, 88,
            -48, -28, -90, 119, 114, -8, -21, 117, 75, 10, 49, 68, 80, -76, -113, -19, 31, 26, -37, -103, -115, 51, -97,
            17, -125, 20
    }; // length = 256

    @NotNull
    private final byte[] checksum = new byte[16];

    @NotNull
    private final byte[] out = new byte[48];

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
    public MD2 clone() {
        final MD2 that = new MD2();
        cloneArray(this.checksum, that.checksum);
        cloneArray(this.out, that.out);
        return that;
    }

    @Override
    public void clear() {
        Arrays.fill(checksum, (byte) 0);
        Arrays.fill(out, (byte) 0);
    }

    @Override
    public void start() {
        clear();
    }

    private void checksumLoop(@NotNull ByteBuffer buffer) {
        final int offset = buffer.position();
        int previous = checksum[16 - 1];
        for (int index = 0; index < 16; index++) {
            int value = buffer.get(offset + index);
            previous = checksum[index] ^ PERMUTATION[0xff & (previous ^ value)];
            checksum[index] = (byte) previous;
        }
    }

    private void outputLoop() {
        for (int i = 0; i < 16; i++) {
            out[32 + i] = (byte) (out[16 + i] ^ out[i]);
        }
        int previous = 0;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 48; j++) {
                previous = out[j] ^ PERMUTATION[0xff & previous];
                out[j] = (byte) previous;
            }
            previous += i;
        }
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        checksumLoop(buffer);
        buffer.get(out, 16, 16);
        outputLoop();
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        final int padding = 16 - buffer.position();
        for (int i = 0; i < padding; i++) {
            buffer.put((byte) padding);
        }
        step(buffer.flip());
        System.arraycopy(checksum, 0, out, 16, 16);
        outputLoop();
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new ByteSequenceDataView(this, ByteOrder.LITTLE_ENDIAN, guard);
    }

    @Override
    public int byteLength() {
        return 16;
    }

    @Override
    public byte getByte(int index) {
        return out[index];
    }

    @Override
    public String toString() {
        return "MD2";
    }
}
