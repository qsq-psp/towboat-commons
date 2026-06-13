package mujica.ds.i8.run;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;

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

    @Override
    public void putFully(@NotNull byte[] array, int offset, int length) {
        final int readerIndex = data.writeBytes(array, offset, length).writerIndex() - maxDistance;
        if (readerIndex < 0) {
            return;
        }
        data.readerIndex(readerIndex).discardSomeReadBytes();
    }

    @Override
    public int copy(int distance, @NotNull byte[] array, int offset, int maxLength) {
        maxLength = super.copy(distance, array, offset, maxLength);
        final int readerIndex = data.writerIndex() - maxDistance;
        if (readerIndex >= 0) {
            data.readerIndex(readerIndex).discardSomeReadBytes();
        }
        return maxLength;
    }

    @Override
    public int copy(int distance, @NotNull OutputStream os, int maxLength) throws IOException {
        maxLength = super.copy(distance, os, maxLength);
        final int readerIndex = data.writerIndex() - maxDistance;
        if (readerIndex >= 0) {
            data.readerIndex(readerIndex).discardSomeReadBytes();
        }
        return maxLength;
    }
}
