package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;
import java.util.Arrays;

@CodeHistory(date = "2025/11/7", name = "BlockLookBackMemory")
@CodeHistory(date = "2025/11/26")
public class BlockRunBuffer extends RunBuffer implements Serializable {

    private static final long serialVersionUID = 0x5f35aab17b09df1eL;

    private final int blockSize;

    private final byte[][] blocks;

    private int position;

    public BlockRunBuffer(int maxDistance, int blockCount) {
        super(maxDistance);
        if (blockCount < 2) {
            throw new IllegalArgumentException();
        }
        blockSize = (maxDistance + blockCount - 2) / (blockCount - 1);
        blocks = new byte[blockCount][];
    }

    @Override
    public void setMaxDistance(int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(byte data) {
        byte[] block = blocks[0];
        if (block == null) {
            block = new byte[blockSize];
            blocks[0] = block;
            position = 0;
            block[position++] = data;
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
        block[position++] = data;
    }

    @Override
    public byte copy(int distance) {
        checkDistance(distance);
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
        put(data);
        return data;
    }

    @Override
    public boolean release() {
        Arrays.fill(blocks, null);
        position = 0;
        return true;
    }
}
