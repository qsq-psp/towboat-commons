package mujica.io.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/12/1.
 */
public class FastByteBufRunBuffer extends ByteBufRunBuffer {

    public FastByteBufRunBuffer(@NotNull ByteBuf data) {
        super(data);
    }

    public FastByteBufRunBuffer(@Nullable ByteBufAllocator allocator) {
        super(allocator);
    }

    public FastByteBufRunBuffer() {
        super();
    }
}
