package mujica.io.hash;

import mujica.ds.of_boolean.BitSequence;
import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created in OSHI on 2025/1/27, named SimpleBitsDigest.
 * Recreated on 2025/4/29.
 */
@CodeHistory(date = "2025/1/27", project = "OSHI", name = "SimpleBitsDigest")
@CodeHistory(date = "2025/4/29")
public class SimpleByteBlockBitStreamHash implements BitStreamHash {

    @NotNull
    protected final ByteBlockBitHashCore core;

    @NotNull
    protected final ByteBuffer concatBuffer;

    @NotNull
    protected final DataView resultView;

    @NotNull
    protected HashState state = HashState.CLEARED;

    /**
     * 0 <= padBitCount < Byte.SIZE = 8
     */
    protected int padBitCount;

    protected SimpleByteBlockBitStreamHash(@NotNull ByteBlockBitHashCore core, @NotNull ByteBuffer concatBuffer) {
        super();
        this.core = core;
        this.concatBuffer = concatBuffer;
        this.resultView = core.getDataView(this::ensureFinished);
    }

    public SimpleByteBlockBitStreamHash(@NotNull ByteBlockBitHashCore core) {
        this(core, ByteBuffer.allocate(core.concatBufferCapacity()));
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
        padBitCount = 0;
        state = HashState.CLEARED;
    }

    @Override
    public void start() {
        core.start();
        concatBuffer.clear();
        padBitCount = 0;
        state = HashState.STARTED;
    }

    private void updateAlignedBlock(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        final int blockBytes = core.blockBytes();
        while (length >= blockBytes) {
            assert concatBuffer.position() == 0; // call requirement
            core.step(concatBuffer.put(array, offset, blockBytes).flip());
            offset += blockBytes;
            length -= blockBytes;
            concatBuffer.clear();
        }
        if (length > 0) {
            assert concatBuffer.position() == 0; // call requirement
            concatBuffer.put(array, offset, length);
            assert concatBuffer.position() < blockBytes;
        }
    }

    private void updateAlignedBlock(@NotNull ByteBuffer input) {
        final int blockBytes = core.blockBytes();
        while (input.remaining() >= blockBytes) {
            assert concatBuffer.position() == 0; // call requirement
            core.step(input);
        }
        if (input.hasRemaining()) {
            assert concatBuffer.position() == 0; // call requirement
            concatBuffer.put(input);
            assert concatBuffer.position() < blockBytes;
        }
    }

    private void updateAlignedBits(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        assert padBitCount == 0; // call requirement
        int position = concatBuffer.position();
        final int blockBytes = core.blockBytes();
        assert position < blockBytes;
        if (position == 0) {
            updateAlignedBlock(array, offset, length);
            return;
        }
        int transferCount = Math.min(blockBytes - position, length);
        if (transferCount > 0) {
            concatBuffer.put(array, offset, transferCount);
            offset += transferCount;
            length -= transferCount;
            position += transferCount;
            if (position == blockBytes) {
                concatBuffer.flip();
                assert concatBuffer.limit() == blockBytes;
                core.step(concatBuffer);
                concatBuffer.clear();
                updateAlignedBlock(array, offset, length);
            }
        }
    }

    private void updateAlignedBits(@NotNull ByteBuffer input) {
        assert padBitCount == 0; // call requirement
        int dstPosition = concatBuffer.position();
        final int blockBytes = core.blockBytes();
        assert dstPosition < blockBytes;
        if (dstPosition == 0) {
            updateAlignedBlock(input);
            return;
        }
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
            dstPosition += transferCount;
            if (dstPosition == blockBytes) {
                concatBuffer.flip();
                assert concatBuffer.limit() == blockBytes;
                core.step(concatBuffer);
                concatBuffer.clear();
                updateAlignedBlock(input);
            }
        }
    }

    private void updateNotAlignedBits(@NotNull ByteSequence input) {
        final int padBitCount = this.padBitCount;
        final int blockBytes = core.blockBytes();
        assert 0 < padBitCount && padBitCount < Byte.SIZE;
        assert (concatBuffer.position() << 3 - padBitCount) < (blockBytes << 3);
        int position = concatBuffer.position() - 1;
        int dstOctet = concatBuffer.get(position);
        concatBuffer.position(position);
        final int byteLength = input.byteLength();
        for (int index = 0; index < byteLength; index++) {
            int srcOctet = input.getByte(index);
            if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                dstOctet = (srcOctet << (Byte.SIZE - padBitCount)) | (dstOctet & (0xff >> padBitCount));
            } else {
                dstOctet = (dstOctet & (0xff << padBitCount)) | (srcOctet >> (Byte.SIZE - padBitCount));
            }
            concatBuffer.put((byte) dstOctet);
            position++;
            if (position == blockBytes) {
                this.padBitCount = 0;
                core.step(concatBuffer.flip());
                concatBuffer.clear();
                this.padBitCount = padBitCount;
                position = 0;
            }
            if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                dstOctet = srcOctet >> padBitCount;
            } else {
                dstOctet = srcOctet << padBitCount;
            }
        }
        concatBuffer.put((byte) dstOctet);
    }

    @Override
    public void update(boolean input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        assert 0 <= padBitCount;
        assert padBitCount < Byte.SIZE;
        assert (concatBuffer.position() << 3 - padBitCount) < (core.blockBytes() << 3);
        if (padBitCount == 0) {
            if (input) {
                if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                    concatBuffer.put((byte) 0x01);
                } else {
                    concatBuffer.put((byte) 0x80);
                }
            } else {
                concatBuffer.put((byte) 0x0);
            }
            padBitCount = Byte.SIZE - 1;
        } else {
            int position = concatBuffer.position() - 1;
            padBitCount--;
            int value;
            if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                value = 0x80 >> padBitCount;
            } else {
                value = 0x01 << padBitCount;
            }
            if (input) {
                value = concatBuffer.get(position) | value;
            } else {
                value = concatBuffer.get(position) & ~value;
            }
            concatBuffer.put(position, (byte) value);
            if (padBitCount == 0) {
                position++;
                if (position == core.blockBytes()) {
                    core.step(concatBuffer.flip());
                    concatBuffer.clear();
                }
            }
        }
    }

    @Override
    public void update(@NotNull BitSequence input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        assert 0 <= padBitCount;
        assert padBitCount < Byte.SIZE;
        assert (concatBuffer.position() << 3 - padBitCount) < (core.blockBytes() << 3);
        final int bitLength = input.bitLength();
        for (int index = 0; index < bitLength; index++) {
            if (padBitCount == 0) {
                if (input.getBit(index)) {
                    if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                        concatBuffer.put((byte) 0x01);
                    } else {
                        concatBuffer.put((byte) 0x80);
                    }
                } else {
                    concatBuffer.put((byte) 0x0);
                }
                padBitCount = Byte.SIZE - 1;
            } else {
                int dstPosition = concatBuffer.position() - 1;
                padBitCount--;
                int octet;
                if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
                    octet = 0x80 >> padBitCount;
                } else {
                    octet = 0x01 << padBitCount;
                }
                if (input.getBit(index)) {
                    octet = concatBuffer.get(dstPosition) | octet;
                } else {
                    octet = concatBuffer.get(dstPosition) & ~octet;
                }
                concatBuffer.put(dstPosition, (byte) octet);
                if (padBitCount == 0) {
                    dstPosition++;
                    if (dstPosition == core.blockBytes()) {
                        core.step(concatBuffer.flip());
                        concatBuffer.clear();
                    }
                }
            }
        }
    }

    @Override
    public void update(byte input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int padBitCount = this.padBitCount;
        final int blockBytes = core.blockBytes();
        if (padBitCount == 0) {
            concatBuffer.put(input);
            if (concatBuffer.position() == blockBytes) {
                core.step(concatBuffer.flip());
                concatBuffer.clear();
            }
            return;
        }
        assert 0 < padBitCount && padBitCount < Byte.SIZE;
        assert (concatBuffer.position() << 3 - padBitCount) < (blockBytes << 3);
        int position = concatBuffer.position() - 1;
        int octet = concatBuffer.get(position);
        if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
            octet = (input << (Byte.SIZE - padBitCount)) | (octet & (0xff >> padBitCount));
        } else {
            octet = (octet & (0xff << padBitCount)) | (input >> (Byte.SIZE - padBitCount));
        }
        concatBuffer.put(position, (byte) octet);
        position++;
        if (position == core.blockBytes()) {
            this.padBitCount = 0;
            core.step(concatBuffer.flip());
            concatBuffer.clear();
            this.padBitCount = padBitCount;
        }
        if (core.bitOrder() == ByteOrder.LITTLE_ENDIAN) {
            octet = input >> padBitCount;
        } else {
            octet = input << padBitCount;
        }
        concatBuffer.put((byte) octet);
    }

    @Override
    public void update(@NotNull ByteSequence input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int byteLength = input.byteLength();
        if (byteLength <= 0) {
            return;
        }
        if (byteLength == 1) {
            update(input.getByte(0));
            return;
        }
        if (padBitCount != 0) {
            updateNotAlignedBits(input);
            return;
        }
        final int blockBytes = core.blockBytes();
        assert concatBuffer.position() < blockBytes;
        for (int index = 0; index < byteLength; index++) {
            concatBuffer.put(input.getByte(index));
            if (concatBuffer.position() == blockBytes) {
                core.step(concatBuffer.flip());
                concatBuffer.clear();
            }
        }
    }

    @Override
    public void update(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        if (length <= 0) {
            return;
        }
        if (padBitCount != 0) {
            updateNotAlignedBits(ByteSequence.of(array, offset, length));
            return;
        }
        if (length >= 2 * core.blockBytes()) {
            updateAlignedBits(ByteBuffer.wrap(array, offset, length));
        } else {
            updateAlignedBits(array, offset, length);
        }
    }

    @Override
    public void update(@NotNull byte[] array) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        final int length = array.length;
        if (length <= 0) {
            return;
        }
        if (padBitCount != 0) {
            updateNotAlignedBits(ByteSequence.of(array, 0, length));
            return;
        }
        if (length >= 2 * core.blockBytes()) {
            updateAlignedBits(ByteBuffer.wrap(array));
        } else {
            updateAlignedBits(array, 0, length);
        }
    }

    @Override
    public void update(@NotNull ByteBuffer input) {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        if (padBitCount == 0) {
            updateAlignedBits(input);
        } else {
            updateNotAlignedBits(ByteSequence.relative(input));
            input.position(input.limit());
        }
    }

    @Override
    public void update(@NotNull DataView input) {
        update((BitSequence) input);
    }

    @NotNull
    @Override
    public DataView finish() {
        if (state != HashState.STARTED) {
            throw new IllegalHashStateException();
        }
        try {
            core.finish(concatBuffer, padBitCount);
            return resultView;
        } finally {
            state = HashState.FINISHED;
        }
    }

    public void ensureFinished() {
        if (state != HashState.FINISHED) {
            throw new IllegalHashStateException();
        }
    }

    @NotNull
    @Override
    public DataView apply(@NotNull BitSequence input) {
        start();
        update(input);
        return finish();
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
    public DataView apply(@NotNull ByteBuffer input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull DataView input) {
        start();
        update(input);
        return finish();
    }
}
