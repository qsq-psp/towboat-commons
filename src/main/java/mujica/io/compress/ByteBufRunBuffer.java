package mujica.io.compress;

import io.netty.buffer.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created on 2025/11/17.
 */
@CodeHistory(date = "2025/11/17", name = "ByteBufLookBackMemory")
@CodeHistory(date = "2025/11/29")
public class ByteBufRunBuffer extends RunBuffer implements ByteBufHolder {

    @NotNull
    protected final ByteBuf data;

    public ByteBufRunBuffer(@NotNull ByteBuf data) {
        super();
        this.data = data;
    }

    public ByteBufRunBuffer(@Nullable ByteBufAllocator allocator) {
        this(Objects.requireNonNullElse(allocator, UnpooledByteBufAllocator.DEFAULT).buffer());
    }

    public ByteBufRunBuffer() {
        this(Unpooled.buffer());
    }

    @Override
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(data);
    }

    @Override
    public ByteBufHolder copy() {
        return replace(data.copy());
    }

    @Override
    public ByteBufHolder duplicate() {
        return replace(data.duplicate());
    }

    @Override
    public ByteBufHolder retainedDuplicate() {
        return replace(data.retainedDuplicate());
    }

    @Override
    public ByteBufHolder replace(ByteBuf content) {
        return new ByteBufRunBuffer(content);
    }

    @Override
    public int refCnt() {
        return data.refCnt();
    }

    @Override
    public ByteBufHolder retain() {
        data.retain();
        return this;
    }

    @Override
    public ByteBufHolder retain(int increment) {
        data.retain(increment);
        return this;
    }

    @Override
    public ByteBufHolder touch() {
        data.touch();
        return this;
    }

    @Override
    public ByteBufHolder touch(Object hint) {
        data.touch(hint);
        return this;
    }

    @Override
    public boolean release() {
        return data.release();
    }

    @Override
    public boolean release(int decrement) {
        return data.release(decrement);
    }

    @Override
    public void put(byte value) {
        data.writeByte(value);
    }

    @Override
    public int put(@NotNull byte[] array, int offset, int maxLength) {
        data.writeBytes(array, offset, maxLength);
        return maxLength;
    }

    @Override
    public void putFully(@NotNull byte[] array, int offset, int length) {
        data.writeBytes(array, offset, length);
    }

    @Override
    public int put(@NotNull InputStream is, int maxLength) throws IOException {
        maxLength = data.writeBytes(is, maxLength);
        if (maxLength == -1) {
            throw new EOFException();
        }
        return maxLength;
    }

    @Override
    public byte copy(int distance) {
        final byte value = data.getByte(data.writerIndex() - distance);
        put(value);
        return value;
    }

    @Override
    public int copy(int distance, @NotNull byte[] array, int offset, int maxLength) {
        maxLength = Math.min(distance, maxLength);
        data.getBytes(data.writerIndex() - distance, array, offset, maxLength);
        data.writeBytes(array, offset, maxLength);
        return maxLength;
    }

    @Override
    public int copy(int distance, @NotNull OutputStream os, int maxLength) throws IOException {
        final int offset = data.writerIndex();
        data.writeBytes(data, offset - distance, maxLength);
        data.getBytes(offset, os, maxLength);
        return maxLength;
    }

    @Override
    public int available() {
        return data.writerIndex();
    }

    @Override
    public void clear() {
        data.clear();
    }
}
