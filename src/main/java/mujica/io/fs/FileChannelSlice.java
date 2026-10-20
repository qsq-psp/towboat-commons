package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@CodeHistory(date = "2026/9/27")
public class FileChannelSlice extends FileChannel {

    @NotNull
    final FileChannel fc;

    final long offset;

    final long length;

    public FileChannelSlice(@NotNull FileChannel fc, long offset, long length) {
        super();
        this.fc = fc;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public int read(@NotNull ByteBuffer dst) throws IOException {
        return 0;
    }

    @Override
    public long read(@NotNull ByteBuffer[] dstArray, int offset, int length) throws IOException {
        return 0;
    }

    @Override
    public int write(@NotNull ByteBuffer src) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public long write(@NotNull ByteBuffer[] srcArray, int offset, int length) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public long position() throws IOException {
        final long position = fc.position() - offset;
        if (position < 0L || position >= length) {
            throw new IOException();
        }
        return position;
    }

    @Override
    public FileChannel position(long newPosition) throws IOException {
        if (newPosition < 0L || newPosition >= length) {
            throw new IllegalArgumentException();
        }
        fc.position(offset + newPosition);
        return this;
    }

    @Override
    public long size() {
        return length;
    }

    @Override
    public FileChannel truncate(long size) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public void force(boolean metaData) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public long transferTo(long position, long count, @NotNull WritableByteChannel target) throws IOException {
        if (position < 0L || count < 0L || Math.addExact(position, count) > length) {
            throw new IllegalArgumentException();
        }
        return fc.transferTo(offset + position, count, target);
    }

    @Override
    public long transferFrom(@NotNull ReadableByteChannel src, long position, long count) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public int read(@NotNull ByteBuffer dst, long position) throws IOException {
        if (position < 0L || position > length) {
            throw new IllegalArgumentException();
        }
        return fc.read(dst, offset + position);
    }

    @Override
    public int write(@NotNull ByteBuffer src, long position) throws IOException {
        throw new IOException("read only");
    }

    @Override
    public MappedByteBuffer map(@NotNull MapMode mode, long position, long size) throws IOException {
        if (position < 0L || size < 0L || Math.addExact(position, size) > length) {
            throw new IllegalArgumentException();
        }
        return fc.map(mode, offset + position, size);
    }

    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        if (position < 0L || size < 0L || Math.addExact(position, size) > length) {
            throw new IllegalArgumentException();
        }
        return fc.lock(offset + position, size, shared);
    }

    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        if (position < 0L || size < 0L || Math.addExact(position, size) > length) {
            throw new IllegalArgumentException();
        }
        return fc.tryLock(offset + position, size, shared);
    }

    @Override
    protected void implCloseChannel() throws IOException {
        fc.close();
    }
}
