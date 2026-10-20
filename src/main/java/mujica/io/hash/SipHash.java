package mujica.io.hash;

import mujica.ds.i8.view.ByteFillPolicy;
import mujica.ds.i8.view.DataView;
import mujica.ds.i8.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@CodeHistory(date = "2026/9/7")
@ReferenceCode(groupId = "com.google.guava", artifactId = "guava", version = "33.6.0-jre", fullyQualifiedName = "com.google.common.hash.SipHashFunction")
public class SipHash extends ByteBlockByteHashCore {

    @Name(value = "number of compression rounds", language = "en")
    private final int c;

    @Name(value = "number of finalization rounds", language = "en")
    private final int d;

    private final long k0;

    private final long k1;

    private long v0, v1, v2, v3;

    private long byteCount;

    public SipHash(int c, int d, long k0, long k1) {
        super();
        if (c <= 0 || d <= 0) {
            throw new IllegalArgumentException();
        }
        this.c = c;
        this.d = d;
        this.k0 = k0;
        this.k1 = k1;
    }

    public SipHash() {
        this(2, 4, 0x0706050403020100L, 0x0f0e0d0c0b0a0908L);
    }

    @Override
    public int blockBytes() {
        return 8;
    }

    @Override
    public int resultBytes() {
        return 8;
    }

    @NotNull
    @Override
    public ByteBlockByteHashCore clone() {
        return new SipHash(c, d, k0, k1);
    }

    @Override
    public void clear() {
        v0 = 0L;
        v1 = 0L;
        v2 = 0L;
        v3 = 0L;
        byteCount = 0L;
    }

    @Override
    public void start() {
        v0 = 0x736f6d6570736575L;
        v1 = 0x646f72616e646f6dL;
        v2 = 0x6c7967656e657261L;
        v3 = 0x7465646279746573L;
        byteCount = 0L;
    }

    private void round() {
        v0 += v1;
        v2 += v3;
        v1 = Long.rotateLeft(v1, 13);
        v3 = Long.rotateLeft(v3, 16);
        v1 ^= v0;
        v3 ^= v2;
        v0 = Long.rotateLeft(v0, 32);
        v2 += v1;
        v0 += v3;
        v1 = Long.rotateLeft(v1, 17);
        v3 = Long.rotateLeft(v3, 21);
        v1 ^= v2;
        v3 ^= v0;
        v2 = Long.rotateLeft(v2, 32);
    }

    private void update(long x) {
        v3 ^= x;
        for (int i = 0; i < c; i++) {
            round();
        }
        v0 ^= x;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byteCount += 8;
        update(buffer.getLong());
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byteCount += buffer.remaining();
        long x = 0L;
        for (int shift = 0; shift < Long.SIZE; shift += Byte.SIZE) {
            if (buffer.hasRemaining()) {
                x |= (buffer.get() & 0xffL) << shift;
            } else {
                break;
            }
        }
        x ^= byteCount;
        update(x);
        v2 ^= 0xffL;
        for (int i = 0; i < d; i++) {
            round();
        }
        x = v0 ^ v1 ^ v2 ^ v3;

    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongDataView(() -> {
            guard.run();
            return v0 ^ v1 ^ v2 ^ v3;
        }, ByteFillPolicy.MIDDLE_TO_RIGHT, Long.SIZE);
    }
}
