package mujica.io.hash;

import mujica.ds.i8.view.ByteFillPolicy;
import mujica.ds.i8.view.DataView;
import mujica.ds.i8.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@CodeHistory(date = "2026/9/11")
@ReferencePage(title = "", href = "https://github.com/jandrewrogers/MetroHash")
@ReferencePage(title = "", href = "http://www.jandrewrogers.com/2015/05/27/metrohash/")
@ReferenceCode(groupId = "cn.hutool", artifactId = "hutool-core", version = "5.8.47", fullyQualifiedName = "cn.hutool.core.lang.hash.MetroHash")
public class MetroHash64 extends ByteBlockByteHashCore {

    private final static long k0_64 = 0xD6D018F5L;
    private final static long k1_64 = 0xA2AA033BL;
    private final static long k2_64 = 0x62992FC1L;
    private final static long k3_64 = 0x30BC5B29L;

    private final long seed;

    private long v0, v1, v2, v3;

    private long result;

    public MetroHash64(long seed) {
        super();
        this.seed = seed;
    }

    @Override
    public int blockBytes() {
        return 32;
    }

    @Override
    public int resultBytes() {
        return 8;
    }

    @NotNull
    @Override
    public MetroHash64 clone() {
        return new MetroHash64(seed);
    }

    @Override
    public void clear() {
        v0 = 0L;
        v1 = 0L;
        v2 = 0L;
        v3 = 0L;
    }

    @Override
    public void start() {
        result = (seed + k2_64) * k0_64;
        v0 = result;
        v1 = result;
        v2 = result;
        v3 = result;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        v0 += buffer.getLong() * k0_64;
        v0 = Long.rotateLeft(v0, 35) + v2;
        v1 += buffer.getLong() * k1_64;
        v1 = Long.rotateLeft(v1, 35) + v3;
        v2 += buffer.getLong() * k2_64;
        v2 = Long.rotateLeft(v0, 35) + v0;
        v3 += buffer.getLong() * k3_64;
        v3 = Long.rotateLeft(v1, 35) + v1;
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongDataView(() -> {
            guard.run();
            return result;
        }, ByteFillPolicy.MIDDLE_TO_RIGHT, Long.SIZE);
    }
}
