package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteBuffer;

@CodeHistory(date = "2024/12/19", project = "OSHI", name = "BlockDigestCore")
@CodeHistory(date = "2025/4/17")
public abstract class ByteBlockByteHashCore implements Serializable {

    private static final long serialVersionUID = 0x22f098248136ae45L;

    protected static void cloneArray(@NotNull byte[] src, @NotNull byte[] dst) {
        assert src.length == dst.length;
        System.arraycopy(src, 0, dst, 0, src.length);
    }

    protected static void cloneArray(@NotNull int[] src, @NotNull int[] dst) {
        assert src.length == dst.length;
        System.arraycopy(src, 0, dst, 0, src.length);
    }

    protected static void cloneArray(@NotNull long[] src, @NotNull long[] dst) {
        assert src.length == dst.length;
        System.arraycopy(src, 0, dst, 0, src.length);
    }

    public ByteBlockByteHashCore() {
        super();
    }

    public abstract int blockBytes();

    public abstract int resultBytes();

    public int concatBufferCapacity() {
        return blockBytes() << 1;
    }

    @NotNull
    @Override
    public abstract ByteBlockByteHashCore clone();

    public abstract void clear();

    public abstract void start();

    public abstract void step(@NotNull ByteBuffer buffer);

    public abstract void finish(@NotNull ByteBuffer buffer);

    public int blockCount() {
        return -1; // -1 means this core does not count blocks
    }

    @NotNull
    public abstract DataView getDataView(@NotNull Runnable guard);

    protected void pad10(@NotNull ByteBuffer buffer, int lengthFieldBytes) {
        //
    }
}
