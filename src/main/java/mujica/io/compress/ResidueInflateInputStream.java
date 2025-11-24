package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.of_boolean.BitSequence;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/11/6")
public abstract class ResidueInflateInputStream extends AbstractInflateInputStream implements BitSequence {

    protected static final int MAX_SYMBOL = 287;

    protected final int[] commonAlphabet = new int[MAX_SYMBOL];

    protected static final int MAX_CODE_LENGTH = 15;

    protected final int[] commonLengthCounts = new int[MAX_CODE_LENGTH + 1];

    protected final int[] commonNextCode = new int[MAX_CODE_LENGTH + 1];

    protected static final int STATE_FREE                           = 0;
    protected static final int STATE_LAST_BLOCK_FREE                = 1;
    protected static final int STATE_NO_COMPRESSION                 = 2;
    protected static final int STATE_LAST_BLOCK_NO_COMPRESSION      = 3;
    protected static final int STATE_FIXED_HUFFMAN                  = 4;
    protected static final int STATE_LAST_BLOCK_FIXED_HUFFMAN       = 5;
    protected static final int STATE_DYNAMIC_HUFFMAN                = 6;
    protected static final int STATE_LAST_BLOCK_DYNAMIC_HUFFMAN     = 7;
    protected static final int STATE_TRAILING_BITS                  = 8;
    protected static final int STATE_TRAILING_BYTES                 = 9;

    protected int state, copyDistance, remainingLength;

    protected static final byte[] REORDER = {
            16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
    }; // 19

    protected static final byte[] LENGTH_EXTRA_BITS = {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            4, 4, 4, 4, 5, 5, 5, 5, 0
    };

    protected static final short[] LENGTH_BASE = {
             3,  4,  5,   6,   7,   8,   9,  10,  11, 13,
            15, 17, 19,  23,  27,  31,  35,  43,  51, 59,
            67, 83, 99, 115, 131, 163, 195, 227, 258
    }; // 29

    protected static final byte[] DISTANCE_EXTRA_BITS = {
            0, 0,  0,  0,  1,  1,  2,  2,  3,  3,
            4, 4,  5,  5,  6,  6,  7,  7,  8,  8,
            9, 9, 10, 10, 11, 11, 12, 12, 13, 13
    };

    protected static final short[] DISTANCE_BASE = {
               1,    2,    3,    4,    5,    7,    9,    13,    17,    25,
              33,   49,   65,   97,  129,  193,  257,   385,   513,   769,
            1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577
    };

    @NotNull
    protected final LookBackMemory runBuffer;

    protected ResidueInflateInputStream(@NotNull InputStream in, @NotNull LookBackMemory runBuffer) {
        super(in);
        this.runBuffer = runBuffer;
    }

    @Override
    public void trailingBytesMode() {
        if (state == STATE_LAST_BLOCK_FREE) {
            state = STATE_TRAILING_BYTES;
        } else {
            throw new IllegalStateException();
        }
    }

    private int bitSize, buffer;

    @Override
    public int bitLength() {
        return bitSize;
    }

    /**
     * trailing bits can be read after EOF, or after EOF and close
     * steganography information contained inside
     */
    @Override
    public boolean getBit(int index) {
        if (index < 0 || index >= bitSize) {
            throw new IndexOutOfBoundsException();
        }
        return (buffer & (1 << bitSize)) != 0;
    }

    protected int readTrailingBit() {
        if (bitSize > 0) {
            boolean bit = (buffer & 1) != 0;
            bitSize--;
            buffer >>>= 1;
            return bit ? 1 : 0;
        } else {
            return -1;
        }
    }

    protected boolean readBit() throws IOException {
        if (bitSize == 0) {
            buffer = in.read();
            if (buffer == -1) {
                throw new EOFException();
            }
            bitSize = Byte.SIZE;
        }
        final boolean bit = (buffer & 1) != 0;
        bitSize--;
        buffer >>>= 1;
        return bit;
    }

    protected int readBitsAhead(int bitCount) throws IOException {
        assert bitCount <= Integer.SIZE - Byte.SIZE;
        while (bitSize < bitCount) {
            int value = in.read();
            if (value == -1) {
                throw new EOFException();
            }
            buffer |= value << bitSize;
            bitSize += Byte.SIZE;
        }
        return buffer & ((1 << bitCount) - 1);
    }

    protected int readBits(int bitCount) throws IOException {
        final int value = readBitsAhead(bitCount);
        bitSize -= bitCount;
        buffer >>>= bitCount;
        return value;
    }

    protected void skipBits(int bitCount) {
        bitSize -= bitCount;
        assert bitSize >= 0;
        buffer >>>= bitCount;
    }

    protected int readTrailingByte() throws IOException {
        if (bitSize >= Byte.SIZE) {
            int alignShift = bitSize & 0x7;
            if (alignShift != 0) {
                bitSize -= alignShift;
                buffer >>>= alignShift;
            }
            int value = 0xff & buffer;
            bitSize -= Byte.SIZE;
            buffer >>>= Byte.SIZE;
            return value;
        } else {
            bitSize = 0;
            buffer = 0;
            return in.read();
        }
    }

    protected int readNoCompressionLength() throws IOException {
        int length = readTrailingByte() | (readTrailingByte() << Byte.SIZE);
        if (length < 0) {
            throw new EOFException();
        }
        int notLength = readTrailingByte() | (readTrailingByte() << Byte.SIZE);
        if (notLength < 0) {
            throw new EOFException();
        }
        if ((length ^ notLength) != 0xffff) {
            throw new CodecException();
        }
        return length;
    }

    @Override
    public abstract int read() throws IOException;

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        int count = 0;
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            int value = read();
            if (value == -1) {
                if (count == 0) {
                    count = -1;
                }
                break;
            }
            array[offset] = (byte) value;
            count++;
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        long m = 0L;
        while (m < n) {
            if (in.read() == -1) {
                break;
            }
            m++;
        }
        return m;
    }

    @Override
    public int available() {
        return remainingLength;
    }

    @Override
    public void close() throws IOException {
        bitSize = 0;
        state = STATE_LAST_BLOCK_FREE;
        remainingLength = 0;
        in.close();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[state = " + state + ", copyDistance = " + copyDistance + ", remainingLength = " + remainingLength + ", runBuffer = " + runBuffer + "]";
    }
}
