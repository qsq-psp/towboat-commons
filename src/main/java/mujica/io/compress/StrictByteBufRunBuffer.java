package mujica.io.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created on 2025/11/29.
 */
public class StrictByteBufRunBuffer extends ByteBufRunBuffer {

    public StrictByteBufRunBuffer(@NotNull ByteBuf data) {
        super(data);
    }

    public StrictByteBufRunBuffer(@Nullable ByteBufAllocator allocator) {
        super(allocator);
    }

    public StrictByteBufRunBuffer() {
        super();
    }

    @Override
    public void put(byte value) {
        content().writeByte(value);
    }

    @Override
    public int put(@NotNull byte[] array, int offset, int maxLength) {
        content().writeBytes(array, offset, maxLength);
        return maxLength;
    }

    @Override
    public void putFully(@NotNull byte[] array, int offset, int length) {
        content().writeBytes(array, offset, length);
    }

    @Override
    public int put(@NotNull InputStream is, int maxLength) throws IOException {
        maxLength = content().writeBytes(is, maxLength);
        if (maxLength == -1) {
            throw new EOFException();
        }
        return maxLength;
    }

    @Override
    public byte copy(int distance) {
        final ByteBuf content = content();
        final byte value = content.getByte(content.writerIndex() - distance);
        put(value);
        return value;
    }

    @Override
    public int copy(int distance, @NotNull byte[] array, int offset, int maxLength) {
        checkDistance(distance);
        final ByteBuf content = content();
        maxLength = Math.min(distance, maxLength);
        content.getBytes(content.writerIndex() - distance, array, offset, maxLength);
        content.writeBytes(array, offset, maxLength);
        return maxLength;
    }

    @Override
    public int copy(int distance, @NotNull OutputStream os, int maxLength) throws IOException {
        checkDistance(distance);
        final ByteBuf content = content();
        final int offset = content.writerIndex();
        content.writeBytes(content, offset - distance, maxLength);
        content.getBytes(offset, os, maxLength);
        return maxLength;
    }

    @Override
    public int available() {
        return content().writerIndex();
    }

    @Override
    public void clear() {
        content().clear();
    }
}
