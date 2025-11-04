package mujica.io.hash;

import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created in OSHI on 2025/1/22, named SimpleBytesDigest.
 * Recreated on 2025/4/29.
 */
@CodeHistory(date = "2025/1/22", project = "OSHI", name = "SimpleBytesDigest")
@CodeHistory(date = "2025/4/29")
public class SimpleByteBlockByteStreamHash implements ByteStreamHash {

    @NotNull
    protected final ByteBlockByteHashCore core;

    @NotNull
    protected final ByteBuffer concatBuffer;

    @NotNull
    protected final DataView resultView;

    @NotNull
    protected HashState state = HashState.CLEARED;

    protected SimpleByteBlockByteStreamHash(@NotNull ByteBlockByteHashCore core, @NotNull ByteBuffer concatBuffer) {
        super();
        this.core = core;
        this.concatBuffer = concatBuffer;
        this.resultView = core.getDataView(this::ensureFinished);
    }

    public SimpleByteBlockByteStreamHash(@NotNull ByteBlockByteHashCore core) {
        this(core, ByteBuffer.allocate(core.concatBufferCapacity()));
    }

    @NotNull
    protected ByteBuffer duplicateConcatBuffer() {
        final int position = concatBuffer.position();
        final int limit = concatBuffer.limit();
        final int capacity = concatBuffer.capacity();
        final ByteBuffer copy = ByteBuffer.allocate(capacity);
        copy.put(concatBuffer.position(0).limit(capacity)).position(position).limit(limit);
        concatBuffer.position(position).limit(limit);
        return copy;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    protected SimpleByteBlockByteStreamHash clone() {
        return new SimpleByteBlockByteStreamHash(core.clone(), duplicateConcatBuffer());
    }

    @Override
    public void clear() {
        core.clear();
        if (concatBuffer.hasArray()) {
            Arrays.fill(concatBuffer.array(), (byte) 0);
        } else {
            int limit = concatBuffer.clear().limit();
            for (int index = 0; index < limit; index++) {
                concatBuffer.put((byte) 0);
            }
            concatBuffer.position(0); // clear again
        }
        state = HashState.CLEARED;
    }

    @Override
    public void start() {
        core.clear();
        concatBuffer.clear();
        state = HashState.STARTED;
    }

    @Override
    public void update(byte input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int blockBytes = core.blockBytes();
        assert concatBuffer.position() < blockBytes;
        concatBuffer.put(input);
        if (concatBuffer.position() == blockBytes) {
            core.step(concatBuffer.flip());
            concatBuffer.clear();
        }
    }

    @Override
    public void update(@NotNull ByteSequence input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int blockBytes = core.blockBytes();
        final int srcLength = input.byteLength();
        int srcIndex = 0;
        while (srcIndex < srcLength) {
            int srcLimit = Math.min(srcLength, srcIndex + blockBytes - concatBuffer.position());
            while (srcIndex < srcLimit) {
                concatBuffer.put(input.getByte(srcIndex++));
            }
            if (concatBuffer.position() == blockBytes) {
                core.step(concatBuffer.flip());
                concatBuffer.clear();
            }
        }
        assert srcIndex == srcLength;
    }

    @Override
    public void update(@NotNull byte[] array, int offset, int length) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int blockBytes = core.blockBytes();
        while (length > 0) {
            int transferCount = Math.min(blockBytes - concatBuffer.position(), length);
            assert transferCount > 0 : concatBuffer.toString();
            concatBuffer.put(array, offset, transferCount);
            offset += transferCount;
            length -= transferCount;
            if (concatBuffer.position() == blockBytes) {
                core.step(concatBuffer.flip());
                concatBuffer.clear();
            }
        }
        assert length == 0;
    }

    @Override
    public void update(@NotNull byte[] array) {
        update(array, 0, array.length);
    }

    @Override
    public void update(@NotNull ByteBuffer input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int blockBytes = core.blockBytes();
        {
            int dstPosition = concatBuffer.position();
            assert dstPosition < blockBytes;
            if (dstPosition != 0) {
                int srcLimit = input.limit();
                int srcPosition = input.position();
                int transferCount = Math.min(blockBytes - dstPosition, srcLimit - srcPosition);
                if (transferCount > 0) {
                    if (transferCount != srcLimit) {
                        input.limit(srcPosition + transferCount);
                    }
                    concatBuffer.put(input);
                    if (transferCount != srcLimit) {
                        input.limit(srcLimit);
                    }
                    if (dstPosition + transferCount == blockBytes) {
                        concatBuffer.flip();
                        assert concatBuffer.limit() == blockBytes;
                        core.step(concatBuffer);
                        concatBuffer.clear();
                    }
                }
            }
        }
        while (input.remaining() >= blockBytes) {
            assert concatBuffer.position() == 0;
            core.step(input);
        }
        if (input.hasRemaining()) {
            concatBuffer.put(input);
            assert concatBuffer.position() < blockBytes;
        }
    }

    @Override
    public void update(@NotNull DataView input) {
        update((ByteSequence) input);
    }

    @NotNull
    @Override
    public DataView finish() {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        try {
            core.finish(concatBuffer);
            return resultView;
        } finally {
            state = HashState.FINISHED;
        }
    }

    public void ensureFinished() {
        if (state != HashState.FINISHED) {
            throw new IllegalHashStateException();
        }
        assert concatBuffer.position() == 0;
        assert concatBuffer.limit() == core.resultBytes();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull ByteSequence input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull byte[] array, int offset, int length) {
        start();
        update(array, offset, length);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull byte[] array) {
        start();
        update(array);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull DataView input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull ByteBuffer input) {
        start();
        update(input);
        return finish();
    }
}
