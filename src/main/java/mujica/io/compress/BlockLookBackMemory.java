package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created on 2025/11/7.
 */
@CodeHistory(date = "2025/11/7", name = "BlockLookBackMemory")
public class BlockLookBackMemory extends MaxDistanceLookBackMemory implements Serializable {

    private static final long serialVersionUID = 0x5F35AAB17B09DF1EL;

    private final int blockSize;

    private final byte[][] blocks;

    private int position;

    public BlockLookBackMemory(int maxDistance, int blockCount) {
        super(maxDistance);
        if (blockCount < 2) {
            throw new IllegalArgumentException();
        }
        blockSize = (maxDistance + blockCount - 2) / (blockCount - 1);
        blocks = new byte[blockCount][];
    }

    @Override
    public void write(int data) {
        byte[] block = blocks[0];
        if (block == null) {
            block = new byte[blockSize];
            blocks[0] = block;
            position = 0;
            block[position++] = (byte) data;
        } else if (position == blockSize) {
            int lastBlockIndex = blocks.length - 1;
            block = blocks[lastBlockIndex];
            System.arraycopy(blocks, 0, blocks, 1, lastBlockIndex);
            if (block == null) {
                block = new byte[blockSize];
            }
            blocks[0] = block;
            position = 0;
        }
        block[position++] = (byte) data;
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        return 0;
    }

    @Override
    public byte copyAndWrite(int distance) {
        if (distance <= 0 || distance > maxDistance) {
            throw new CodecException("distance = " + distance + ", maxDistance = " + maxDistance);
        }
        byte data;
        if (distance <= position) {
            data = blocks[0][position - distance];
        } else {
            distance -= position;
            // now distance > 0
            int index = (distance - 1) / blockSize;
            distance -= index * blockSize;
            // now 1 <= distance < blockSize
            data = blocks[index + 1][blockSize - distance];
        }
        write(data);
        return data;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        return 0;
    }

    @Override
    public boolean release() {
        Arrays.fill(blocks, null);
        position = 0;
        return true;
    }
}
